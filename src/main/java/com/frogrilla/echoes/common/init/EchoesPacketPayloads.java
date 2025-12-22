package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.common.payload.SignalOverridePayload;
import com.frogrilla.echoes.common.payload.SignalUpdatePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class EchoesPacketPayloads {
    public static void registerPacketPayloads() {
        PayloadTypeRegistry.playS2C().register(SignalUpdatePayload.ID, SignalUpdatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SignalOverridePayload.ID, SignalOverridePayload.CODEC);
    }
}