package com.frogrilla.echoes;

import com.frogrilla.echoes.common.payload.SignalOverridePayload;
import com.frogrilla.echoes.common.payload.SignalUpdatePayload;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.render.signal.SignalRenderer;
import com.frogrilla.echoes.render.state.SignalRenderState;
import com.frogrilla.echoes.signal.SignalManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EchoesClient implements ClientModInitializer {

    public static HashMap<Integer, SignalRenderState> signalCache = new HashMap<Integer, SignalRenderState>();

	@Override
	public void onInitializeClient() {
        SignalRenderer.createInstance();;
        WorldRenderEvents.BEFORE_TRANSLUCENT.register(SignalRenderer.getInstance()::extractAndDraw);

        ClientPlayNetworking.registerGlobalReceiver(SignalUpdatePayload.ID, (payload, context) -> {
            payload.removedSignals().forEach(removedID -> {
                signalCache.remove(removedID);
            });
            payload.updatedSignals().forEach(signal -> {
                if(signalCache.containsKey(signal.getID())){
                    var state = signalCache.get(signal.getID());
                    signalCache.get(signal.getID()).update(signal);
                }
                else{
                    signalCache.put(signal.getID(), new SignalRenderState(signal));
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(SignalOverridePayload.ID, (payload, context) -> {
           signalCache.clear();
           payload.signals().forEach(signal -> {
               signalCache.put(signal.getID(), new SignalRenderState<>(signal));
           });
        });
	}
}