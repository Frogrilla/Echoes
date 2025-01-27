package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.signal.CorruptedSignal;
import com.frogrilla.echoes.signal.PersistentManagerState;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class CorruptedEchoRodBlock extends EchoRodBlock{
    public CorruptedEchoRodBlock(Settings settings) {
        super(settings);
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
                SignalManager manager = PersistentManagerState.getServerWorldState((ServerWorld) world).signalManager;
                CorruptedSignal signal = new CorruptedSignal(pos.offset(state.get(FACING), 2), state.get(FACING), power);
                manager.addSignal(signal);
            }
            world.setBlockState(pos, state.with(POWERED, powered));
        }
    }
}
