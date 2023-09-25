package com.teragrep.blf_01.tokenizer;

import java.nio.ByteBuffer;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public class TokenizeFunction implements BiFunction<Stream, ByteBuffer, ByteBuffer> {

    private static final String regex = "(\\t|\\n|\\r| |\\!|\\\"|%0A|%20|%21|%2520|%2526|%26|%28|%29|%2B|%2C|%3A|%3B|%3D|%5B|%5D|%7C|&|\\'|\\(|\\)|\\*|\\+|,|--|;|<|>|\\?|\\[|\\]|\\{|\\||\\})";
    private static final Pattern compiledRegex = Pattern.compile(regex);

    @Override
    public ByteBuffer apply(Stream stream, ByteBuffer buffer) {
        if (!stream.next()) {
            throw new RuntimeException("Empty stream");
        }
        byte b;
        int remaining = buffer.capacity();
        System.out.println("TokenizeFunction buffer capacity " + remaining);


        b = stream.get();

        while (remaining > 0) {
            char c = (char) b;
            System.out.println(c);
            buffer.put(b);

            if (b == 37 || b == 45) {
                handleOverlaps(stream, b);
            }

            if (match(String.valueOf(c))) {
                System.out.println("Single char match: " + c);
            }

            if (!stream.next()) {
                System.out.println("Out of stream");
            }

            b = stream.get();
            remaining--;
        }

        buffer.flip();
        return buffer;
    }

    public void handleOverlaps(Stream stream, byte b) {
        stream.mark();

        StringBuilder overlap = new StringBuilder();
        char ch = (char) b;
        overlap.append(ch);

        for (int i = 1; i <= 5; i++) {
            if (!stream.next()) {
                break;
            }
            b = stream.get();
            char overlapChar = (char) b;
            overlap.append(overlapChar);

            if (match(overlap.toString())) {
                // resultSet.add(new Token(overlap.toString()));
                System.out.println("Match:" + overlap);
                System.out.println(overlap.deleteCharAt(overlap.length() - 1));
                // resultSet.add(new Token(overlap.deleteCharAt(overlap.length() - 1).toString()));
                overlap.setLength(0);
                stream.reset();
                stream.skip(i);
                break;
            }
        }
        stream.reset();

    }

    private boolean match(String value) {
        return compiledRegex.matcher(value).matches();
    }
}
