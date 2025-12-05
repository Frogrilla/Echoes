package com.frogrilla.echoes.common.signal;

import com.frogrilla.echoes.common.init.EchoesSignalTypes;
import com.frogrilla.echoes.signal.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ItemEcho extends Echo {
    public static final MapCodec<ItemEcho> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPos.CODEC.fieldOf("block_pos").forGetter(ItemEcho::getBlockPos),
            Direction.CODEC.fieldOf("direction").forGetter(ItemEcho::getDirection),
            Codec.BYTE.fieldOf("strength").forGetter(ItemEcho::getStrength),
            ItemStack.CODEC.fieldOf("item").forGetter(ItemEcho::getItem)
    ).apply(instance, ItemEcho::new));

    private ItemStack item = ItemStack.EMPTY;

    @Override
    public SignalType<?> getType() {
        return EchoesSignalTypes.ITEM_ECHO;
    }

    public ItemEcho(BlockPos blockPos, Direction direction, int strength, ItemStack item) {
        super(blockPos, direction, strength);
        this.item = item;
    }

    public ItemEcho(SignalProperties properties, ItemStack item) {
        super(properties);
        this.item = item;
    }

    public ItemEcho(SignalProperties properties) {
        super(properties);
    }

    public ItemEcho() {};

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public void onRemove(SignalManager signalManager, ServerLevel serverLevel, BlockState blockState) {
        Vec3 pos = this.blockPos.relative(this.direction.getOpposite()).getCenter();
        ItemEntity item = new ItemEntity(serverLevel, pos.x, pos.y, pos.z, this.item.copy());
        if(blockState.isAir()) {
            item.push(this.direction.getUnitVec3().scale(0.6f).offsetRandom(RandomSource.create(), 0.1f));
        }
        else{
            item.push(this.direction.getOpposite().getUnitVec3().scale(0.6f).offsetRandom(RandomSource.create(), 0.1f));
        }
        serverLevel.addFreshEntity(item);
    }

    @Override
    public void doEffects(ServerLevel serverLevel, BlockState blockState, SignalTickResult signalTickResult) {
        Vec3 pos = this.getBlockPos().getCenter();
        switch(signalTickResult){
            case CONTINUE -> {
                serverLevel.sendParticles(ParticleTypes.COPPER_FIRE_FLAME, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            }
            case REMOVE -> {
                serverLevel.sendParticles(ParticleTypes.COPPER_FIRE_FLAME, pos.x, pos.y, pos.z, 10, 0, 0, 0, 0.1);
                serverLevel.playSound(null, this.getBlockPos(), SoundEvents.BUBBLE_POP, SoundSource.BLOCKS, 3, 1);
            }
        }
    }
}
