package com.frogrilla.frog_signals.common.block;

import com.frogrilla.frog_signals.signals.Signal;
import com.frogrilla.frog_signals.signals.SignalManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;

public class RibbitBlock extends Block implements ISignalInteractor {

    public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;

    public RibbitBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(TRIGGERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(TRIGGERED) ? 15 : 0;
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(TRIGGERED)){
            world.setBlockState(pos, state.with(TRIGGERED,false));
        }
    }

    @Override
    public void processSignal(Signal incoming, SignalManager manager, ServerWorld serverWorld) {
        BlockState state = serverWorld.getBlockState(incoming.getBlockPos());
        if(!state.get(TRIGGERED)){
            serverWorld.setBlockState(incoming.getBlockPos(), state.with(TRIGGERED, true));
            serverWorld.scheduleBlockTick(incoming.getBlockPos(), this, 4);
        }

        ISignalInteractor.super.processSignal(incoming, manager, serverWorld);
    }
}
