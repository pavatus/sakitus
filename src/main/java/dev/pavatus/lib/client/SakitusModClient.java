package dev.pavatus.lib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import dev.pavatus.lib.register.Registries;

public class SakitusModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            Registries.getInstance().subscribe(Registries.InitType.CLIENT);
        });
    }
}
