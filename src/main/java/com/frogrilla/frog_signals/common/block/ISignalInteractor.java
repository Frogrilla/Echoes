package com.frogrilla.frog_signals.common.block;

import com.frogrilla.frog_signals.signals.Signal;
import com.frogrilla.frog_signals.signals.SignalManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;

public interface ISignalInteractor {
    // If returns true - deletes the processed signal, otherwise it continues
    default void processSignal(Signal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        incoming.step();
        if(incoming.getPower() == 0){
            manager.removeSignal(incoming);
        }
    }
}
