package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.common.block.iSignalInteractor;
import com.frogrilla.echoes.common.init.EchoesSignalTypes;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class SignalManager extends SavedData {

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
            BlockState blockState = serverLevel.getBlockState(signal.getBlockPos());
            signal.onAppend(this, serverLevel, blockState);
        });

        signals.addAll(signalBuffer);
        signalBuffer.clear();

        ListIterator<AbstractSignal> iter = signals.listIterator();

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
                    signal.onRemove(this, serverLevel, blockState);
                    iter.remove();
                }
            }
        }

        Collections.shuffle(signals);
    };

    public List<AbstractSignal> getSignals() {
        return signals;
    }
}
