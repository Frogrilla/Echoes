package com.frogrilla.echoes.common.block.entity;

import com.frogrilla.echoes.common.init.EchoesBlockEntities;
import com.frogrilla.echoes.common.screen.CrystalisationTableScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrystalisationTableBlockEntity extends BlockEntity {

    public ItemStack heldItem = ItemStack.EMPTY;
    public CrystalisationTableBlockEntity(BlockPos pos, BlockState state) {
        super(EchoesBlockEntities.CRYSTALISATION_TABLE_BLOCK_ENTITY, pos, state);
    }

    public boolean empty(){
        return heldItem.isEmpty();
    }

    public void setHeldItem(ItemStack in){;
        heldItem = in.copy();
        heldItem.setCount(1);
    }

    public ItemStack pop(){
        ItemStack out = heldItem.copy();
        heldItem = ItemStack.EMPTY;
        return out;
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if(!empty()){
            nbt.put("item", heldItem.toNbt(registries));
        }

        super.writeNbt(nbt, registries);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        if(nbt.contains("item")){
            heldItem = ItemStack.fromNbt(registries, nbt.get("item")).orElse(ItemStack.EMPTY);;
        }
        else{
            heldItem = ItemStack.EMPTY;
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
