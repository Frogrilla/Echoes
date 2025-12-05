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

public class Echo extends AbstractSignal {

    public static final MapCodec<Echo> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPos.CODEC.fieldOf("block_pos").forGetter(Echo::getBlockPos),
            Direction.CODEC.fieldOf("direction").forGetter(Echo::getDirection),
            Codec.BYTE.fieldOf("strength").forGetter(Echo::getStrength)
    ).apply(instance, Echo::new));

    @Override
    public SignalType<?> getType() {
        return EchoesSignalTypes.ECHO;
    }

    BlockPos blockPos = BlockPos.ZERO;
    Direction direction = Direction.UP;
    byte strength = 0;
    int counter = 0;


    public Echo(BlockPos blockPos, Direction direction, int strength) {
        this.blockPos = blockPos;
        this.direction = direction;
        this.strength = (byte) strength;
    }

    public Echo(SignalProperties properties) {
        this.blockPos = properties.blockPos;
        this.direction = properties.direction;
        this.strength = properties.strength;
    }

    public Echo() {};

    @Override
    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public byte getStrength() {
        return strength;
    }

    @Override
    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void setStrength(int strength) {
        this.strength = (byte)strength;
    }

    @Override
    public SignalStatus preTick(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {
        counter++;
        return SignalStatus.TICK;
    }

    @Override
    public SignalTickResult tick(SignalInteractionResult interactionResult, SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {
        blockPos = blockPos.relative(direction);
        return (counter >= 16 || interactionResult == SignalInteractionResult.DESTROY) ? SignalTickResult.REMOVE : SignalTickResult.CONTINUE;
    }

    @Override
    public void onRemove(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {

    }
}
