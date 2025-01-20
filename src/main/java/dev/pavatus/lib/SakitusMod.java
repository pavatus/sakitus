package dev.pavatus.lib;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Identifier;

import dev.pavatus.lib.register.Registries;

public class SakitusMod implements ModInitializer {
    public static final String MOD_ID = "sakitus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Registries.getInstance().subscribe(Registries.InitType.COMMON);
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}