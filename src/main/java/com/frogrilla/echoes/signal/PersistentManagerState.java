package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.common.signal.Signal;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.lang.reflect.Constructor;

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
            NbtCompound compound = (NbtCompound)element;
            String type = compound.getString("type");
            if(AbstractSignal.SIGNAL_TYPES.containsKey(type)){
                Class<? extends AbstractSignal> signalClass = AbstractSignal.SIGNAL_TYPES.get(type);
                try {
                    Constructor<? extends AbstractSignal> constructor = signalClass.getConstructor(NbtCompound.class);
                    AbstractSignal signal = constructor.newInstance(compound);
                    state.signalManager.addSignal(signal);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                Echoes.LOGGER.warn("Signal type %s doesn't exist".formatted(type));
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
