package dev.pavatus.lib.datagen.sound;

import net.minecraft.sound.SoundEvent;

@FunctionalInterface
public interface SoundBuilder {
    void add(String soundName, SoundEvent[] soundEvents);

    default void add(String soundName, SoundEvent soundEvent) {
        add(soundName, new SoundEvent[]{soundEvent});
    }
}
