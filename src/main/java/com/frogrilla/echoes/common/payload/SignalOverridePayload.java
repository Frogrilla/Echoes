package com.frogrilla.echoes.common.payload;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SignalOverridePayload(List<AbstractSignal> signals) implements CustomPacketPayload {

    public static final ResourceLocation SIGNAL_OVERRIDE_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, "signal_override");
    public static final Type<SignalOverridePayload> ID = new Type<>(SIGNAL_OVERRIDE_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SignalOverridePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodecWithRegistries(AbstractSignal.SIGNAL_CODEC).apply(ByteBufCodecs.list()),
            SignalOverridePayload::signals,
            SignalOverridePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}
