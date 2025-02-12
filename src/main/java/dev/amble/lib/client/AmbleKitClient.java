package dev.amble.lib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import dev.amble.lib.api.AmbleKitClientInitializer;
import dev.amble.lib.register.Registries;

public class AmbleKitClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            Registries.getInstance().subscribe(Registries.InitType.CLIENT);
        });

        FabricLoader.getInstance().invokeEntrypoints("amblekit-client", AmbleKitClientInitializer.class,
                AmbleKitClientInitializer::onInitialize);
    }
}
