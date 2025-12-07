package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.signal.*;
import com.frogrilla.echoes.signal.SignalType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class EchoesSignalTypes {
    // the getType method in their respective subclasses.
    public static final SignalType<Echo> ECHO = register("echo", new SignalType<>(Echo.CODEC, Echo::new));
    public static final SignalType<CorruptedEcho> CORRUPTED_ECHO = register("corrupted_echo", new SignalType<>(CorruptedEcho.CODEC, CorruptedEcho::new));
    public static final SignalType<ItemEcho> ITEM_ECHO = register("item_echo", new SignalType<>(ItemEcho.CODEC, ItemEcho::new));
    public static final SignalType<PulseEcho> PULSE_ECHO = register("pulse_echo", new SignalType<>(PulseEcho.CODEC, PulseEcho::new));

    private static <T extends AbstractSignal> SignalType<T> register(String id, SignalType<T> signalType) {
        return Registry.register(SignalType.REGISTRY, ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, id), signalType);
    }
    public static void registerSignalTypes() {
        Echoes.LOGGER.info("Registering SignalTypes for " + Echoes.MOD_ID);
    }
}
