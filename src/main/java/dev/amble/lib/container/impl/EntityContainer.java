package dev.amble.lib.container.impl;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import dev.amble.lib.container.RegistryContainer;

public interface EntityContainer extends RegistryContainer<EntityType<?>> {

    @Override
    default Class<EntityType<?>> getTargetClass() {
        return RegistryContainer.conform(EntityType.class);
    }

    @Override
    default Registry<EntityType<?>> getRegistry() {
        return Registries.ENTITY_TYPE;
    }
}
