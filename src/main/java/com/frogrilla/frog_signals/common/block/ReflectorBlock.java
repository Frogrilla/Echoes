package com.frogrilla.frog_signals.common.block;

import com.frogrilla.frog_signals.signals.Signal;
import com.frogrilla.frog_signals.signals.SignalManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ReflectorBlock extends Block implements ISignalInteractor {

    public static final Property<Direction> FACING = Properties.FACING;
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");

    public ReflectorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(FACING, Direction.UP)
                .with(UP, true)
                .with(DOWN, true)
                .with(NORTH, true)
                .with(EAST, true)
                .with(SOUTH, true)
                .with(WEST, true)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(UP);
        builder.add(DOWN);
        builder.add(NORTH);
        builder.add(EAST);
        builder.add(SOUTH);
        builder.add(WEST);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = getDefaultState();
        if(ctx.getPlayer().isSneaking()){
            state = state.with(FACING, ctx.getSide().getOpposite());
        }
        else{
            state = state.with(FACING, ctx.getSide());
        }

        state = state.with(getPropertyFromDirection(ctx.getSide()), false).with(getPropertyFromDirection(ctx.getSide().getOpposite()), false);
        return state;
    }

    public static Direction getReflectionDirection(Direction facing, Direction incoming){
        if(facing.getAxis() == incoming.getAxis()) return incoming;

        switch (facing){
            case UP:
                switch (incoming) {
                    case NORTH -> {
                        return Direction.WEST;
                    }
                    case WEST -> {
                        return Direction.NORTH;
                    }

                    case SOUTH -> {
                        return Direction.EAST;
                    }
                    case EAST -> {
                        return Direction.SOUTH;
                    }
                }
            case DOWN:
                switch (incoming) {
                    case NORTH -> {
                        return Direction.EAST;
                    }
                    case EAST -> {
                        return Direction.NORTH;
                    }

                    case SOUTH -> {
                        return Direction.WEST;
                    }
                    case WEST -> {
                        return Direction.SOUTH;
                    }
                }
            case NORTH:
                switch (incoming) {
                    case UP -> {
                        return Direction.EAST;
                    }
                    case EAST -> {
                        return Direction.UP;
                    }
                    case DOWN -> {
                        return Direction.WEST;
                    }
                    case WEST -> {
                        return Direction.DOWN;
                    }
                }
            case EAST:
                switch (incoming) {
                    case NORTH -> {
                        return Direction.DOWN;
                    }
                    case DOWN -> {
                        return Direction.NORTH;
                    }
                    case SOUTH -> {
                        return Direction.UP;
                    }
                    case UP -> {
                        return Direction.SOUTH;
                    }
                }
            case SOUTH:
                switch (incoming) {
                    case UP -> {
                        return Direction.WEST;
                    }
                    case WEST -> {
                        return Direction.UP;
                    }
                    case DOWN -> {
                        return Direction.EAST;
                    }
                    case EAST -> {
                        return Direction.DOWN;
                    }
                }
            case WEST:
                switch (incoming) {
                    case NORTH -> {
                        return Direction.UP;
                    }
                    case UP -> {
                        return Direction.NORTH;
                    }
                    case SOUTH -> {
                        return Direction.DOWN;
                    }
                    case DOWN -> {
                        return Direction.SOUTH;
                    }
                }
        }

        return incoming;
    }

    public static BooleanProperty getPropertyFromDirection(Direction direction){
        switch (direction){
            case UP -> {
                return UP;
            }
            case DOWN -> {
                return DOWN;
            }
            case NORTH -> {
                return NORTH;
            }
            case EAST -> {
                return EAST;
            }
            case SOUTH -> {
                return SOUTH;
            }
            case WEST -> {
                return WEST;
            }
        }
        return UP;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(state.get(FACING).getAxis() != hit.getSide().getAxis()){
            BooleanProperty side = getPropertyFromDirection(hit.getSide());
            world.setBlockState(pos, state.cycle(side));

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void processSignal(Signal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        BooleanProperty side = getPropertyFromDirection(incoming.getDirection().getOpposite());
        if(state.get(side)){
            incoming.setDirection(getReflectionDirection(state.get(FACING), incoming.getDirection()));
        }

        ISignalInteractor.super.processSignal(incoming, manager, serverWorld, state);
    }
}
