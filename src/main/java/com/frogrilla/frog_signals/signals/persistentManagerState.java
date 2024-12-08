package com.frogrilla.frog_signals.signals;

import com.frogrilla.frog_signals.FrogSignals;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class persistentManagerState extends PersistentState {

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

    public static persistentManagerState createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        persistentManagerState state = new persistentManagerState();
        NbtList signalNbtList = tag.getList("signals", 10);
        signalNbtList.forEach(element -> {
            state.signalManager.addSignal(Signal.fromCompound((NbtCompound) element));
        });
        return state;
    }

    private static Type<persistentManagerState> type = new Type<>(
            persistentManagerState::new,
            persistentManagerState::createFromNbt,
            null
    );

    public static persistentManagerState getServerWorldState(ServerWorld serverWorld) {
        PersistentStateManager persistentStateManager = serverWorld.getPersistentStateManager();
        persistentManagerState state = persistentStateManager.getOrCreate(type, FrogSignals.MOD_ID);
        state.markDirty();

        return state;
    }
}
