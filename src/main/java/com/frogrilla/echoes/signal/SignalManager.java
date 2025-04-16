package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.block.ISignalInteractor;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.Portal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.TeleportTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignalManager {

    public List<AbstractSignal> signals = new ArrayList<>();
    public List<AbstractSignal> signalBuffer = new ArrayList<>();

    public void addSignal(AbstractSignal signal){
        signalBuffer.add(signal);
    }

    public void removeSignalAt(int index){
        signals.get(index).removalFlag = true;
    }

    public void tickSignals(ServerWorld world){
        Collections.shuffle(signals);

        signals.forEach(signal ->{
            if(!world.isPosLoaded(signal.blockPos)) return;
            signal.preTick();
            if(signal.shouldTick()){
                BlockState state = world.getBlockState(signal.blockPos);

                // Signal disrupted by interactor
                if(state.getBlock() instanceof ISignalInteractor interactor){
                    signal.processedBy(interactor, this, world, state);
                    return;
                }

                signal.effects(world, signal.blockPos.toCenterPos());

                if(state.getBlock() instanceof Portal){
                    Portal portal = (Portal)state.getBlock();
                    Entity dummy = new ItemEntity(EntityType.ITEM, world);
                    dummy.setPosition(signal.blockPos.toCenterPos());
                    TeleportTarget target = portal.createTeleportTarget(world, dummy, signal.blockPos);
                    PersistentManagerState worldState = PersistentManagerState.getServerWorldState(target.world());


                    AbstractSignal clonedSignal = signal.copy();
                    clonedSignal.blockPos = BlockPos.ofFloored(target.position());
                    //target.world().setBlockState(clonedSignal.blockPos, Blocks.GREEN_STAINED_GLASS.getDefaultState());
                    clonedSignal.tick();
                    worldState.signalManager.addSignal(clonedSignal);


                    signal.removalFlag = true;
                }
                else{
                    signal.tick();
                }
            }
        });
        updateSignals();
    }



    public void updateSignals() {
        signals.removeIf(signal -> signal.removalFlag);
        signals.addAll(signalBuffer);
        signalBuffer.clear();
    }

}