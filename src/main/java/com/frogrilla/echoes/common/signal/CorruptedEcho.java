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

public class CorruptedEcho extends Echo{

    public static final MapCodec<CorruptedEcho> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPos.CODEC.fieldOf("block_pos").forGetter(CorruptedEcho::getBlockPos),
            Direction.CODEC.fieldOf("direction").forGetter(CorruptedEcho::getDirection),
            Codec.BYTE.fieldOf("strength").forGetter(CorruptedEcho::getStrength)
    ).apply(instance, CorruptedEcho::new));

    @Override
    public SignalType<?> getType() {
        return EchoesSignalTypes.CORRUPTED_ECHO;
    }

    public CorruptedEcho(BlockPos blockPos, Direction direction, int strength) {
        super(blockPos, direction, strength);
    }

    public CorruptedEcho(SignalProperties properties) {
        super(properties);
    }

    public CorruptedEcho() {};

    @Override
    public SignalStatus preTick(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {
        counter++;
        return counter % 2 == 1 ? SignalStatus.TICK : SignalStatus.NO_TICK;
    }

    @Override
    public SignalTickResult tick(SignalInteractionResult interactionResult, SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {
        if (counter >= 16 || interactionResult == SignalInteractionResult.DESTROY) return SignalTickResult.REMOVE;
        blockPos = blockPos.relative(direction, 2);
        return SignalTickResult.CONTINUE;
    }

    @Override
    public void doEffects(ServerLevel serverLevel, BlockState blockState, SignalTickResult signalTickResult) {
        Vec3 pos = this.getBlockPos().getCenter();
        switch(signalTickResult){
            case CONTINUE -> {
                serverLevel.sendParticles(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            }
            case REMOVE -> {
                serverLevel.sendParticles(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 10, 0, 0, 0, 0.1);
                serverLevel.playSound(null, this.getBlockPos(), SoundEvents.FIREWORK_ROCKET_BLAST_FAR, SoundSource.BLOCKS, 3, 1);
            }
        }
    }
}
