package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EchoesTags {
    public static final TagKey<Item> ECHO_SHARDS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Echoes.MOD_ID, "echo_shards"));
}
