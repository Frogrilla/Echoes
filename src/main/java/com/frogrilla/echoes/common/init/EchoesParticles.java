package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EchoesParticles {

    public static final SimpleParticleType SIGNAL_STEP =  FabricParticleTypes.simple();

    public static void registerParticles(){
        Echoes.LOGGER.info("Registering particles for " + Echoes.MOD_ID);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Echoes.MOD_ID, "signal_step"), SIGNAL_STEP);
    }
}
