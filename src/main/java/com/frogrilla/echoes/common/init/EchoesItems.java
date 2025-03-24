package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class EchoesItems {

    public static final Item ECHO_FRAGMENTS = Register("echo_fragments", new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Echoes.MOD_ID, "echo_fragments")))));
    public static final Item CORRUPTED_ECHO_SHARD = Register("corrupted_echo_shard", new Item(new Item.Settings().rarity(Rarity.UNCOMMON).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Echoes.MOD_ID, "corrupted_echo_shard")))));
    public static final Item CORRUPTED_ECHO_FRAGMENTS = Register("corrupted_echo_fragments", new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Echoes.MOD_ID, "corrupted_echo_fragments")))));
    public static Item Register(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(Echoes.MOD_ID, name), item);
    }

    public static void registerItems(){
        Echoes.LOGGER.info("Registering items for " + Echoes.MOD_ID);
    }
}
