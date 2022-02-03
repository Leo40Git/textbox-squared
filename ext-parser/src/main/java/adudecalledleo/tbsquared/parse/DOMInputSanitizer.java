package adudecalledleo.tbsquared.parse;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public final class DOMInputSanitizer {
    private static final UnaryOperator<String> FUNCTION = createFunction();

    public static String apply(String input) {
        return FUNCTION.apply(input);
    }

    private static UnaryOperator<String> createFunction() {
        UnaryOperator<String> f;

        if ("\n".equals(System.lineSeparator())) {
            f = UnaryOperator.identity();
        } else {
            f = new UnaryOperator<>() {
                private final Pattern pattern = Pattern.compile(System.lineSeparator(), Pattern.LITERAL);

                @Override
                public String apply(String input) {
                    return pattern.matcher(input).replaceAll("\n");
                }
            };
        }

        return f;
    }
}
