package dev.pavatus.lib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import dev.pavatus.lib.api.SakitusClientModInitializer;
import dev.pavatus.lib.register.Registries;

public class SakitusModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            Registries.getInstance().subscribe(Registries.InitType.CLIENT);
        });

        FabricLoader.getInstance().invokeEntrypoints("sakitus-client", SakitusClientModInitializer.class,
                SakitusClientModInitializer::onInitializeSakitus);
    }
}
