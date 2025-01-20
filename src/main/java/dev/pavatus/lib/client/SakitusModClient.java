package dev.pavatus.lib.client;

import net.fabricmc.api.ClientModInitializer;

import dev.pavatus.lib.register.Registries;

public class SakitusModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Registries.getInstance().subscribe(Registries.InitType.CLIENT);
    }
}
