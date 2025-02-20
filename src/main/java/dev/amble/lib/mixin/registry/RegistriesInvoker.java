package dev.amble.lib.mixin.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Registries.class)
public interface RegistriesInvoker {

    @Invoker("create")
    static <T, R extends MutableRegistry<T>> R amble$create(
            RegistryKey<? extends Registry<T>> key, R registry, Registries.Initializer<T> initializer, Lifecycle lifecycle
    ) {
        throw new UnsupportedOperationException("Implemented by vanilla.");
    }
}
