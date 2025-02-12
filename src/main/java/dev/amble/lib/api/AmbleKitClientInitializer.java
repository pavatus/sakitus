package dev.amble.lib.api;

/**
 * Called when the amblekit client mod is initialized
 * This is named "amblekit-client" in entrypoints
 * Use this for registry purposes
 */
public interface AmbleKitClientInitializer {
    void onInitialize();
}
