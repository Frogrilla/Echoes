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
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class EchoesBlocks {
    // Signal interacts
    public static final Block ECHO_ROD = registerBlockWithItem("echo_rod" ,new EchoRodBlock(AbstractBlock.Settings.create().breakInstantly().solidBlock(Blocks::never).nonOpaque().sounds(BlockSoundGroup.BONE).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_rod")))));
    public static final Block ECHO_HEART = registerBlockWithItem("echo_heart" ,new EchoHeartBlock(AbstractBlock.Settings.create().strength(1F).solidBlock(Blocks::never).pistonBehavior(PistonBehavior.BLOCK).nonOpaque().sounds(BlockSoundGroup.BONE).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_heart")))));
    public static final Block ECHO_LATCH = registerBlockWithItem("echo_latch", new EchoLatchBlock(AbstractBlock.Settings.create().strength(1F).sounds(BlockSoundGroup.BONE).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_latch")))));
    public static final Block FLIP_FROG = registerBlockWithItem("flip_frog", new FlipFrogBlock(AbstractBlock.Settings.create().strength(1F).solidBlock(Blocks::never).sounds(BlockSoundGroup.POLISHED_TUFF).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "flip_frog")))));
    public static final Block REFLECTOR = registerBlockWithItem("reflector", new ReflectorBlock(AbstractBlock.Settings.create().solidBlock(Blocks::never).nonOpaque().sounds(BlockSoundGroup.DEEPSLATE_TILES).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "reflector")))));
    public static final Block ECHO_BLOCKER = registerBlockWithItem("echo_blocker", new EchoBlockerBlock(AbstractBlock.Settings.create().strength(1F).sounds(BlockSoundGroup.BONE).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_blocker")))));
    public static final Block ECHO_LOTUS = registerBlockWithItem("echo_lotus", new EchoLotusBlock(AbstractBlock.Settings.create().breakInstantly().solidBlock(Blocks::never).nonOpaque().pistonBehavior(PistonBehavior.DESTROY).sounds(BlockSoundGroup.SPORE_BLOSSOM).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_lotus")))));
    public static final Block ECHO_BOOSTER = registerBlockWithItem("echo_booster", new EchoBoosterBlock(AbstractBlock.Settings.create().strength(1F).solidBlock(Blocks::never).nonOpaque().sounds(BlockSoundGroup.BONE).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_booster")))));
    public static final Block ECHO_MODULATOR = registerBlockWithItem("echo_modulator", new EchoModulatorBlock(AbstractBlock.Settings.create().strength(1F).solidBlock(Blocks::never).sounds(BlockSoundGroup.METAL).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "echo_modulator")))));
    public static final Block CRYSTALISATION_TABLE = registerBlockWithItem("crystalisation_table", new CrystalisationTableBlock(AbstractBlock.Settings.create().strength(3F).nonOpaque().solidBlock(Blocks::never).pistonBehavior(PistonBehavior.IGNORE).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "crystalisation_table")))));

    // Extra hidden blocks
    public static final Block CORRUPTED_ECHO_ROD = registerBlockWithItem("corrupted_echo_rod" ,new CorruptedEchoRodBlock(AbstractBlock.Settings.create().breakInstantly().solidBlock(Blocks::never).nonOpaque().sounds(BlockSoundGroup.BONE).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "corrupted_echo_rod")))));
    public static final Block SIGNAL_TEST_BLOCK = registerBlockWithItem("signal_test_block" ,new SignalTestBlock(AbstractBlock.Settings.create().strength(-1f).sounds(BlockSoundGroup.COPPER).registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Echoes.MOD_ID, "test_signal_block")))));

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
