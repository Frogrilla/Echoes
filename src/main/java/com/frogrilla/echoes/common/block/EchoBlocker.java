package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.signal.SignalInteractionResult;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EchoBlocker extends Block implements iSignalInteractor {
    public EchoBlocker(Properties properties) {
        super(properties);
    }

    @Override
    public SignalInteractionResult processSignal(AbstractSignal incoming, SignalManager manager, ServerLevel serverLevel, BlockState state) {
        serverLevel.playSound(null, incoming.getBlockPos(), SoundEvents.IRON_BREAK, SoundSource.BLOCKS, 1, 1);
        return SignalInteractionResult.DESTROY;
    }
}
