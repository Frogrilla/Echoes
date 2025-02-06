package com.frogrilla.echoes;

import com.frogrilla.echoes.common.init.*;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Echoes implements ModInitializer {
	public static final String MOD_ID = "echoes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		EchoesBlocks.registerBlocks();
		EchoesItems.registerItems();
		EchoesParticles.registerParticles();
		EchoesItemGroups.registerItemGroups();
		EchoesSignals.registerTypes();
		EchoesSounds.registerSounds();
	}
}