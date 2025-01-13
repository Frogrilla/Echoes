package com.frogrilla.frog_signals.common.block;

import com.frogrilla.frog_signals.FrogSignals;
import com.frogrilla.frog_signals.signals.Signal;
import com.frogrilla.frog_signals.signals.SignalManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlipperBlock extends PillarBlock implements ISignalInteractor {

    public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;
    public static final IntProperty POWER = Properties.POWER;

    public BlipperBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(TRIGGERED, false).with(POWER, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(TRIGGERED);
        builder.add(POWER);
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
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(POWER);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(TRIGGERED)){
            world.setBlockState(pos, state.with(TRIGGERED,false).with(POWER, 0));
        }
    }

    @Override
    public void processSignal(Signal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        if(!state.get(TRIGGERED)){
            serverWorld.playSound((PlayerEntity) null, incoming.getBlockPos(), SoundEvents.BLOCK_FROGSPAWN_BREAK, SoundCategory.BLOCKS);
            serverWorld.setBlockState(incoming.getBlockPos(), state.with(TRIGGERED, true).with(POWER, incoming.getFrequency()));
            serverWorld.scheduleBlockTick(incoming.getBlockPos(), this, 2);
        }

        ISignalInteractor.super.processSignal(incoming, manager, serverWorld, state);
    }
}
