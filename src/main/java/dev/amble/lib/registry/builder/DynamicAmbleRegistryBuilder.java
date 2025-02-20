package dev.amble.lib.registry.builder;

import com.mojang.serialization.Codec;
import dev.amble.lib.registry.DynamicAmbleRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class DynamicAmbleRegistryBuilder<T> {

    private final RegistryKey<? extends Registry<T>> key;
    private final Codec<T> codec;

    protected DynamicAmbleRegistryBuilder(RegistryKey<? extends Registry<T>> key, Codec<T> codec) {
        this.key = key;
        this.codec = codec;
    }

    public DynamicAmbleRegistry<T> build() {
        return new DynamicAmbleRegistry<>(key, codec);
    }
}
