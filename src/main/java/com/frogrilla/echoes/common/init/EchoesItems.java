package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.item.SignalSpawner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class EchoesItems {

    public static final Item TEST_ITEM = register("test_item", SignalSpawner::new, new Item.Properties());

    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void registerItems(){
        Echoes.LOGGER.info("Registering items for " + Echoes.MOD_ID);
    }
}
