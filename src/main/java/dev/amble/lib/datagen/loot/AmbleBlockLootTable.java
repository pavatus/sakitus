package dev.amble.lib.datagen.loot;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.block.Block;

import dev.amble.lib.container.impl.BlockContainer;
import dev.amble.lib.datagen.util.NoBlockDrop;
import dev.amble.lib.util.ReflectionUtil;


public class AmbleBlockLootTable extends FabricBlockLootTableProvider {
    protected Queue<Class<? extends BlockContainer>> blockClass;

    public AmbleBlockLootTable(FabricDataOutput dataOutput) {
        super(dataOutput);

        this.blockClass = new LinkedList<>();
    }

    @Override
    public void generate() {
        // automatic self block drops
        this.blockClass.forEach(clazz -> ReflectionUtil.getAnnotatedValues(clazz, Block.class, NoBlockDrop.class, true).keySet().forEach(this::process));
    }

    public AmbleBlockLootTable withBlocks(Class<? extends BlockContainer>... blockClass) {
        // add all to queue
        this.blockClass.addAll(Arrays.asList(blockClass));

        return this;
    }

    protected void process(Block block) {
        this.addDrop(block);
        this.addDropWithSilkTouch(block);
    }
}
