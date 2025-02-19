package dev.amble.lib.register;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import dev.amble.lib.AmbleKit;
import dev.amble.lib.api.KitEvents;


// TODO: move all registries over to here
public class AmbleRegistries {

    private static AmbleRegistries INSTANCE;
    private final HashSet<Registry> registries = new HashSet<>();
    private final Set<InitType> initialized = new HashSet<>();

    static {
        KitEvents.PRE_DATAPACK_LOAD.register(() -> {
            AmbleRegistries.getInstance().subscribe(InitType.COMMON);
            AmbleRegistries.getInstance().subscribe(InitType.SERVER);
        });

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            registerClientStart();
        }
    }

    @Environment(EnvType.CLIENT)
    private static void registerClientStart() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            AmbleRegistries.getInstance().subscribe(InitType.CLIENT);
        });
    }

    private AmbleRegistries() {

    }


    protected void subscribe(InitType env) {
        if (env == InitType.CLIENT && FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
            throw new UnsupportedOperationException("Cannot call onInitializeClient while not running a client!");

        if (initialized.contains(env))
            return;

        if (env == InitType.CLIENT) {
            this.subscribe(InitType.COMMON);
        }

        AmbleKit.LOGGER.info("Initializing {} side registries..", env);

        for (Registry registry : registries) {
            env.init(registry);
        }

        initialized.add(env);
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
