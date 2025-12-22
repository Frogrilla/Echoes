package com.frogrilla.echoes.common.signal;

import com.frogrilla.echoes.common.init.EchoesSignalTypes;
import com.frogrilla.echoes.signal.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class PulseEcho extends AbstractSignal{

    public static final MapCodec<PulseEcho> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPos.CODEC.fieldOf("block_pos").forGetter(PulseEcho::getBlockPos),
            Codec.BYTE.fieldOf("strength").forGetter(PulseEcho::getStrength),
            Codec.INT.fieldOf("id").forGetter(PulseEcho::getID)
    ).apply(instance, PulseEcho::new));

    BlockPos blockPos = BlockPos.ZERO;
    byte strength = 0;

    @Override
    public SignalType<?> getType() {
        return EchoesSignalTypes.PULSE_ECHO;
    }

    public PulseEcho(BlockPos blockPos, int strength, int id) {
        this.blockPos = blockPos;
        this.strength = (byte) strength;
        this.id = id;
    }

    public PulseEcho(SignalProperties properties) {
        this.blockPos = properties.blockPos;
        this.strength = properties.strength;
    }

    // PulseEcho should not be modified by interactors
    @Override
    public void setBlockPos(BlockPos blockPos) {

    }

    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public void setStrength(int strength) {

    }

    @Override
    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public byte getStrength() {
        return this.strength;
    }

    @Override
    public SignalStatus preTick(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {
        return SignalStatus.TICK;
    }

    @Override
    public SignalTickResult tick(SignalInteractionResult interactionResult, SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {
        return SignalTickResult.REMOVE;
    }

    @Override
    public void onRemove(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {

    }

    @Override
    public void onAppend(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {
        Vec3 pos = this.getBlockPos().getCenter();
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x+0.5f, pos.y+0.5f, pos.z+0.5f, 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x+0.5f, pos.y+0.5f, pos.z-0.5f, 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x+0.5f, pos.y-0.5f, pos.z+0.5f, 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x+0.5f, pos.y-0.5f, pos.z-0.5f, 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x-0.5f, pos.y+0.5f, pos.z+0.5f, 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x-0.5f, pos.y+0.5f, pos.z-0.5f, 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x-0.5f, pos.y-0.5f, pos.z+0.5f, 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x-0.5f, pos.y-0.5f, pos.z-0.5f, 1, 0, 0, 0, 0);
        serverLevel.playSound(null, this.getBlockPos(), SoundEvents.SCULK_BLOCK_CHARGE, SoundSource.BLOCKS, 3, 1);
    }

    @Override
    public void doEffects(ServerLevel serverLevel, BlockState blockState, SignalTickResult signalTickResult) {

    }
}
