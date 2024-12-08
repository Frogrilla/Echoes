package com.frogrilla.frog_signals.signals;

import com.frogrilla.frog_signals.FrogSignals;
import com.frogrilla.frog_signals.common.block.ISignalInteractor;
import com.frogrilla.frog_signals.common.init.FSParticles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class SignalManager {

    public final static int ticksPerStep = 1;

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
        signals.forEach(signal ->{
            if(!world.isPosLoaded(signal.getBlockPos())) return;
            if(signal.increment()){
                BlockState state = world.getBlockState(signal.getBlockPos());
                // Signal disrupted
                if(state.getBlock() instanceof ISignalInteractor interactor){
                    interactor.processSignal(signal, this, world);
                    return;
                }

                Vec3d pos = signal.getBlockPos().toCenterPos();
                signal.step();

                // Signal ends
                if (signal.getPower() == 0) {
                    world.spawnParticles(FSParticles.SIGNAL_STEP, pos.x, pos.y, pos.z, 40, 0, 0, 0, 0.1);
                    world.playSound((PlayerEntity) null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_SCULK_SENSOR_BREAK, SoundCategory.BLOCKS, 2, 1);
                    removeSignal(signal);
                }
                else{
                    world.spawnParticles(FSParticles.SIGNAL_STEP, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
                }
            }
//            Vec3d pos = signal.getWorldPos();
//            world.spawnParticles(ParticleTypes.SCULK_CHARGE_POP, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
        });
        updateSignals();
    }

    public void updateSignals() {
        signalBin.forEach(signal -> {
            try {
                signals.remove(signal);
            }
            catch(Exception e){
                FrogSignals.LOGGER.error("Tried to remove a non-existent signal");
            }
        });
        signals.addAll(signalBuffer);
        signalBuffer.clear();
        signalBin.clear();
    }

}