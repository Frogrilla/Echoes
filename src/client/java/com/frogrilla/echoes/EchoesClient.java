package com.frogrilla.echoes;

import com.frogrilla.echoes.common.init.EchoesBlocks;
import com.frogrilla.echoes.common.init.EchoesParticles;
import com.frogrilla.echoes.particle.EchoChargeParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SculkChargePopParticle;
import net.minecraft.client.render.RenderLayer;

public class EchoesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ParticleFactoryRegistry.getInstance().register(EchoesParticles.ECHO_CHARGE, EchoChargeParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(EchoesParticles.CORRUPTED_ECHO_CHARGE, EchoChargeParticle.Factory::new);
		BlockRenderLayerMap.INSTANCE.putBlock(EchoesBlocks.REFLECTOR, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(EchoesBlocks.ECHO_ROD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(EchoesBlocks.CORRUPTED_ECHO_ROD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(EchoesBlocks.ECHO_LOTUS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(EchoesBlocks.CRYSTALISATION_TABLE, RenderLayer.getCutout());
	}
}