package com.frogrilla.echoes.signals;

import com.frogrilla.echoes.Echoes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class PersistentManagerState extends PersistentState {

    public SignalManager signalManager = new SignalManager();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList signalNbtList = new NbtList();
        signalManager.updateSignals();
        signalManager.signals.forEach(signal -> {
            NbtCompound element = signal.asCompound();
            signalNbtList.add(element);
        });
        nbt.put("signals", signalNbtList);
        return nbt;
    }

    public static PersistentManagerState createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        PersistentManagerState state = new PersistentManagerState();
        NbtList signalNbtList = tag.getList("signals", 10);
        signalNbtList.forEach(element -> {
            try {
                state.signalManager.addSignal(ISignal.fromCompound((NbtCompound) element));
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return state;
    }

    private static Type<PersistentManagerState> type = new Type<>(
            PersistentManagerState::new,
            PersistentManagerState::createFromNbt,
            null
    );

    public static PersistentManagerState getServerWorldState(ServerWorld serverWorld) {
        PersistentStateManager persistentStateManager = serverWorld.getPersistentStateManager();
        PersistentManagerState state = persistentStateManager.getOrCreate(type, Echoes.MOD_ID);
        state.markDirty();

        return state;
    }
}
