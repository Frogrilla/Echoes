package com.frogrilla.echoes.signals;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.block.ISignalInteractor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignalManager {

    public final static int ticksPerStep = 1;
    public final static int defaultPower = 15;

    public List<Signal> signals = new ArrayList<>();
    public List<Signal> signalBuffer = new ArrayList<>();
    public List<Signal> signalBin = new ArrayList<>();

    public void addSignal(Signal signal){
        signalBuffer.add(signal);
    }

    public void removeSignal(Signal signal){
        signalBin.add(signal);
    }

    public void tickSignals(ServerWorld world){
        Collections.shuffle(signals);
        signals.forEach(signal ->{
            if(!world.isPosLoaded(signal.getBlockPos())) return;
            signal.increment();
            if(signal.getCounter() == 0){
                BlockState state = world.getBlockState(signal.getBlockPos());

                // Signal disrupted by interactor
                if(state.getBlock() instanceof ISignalInteractor interactor){
                    interactor.processSignal(signal, this, world, state);
                    return;
                }

                Vec3d pos = signal.getBlockPos().toCenterPos();

                // Signal ends
                if (signal.getPower() == 1) {
                    world.spawnParticles(ParticleTypes.SCULK_CHARGE_POP, pos.x, pos.y, pos.z, 40, 0, 0, 0, 0.1);
                    world.playSound((PlayerEntity) null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_SCULK_SENSOR_BREAK, SoundCategory.BLOCKS, 2, 1);
                    removeSignal(signal);
                    return;
                }

                // Signal continues to next block
                world.spawnParticles(ParticleTypes.SCULK_CHARGE_POP, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
                signal.step();
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