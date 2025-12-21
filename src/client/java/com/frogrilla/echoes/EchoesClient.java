package com.frogrilla.echoes;

import com.frogrilla.echoes.networking.SignalRemovePayload;
import com.frogrilla.echoes.networking.SignalUpdatePayload;
import com.frogrilla.echoes.render.signal.DebugSignalRenderer;
import com.frogrilla.echoes.signal.SignalInterpolator;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EchoesClient implements ClientModInitializer {

    public static Map<UUID, SignalInterpolator> signalMap = new HashMap<>();

	@Override
	public void onInitializeClient() {
        DebugSignalRenderer.createInstance();;
        WorldRenderEvents.BEFORE_TRANSLUCENT.register(DebugSignalRenderer.getInstance()::extractAndDraw);

        ClientPlayNetworking.registerGlobalReceiver(SignalUpdatePayload.ID, (payload, context) -> {
            signalMap.put(payload.uuid(), payload.interpolator());
        });
        ClientPlayNetworking.registerGlobalReceiver(SignalRemovePayload.ID, (payload, context) -> {
            signalMap.remove(payload.uuid());
        });
	}
}