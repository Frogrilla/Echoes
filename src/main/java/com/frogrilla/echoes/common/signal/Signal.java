package com.frogrilla.echoes.common.signal;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.init.EchoesParticles;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Signal extends AbstractSignal{
    public static final int DEFAULT_POWER = 16;
    private int power = 16;

    public Signal(BlockPos blockPos, Direction direction, int frequency) {
        super(blockPos, direction, frequency);
    }
    public Signal(NbtCompound compound){
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
        blockPos = blockPos.offset(direction);
    }

    @Override
    public void refreshProperties() {
        power = DEFAULT_POWER;
    }

    @Override
    public void regularTick(SignalManager manager) {
        if(power <= 1){
            manager.removeSignal(this);
        }
        else{
            step();
            power--;
        }

    }

    @Override
    public void effects(ServerWorld world, Vec3d pos) {
        if(power == 1){
            world.playSound((PlayerEntity) null, blockPos, SoundEvents.BLOCK_SCULK_BREAK, SoundCategory.BLOCKS);
            world.spawnParticles(EchoesParticles.ECHO_CHARGE, pos.x, pos.y, pos.z, 10, 0, 0, 0, 0.1);
        }
        else{
            world.spawnParticles(EchoesParticles.ECHO_CHARGE, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
        }
    }
}
