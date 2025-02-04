package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.init.EchoesParticles;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.common.signal.PulseSignal;
import com.frogrilla.echoes.common.signal.Signal;
import com.frogrilla.echoes.signal.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RodBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class EchoRodBlock extends RodBlock implements ISignalInteractor{

    public static final MapCodec<EchoRodBlock> CODEC = createCodec(EchoRodBlock::new);
    public static final BooleanProperty POWERED = Properties.POWERED;

    public EchoRodBlock(Settings settings) {
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
        BlockState state = super.getPlacementState(ctx).with(FACING, ctx.getSide());
        return state.with(POWERED, getPowered(ctx.getWorld(), ctx.getBlockPos(), state));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(world.isClient()) return;
        if(state.get(POWERED)){
            emit((ServerWorld) world, pos, state, getPower(world, pos, state));
        }
    }

    public void doEffects(ServerWorld world, BlockPos pos, Direction direction){
        Vec3d position = pos.toCenterPos().add(direction.getDoubleVector().multiply(0.365d));
        world.spawnParticles(EchoesParticles.ECHO_CHARGE, position.x, position.y, position.z, 1, 0, 0, 0, 0);
        world.playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_SCULK_CHARGE, SoundCategory.BLOCKS);
    }

    public int getPower(World world, BlockPos pos, BlockState state){
        int power = 0;
        for(Direction direction: DIRECTIONS){
            if (direction != state.get(FACING) && world.isEmittingRedstonePower(pos.offset(direction), direction)) {
                power = Math.max(power, world.getEmittedRedstonePower(pos.offset(direction), direction));
            }
        }
        return power;
    }

    public boolean getPowered(World world, BlockPos pos, BlockState state){
        for(Direction direction: DIRECTIONS){
            if (direction != state.get(FACING) && world.isEmittingRedstonePower(pos.offset(direction), direction)) {
                return true;
            }
        }
        return false;
    }

    public void emit(ServerWorld world, BlockPos pos, BlockState state, int power){
        if(power == 0) return;
        doEffects(world, pos, state.get(FACING));
        SignalManager manager = PersistentManagerState.getServerWorldState(world).signalManager;
        Signal signal = new Signal(pos.offset(state.get(FACING)), state.get(FACING), (byte) power);
        manager.addSignal(signal);
    }


    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if(world.isClient()) return;

        int power = getPower(world, pos, state);

        boolean powered = power > 0;

        if(state.get(POWERED) != powered){
            if(powered) emit((ServerWorld) world, pos, state, power);
            world.setBlockState(pos, state.with(POWERED, powered));
        }
    }

    @Override
    public void processSignal(AbstractSignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state, boolean controlsEffects) {
        incoming.removalFlag = true;

        if(!state.get(POWERED)){
            doEffects(serverWorld, incoming.blockPos, state.get(FACING));
            manager.addSignal(new Signal(incoming.blockPos.offset(state.get(FACING)), state.get(FACING), incoming.frequency));
        }
    }
}
