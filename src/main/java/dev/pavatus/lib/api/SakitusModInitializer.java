package dev.pavatus.lib.api;

/**
 * Called when the sakitus mod is initialized
 * This is named "sakitus-main" in entrypoints
 * Use this for registry purposes
 */
public interface SakitusModInitializer {
    void onInitializeSakitus();
}
