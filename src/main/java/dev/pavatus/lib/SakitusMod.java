package dev.pavatus.lib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Identifier;

import dev.pavatus.lib.api.SakitusModInitializer;
import dev.pavatus.lib.register.Registries;
import dev.pavatus.lib.util.ServerLifecycleHooks;

public class SakitusMod implements ModInitializer {
    public static final String MOD_ID = "sakitus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ServerLifecycleHooks.init();

        FabricLoader.getInstance().invokeEntrypoints("sakitus-main", SakitusModInitializer.class,
                SakitusModInitializer::onInitializeSakitus);

        Registries.getInstance().subscribe(Registries.InitType.COMMON);
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}