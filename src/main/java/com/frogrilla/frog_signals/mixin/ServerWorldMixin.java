package com.frogrilla.frog_signals.mixin;

import com.frogrilla.frog_signals.signals.persistentManagerState;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    private ServerWorld serverWorld = ((ServerWorld) ((Object) this));

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 1), method = "tick")
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info){
        if(serverWorld.getTickManager().shouldTick()) {
            persistentManagerState.getServerWorldState(serverWorld).signalManager.tickSignals(serverWorld);
        }
    }
}