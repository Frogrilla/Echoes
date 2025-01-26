package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.signals.Signal;
import com.frogrilla.echoes.signals.SignalManager;
import net.minecraft.block.BlockState;
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
