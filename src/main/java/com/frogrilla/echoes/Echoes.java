package com.frogrilla.echoes;

import com.frogrilla.echoes.common.init.*;
import com.frogrilla.echoes.signal.DirectionArgument;
import com.frogrilla.echoes.signal.SignalTypeArgument;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.Util;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.JukeboxSong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Echoes implements ModInitializer {
	public static final String MOD_ID = "echoes";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

        EchoesSignalTypes.registerSignalTypes();
        EchoesBlocks.registerBlocks();
        EchoesItems.registerItems();
        EchoesPacketPayloads.registerPacketPayloads();
        EchoesAttatchmentTypes.registerAttatchmentTypes();

        ArgumentTypeRegistry.registerArgumentType(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "signal_type"),
                SignalTypeArgument.class,
                SingletonArgumentInfo.contextFree(SignalTypeArgument::new)
        );

        ArgumentTypeRegistry.registerArgumentType(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "direction"),
                DirectionArgument.class,
                SingletonArgumentInfo.contextFree(DirectionArgument::new)
        );

        EchoesCommands.registerCommands();

		LOGGER.info("Initialized echoes");
	}
}