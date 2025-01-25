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
    public static final ItemGroup FROG_SIGNALS_TECHNOLOGY = registerItemGroup("frog_signals", FabricItemGroup.builder()
            .icon(() -> new ItemStack(FSItems.ECHO_DUST))
            .displayName(Text.translatable("itemGroup.frog_signals.frog_signals"))
            .entries((context, entries) -> {
                entries.addAll(new ArrayList<>() {
                    {
                        add(FSBlocks.ECHO_ROD.asItem().getDefaultStack());
                        add(FSBlocks.ECHO_HEART.asItem().getDefaultStack());
                        add(FSBlocks.ECHO_LATCH.asItem().getDefaultStack());
                        add(FSBlocks.FLIP_FROG.asItem().getDefaultStack());
                        add(FSBlocks.REFLECTOR.asItem().getDefaultStack());
                        add(FSBlocks.ECHO_BLOCKER.asItem().getDefaultStack());
                        add(FSItems.ECHO_DUST.getDefaultStack());
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
