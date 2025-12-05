package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.signal.SignalInteractionResult;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EchoFlipper extends Block implements iSignalInteractor {
    public EchoFlipper(Properties properties) {
        super(properties);
    }

    @Override
    public SignalInteractionResult processSignal(AbstractSignal incoming, SignalManager manager, ServerLevel serverLevel, BlockState state) {
        incoming.setDirection(incoming.getDirection().getOpposite());
        serverLevel.playSound(null, incoming.getBlockPos(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1, 1);
        return SignalInteractionResult.MODIFIED;
    }
}
