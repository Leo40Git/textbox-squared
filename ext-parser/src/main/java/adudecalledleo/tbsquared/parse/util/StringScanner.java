package adudecalledleo.tbsquared.parse.util;

import java.util.Optional;

public final class StringScanner {
    public static final char EOF = 0;

    private final char[] chars;
    private final StringBuilder tempSB;
    private final int maxPos;
    private int pos;

    public StringScanner(String string) {
        chars = string.toCharArray();
        tempSB = new StringBuilder();
        maxPos = chars.length - 1;
        pos = 0;
    }

    public int tell() {
        return pos;
    }

    public void seek(int newPos) {
        this.pos = Math.max(0, Math.min(maxPos, newPos));
    }

    public char peek() {
        if (pos >= maxPos) {
            return EOF;
        }
        return chars[pos];
    }

    public char read() {
        if (pos >= maxPos) {
            return EOF;
        }
        return chars[pos++];
    }

    public Optional<String> read(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length <= 0");
        }
        if (pos + length >= maxPos) {
            return Optional.empty();
        }
        var s = new String(chars, pos, length);
        pos += length;
        return Optional.of(s);
    }

    public Optional<String> until(char terminator) {
        final int oldPos = pos;
        tempSB.setLength(0);
        char c;
        while ((c = read()) != EOF && c != terminator) {
            tempSB.append(c);
        }
        if (c == terminator) {
            pos++;
            return Optional.of(tempSB.toString());
        } else {
            pos = oldPos;
            return Optional.empty();
        }
    }

    public Optional<String> until(String terminator) {
        final int oldPos = pos;
        tempSB.setLength(0);
        boolean found = false;
        final char[] termChars = terminator.toCharArray();
        final int maxSearchPos = maxPos - termChars.length + 1;
        for (int i = pos; i < maxSearchPos; i++) {
            pos = i;
            found = true;
            for (int j = 0; j < termChars.length; j++) {
                if (chars[i + j] != termChars[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                break;
            } else {
                tempSB.append(peek());
            }
        }
        if (found) {
            return Optional.of(tempSB.toString());
        } else {
            pos = oldPos;
            return Optional.empty();
        }
    }
}
