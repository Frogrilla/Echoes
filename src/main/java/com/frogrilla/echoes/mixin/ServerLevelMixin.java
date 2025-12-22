package com.frogrilla.echoes.mixin;

import com.frogrilla.echoes.common.payload.SignalOverridePayload;
import com.frogrilla.echoes.common.payload.SignalUpdatePayload;
import com.frogrilla.echoes.signal.SignalManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    private ServerLevel serverLevel = ((ServerLevel) ((Object) this));

    @Inject(at = @At(value = "TAIL"), method = "tick")
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info){
        if(serverLevel.tickRateManager().runsNormally()) {
            SignalManager signalManager = SignalManager.getSignalManager(serverLevel);
            signalManager.tick(serverLevel);
        }
    }

    @Inject(at = @At(value = "TAIL"), method = "addNewPlayer")
    public void addNewPlayer(ServerPlayer serverPlayer, CallbackInfo info) {
        sendPlayerSignalPacket(serverPlayer);
    }

    @Inject(at = @At(value = "TAIL"), method = "addPlayer")
    public void addPlayer(ServerPlayer serverPlayer, CallbackInfo info) {
        sendPlayerSignalPacket(serverPlayer);
    }

    @Unique
    private void sendPlayerSignalPacket(ServerPlayer serverPlayer) {
        SignalManager signalManager = SignalManager.getSignalManager(serverLevel);
        SignalOverridePayload payload = new SignalOverridePayload(signalManager.getSignals());
        ServerPlayNetworking.send(serverPlayer, payload);
    }
}