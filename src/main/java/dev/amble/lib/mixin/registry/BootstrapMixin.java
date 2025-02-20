package dev.amble.lib.mixin.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.registry.Registries;

import dev.amble.lib.registry.entrypoint.BootstrapEntrypoint;
import dev.amble.lib.registry.entrypoint.ClientBootstrapEntrypoint;

@Mixin(Registries.class)
public class BootstrapMixin {

    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void bootstrap(CallbackInfo ci) {
        amble$bootstrap();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            amble$clientBootstrap();
    }

    @Unique private static void amble$bootstrap() {
        FabricLoader.getInstance().invokeEntrypoints(BootstrapEntrypoint.KEY,
                BootstrapEntrypoint.class, BootstrapEntrypoint::onBootstrap);
    }

    @Unique @Environment(EnvType.CLIENT)
    private static void amble$clientBootstrap() {
        FabricLoader.getInstance().invokeEntrypoints(
                ClientBootstrapEntrypoint.KEY, ClientBootstrapEntrypoint.class,
                ClientBootstrapEntrypoint::onClientBootstrap
        );
    }
}
