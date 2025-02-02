package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class EchoesItemGroups {
    public static final ItemGroup TECHNOLOGY = registerItemGroup("technology", FabricItemGroup.builder()
        .icon(() -> new ItemStack(EchoesBlocks.ECHO_ROD))
        .displayName(Text.translatable("itemGroup.echoes.technology"))
        .entries((context, entries) -> {
            entries.addAll(new ArrayList<>() {
                {
                    add(EchoesBlocks.ECHO_ROD.asItem().getDefaultStack());
                    add(EchoesBlocks.ECHO_HEART.asItem().getDefaultStack());
                    add(EchoesBlocks.ECHO_LATCH.asItem().getDefaultStack());
                    add(EchoesBlocks.FLIP_FROG.asItem().getDefaultStack());
                    add(EchoesBlocks.REFLECTOR.asItem().getDefaultStack());
                    add(EchoesBlocks.ECHO_BLOCKER.asItem().getDefaultStack());
                    add(EchoesBlocks.ECHO_LOTUS.asItem().getDefaultStack());
                    add(EchoesBlocks.ECHO_BOOSTER.asItem().getDefaultStack());
                    add(EchoesBlocks.CORRUPTED_ECHO_ROD.asItem().getDefaultStack());
                }
            });
        })
        .build()
    );

    public static final ItemGroup MATERIALS = registerItemGroup("materials", FabricItemGroup.builder()
        .icon(() -> new ItemStack(EchoesItems.ECHO_DUST))
        .displayName(Text.translatable("itemGroup.echoes.materials"))
        .entries((context, entries) -> {
            entries.addAll(new ArrayList<>() {
                {
                    add(EchoesItems.ECHO_DUST.getDefaultStack());
                }
            });
        })
        .build()
    );

    private static ItemGroup registerItemGroup(String name, ItemGroup group){
        return Registry.register(Registries.ITEM_GROUP, Identifier.of(Echoes.MOD_ID, name), group);
    }

    public static void registerItemGroups(){
        Echoes.LOGGER.info("Registering item groups for " + Echoes.MOD_ID);
    }
}
