package dev.pavatus.lib.api;

/**
 * Called when the sakitus client mod is initialized
 * This is named "sakitus-client" in entrypoints
 * Use this for registry purposes
 */
public interface SakitusClientModInitializer {
    void onInitializeSakitus();
}
