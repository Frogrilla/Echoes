package com.frogrilla.echoes.mixin;

import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    private ServerLevel serverLevel = ((ServerLevel) ((Object) this));

    @Inject(at = @At(value = "TAIL"), method = "tick")
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info){
        SignalManager signalManager = SignalManager.getSignalManager(serverLevel);
        signalManager.tick(serverLevel);
    }
}