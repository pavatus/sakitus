package dev.pavatus.lib.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.block.Block;

import dev.pavatus.lib.container.impl.BlockContainer;
import dev.pavatus.lib.datagen.util.NoBlockDrop;
import dev.pavatus.lib.util.ReflectionUtil;


public class SakitusBlockLootTable extends FabricBlockLootTableProvider {
    protected Class<? extends BlockContainer> blockClass;

    protected SakitusBlockLootTable(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        if (blockClass != null) {
            // automatic self block drops
            ReflectionUtil.getAnnotatedValues(blockClass, Block.class, NoBlockDrop.class, true).keySet().forEach(this::addDrop);
        }
    }

    public SakitusBlockLootTable withBlocks(Class<? extends BlockContainer> blockClass) {
        this.blockClass = blockClass;
        return this;
    }
}
