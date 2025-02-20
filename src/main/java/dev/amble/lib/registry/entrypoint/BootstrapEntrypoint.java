package dev.amble.lib.registry.entrypoint;

public interface BootstrapEntrypoint {

    String KEY = "bootstrap-main";

    void onBootstrap();
}
