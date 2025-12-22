package com.frogrilla.echoes.common.payload;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SignalUpdatePayload(List<AbstractSignal> updatedSignals, List<Integer> removedSignals) implements CustomPacketPayload {

    public static final ResourceLocation SIGNAL_UPDATE_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, "signal_update");
    public static final CustomPacketPayload.Type<SignalUpdatePayload> ID = new CustomPacketPayload.Type<>(SIGNAL_UPDATE_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SignalUpdatePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodecWithRegistries(AbstractSignal.SIGNAL_CODEC).apply(ByteBufCodecs.list()),
            SignalUpdatePayload::updatedSignals,
            ByteBufCodecs.INT.apply(ByteBufCodecs.list()),
            SignalUpdatePayload::removedSignals,
            SignalUpdatePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}
