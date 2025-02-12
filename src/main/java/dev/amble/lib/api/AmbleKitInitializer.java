package dev.amble.lib.api;

/**
 * Called when the amblekit mod is initialized
 * This is named "amblekit-main" in entrypoints
 * Use this for registry purposes
 */
public interface AmbleKitInitializer {
    void onInitialize();
}
