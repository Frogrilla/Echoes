package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.common.block.iSignalInteractor;
import com.frogrilla.echoes.common.payload.SignalUpdatePayload;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;
import java.util.stream.Stream;

public class SignalManager extends SavedData {

    private int freeID = 0;

    private static final Codec<SignalManager> CODEC = AbstractSignal.SIGNAL_CODEC.listOf().xmap(
            SignalManager::new,
            SignalManager::getSignals
    );

    private static final SavedDataType<SignalManager> TYPE = new SavedDataType<>(
            "signal_manager", // The unique name for this saved data.
            SignalManager::new, // If there's no 'SavedBlockData', yet create one and refresh fields.
            CODEC, // The codec used for serialization/deserialization.
            null // A data fixer, which is not needed here.
    );

    public Codec<Double> getValue(String key) {
        return Codec.DOUBLE;
    }


    public static SignalManager getSignalManager(ServerLevel level) {
        if(level == null) return null;

        SignalManager savedData = level.getDataStorage().computeIfAbsent(TYPE);
        savedData.setDirty();

        return savedData;
    }

    private List<AbstractSignal> signals = new LinkedList<>();
    private List<AbstractSignal> signalBuffer = new LinkedList<>();

    public SignalManager() {}

    public SignalManager(List<AbstractSignal> signals) {
        if(signals != null) {
            this.signals.addAll(signals);
        }
    }

    public <T extends AbstractSignal> void addSignal(T signal) {
        signalBuffer.add(signal);
    }

    public void tick(ServerLevel serverLevel) {

        signalBuffer.forEach(signal -> {
            signal.setID(this.getNextSignalID());
            BlockState blockState = serverLevel.getBlockState(signal.getBlockPos());
            signal.onAppend(this, serverLevel, blockState);
        });

        signals.addAll(signalBuffer);
        signalBuffer.clear();

        ListIterator<AbstractSignal> iter = signals.listIterator();

        List<Integer> removedIDs = new ArrayList<>();

        while(iter.hasNext()){
            AbstractSignal signal = iter.next();
            if(serverLevel.shouldTickBlocksAt(signal.getBlockPos())) {
                BlockState blockState = serverLevel.getBlockState(signal.getBlockPos());

                // preTick to allow for signals to not tick every tick.
                SignalStatus signalStatus = signal.preTick(this, serverLevel, blockState);
                if(signalStatus == SignalStatus.NO_TICK) continue;

                SignalInteractionResult interactionResult = SignalInteractionResult.NONE;
                // Do block interaction if block is a signal interactor - and store result.
                if(blockState.getBlock() instanceof iSignalInteractor signalInteractor) {
                    interactionResult = signalInteractor.processSignal(signal, this, serverLevel, blockState);
                }

                // Process signal tick.
                SignalTickResult tickResult = signal.tick(interactionResult, this, serverLevel, blockState);

                // Signal effects (particles / sounds)
                signal.doEffects(serverLevel, blockState, tickResult);

                // Handle result.
                if(tickResult == SignalTickResult.REMOVE){
                    removedIDs.add(signal.getID());
                    freeID = Math.min(signal.getID(), freeID);
                    signal.onRemove(this, serverLevel, blockState);
                    iter.remove();
                }
            }
        }

        PlayerLookup.world(serverLevel).forEach(player -> {
            SignalUpdatePayload payload = new SignalUpdatePayload(this.signals, removedIDs);
            ServerPlayNetworking.send(player, payload);
        });
    };

    public List<AbstractSignal> getSignals() {
        return signals;
    }

    public int getNextSignalID() {
        final var ID = freeID;
        freeID++;
        signals.forEach(signal -> {
            if(signal.getID() == freeID){
                freeID++;
            }
        });
        return ID;
    }
}
