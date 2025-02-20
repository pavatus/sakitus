package dev.amble.lib.registry;

import net.minecraft.registry.*;

public class SimpleAmbleRegistry<T> implements AmbleRegistry<T> {

    private final RegistryKey<? extends Registry<T>> key;
    private final Registry<T> registry;

    public SimpleAmbleRegistry(
            RegistryKey<? extends Registry<T>> key, Registry<T> registry) {
        this.key = key;
        this.registry = registry;
    }

    public Registry<T> get() {
        return registry;
    }

    @Override
    public RegistryKey<? extends Registry<T>> key() {
        return key;
    }
}
