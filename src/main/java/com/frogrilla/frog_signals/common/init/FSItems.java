package com.frogrilla.frog_signals.common.init;

import com.frogrilla.frog_signals.FrogSignals;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class FSItems {

    public static final Item ECHO_DUST = Register("echo_dust", new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FrogSignals.MOD_ID, "echo_dust")))));
    public static Item Register(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(FrogSignals.MOD_ID, name), item);
    }

    public static void registerItems(){
        FrogSignals.LOGGER.info("Registering items for " + FrogSignals.MOD_ID);
    }
}
