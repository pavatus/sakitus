package dev.pavatus.lib.register;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import dev.pavatus.lib.register.api.RegistryEvents;

// TODO: move all registries over to here
public class Registries {

    private static Registries INSTANCE;
    private final List<Registry> registries = new ArrayList<>();
    private final List<Registry> clientRegistries = new ArrayList<>();

    private Registries() {
        this.onCommonInit();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            this.onClientInit();
    }

    private void onCommonInit() {
        RegistryEvents.INIT.invoker().init(this, false);
    }

    private void onClientInit() {
        RegistryEvents.INIT.invoker().init(this, true);
    }

    public void subscribe(InitType env) {
        if (env == InitType.CLIENT && FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
            throw new UnsupportedOperationException("Cannot call onInitializeClient while not running a client!");

        if (env == InitType.CLIENT) {
            for (Registry registry : clientRegistries) {
                InitType.COMMON.init(registry); // also call common init for client registries, since client only registries are loaded after the common is fired
            }
        }

        for (Registry registry : registries) {
            env.init(registry);
        }

        RegistryEvents.SUBSCRIBE.invoker().subscribe(this, env);
    }

    public Registry register(Registry registry, boolean isClient) {
        registries.add(registry);

        if (isClient)
            clientRegistries.add(registry);

        return registry;
    }
    public Registry register(Registry registry) {
        return register(registry, false);
    }

    public static Registries getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Registries();

        return INSTANCE;
    }

    public enum InitType {
        CLIENT(Registry::onClientInit), SERVER(Registry::onServerInit), COMMON(Registry::onCommonInit);

        private final Consumer<Registry> consumer;

        InitType(Consumer<Registry> consumer) {
            this.consumer = consumer;
        }

        public void init(Registry registry) {
            this.consumer.accept(registry);
        }
    }
}
