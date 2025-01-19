package dev.pavatus.lib.datagen.tag;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import dev.pavatus.lib.container.impl.BlockContainer;
import dev.pavatus.lib.datagen.util.PickaxeMineable;
import dev.pavatus.lib.util.ReflectionUtil;

public class SakitusBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    protected Class<? extends BlockContainer> blockClass;

    public SakitusBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        if (blockClass != null) {
            FabricTagBuilder pickaxeBuilder = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE);
            HashMap<Block, PickaxeMineable> pickaxeBlocks = ReflectionUtil.getAnnotatedValues(blockClass, Block.class, PickaxeMineable.class, false);

            for (Block block : pickaxeBlocks.keySet()) {
                pickaxeBuilder.add(block);
                PickaxeMineable annotation = pickaxeBlocks.get(block);

                if (annotation.tool() != PickaxeMineable.Tool.NONE) {
                    getOrCreateTagBuilder(annotation.tool().tag).add(block);
                }
            }
        }
    }

    public SakitusBlockTagProvider withBlocks(Class<? extends BlockContainer> blockClass) {
        this.blockClass = blockClass;
        return this;
    }
}
