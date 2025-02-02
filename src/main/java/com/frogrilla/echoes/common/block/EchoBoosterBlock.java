package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;

public class EchoBoosterBlock extends Block implements ISignalInteractor {
    public EchoBoosterBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public void processSignal(AbstractSignal incoming, SignalManager manager, ServerWorld serverWorld, BlockState state, boolean controlsEffects) {
        if(controlsEffects) incoming.effects(serverWorld, incoming.blockPos.toCenterPos());

        incoming.step();
        incoming.refreshProperties();
    }

}
