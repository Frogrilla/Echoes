package com.frogrilla.echoes.common.signal;

import com.frogrilla.echoes.signal.*;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.MappedRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public abstract class AbstractSignal {

    public UUID uuid = UUID.randomUUID();

    // Type stuff
    public static Codec<AbstractSignal> CODEC = SignalType.REGISTRY.byNameCodec()
            .dispatch("type", AbstractSignal::getType, SignalType::codec);

    public abstract SignalType<?> getType();

    public static AbstractSignal fromSignalType(SignalType<?> type, SignalProperties properties) {
        return type.factory().apply(properties);
    };

    // Methods for interactors to mutate signals
    public abstract void setBlockPos(BlockPos blockPos);
    public abstract void setDirection(Direction direction);
    public abstract void setStrength(int strength);

    // Retrieve signal state
    public abstract BlockPos getBlockPos();
    public abstract Direction getDirection();
    public abstract byte getStrength();

    // Signal processing
    public abstract SignalStatus preTick(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState);
    public abstract SignalTickResult tick(SignalInteractionResult interactionResult, SignalManager signalManager, ServerLevel serverLevel, BlockState blockState);
    public abstract void onRemove(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState);
    public abstract void onAppend(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState);

    // Effects
    public abstract void doEffects(ServerLevel serverLevel, BlockState blockState, SignalTickResult signalTickResult);

    // Util
    @Override
    public String toString() {
        return String.format("%s %s %s %d", SignalType.REGISTRY.getKey(this.getType()).toString(), this.getBlockPos().toShortString(), this.getDirection().toString(), this.getStrength());
    }
}
