package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;

public interface ISignalInteractor {
    default void processSignal(AbstractSignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state) {
        incoming.tick(manager);
    }
}
