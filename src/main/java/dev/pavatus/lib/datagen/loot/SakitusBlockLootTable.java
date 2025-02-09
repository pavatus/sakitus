package dev.pavatus.lib.datagen.loot;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.block.Block;

import dev.pavatus.lib.container.impl.BlockContainer;
import dev.pavatus.lib.datagen.util.NoBlockDrop;
import dev.pavatus.lib.util.ReflectionUtil;


public class SakitusBlockLootTable extends FabricBlockLootTableProvider {
    protected Queue<Class<? extends BlockContainer>> blockClass;

    public SakitusBlockLootTable(FabricDataOutput dataOutput) {
        super(dataOutput);

        this.blockClass = new LinkedList<>();
    }

    @Override
    public void generate() {
        // automatic self block drops
        this.blockClass.forEach(clazz -> ReflectionUtil.getAnnotatedValues(clazz, Block.class, NoBlockDrop.class, true).keySet().forEach(this::addDrop));
    }

    public SakitusBlockLootTable withBlocks(Class<? extends BlockContainer>... blockClass) {
        // add all to queue
        this.blockClass.addAll(Arrays.asList(blockClass));

        return this;
    }
}
