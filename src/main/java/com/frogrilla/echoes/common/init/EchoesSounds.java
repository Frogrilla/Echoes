package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class EchoesSounds {
    // Rods
    public static SoundEvent ECHO_ROD_ACTIVATE = Register("echo_rod_activate");
    public static SoundEvent CORRUPTED_ECHO_ROD_ACTIVATE = Register("corrupted_echo_rod_activate");

    // Flip Frog
    public static SoundEvent FLIP_FROG_OFF = Register("flip_frog_off");
    public static SoundEvent FLIP_FROG_ON = Register("flip_frog_on");

    // Generic signal sounds
    public static SoundEvent SIGNAL_BLOCKED = Register("signal_blocked");
    public static SoundEvent SIGNAL_FIZZLE = Register("signal_fizzle");

    // Corrupted Signal Sounds
    public static SoundEvent CORRUPTED_SIGNAL_FIZZLE = Register("corrupted_signal_fizzle");
    public static SoundEvent Register(String name){
        Identifier id = Identifier.of(Echoes.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds(){
        Echoes.LOGGER.info("Registering sounds for " + Echoes.MOD_ID);
    }
}
