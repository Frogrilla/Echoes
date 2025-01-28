package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.common.signal.Signal;
import com.frogrilla.echoes.signal.*;
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
    public void processSignal(AbstractSignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        //Vec3d pos = incoming.getBlockPos().toCenterPos();
        serverWorld.playSound((PlayerEntity) null, incoming.blockPos, SoundEvents.BLOCK_COPPER_GRATE_PLACE, SoundCategory.BLOCKS, 1, 1);

        Direction dir = incoming.direction.rotateClockwise(Direction.Axis.X);
        incoming.refreshProperties();
        incoming.step();
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(world.isClient()) return;
        SignalManager manager = PersistentManagerState.getServerWorldState((ServerWorld) world).signalManager;
        Signal signal = new Signal(pos.up(), Direction.UP, 15);
        manager.addSignal(signal);
    }
}
