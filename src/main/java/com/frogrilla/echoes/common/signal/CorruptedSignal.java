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
    public static final byte DEFAULT_POWER = 8;
    private byte power = DEFAULT_POWER;
    private boolean stepBuffered = true;

    public CorruptedSignal(BlockPos blockPos, Direction direction, byte frequency) {
        super(blockPos, direction, frequency);
    }
    public CorruptedSignal(NbtCompound compound){
        super(compound);
        power = compound.getByte("power");
        stepBuffered = compound.getBoolean("step_buffered");
    }

    @Override
    public NbtCompound asCompound() {
        NbtCompound compound = super.asCompound();
        compound.putByte("power", power);
        compound.putBoolean("step_buffered", stepBuffered);
        return compound;
    }

    @Override
    public void preTick() {
        stepBuffered ^= true;
    }

    @Override
    public boolean shouldTick() {
        return stepBuffered;
    }

    @Override
    public void refreshProperties() {
        power = DEFAULT_POWER;
        stepBuffered = true;
    }

    @Override
    public void step() {
        blockPos = blockPos.offset(direction, 2);
    }

    @Override
    public void tick(SignalManager manager) {
        if(power <= 1){
            removalFlag = true;
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
