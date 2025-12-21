package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.common.signal.Echo;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

public record SignalType<T extends AbstractSignal>(MapCodec<T> codec, Function<SignalProperties, T> factory) {
    public static final Registry<SignalType<?>> REGISTRY = new MappedRegistry<>(
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, "signal_types")), Lifecycle.stable());

    public static final StreamCodec<RegistryFriendlyByteBuf, SignalType<?>> STREAM_CODEC = ByteBufCodecs.registry(REGISTRY.key());

}
