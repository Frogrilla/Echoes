package com.frogrilla.frog_signals.common.block;

import com.frogrilla.frog_signals.signals.Signal;
import com.frogrilla.frog_signals.signals.SignalManager;
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

    public static final BooleanProperty ENABLED = Properties.ENABLED;

    public FlipFrogBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ENABLED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }

    @Override
    public void processSignal(Signal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        if(state.get(ENABLED)){
            serverWorld.playSound((PlayerEntity) null, incoming.getBlockPos(), SoundEvents.BLOCK_COPPER_BULB_TURN_OFF, SoundCategory.BLOCKS, 2, 1);
        }
        else{
            serverWorld.playSound((PlayerEntity) null, incoming.getBlockPos(), SoundEvents.BLOCK_COPPER_BULB_TURN_ON, SoundCategory.BLOCKS, 2, 1);
        }

        serverWorld.setBlockState(incoming.getBlockPos(), serverWorld.getBlockState(incoming.getBlockPos()).cycle(ENABLED));
        ISignalInteractor.super.processSignal(incoming, manager, serverWorld, state);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(ENABLED) ? 15 : 0;
    }

}
