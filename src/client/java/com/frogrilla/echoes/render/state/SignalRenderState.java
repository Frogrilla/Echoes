package com.frogrilla.echoes.render.state;

import com.frogrilla.echoes.common.signal.AbstractSignal;
import net.minecraft.world.phys.Vec3;

public class SignalRenderState<T extends AbstractSignal> {

    public Vec3 targetPos = Vec3.ZERO;
    public Vec3 pos = Vec3.ZERO;
    public long updateTime = 0;
    public static final int LERP_MILLIS = 50;

    public SignalRenderState(T signal) {
        this.pos = signal.getBlockPos().getCenter();
        this.targetPos = pos;
        updateTime = System.currentTimeMillis();
    }

    public void update(T signal) {
        this.pos = this.targetPos;
        this.targetPos = signal.getBlockPos().getCenter();
        updateTime = System.currentTimeMillis();
    }

}