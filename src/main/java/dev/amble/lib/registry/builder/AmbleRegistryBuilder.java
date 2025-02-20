package dev.amble.lib.registry.builder;

import com.mojang.serialization.Codec;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public interface AmbleRegistryBuilder {

    static <T> SimpleAmbleRegistryBuilder<T> simple(RegistryKey<? extends Registry<T>> key) {
        return new SimpleAmbleRegistryBuilder<>(key);
    }

    static <T> DynamicAmbleRegistryBuilder<T> dynamic(RegistryKey<? extends Registry<T>> key, Codec<T> codec) {
        return new DynamicAmbleRegistryBuilder<>(key, codec);
    }
}
