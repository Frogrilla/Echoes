package com.frogrilla.frog_signals.common.block;

import com.frogrilla.frog_signals.common.init.FSParticles;
import com.frogrilla.frog_signals.signals.Signal;
import com.frogrilla.frog_signals.signals.SignalManager;
import com.frogrilla.frog_signals.signals.persistentManagerState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class CroakingRodBlock extends Block implements ISignalInteractor{

    public static final EnumProperty<Direction> FACING = Properties.FACING;
    public static final BooleanProperty POWERED = Properties.POWERED;

    public CroakingRodBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.UP).with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(POWERED);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getSide());
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if(world.isClient()) return;
        boolean powered = world.isReceivingRedstonePower(pos);

        if(state.get(POWERED) != powered){
            if(powered){
                ServerWorld serverWorld = (ServerWorld)world;
                Vec3d position = pos.toCenterPos();
                serverWorld.spawnParticles(FSParticles.SIGNAL_STEP, position.x, position.y, position.z, 10, 0, 0, 0, 0.01);
                serverWorld.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_FROG_AMBIENT, SoundCategory.BLOCKS);

                SignalManager manager = persistentManagerState.getServerWorldState((ServerWorld) world).signalManager;
                manager.addSignal(new Signal(pos.offset(state.get(FACING)), world.getReceivedRedstonePower(pos), state.get(FACING)));
            }
            world.setBlockState(pos, state.with(POWERED, powered));
        }
    }

    @Override
    public void processSignal(Signal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        if(state.get(POWERED)){
            manager.removeSignal(incoming);
        }
        else{
            Direction facing = state.get(FACING);
            incoming.setBlockPos(incoming.getBlockPos().offset(facing));
            incoming.setDirection(facing);
            incoming.setPower(SignalManager.defaultPower);
        }
    }
}
