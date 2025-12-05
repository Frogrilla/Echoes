package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.block.EchoBlocker;
import com.frogrilla.echoes.common.block.EchoFlipper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class EchoesBlocks {

    public static final Block ECHO_BLOCKER = register(
            "echo_blocker",
            EchoBlocker::new,
            BlockBehaviour.Properties.of().sound(SoundType.ANVIL),
            true
    );

    public static final Block ECHO_FLIPPER = register(
            "echo_flipper",
            EchoFlipper::new,
            BlockBehaviour.Properties.of().sound(SoundType.AMETHYST),
            true
    );

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        ResourceKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(settings.setId(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, name));
    }

    public static void registerBlocks(){
        Echoes.LOGGER.info("Registering blocks for " + Echoes.MOD_ID);
    }
}
