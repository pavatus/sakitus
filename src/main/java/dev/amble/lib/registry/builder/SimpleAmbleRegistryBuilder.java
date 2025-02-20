package dev.amble.lib.registry.builder;

import com.mojang.serialization.Lifecycle;
import dev.amble.lib.mixin.registry.RegistriesInvoker;
import dev.amble.lib.registry.SimpleAmbleRegistry;
import net.minecraft.registry.*;
import org.jetbrains.annotations.Nullable;

public class SimpleAmbleRegistryBuilder<T> {

    private final RegistryKey<? extends Registry<T>> key;
    private Lifecycle lifecycle = Lifecycle.stable();
    private boolean intrusive = false;

    private String defaultId;

    private RegistryCreator<T> creator = (d, k, l, i) -> {
        if (d == null)
            return new SimpleRegistry<>(k, l, i);

        return new SimpleDefaultedRegistry<>(d, k, l, i);
    };

    private Initializer<T> defaults;

    public SimpleAmbleRegistryBuilder(RegistryKey<? extends Registry<T>> key) {
        this.key = key;
    }

    public SimpleAmbleRegistryBuilder<T> deprecated(int since) {
        this.lifecycle = Lifecycle.deprecated(since);
        return this;
    }

    public SimpleAmbleRegistryBuilder<T> experimental() {
        this.lifecycle = Lifecycle.experimental();
        return this;
    }

    public SimpleAmbleRegistryBuilder<T> intrusive() {
        this.intrusive = true;
        return this;
    }

    public SimpleAmbleRegistryBuilder<T> defaultId(String defaultId) {
        this.defaultId = defaultId;
        return this;
    }

    public SimpleAmbleRegistryBuilder<T> creator(RegistryCreator<T> creator) {
        this.creator = creator;
        return this;
    }

    public SimpleAmbleRegistryBuilder<T> defaults(Initializer<T> defaults) {
        this.defaults = defaults;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <R extends MutableRegistry<T>> SimpleAmbleRegistry<T> build() {
        Registry<T> mcReg = this.creator.create(defaultId, key, lifecycle, intrusive);

        mcReg = RegistriesInvoker.amble$create(
                key, (R) mcReg, defaults::run, lifecycle);

        return new SimpleAmbleRegistry<>(key, mcReg);
    }

    @FunctionalInterface
    public interface RegistryCreator<T> {
        Registry<T> create(@Nullable String defaultId, RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, boolean intrusive);
    }

    @FunctionalInterface
    public interface Initializer<T> {
        T run(Registry<T> registry);
    }
}
