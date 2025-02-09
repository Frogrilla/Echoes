package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.init.EchoesSounds;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class FlipFrogBlock extends Block implements ISignalInteractor {

    public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;

    public FlipFrogBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(TRIGGERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }

    @Override
    public void processSignal(AbstractSignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state, boolean controlsEffects) {
        if(state.get(TRIGGERED)){
            serverWorld.playSound((PlayerEntity) null, incoming.blockPos, EchoesSounds.FLIP_FROG_OFF, SoundCategory.BLOCKS);
        }
        else{
            serverWorld.playSound((PlayerEntity) null, incoming.blockPos, EchoesSounds.FLIP_FROG_ON, SoundCategory.BLOCKS);
        }

        serverWorld.setBlockState(incoming.blockPos, serverWorld.getBlockState(incoming.blockPos).cycle(TRIGGERED));
        ISignalInteractor.super.processSignal(incoming, manager, serverWorld, state, controlsEffects);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(TRIGGERED) ? 15 : 0;
    }

}
