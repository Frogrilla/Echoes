package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.signals.ISignal;
import com.frogrilla.echoes.signals.Signal;
import com.frogrilla.echoes.signals.SignalManager;
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
    public static final BooleanProperty SIDE_A = BooleanProperty.of("side_a");
    public static final BooleanProperty SIDE_B = BooleanProperty.of("side_b");

    public ReflectorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(FACING, Direction.UP)
                .with(SIDE_A, true)
                .with(SIDE_B, true)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(SIDE_A);
        builder.add(SIDE_B);
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

    public static BooleanProperty getPropertyFromDirection(Direction facing, Direction incoming){
        if(facing.getAxis() == incoming.getAxis()) return SIDE_A;

        return switch (facing) {
            case UP -> switch (incoming) {
                case SOUTH, WEST -> SIDE_A;
                case NORTH, EAST -> SIDE_B;
                default -> null;
            };
            case DOWN -> switch (incoming) {
                case NORTH, WEST -> SIDE_A;
                case SOUTH, EAST -> SIDE_B;
                default -> null;
            };
            case NORTH -> switch (incoming) {
                case UP, WEST -> SIDE_A;
                case DOWN, EAST -> SIDE_B;
                default -> null;
            };
            case SOUTH -> switch (incoming) {
                case UP, EAST -> SIDE_A;
                case DOWN, WEST -> SIDE_B;
                default -> null;
            };
            case EAST -> switch (incoming) {
                case NORTH, UP -> SIDE_A;
                case SOUTH, DOWN -> SIDE_B;
                default -> null;
            };
            case WEST -> switch (incoming) {
                case SOUTH, UP -> SIDE_A;
                case NORTH, DOWN -> SIDE_B;
                default -> null;
            };
        };

    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(state.get(FACING).getAxis() != hit.getSide().getAxis()){
            BooleanProperty side = getPropertyFromDirection(state.get(FACING), hit.getSide());
            world.setBlockState(pos, state.cycle(side));

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void processSignal(ISignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        if(incoming.getDirection().getAxis() == state.get(FACING).getAxis()) {
            manager.removeSignal(incoming);
            return;
        }

        BooleanProperty side = getPropertyFromDirection(state.get(FACING), incoming.getDirection().getOpposite());
        if(state.get(side)){
            incoming.setDirection(getReflectionDirection(state.get(FACING), incoming.getDirection()));
        }

        ISignalInteractor.super.processSignal(incoming, manager, serverWorld, state);
    }
}
