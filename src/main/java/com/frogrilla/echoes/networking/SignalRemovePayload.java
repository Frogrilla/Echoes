package com.frogrilla.echoes.networking;

import com.frogrilla.echoes.Echoes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record SignalRemovePayload(UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation SIGNAL_REMOVE_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, "signal_remove");
    public static final Type<SignalRemovePayload> ID = new Type<>(SIGNAL_REMOVE_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SignalRemovePayload> CODEC = StreamCodec.composite(UUIDUtil.STREAM_CODEC, SignalRemovePayload::uuid, SignalRemovePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
