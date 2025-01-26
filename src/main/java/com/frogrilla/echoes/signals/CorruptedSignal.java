package com.frogrilla.echoes.signals;

import com.frogrilla.echoes.common.init.EchoesParticles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class CorruptedSignal extends Signal{

    private int counter = 0;

    @Override
    public boolean tickShouldStep() {
        counter++;
        if(counter == 2){
            counter = 0;
            return true;
        }
        return false;
    }

    @Override
    public void step() {
        setBlockPos(getBlockPos().offset(getDirection(), 2));
    }

    @Override
    public ISignal fromTypedCompound(NbtCompound compound) {
        CorruptedSignal signal = new CorruptedSignal();
        signal.setBlockPos(BlockPos.fromLong(compound.getLong("pos")));
        signal.setFrequency(compound.getInt("frequency"));
        signal.setDirection(Direction.byId(compound.getInt("direction")));

        signal.setPower(compound.getInt("power"));
        return signal;
    }


    public void stepEffects(ServerWorld world){
        Vec3d pos = getBlockPos().toCenterPos();
        world.spawnParticles(EchoesParticles.SIGNAL_STEP, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
    }
    public void deathEffects(ServerWorld world){
        Vec3d pos = getBlockPos().toCenterPos();
        world.spawnParticles(EchoesParticles.SIGNAL_STEP, pos.x, pos.y, pos.z, 40, 0, 0, 0, 0.1);
        world.playSound((PlayerEntity) null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 2, 1);
    }
}
