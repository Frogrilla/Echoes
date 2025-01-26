package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.signals.ISignal;
import com.frogrilla.echoes.signals.SignalManager;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;

public interface ISignalInteractor {
    default void processSignal(ISignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        incoming.step();
        incoming.decrement();
        if(incoming.getPower() == 0){
            manager.removeSignal(incoming);
        }
    }
}
