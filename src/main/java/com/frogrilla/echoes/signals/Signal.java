package com.frogrilla.echoes.signals;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Signal implements ISignal {

    private BlockPos blockPos;
    private Direction direction;
    private int frequency;
    private int power = SignalManager.defaultPower;
    public BlockPos getBlockPos() { return blockPos; }
    public void setBlockPos(BlockPos blockPos) { this.blockPos = blockPos; }

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public Direction getDirection() {return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }
    @Override
    public void step() {
        blockPos = blockPos.offset(direction);
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", blockPos.toShortString(), frequency, direction, power);
    }

    @Override
    public ISignal fromTypedCompound(NbtCompound compound) {
        Signal signal = new Signal();
        signal.setBlockPos(BlockPos.fromLong(compound.getLong("pos")));
        signal.setFrequency(compound.getInt("frequency"));
        signal.setDirection(Direction.byId(compound.getInt("direction")));

        signal.setPower(compound.getInt("power"));
        return signal;
    }

    @Override
    public boolean tickShouldStep() {
        return true;
    }

    @Override
    public void decrement() {
        power--;
    }

    public void stepEffects(ServerWorld world){
        Vec3d pos = blockPos.toCenterPos();
        world.spawnParticles(ParticleTypes.SCULK_CHARGE_POP, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
    }
    public void deathEffects(ServerWorld world){
        Vec3d pos = blockPos.toCenterPos();
        world.spawnParticles(ParticleTypes.SCULK_CHARGE_POP, pos.x, pos.y, pos.z, 40, 0, 0, 0, 0.1);
        world.playSound((PlayerEntity) null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_SCULK_SENSOR_BREAK, SoundCategory.BLOCKS, 2, 1);
    }
}
