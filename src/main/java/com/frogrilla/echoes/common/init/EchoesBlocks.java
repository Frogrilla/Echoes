package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.block.*;
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

public class EchoesBlocks {
    // Signal interacts
    public static final Block ECHO_ROD = registerBlockWithItem("echo_rod" ,new EchoRodBlock(AbstractBlock.Settings.copy(Blocks.LIGHTNING_ROD).solidBlock(Blocks::never).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_rod")))));
    public static final Block ECHO_HEART = registerBlockWithItem("echo_heart" ,new EchoHeartBlock(AbstractBlock.Settings.copy(Blocks.BONE_BLOCK).solidBlock(Blocks::never).pistonBehavior(PistonBehavior.BLOCK).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_heart")))));
    public static final Block ECHO_LATCH = registerBlockWithItem("echo_latch", new EchoLatchBlock(AbstractBlock.Settings.copy(Blocks.BONE_BLOCK).solidBlock(Blocks::never).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_latch")))));
    public static final Block FLIP_FROG = registerBlockWithItem("flip_frog", new FlipFrogBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_TUFF).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "flip_frog")))));
    public static final Block REFLECTOR = registerBlockWithItem("reflector", new ReflectorBlock(AbstractBlock.Settings.copy(Blocks.GLASS).solidBlock(Blocks::never).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "reflector")))));
    public static final Block ECHO_BLOCKER = registerBlockWithItem("echo_blocker", new EchoBlockerBlock(AbstractBlock.Settings.copy(Blocks.CHISELED_TUFF).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_blocker")))));
    public static final Block ECHO_LOTUS = registerBlockWithItem("echo_lotus", new EchoLotusBlock(AbstractBlock.Settings.copy(Blocks.SPORE_BLOSSOM).solidBlock(Blocks::never).nonOpaque().pistonBehavior(PistonBehavior.DESTROY).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_lotus")))));
    public static final Block ECHO_BOOSTER = registerBlockWithItem("echo_booster", new EchoBoosterBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DEEPSLATE).solidBlock(Blocks::never).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_booster")))));
    public static final Block ECHO_MODULATOR = registerBlockWithItem("echo_modulator", new EchoModulatorBlock(AbstractBlock.Settings.copy(Blocks.BONE_BLOCK).solidBlock(Blocks::never).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_modulator")))));


    // Extra hidden blocks
    public static final Block CORRUPTED_ECHO_ROD = registerBlockWithItem("corrupted_echo_rod" ,new CorruptedEchoRodBlock(AbstractBlock.Settings.copy(Blocks.LIGHTNING_ROD).solidBlock(Blocks::never).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "corrupted_echo_rod")))));
    public static final Block SIGNAL_TEST_BLOCK = registerBlockWithItem("signal_test_block" ,new SignalTestBlock(AbstractBlock.Settings.copy(Blocks.WEATHERED_COPPER).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "test_signal_block")))));

    private static Block registerBlock(String name, Block block){
        return Registry.register(Registries.BLOCK, Identifier.of(Echoes.MOD_ID, name), block);
    }

    private static Block registerBlockWithItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(Echoes.MOD_ID, name), new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Echoes.MOD_ID, name)))));
        return Registry.register(Registries.BLOCK, Identifier.of(Echoes.MOD_ID, name), block);
    }

    public static void registerBlocks(){
        Echoes.LOGGER.info("Registering blocks for " + Echoes.MOD_ID);
    }
}
