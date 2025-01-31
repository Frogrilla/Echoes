package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.common.block.ISignalInteractor;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignalManager {

    public List<AbstractSignal> signals = new ArrayList<>();
    public List<AbstractSignal> signalBuffer = new ArrayList<>();

    public void addSignal(AbstractSignal signal){
        signalBuffer.add(signal);
    }

    public void removeSignalAt(int index){
        signals.get(index).removalFlag = true;
    }

    public void tickSignals(ServerWorld world){
        Collections.shuffle(signals);

        signals.forEach(signal ->{
            if(!world.isPosLoaded(signal.blockPos)) return;
            signal.preTick();
            if(signal.shouldTick()){
                BlockState state = world.getBlockState(signal.blockPos);

                // Signal disrupted by interactor
                if(state.getBlock() instanceof ISignalInteractor interactor){
                    interactor.processSignal(signal, this, world, state);
                    return;
                }

                signal.effects(world, signal.blockPos.toCenterPos());
                signal.tick(this);
            }
        });
        updateSignals();
    }

    public void updateSignals() {
        signals.removeIf(signal -> signal.removalFlag);
        signals.addAll(signalBuffer);
        signalBuffer.clear();
    }

}