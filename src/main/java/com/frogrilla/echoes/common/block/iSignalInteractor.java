package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.signal.SignalInteractionResult;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface iSignalInteractor {

    default SignalInteractionResult processSignal(AbstractSignal incoming, SignalManager manager, ServerLevel serverLevel, BlockState state) {
        return SignalInteractionResult.PASS;
    }

}
