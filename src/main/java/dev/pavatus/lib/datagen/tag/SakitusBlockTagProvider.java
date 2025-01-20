package dev.pavatus.lib.datagen.tag;

import java.util.*;
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
    protected Queue<Class<? extends BlockContainer>> blockClass;

    public SakitusBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);

        this.blockClass = new LinkedList<>();
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.blockClass.forEach(clazz -> {
            FabricTagBuilder pickaxeBuilder = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE);
            HashMap<Block, Optional<PickaxeMineable>> pickaxeBlocks = ReflectionUtil.getAnnotatedValues(clazz, Block.class, PickaxeMineable.class, false);

            for (Block block : pickaxeBlocks.keySet()) {
                pickaxeBuilder.add(block);
                PickaxeMineable annotation = pickaxeBlocks.get(block).orElseThrow();

                if (annotation.tool() != PickaxeMineable.Tool.NONE) {
                    getOrCreateTagBuilder(annotation.tool().tag).add(block);
                }
            }
        });
    }

    public SakitusBlockTagProvider withBlocks(Class<? extends BlockContainer>... blockClass) {
        // add all to queue
        this.blockClass.addAll(Arrays.asList(blockClass));

        return this;
    }
}
