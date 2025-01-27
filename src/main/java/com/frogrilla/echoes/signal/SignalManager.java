package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.block.ISignalInteractor;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignalManager {

    public final static int defaultPower = 15;

    public List<AbstractSignal> signals = new ArrayList<>();
    public List<AbstractSignal> signalBuffer = new ArrayList<>();
    public List<AbstractSignal> signalBin = new ArrayList<>();

    public void addSignal(AbstractSignal signal){
        signalBuffer.add(signal);
    }

    public void removeSignal(AbstractSignal signal){
        signalBin.add(signal);
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

                signal.defaultTick(this, world);
            }
        });
        updateSignals();
    }

    public void updateSignals() {
        signalBin.forEach(signal -> {
            try {
                signals.remove(signal);
            }
            catch(Exception e){
                Echoes.LOGGER.error("Tried to remove a non-existent signal");
            }
        });
        signals.addAll(signalBuffer);
        signalBuffer.clear();
        signalBin.clear();
    }

}