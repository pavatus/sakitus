package dev.amble.lib.register;


public interface Registry {
    /**
     * This is called by both
     * This is AFTER most things are frozen
     */
    default void onCommonInit() {
    }

    /**
     * This is called when the client finishes loading the game
     */
    default void onClientInit() {
    }

    /**
     * This is called once when the server is starting
     */
    default void onServerInit() {
    }

    default void register() {
        AmbleRegistries.getInstance().register(this);
    }
}
