package com.frogrilla.echoes.networking;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.signal.SignalInterpolator;
import com.frogrilla.echoes.signal.SignalType;
import com.mojang.datafixers.types.Type;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record SignalUpdatePayload(SignalInterpolator interpolator, UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation SIGNAL_UPDATE_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, "signal_update");
    public static final CustomPacketPayload.Type<SignalUpdatePayload> ID = new CustomPacketPayload.Type<>(SIGNAL_UPDATE_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SignalUpdatePayload> CODEC = StreamCodec.composite(SignalInterpolator.STREAM_CODEC, SignalUpdatePayload::interpolator, UUIDUtil.STREAM_CODEC, SignalUpdatePayload::uuid, SignalUpdatePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
