package com.frogrilla.echoes.signals;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.lang.reflect.Constructor;
import java.util.*;

public interface ISignal {

    List<Class<? extends ISignal>> SignalTypes = new ArrayList<>(){
        {
            add(Signal.class);
            add(CorruptedSignal.class);
        }
    };

    BlockPos getBlockPos();
    void setBlockPos(BlockPos blockPos);

    int getPower();
    void setPower(int power);

    int getFrequency();
    void setFrequency(int frequency);

    Direction getDirection();
    void setDirection(Direction direction);

    default NbtCompound asCompound(){
        NbtCompound compound = new NbtCompound();
        compound.putInt("type", SignalTypes.indexOf(this.getClass()));
        compound.putLong("pos", getBlockPos().asLong());
        compound.putInt("power", getPower());
        compound.putInt("frequency", getFrequency());
        compound.putInt("direction", getDirection().getId());
        return compound;
    }

    static ISignal fromCompound(NbtCompound compound) throws InstantiationException, IllegalAccessException {
        Class<? extends ISignal> type = SignalTypes.get(compound.getInt("type"));
        ISignal signal = type.newInstance();
        signal = signal.fromTypedCompound(compound);
        return signal;
    }

    ISignal fromTypedCompound(NbtCompound compound);

    boolean tickShouldStep();
    void step();
    void decrement();

    void stepEffects(ServerWorld world);
    void deathEffects(ServerWorld world);
}