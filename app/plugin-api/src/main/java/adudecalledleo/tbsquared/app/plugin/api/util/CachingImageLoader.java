package adudecalledleo.tbsquared.app.plugin.api.util;

import java.awt.image.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class CachingImageLoader implements ImageLoader {
    private final ImageLoader delegate;
    private final Map<String, BufferedImage> cache;

    public CachingImageLoader(ImageLoader delegate) {
        this.delegate = delegate;
        this.cache = new HashMap<>();
    }

    @Override
    public BufferedImage loadImage(String path) throws IOException {
        var image = cache.get(path);
        if (image == null) {
            image = delegate.loadImage(path);
            cache.put(path, image);
        }
        return image;
    }

    public void clearCache() {
        cache.clear();
    }
}
