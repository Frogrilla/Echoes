package com.frogrilla.frog_signals.common.init;

import com.frogrilla.frog_signals.FrogSignals;
import com.frogrilla.frog_signals.common.block.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class FSBlocks {

    public static final Block SIGNAL_TEST_BLOCK = registerBlockWithItem("signal_test_block" ,new SignalTestBlock(AbstractBlock.Settings.copy(Blocks.WEATHERED_COPPER).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FrogSignals.MOD_ID, "test_signal_block")))));

    public static final Block ECHO_ROD = registerBlockWithItem("echo_rod" ,new EchoRodBlock(AbstractBlock.Settings.copy(Blocks.LIGHTNING_ROD).solidBlock(Blocks::never).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FrogSignals.MOD_ID, "croaking_rod")))));

    public static final Block ECHO_HEART = registerBlockWithItem("echo_heart" ,new EchoHeartBlock(AbstractBlock.Settings.copy(Blocks.BONE_BLOCK).solidBlock(Blocks::never).pistonBehavior(PistonBehavior.BLOCK).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FrogSignals.MOD_ID, "blipper")))));

    public static final Block ECHO_LATCH = registerBlockWithItem("echo_latch", new EchoLatchBlock(AbstractBlock.Settings.copy(Blocks.BONE_BLOCK).solidBlock(Blocks::never).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FrogSignals.MOD_ID, "ribbyte")))));

    public static final Block REFLECTOR = registerBlockWithItem("reflector", new ReflectorBlock(AbstractBlock.Settings.copy(Blocks.GLASS).solidBlock(Blocks::never).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FrogSignals.MOD_ID, "reflector")))));

    public static final Block TUFF_BLOCKER = registerBlockWithItem("tuff_blocker", new TuffBlockerBlock(AbstractBlock.Settings.copy(Blocks.CHISELED_TUFF).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FrogSignals.MOD_ID, "tuff_blocker")))));

    public static final Block FLIP_FROG = registerBlockWithItem("flip_frog", new FlipFrogBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_TUFF).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(FrogSignals.MOD_ID, "ochre_flip_frog")))));

    private static Block registerBlock(String name, Block block){
        return Registry.register(Registries.BLOCK, Identifier.of(FrogSignals.MOD_ID, name), block);
    }

    private static Block registerBlockWithItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(FrogSignals.MOD_ID, name), new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FrogSignals.MOD_ID, name)))));
        return Registry.register(Registries.BLOCK, Identifier.of(FrogSignals.MOD_ID, name), block);
    }

    public static void registerBlocks(){
        FrogSignals.LOGGER.info("Registering blocks for " + FrogSignals.MOD_ID);
    }
}
