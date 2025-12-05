package com.frogrilla.echoes.common.signal;

import com.frogrilla.echoes.common.init.EchoesSignalTypes;
import com.frogrilla.echoes.signal.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class CorruptedEcho extends Echo{

    public static final MapCodec<CorruptedEcho> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPos.CODEC.fieldOf("block_pos").forGetter(CorruptedEcho::getBlockPos),
            Direction.CODEC.fieldOf("direction").forGetter(CorruptedEcho::getDirection),
            Codec.BYTE.fieldOf("strength").forGetter(CorruptedEcho::getStrength)
    ).apply(instance, CorruptedEcho::new));

    @Override
    public SignalType<?> getType() {
        return EchoesSignalTypes.CORRUPTED_ECHO;
    }

    public CorruptedEcho(BlockPos blockPos, Direction direction, int strength) {
        super(blockPos, direction, strength);
    }

    public CorruptedEcho(SignalProperties properties) {
        super(properties);
    }

    public CorruptedEcho() {};

    @Override
    public SignalStatus preTick(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {
        counter++;
        return counter % 2 == 1 ? SignalStatus.TICK : SignalStatus.NO_TICK;
    }

    @Override
    public SignalTickResult tick(SignalInteractionResult interactionResult, SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {
        blockPos = blockPos.relative(direction, 2);
        return (counter >= 16 || interactionResult == SignalInteractionResult.DESTROY) ? SignalTickResult.REMOVE : SignalTickResult.CONTINUE;
    }
}
