package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.common.signal.PulseSignal;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EchoLotusBlock extends Block implements ISignalInteractor{

    public static final Property<Direction> FACING = Properties.FACING;

    public static final VoxelShape UP = Block.createCuboidShape(2, 0, 2, 14, 2, 14);
    public static final VoxelShape DOWN = Block.createCuboidShape(2, 14, 2, 14, 16, 14);
    public static final VoxelShape NORTH = Block.createCuboidShape(2, 2, 14, 14, 14, 16);
    public static final VoxelShape WEST = Block.createCuboidShape(14, 2, 2, 16, 14, 14);
    public static final VoxelShape SOUTH = Block.createCuboidShape(2, 2, 0, 14, 14, 2);
    public static final VoxelShape EAST = Block.createCuboidShape(0, 2, 2, 2, 14, 14);

    public EchoLotusBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(FACING, Direction.UP)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getSide());
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch(state.get(FACING)){
            case DOWN -> DOWN;
            case UP -> UP;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }

    @Override
    public void processSignal(AbstractSignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state, boolean controlsEffects) {
        if(controlsEffects) incoming.effects(serverWorld, incoming.blockPos.toCenterPos());
        if(incoming.direction != state.get(FACING).getOpposite()){
            incoming.tick();
        }
        else{
            incoming.removalFlag = true;
            manager.addSignal(new PulseSignal(incoming.blockPos.offset(incoming.direction), incoming.direction, incoming.frequency));
        }
    }
}
