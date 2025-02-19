package dev.amble.lib.register;

import java.util.HashSet;
import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import dev.amble.lib.AmbleKit;


public class AmbleRegistries {

    private static AmbleRegistries INSTANCE;
    private final HashSet<Registry> registries = new HashSet<>();

    public static void init() {
        AmbleRegistries.getInstance().subscribe(InitType.COMMON);
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        AmbleRegistries.getInstance().subscribe(InitType.CLIENT);
    }

    private AmbleRegistries() {

    }

    protected void subscribe(InitType env) {
        if (env == InitType.CLIENT && FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
            throw new UnsupportedOperationException("Cannot initialize a client registry while not running a client!");

        if (env == InitType.SERVER && FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER)
            throw new UnsupportedOperationException("Cannot initialize a dedicated server registry while not running a dedicated server!");

        AmbleKit.LOGGER.info("Initializing {} side registries..", env);

        for (Registry registry : registries) {
            env.init(registry);
        }
    }

    public Registry register(Registry registry) {
        registries.add(registry);

        return registry;
    }

    public void registerAll(Registry... registries) {
        for (Registry registry : registries) {
            register(registry);
        }
    }

    public static AmbleRegistries getInstance() {
        if (INSTANCE == null)
            INSTANCE = new AmbleRegistries();

        return INSTANCE;
    }

    public enum InitType {
        CLIENT(Registry::onClientInit),
        SERVER(Registry::onServerInit),
        COMMON(Registry::onCommonInit);

        private final Consumer<Registry> consumer;

        InitType(Consumer<Registry> consumer) {
            this.consumer = consumer;
        }

        public void init(Registry registry) {
            this.consumer.accept(registry);
        }
    }
}
