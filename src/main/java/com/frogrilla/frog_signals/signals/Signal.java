package com.frogrilla.frog_signals.signals;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Signal {

    private BlockPos blockPos;
    private Direction direction;
    private int power;
    private int counter = 0;

    public Signal(BlockPos blockPos, Direction direction, int power){
        this.power = power;
        this.direction = direction;
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() { return blockPos; }
    public void  setBlockPos(BlockPos blockPos) { this.blockPos = blockPos; }

    public Direction getDirection() {return direction; }
    public void  setDirection(Direction direction) { this.direction = direction; }

    public int getPower() { return power; }
    public void  setPower(int power) { this.power = power; }

    public int getCounter() { return counter; }

    public boolean increment(){
        counter++;
        counter %= SignalManager.ticksPerStep;
        return counter == 0;
    }

    public void step() {
        power--;
        blockPos = blockPos.offset(direction);
    }

    public Vec3d getWorldPos(){
        return new Vec3d(blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5).add(direction.getDoubleVector().multiply(((double) counter /SignalManager.ticksPerStep) - 1));
    }

    public NbtCompound asCompound(){
        NbtCompound compound = new NbtCompound();
        compound.putLong("pos", this.blockPos.asLong());
        compound.putInt("power", this.power);
        compound.putInt("counter", this.counter);
        compound.putInt("direction", direction.getId());
        return compound;
    }

    public static Signal fromCompound(NbtCompound compound){
        Signal signal = new Signal(
                BlockPos.fromLong(compound.getLong("pos")),
                Direction.byId(compound.getInt("direction")),
                compound.getInt("power"));

        signal.counter = compound.getInt("counter");
        return signal;
    }
}
