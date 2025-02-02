package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class EchoModulatorBlock extends Block implements ISignalInteractor {

    public static final IntProperty POWER = Properties.POWER;
    public EchoModulatorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(POWER, 0)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        return state.with(POWER, world.getReceivedRedstonePower(pos));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(POWER, ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos()));
    }

    @Override
    public void processSignal(AbstractSignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state, boolean controlsEffects) {
        incoming.frequency = state.get(POWER).byteValue();
        incoming.tick();
    }
}
