package dev.amble.lib.registry.entrypoint;

public interface ClientBootstrapEntrypoint {

    String KEY = "bootstrap-client";

    void onClientBootstrap();
}
