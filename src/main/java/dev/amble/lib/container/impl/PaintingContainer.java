package dev.amble.lib.container.impl;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import dev.amble.lib.container.RegistryContainer;

public interface PaintingContainer extends RegistryContainer<PaintingVariant> {

    @Override
    default Class<PaintingVariant> getTargetClass() {
        return PaintingVariant.class;
    }

    @Override
    default Registry<PaintingVariant> getRegistry() {
        return Registries.PAINTING_VARIANT;
    }
}
