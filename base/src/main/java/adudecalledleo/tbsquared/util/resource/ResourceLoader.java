package adudecalledleo.tbsquared.util.resource;

import java.io.*;
import java.util.ArrayList;

public class ResourceLoader {
    private final ClassLoader delegate;

    public ResourceLoader(ClassLoader delegate) {
        this.delegate = delegate;
    }

    public ResourceLoader(Class<?> clazz) {
        this(clazz.getClassLoader());
    }

    public InputStream newInputStream(String path) throws IOException {
        var in = delegate.getResourceAsStream(path);
        if (in == null) {
            throw new FileNotFoundException(path);
        }
        return in;
    }

    public BufferedReader newBufferedReader(String path) throws IOException {
        return new BufferedReader(new InputStreamReader(newInputStream(path)));
    }

    public String[] loadAllLines(String path) throws IOException {
        try (var reader = newBufferedReader(path)) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines.toArray(String[]::new);
        }
    }
}
