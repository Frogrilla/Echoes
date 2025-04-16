package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.screen.CrystalisationTableScreenHandler;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class EchoesScreenHandlers {

    public static final ScreenHandlerType<CrystalisationTableScreenHandler> CRYSTALISATION_TABLE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Echoes.MOD_ID, "crystalisation_table"), new ScreenHandlerType<>(CrystalisationTableScreenHandler::new, FeatureSet.empty()));

    public static void registerParticles(){
        Echoes.LOGGER.info("Registering screen handlers for " + Echoes.MOD_ID);
    }
}
