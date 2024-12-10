package com.frogrilla.frog_signals.signals;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Signal {

    private BlockPos blockPos;
    private Direction direction;
    private int frequency;
    private int power = SignalManager.defaultPower;
    private int counter = 0;

    public Signal(BlockPos blockPos, int frequency, Direction direction){
        this.blockPos = blockPos;
        this.frequency = frequency;
        this.direction = direction;
    }

    public BlockPos getBlockPos() { return blockPos; }
    public void setBlockPos(BlockPos blockPos) { this.blockPos = blockPos; }

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public Direction getDirection() {return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }

    public int getCounter() { return counter; }
    public void setCounter(int counter) { this.counter = counter; }

    public void increment(){
        counter++;
        counter %= SignalManager.ticksPerStep;
    }

    public void step() {
        power--;
        blockPos = blockPos.offset(direction);
    }

    public Vec3d getWorldPos(){
        return new Vec3d(blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5).add(direction.getDoubleVector().multiply(((double) counter /SignalManager.ticksPerStep) - 1));
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", blockPos.toShortString(), frequency, direction, power, counter);
    }

    public NbtCompound asCompound(){
        NbtCompound compound = new NbtCompound();
        compound.putLong("pos", this.blockPos.asLong());
        compound.putInt("power", this.power);
        compound.putInt("frequency", this.frequency);
        compound.putInt("direction", direction.getId());
        compound.putInt("counter", this.counter);
        return compound;
    }

    public static Signal fromCompound(NbtCompound compound){
        Signal signal = new Signal(
                BlockPos.fromLong(compound.getLong("pos")),
                compound.getInt("frequency"),
                Direction.byId(compound.getInt("direction"))
        );

        signal.setPower(compound.getInt("power"));
        signal.setCounter(compound.getInt("counter"));
        return signal;
    }
}
