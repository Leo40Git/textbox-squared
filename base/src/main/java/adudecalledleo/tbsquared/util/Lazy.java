package adudecalledleo.tbsquared.util;

import java.util.function.Supplier;

@FunctionalInterface
public interface Lazy<T> extends Supplier<T> {
    @Override
    T get();

    static <T> Lazy<T> of(T value) {
        return new Lazy<>() {
            private final T _value = value;

            @Override
            public T get() {
                return _value;
            }
        };
    }

    static <T> Lazy<T> wrap(Supplier<T> supplier) {
        return new Lazy<>() {
            private final Supplier<T> _supplier = supplier;
            private boolean computed;
            private T value;

            @Override
            public T get() {
                if (!computed) {
                    value = _supplier.get();
                    computed = true;
                }
                return value;
            }
        };
    }
}
