package dev.amble.lib.mixin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.SaveLoading;

import dev.amble.lib.api.KitEvents;

@Mixin(SaveLoading.class)
public class SaveLoadingMixin {
    @Inject(method = "load(Lnet/minecraft/server/SaveLoading$ServerConfig;Lnet/minecraft/server/SaveLoading$LoadContextSupplier;Lnet/minecraft/server/SaveLoading$SaveApplierFactory;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"))
    private static <D, R> void amblekit$load(SaveLoading.ServerConfig serverConfig, SaveLoading.LoadContextSupplier<D> loadContextSupplier, SaveLoading.SaveApplierFactory<D, R> saveApplierFactory, Executor prepareExecutor, Executor applyExecutor, CallbackInfoReturnable<CompletableFuture<R>> cir) {
        KitEvents.PRE_DATAPACK_LOAD.invoker().load();
    }
}
