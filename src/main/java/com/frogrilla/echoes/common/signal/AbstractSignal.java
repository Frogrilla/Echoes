package com.frogrilla.echoes.common.signal;

import com.frogrilla.echoes.signal.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractSignal {

    public static Codec<AbstractSignal> SIGNAL_CODEC = SignalType.REGISTRY.byNameCodec()
            .dispatch("type", AbstractSignal::getType, SignalType::codec);

    public abstract SignalType<?> getType();

    public static AbstractSignal fromSignalType(SignalType<?> type, SignalProperties properties) {
        return type.factory().apply(properties);
    };

    public abstract void setBlockPos(BlockPos blockPos);
    public abstract void setDirection(Direction direction);
    public abstract void setStrength(int strength);
    public abstract BlockPos getBlockPos();
    public abstract Direction getDirection();
    public abstract byte getStrength();

    public abstract SignalStatus preTick(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState);
    public abstract SignalTickResult tick(SignalInteractionResult interactionResult, SignalManager signalManager, ServerLevel serverLevel, BlockState blockState);
    public abstract void onRemove(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState);

    public abstract void doEffects(ServerLevel serverLevel, BlockState blockState, SignalTickResult signalTickResult);

    @Override
    public String toString() {
        return String.format("%s %s %s %d", SignalType.REGISTRY.getKey(this.getType()).toString(), this.getBlockPos().toShortString(), this.getDirection().toString(), this.getStrength());
    }
}
