package com.frogrilla.echoes.common.signal;

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

public class PulseSignal extends AbstractSignal{
    public PulseSignal(BlockPos blockPos, Direction direction, byte frequency) {
        super(blockPos, direction, frequency);
    }

    public PulseSignal(NbtCompound compound) {
        super(compound);
    }

    @Override
    public void preTick() {
        removalFlag = true;
    }

    @Override
    public void step() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void effects(ServerWorld world, Vec3d pos) {
        world.playSound((PlayerEntity) null, blockPos, SoundEvents.BLOCK_SCULK_PLACE, SoundCategory.BLOCKS);
        world.spawnParticles(ParticleTypes.GLOW, pos.x, pos.y, pos.z, 10, 0.1, 0.1, 0.1, 0.05);
    }
}
