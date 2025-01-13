package com.frogrilla.frog_signals.common.block;

import com.frogrilla.frog_signals.common.init.FSParticles;
import com.frogrilla.frog_signals.signals.Signal;
import com.frogrilla.frog_signals.signals.SignalManager;
import com.frogrilla.frog_signals.signals.persistentManagerState;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.block.RodBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.Vibrations;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.Arrays;
import java.util.stream.Stream;

public class CroakingRodBlock extends RodBlock implements ISignalInteractor{

    public static final MapCodec<CroakingRodBlock> CODEC = createCodec(CroakingRodBlock::new);
    public static final BooleanProperty POWERED = Properties.POWERED;

    public CroakingRodBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.UP).with(POWERED, false));
    }

    @Override
    protected MapCodec<? extends RodBlock> getCodec() {
        return null;
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

    public static void doEffects(ServerWorld world, BlockPos pos, Direction direction){
        Vec3d position = pos.toCenterPos().add(direction.getDoubleVector().multiply(0.365d));
        //world.spawnParticles(ParticleTypes.SONIC_BOOM, position.x, position.y, position.z, 1, 0, 0, 0, 0);
        world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_FROG_AMBIENT, SoundCategory.BLOCKS);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if(world.isClient()) return;

        int power = 0;
        for (Direction direction : DIRECTIONS) {
            if(direction == state.get(FACING)) continue;
            BlockPos check = pos.offset(direction);
            power = Math.max(power, world.getEmittedRedstonePower(check, direction.getOpposite()));
        }

        boolean powered = power > 0;

        if(state.get(POWERED) != powered){
            if(powered){
                doEffects((ServerWorld) world, pos, state.get(FACING));
                SignalManager manager = persistentManagerState.getServerWorldState((ServerWorld) world).signalManager;
                manager.addSignal(new Signal(pos.offset(state.get(FACING)), power, state.get(FACING)));
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
            doEffects(serverWorld, incoming.getBlockPos(), facing);
            incoming.setBlockPos(incoming.getBlockPos().offset(facing));
            incoming.setDirection(facing);
            incoming.setPower(SignalManager.defaultPower);
        }
    }
}
