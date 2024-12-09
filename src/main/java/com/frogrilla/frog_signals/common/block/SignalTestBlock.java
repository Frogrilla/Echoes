package com.frogrilla.frog_signals.common.block;

import com.frogrilla.frog_signals.signals.Signal;
import com.frogrilla.frog_signals.signals.SignalManager;
import com.frogrilla.frog_signals.signals.persistentManagerState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SignalTestBlock extends Block implements ISignalInteractor {
    public SignalTestBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void processSignal(Signal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        //Vec3d pos = incoming.getBlockPos().toCenterPos();
        serverWorld.playSound((PlayerEntity) null, incoming.getBlockPos(), SoundEvents.BLOCK_COPPER_GRATE_PLACE, SoundCategory.BLOCKS, 1, 1);

        Direction dir = incoming.getDirection().rotateClockwise(Direction.Axis.X);
        incoming.setBlockPos(incoming.getBlockPos().offset(dir));
        incoming.setDirection(dir);
        incoming.setPower(16);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(world.isClient()) return;
        SignalManager manager = persistentManagerState.getServerWorldState((ServerWorld) world).signalManager;
        manager.addSignal(new Signal(pos.up(), 15, Direction.UP));
    }
}
