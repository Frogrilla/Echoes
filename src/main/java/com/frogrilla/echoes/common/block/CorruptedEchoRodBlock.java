package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.init.EchoesParticles;
import com.frogrilla.echoes.common.init.EchoesSounds;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.common.signal.CorruptedSignal;
import com.frogrilla.echoes.common.signal.Signal;
import com.frogrilla.echoes.signal.PersistentManagerState;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class CorruptedEchoRodBlock extends EchoRodBlock{
    public CorruptedEchoRodBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void doEffects(ServerWorld world, BlockPos pos, Direction direction) {
        Vec3d position = pos.toCenterPos().add(direction.getDoubleVector().multiply(0.365d));
        world.spawnParticles(EchoesParticles.CORRUPTED_ECHO_CHARGE, position.x, position.y, position.z, 1, 0, 0, 0, 0);
        world.playSound((PlayerEntity) null, pos, EchoesSounds.CORRUPTED_ECHO_ROD_ACTIVATE, SoundCategory.BLOCKS);
    }

    @Override
    public void emit(ServerWorld world, BlockPos pos, BlockState state, int power) {
        if(power == 0) return;
        doEffects(world, pos, state.get(FACING));
        SignalManager manager = PersistentManagerState.getServerWorldState(world).signalManager;
        CorruptedSignal signal = new CorruptedSignal(pos.offset(state.get(FACING), 2), state.get(FACING), (byte) power);
        manager.addSignal(signal);
    }

    @Override
    public void processSignal(AbstractSignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state, boolean controlsEffects) {
        incoming.removalFlag = true;

        if(!state.get(POWERED)){
            doEffects(serverWorld, incoming.blockPos, state.get(FACING));
            manager.addSignal(new CorruptedSignal(incoming.blockPos.offset(state.get(FACING), 2), state.get(FACING), incoming.frequency));
        }
    }
}
