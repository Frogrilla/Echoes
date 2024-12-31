package com.frogrilla.frog_signals;

import com.frogrilla.frog_signals.common.init.FSBlocks;
import com.frogrilla.frog_signals.common.init.FSParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.SculkChargePopParticle;
import net.minecraft.client.render.RenderLayer;

public class FrogSignalsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ParticleFactoryRegistry.getInstance().register(FSParticles.SIGNAL_STEP, SculkChargePopParticle.Factory::new);
		BlockRenderLayerMap.INSTANCE.putBlock(FSBlocks.REFLECTOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(FSBlocks.CROAKING_ROD, RenderLayer.getCutout());
	}
}