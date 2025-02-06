package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class EchoesSounds {
    public static SoundEvent ECHO_ROD_ACTIVATE = Register("echo_rod_activate");
    public static SoundEvent CORRUPTED_ECHO_ROD_ACTIVATE = Register("corrupted_echo_rod_activate");
    public static SoundEvent Register(String name){
        Identifier id = Identifier.of(Echoes.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds(){
        Echoes.LOGGER.info("Registering sounds for " + Echoes.MOD_ID);
    }
}
