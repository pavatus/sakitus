package dev.amble.lib.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class KitEvents {
    public static final Event<PreDatapackLoad> PRE_DATAPACK_LOAD = EventFactory.createArrayBacked(PreDatapackLoad.class, callbacks -> () -> {
        for (PreDatapackLoad callback : callbacks) {
            callback.load();
        }
    });

    /**
     * Called when just before datapacks are loaded
     */
    @FunctionalInterface
    public interface PreDatapackLoad {
        void load();
    }
}
