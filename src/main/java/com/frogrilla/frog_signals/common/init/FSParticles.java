package com.frogrilla.frog_signals.common.init;

import com.frogrilla.frog_signals.FrogSignals;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FSParticles {

    public static final SimpleParticleType SIGNAL_STEP =  FabricParticleTypes.simple();

    public static void registerParticles(){
        FrogSignals.LOGGER.info("Registering particles for " + FrogSignals.MOD_ID);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(FrogSignals.MOD_ID, "signal_step"), SIGNAL_STEP);
    }
}
