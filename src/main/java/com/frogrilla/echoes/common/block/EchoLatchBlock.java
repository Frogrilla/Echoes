package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class EchoLatchBlock extends Block implements ISignalInteractor {

    public static final IntProperty FREQUENCY = IntProperty.of("frequency", 0, 15);
    public static final BooleanProperty POWERED = Properties.POWERED;
    public EchoLatchBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(FREQUENCY, 0)
                .with(POWERED, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FREQUENCY);
        builder.add(POWERED);
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(FREQUENCY);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos);
        if(state.get(POWERED) != powered){
            world.setBlockState(pos, state.with(POWERED, powered).with(FREQUENCY, 0));
        }
    }

    @Override
    public void processSignal(AbstractSignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state, boolean controlsEffects) {
        if(!state.get(POWERED)){
            serverWorld.setBlockState(incoming.blockPos, state.with(FREQUENCY, (int)incoming.frequency));
        }
        ISignalInteractor.super.processSignal(incoming, manager, serverWorld, state, controlsEffects);
    }

}
