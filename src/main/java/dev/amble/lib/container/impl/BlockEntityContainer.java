package dev.amble.lib.container.impl;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import dev.amble.lib.container.RegistryContainer;

public interface BlockEntityContainer extends RegistryContainer<BlockEntityType<?>> {

    @Override
    default Class<BlockEntityType<?>> getTargetClass() {
        return RegistryContainer.conform(BlockEntityType.class);
    }

    @Override
    default Registry<BlockEntityType<?>> getRegistry() {
        return Registries.BLOCK_ENTITY_TYPE;
    }
}
