package dev.amble.lib.register.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import dev.amble.lib.register.AmbleRegistries;
import dev.amble.lib.register.Registry;

public final class RegistryEvents {
    /**
     * @deprecated This is now automatically called
     * @see AmbleRegistries#register(Registry)
     */
    @Deprecated(forRemoval = true, since = "1.1.1")
    public static final Event<Subscribe> SUBSCRIBE = EventFactory.createArrayBacked(Subscribe.class, callbacks -> (registries, env) -> {
        for (Subscribe callback : callbacks) {
            callback.subscribe(registries, env);
        }
    });

    @Deprecated(forRemoval = true, since = "1.1.1")
    public static final Event<Init> INIT = EventFactory.createArrayBacked(Init.class, callbacks -> (registries, isClient) -> {
        for (Init callback : callbacks) {
            callback.init(registries, isClient);
        }
    });

    /**
     * Called when a Registries is subscribed
     */
    @FunctionalInterface
    public interface Subscribe {
        void subscribe(AmbleRegistries registries, AmbleRegistries.InitType env);
    }

    /**
     * Called when Registries is initialized
     */
    @FunctionalInterface
    public interface Init {
        void init(AmbleRegistries registries, boolean isClient);
    }
}
