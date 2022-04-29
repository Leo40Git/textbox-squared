package adudecalledleo.tbsquared.app.plugin;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import adudecalledleo.tbsquared.app.plugin.renderer.SceneRendererProvider;

public final class SceneRendererProviderRegistry {
    private static final List<SceneRendererProvider> PROVIDERS = new LinkedList<>();
    private static final Set<Integer> EXISTING_PROVIDERS = new HashSet<>();

    public static void register(SceneRendererProvider provider) {
        if (EXISTING_PROVIDERS.add(System.identityHashCode(provider))) {
            PROVIDERS.add(provider);
        }
    }

    public static List<SceneRendererProvider> getProviders() {
        return PROVIDERS;
    }
}
