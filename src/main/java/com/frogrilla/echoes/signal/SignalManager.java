package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.block.iSignalInteractor;
import com.frogrilla.echoes.common.init.EchoesAttatchmentTypes;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class SignalManager {

    public static final Codec<SignalManager> CODEC = AbstractSignal.CODEC.listOf().xmap(
            SignalManager::new,
            SignalManager::getSignals
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SignalManager> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

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
                BlockPos lastPos = signal.getBlockPos();
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
//                    PlayerLookup.world(serverLevel).forEach(player -> {
//                        SignalRemovePayload payload = new SignalRemovePayload(signal.uuid);
//                        ServerPlayNetworking.send(player, payload);
//                    });

                    signal.onRemove(this, serverLevel, blockState);
                    iter.remove();
                }
//                else{
//                    PlayerLookup.world(serverLevel).forEach(player -> {
//                        SignalUpdatePayload payload = new SignalUpdatePayload(new SignalInterpolator(lastPos.getCenter(), signal.getBlockPos().getCenter()), signal.uuid);
//                        ServerPlayNetworking.send(player, payload);
//                    });
//                }
            }
        }

        Collections.shuffle(signals);

        serverLevel.modifyAttached(EchoesAttatchmentTypes.SIGNAL_MANAGER_ATTACHMENT_TYPE, (s) -> this);
    };

    public List<AbstractSignal> getSignals() {
        return signals;
    }

    public int getSignalCount() {
        return signals.size();
    }

    public static SignalManager getSignalManager(Level level){
        return level.getAttachedOrSet(EchoesAttatchmentTypes.SIGNAL_MANAGER_ATTACHMENT_TYPE, new SignalManager());
    }
}
