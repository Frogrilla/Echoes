package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.networking.SignalRemovePayload;
import com.frogrilla.echoes.networking.SignalUpdatePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class EchoesPacketPayloads {
    public static void registerPacketPayloads() {
        PayloadTypeRegistry.playS2C().register(SignalUpdatePayload.ID, SignalUpdatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SignalRemovePayload.ID, SignalRemovePayload.CODEC);
    }
}
