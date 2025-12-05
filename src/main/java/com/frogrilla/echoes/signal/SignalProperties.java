package com.frogrilla.echoes.signal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class SignalProperties {
    public BlockPos blockPos = BlockPos.ZERO;
    public Direction direction = Direction.UP;
    public byte strength = 0;

    public SignalProperties(BlockPos blockPos, Direction direction, int strength) {
        this.blockPos = blockPos;
        this.direction = direction;
        this.strength = (byte) strength;
    }

    public SignalProperties() {};
}
