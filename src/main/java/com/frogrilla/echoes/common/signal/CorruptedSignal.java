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

public class CorruptedSignal extends AbstractSignal{
    public static final int DEFAULT_POWER = 8;
    private int power = DEFAULT_POWER;
    private int counter = 0;

    public CorruptedSignal(BlockPos blockPos, Direction direction, int frequency) {
        super(blockPos, direction, frequency);
    }
    public CorruptedSignal(NbtCompound compound){
        super(compound);
        power = compound.getInt("power");
        counter = compound.getInt("counter");
    }

    @Override
    public NbtCompound asCompound() {
        NbtCompound compound = super.asCompound();
        compound.putInt("power", power);
        compound.putInt("counter", counter);
        return compound;
    }

    @Override
    public void preTick() {
        counter++;
        counter %= 2;
    }

    @Override
    public boolean shouldTick() {
        return counter == 0;
    }

    @Override
    public void refreshProperties() {
        power = DEFAULT_POWER;
        counter = 0;
    }

    @Override
    public void step() {
        blockPos = blockPos.offset(direction, 2);
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
        if (power == 1) {
            world.playSound((PlayerEntity) null, blockPos, SoundEvents.BLOCK_SOUL_SOIL_BREAK, SoundCategory.BLOCKS);
            world.spawnParticles(EchoesParticles.CORRUPTED_ECHO_CHARGE, pos.x, pos.y, pos.z, 10, 0, 0, 0, 0.1);
        } else {
            world.spawnParticles(EchoesParticles.CORRUPTED_ECHO_CHARGE, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
        }
    }
}
