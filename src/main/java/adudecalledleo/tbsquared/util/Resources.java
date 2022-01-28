package adudecalledleo.tbsquared.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class Resources {
    private Resources() { }

    public static InputStream openStream(Class<?> clazz, String path) throws IOException {
        var in = clazz.getResourceAsStream(path);
        if (in == null) {
            throw new FileNotFoundException(path);
        }
        return in;
    }
}
