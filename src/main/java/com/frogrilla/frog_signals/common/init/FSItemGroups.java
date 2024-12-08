package com.frogrilla.frog_signals.common.init;

import com.frogrilla.frog_signals.FrogSignals;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class FSItemGroups {
    public static final ItemGroup FROG_SIGNALS_TECHNOLOGY = registerItemGroup("frog_signals_technology", FabricItemGroup.builder()
            .icon(() -> new ItemStack(FSBlocks.OCHRE_FLIP_FROG.asItem()))
            .displayName(Text.translatable("itemGroup.frog_signals.frog_signals_technology"))
            .entries((context, entries) -> {
                entries.addAll(new ArrayList<>() {
                    {
                        add(FSBlocks.SIGNAL_TEST_BLOCK.asItem().getDefaultStack());
                        add(FSBlocks.CROAKING_ROD.asItem().getDefaultStack());
                        add(FSBlocks.RIBBIT_BLOCK.asItem().getDefaultStack());
                        add(FSBlocks.TUFF_BLOCKER.asItem().getDefaultStack());
                        add(FSBlocks.OCHRE_FLIP_FROG.asItem().getDefaultStack());
                        add(FSBlocks.VERDANT_FLIP_FROG.asItem().getDefaultStack());
                        add(FSBlocks.PEARLESCENT_FLIP_FROG.asItem().getDefaultStack());
                    }
                });
            })
            .build()
    );

    private static ItemGroup registerItemGroup(String name, ItemGroup group){
        return Registry.register(Registries.ITEM_GROUP, Identifier.of(FrogSignals.MOD_ID, name), group);
    }

    public static void registerItemGroups(){
        FrogSignals.LOGGER.info("Registering item groups for " + FrogSignals.MOD_ID);
    }
}
