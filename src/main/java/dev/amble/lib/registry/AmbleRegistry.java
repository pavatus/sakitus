package dev.amble.lib.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public interface AmbleRegistry<T> {

    RegistryKey<? extends Registry<T>> key();
}
