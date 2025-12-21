package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.networking.SignalUpdatePayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public class SignalInterpolator {

    public static final StreamCodec<RegistryFriendlyByteBuf, SignalInterpolator> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, SignalInterpolator::getPosition, Vec3.STREAM_CODEC, SignalInterpolator::getTarget, SignalInterpolator::new);

    public Vec3 position = Vec3.ZERO;
    public Vec3 target = Vec3.ZERO;

    public SignalInterpolator(Vec3 position, Vec3 target){
        this.position = position;
        this.target = target;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public Vec3 getTarget() {
        return this.target;
    }
}
