package dev.amble.lib.mixin.registry;

import net.minecraft.registry.RegistryLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {

    @Mutable
    @Shadow @Final public static List<RegistryLoader.Entry<?>> DYNAMIC_REGISTRIES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void postStaticInit(CallbackInfo ci) {
        DYNAMIC_REGISTRIES = new ArrayList<>(DYNAMIC_REGISTRIES);
    }
}
