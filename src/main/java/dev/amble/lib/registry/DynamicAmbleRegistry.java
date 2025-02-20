package dev.amble.lib.registry;

import com.mojang.serialization.Codec;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class DynamicAmbleRegistry<T> implements AmbleRegistry<T> {

    private final RegistryKey<? extends Registry<T>> key;
    private final Codec<T> codec;

    public DynamicAmbleRegistry(RegistryKey<? extends Registry<T>> key, Codec<T> codec) {
        this.key = key;
        this.codec = codec;
    }

    @Override
    public RegistryKey<? extends Registry<T>> key() {
        return key;
    }

    public Codec<T> codec() {
        return codec;
    }

    public Registry<T> get(World world) {
        return world.getRegistryManager().get(this.key());
    }
}
