package com.frogrilla.frog_signals;

import com.frogrilla.frog_signals.common.init.*;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrogSignals implements ModInitializer {
	public static final String MOD_ID = "frog-signals";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		FSBlocks.registerBlocks();
		FSItems.registerItems();
		FSParticles.registerParticles();
		FSCommands.registerCommands();
		FSItemGroups.registerItemGroups();
	}
}