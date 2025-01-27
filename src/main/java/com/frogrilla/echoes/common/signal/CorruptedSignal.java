package com.frogrilla.echoes.common.signal;

import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CorruptedSignal extends AbstractSignal{
    public static final int DEFAULT_POWER = 8;
    private int power = DEFAULT_POWER;

    public CorruptedSignal(BlockPos blockPos, Direction direction, int frequency) {
        super(blockPos, direction, frequency);
    }
    public CorruptedSignal(NbtCompound compound){
        super(compound);
        power = compound.getInt("power");
    }

    @Override
    public NbtCompound asCompound() {
        NbtCompound compound = super.asCompound();
        compound.putInt("power", power);
        return compound;
    }

    @Override
    public void step() {
        blockPos = blockPos.offset(direction, 2);
    }

    @Override
    public void defaultTick(SignalManager manager, ServerWorld world) {
        if(power <= 1){
            signalEffect(world);
            manager.removeSignal(this);
        }
        else{
            signalEffect(world);
            step();
            power--;
        }
    }
}
