package adudecalledleo.tbsquared.app.plugin.impl;

import java.util.LinkedList;
import java.util.List;

import adudecalledleo.tbsquared.app.Main;
import adudecalledleo.tbsquared.app.plugin.api.PluginAPI;
import adudecalledleo.tbsquared.app.plugin.api.PostLoadListener;
import adudecalledleo.tbsquared.app.plugin.api.renderer.SceneRendererProvider;
import adudecalledleo.tbsquared.app.plugin.impl.renderer.SceneRendererProviderRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zafarkhaja.semver.Version;
import org.pf4j.PluginManager;

public final class PluginAPIImpl implements PluginAPI {
    public static final PluginAPIImpl INSTANCE = new PluginAPIImpl();

    public static void set() {
        PluginAPIHolder.setInstance(INSTANCE);
    }

    private boolean postLoadListenersAllowed;
    private List<PostLoadListener> postLoadListeners;

    private PluginAPIImpl() {
        postLoadListenersAllowed = true;
    }

    @Override
    public Version getVersion() {
        return Main.PLUGIN_API_VERSION;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return Main.JACKSON;
    }

    @Override
    public void registerSceneRendererProvider(SceneRendererProvider provider) {
        SceneRendererProviderRegistry.register(provider);
    }

    @Override
    public void addPostLoadListener(PostLoadListener listener) {
        if (postLoadListenersAllowed) {
            if (postLoadListeners == null) {
                postLoadListeners = new LinkedList<>();
            }
            postLoadListeners.add(listener);
        } else {
            throw new IllegalStateException("Post load listeners have already been executed!");
        }
    }

    public void executePostLoadListeners(PluginManager pluginManager) {
        if (postLoadListeners != null) {
            for (var listener : postLoadListeners) {
                listener.onPostLoad(pluginManager);
            }
            postLoadListenersAllowed = false;
            postLoadListeners.clear();
            postLoadListeners = null;
        }
    }
}
