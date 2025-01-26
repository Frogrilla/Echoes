package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.signals.ISignal;
import com.frogrilla.echoes.signals.Signal;
import com.frogrilla.echoes.signals.SignalManager;
import com.frogrilla.echoes.signals.PersistentManagerState;
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
    public void processSignal(ISignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        //Vec3d pos = incoming.getBlockPos().toCenterPos();
        serverWorld.playSound((PlayerEntity) null, incoming.getBlockPos(), SoundEvents.BLOCK_COPPER_GRATE_PLACE, SoundCategory.BLOCKS, 1, 1);

        Direction dir = incoming.getDirection().rotateClockwise(Direction.Axis.X);
        incoming.setDirection(dir);
        incoming.step();
        incoming.setPower(16);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(world.isClient()) return;
        SignalManager manager = PersistentManagerState.getServerWorldState((ServerWorld) world).signalManager;
        Signal signal = new Signal();
        signal.setBlockPos(pos.up());
        signal.setFrequency(15);
        signal.setDirection(Direction.UP);
        manager.addSignal(signal);
    }
}
