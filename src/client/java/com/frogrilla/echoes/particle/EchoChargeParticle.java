package com.frogrilla.echoes.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class EchoChargeParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    EchoChargeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityMultiplier = 0.96F;
        this.spriteProvider = spriteProvider;
        this.scale(1.0F);
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
    }

    public int getBrightness(float tint) {
        return 240;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static record Factory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            EchoChargeParticle echoChargeParticle = new EchoChargeParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            echoChargeParticle.setAlpha(1.0F);
            echoChargeParticle.setVelocity(g, h, i);
            echoChargeParticle.setMaxAge(3);
            return echoChargeParticle;
        }

        public SpriteProvider spriteProvider() {
            return this.spriteProvider;
        }
    }
}
