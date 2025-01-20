package dev.pavatus.lib.datagen.model;

import java.util.Optional;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import dev.pavatus.lib.SakitusMod;
import dev.pavatus.lib.container.impl.BlockContainer;
import dev.pavatus.lib.container.impl.ItemContainer;
import dev.pavatus.lib.datagen.util.AutomaticModel;
import dev.pavatus.lib.util.ReflectionUtil;

public class SakitusModelProvider extends FabricModelProvider {
    protected final String modid;
    protected final FabricDataOutput output;
    protected Class<? extends BlockContainer> blockClass;
    protected Class<? extends ItemContainer> itemClass;

    public SakitusModelProvider(FabricDataOutput output) {
        super(output);

        this.modid = output.getModId();
        this.output = output;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        if (blockClass != null) {
            ReflectionUtil.getAnnotatedValues(blockClass, Block.class, AutomaticModel.class, false).keySet().forEach(generator::registerSimpleCubeAll);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        if (blockClass != null) {
            ReflectionUtil.getAnnotatedValues(blockClass, Block.class, AutomaticModel.class, false).forEach((block, annotation) -> {
                if (annotation.orElseThrow().justItem()) {
                    registerItem(generator, block.asItem(), modid);
                }
            });
        }

        if (itemClass != null) {
            ReflectionUtil.getAnnotatedValues(itemClass, Item.class, AutomaticModel.class, false).forEach((item, annotation) -> {
                registerItem(generator, item, modid);
            });
        }
    }

    public SakitusModelProvider withBlocks(Class<? extends BlockContainer> blockClass) {
        this.blockClass = blockClass;
        return this;
    }
    public SakitusModelProvider withItems(Class<? extends ItemContainer> itemClass) {
        this.itemClass = itemClass;
        return this;
    }

    private static Model item(String modid, String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(new Identifier(modid, "item/" + parent)), Optional.empty(), requiredTextureKeys);
    }
    private static Model item(TextureKey... requiredTextureKeys) {
        return item("minecraft", "generated", requiredTextureKeys);
    }
    private void registerItem(ItemModelGenerator generator, Item item, String modid) {
        Model model = item(TextureKey.LAYER0);
        model.upload(ModelIds.getItemModelId(item), createTextureMap(item, modid), generator.writer);
    }
    private TextureMap createTextureMap(Item item, String modid) {
        Identifier texture = new Identifier(modid, "item/" + getItemName(item));
        if (!(doesTextureExist(texture))) {
            texture = SakitusMod.id("item/error");
        }

        return new TextureMap().put(TextureKey.LAYER0, texture);
    }
    private static String getItemName(Item item) {
        return item.getTranslationKey().split("\\.")[2];
    }

    public boolean doesTextureExist(Identifier texture) {
        return this.output.getModContainer().findPath("assets/" + texture.getNamespace() + "/textures/" + texture.getPath() + ".png").isPresent();
    }
}
