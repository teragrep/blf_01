package com.teragrep.blf_01.tokenizer;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamTest {

    @Test
    public void getBytesTest() {
        String input = "testString";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        StringBuilder builder = new StringBuilder();
        int max = input.length();
        byte b;

        stream.next();

        b = stream.get();

        while(max > 0) {
            max--;

            char c = (char) b;
            builder.append(c);

            if (max == 2) {
                stream.mark();
            }

            stream.next();

            b = stream.get();
        }

        assertEquals(input, builder.toString());

    }

    @Test
    public void streamMarkTest() {
        String input = "test";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        int max = input.length();

        stream.next();

        stream.get();

        while(max > 0) {
            max--;

            if (max == 2) {
                stream.mark();
            }

            stream.next();

            stream.get();
        }

        assertEquals(2, stream.getMark());
    }

    @Test
    public void streamTestReset() {

        String input = "abcd";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        StringBuilder builder = new StringBuilder();
        char c;
        int max = input.length();

        byte b;

        stream.next();

        b = stream.get();
        c = (char) b;
        builder.append(c);

        while(max > 0) {
            max--;

            if (max == 2) {
                stream.mark();
            }

            if (!stream.next()) {
                max = stream.getMark();
                break;
            }

            b = stream.get();
            c = (char) b;
            builder.append(c);
        }

        stream.reset();

        while(max > 0) {
            max--;

            if (!stream.next()) {
                break;
            }

            b = stream.get();
            c = (char) b;
            builder.append(c);
        }

        assertEquals("abcdcd", builder.toString());

    }

    @Test
    public void streamSkipTest() {
        String input = "abcdef";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        StringBuilder builder = new StringBuilder();
        char c;
        int max = input.length();

        byte b;
        stream.next();

        b = stream.get();
        c = (char) b;
        builder.append(c);

        while(max > 0) {
            max--;

            if (max == 2) {
                stream.skip(1);
                max--;
            }

            if (!stream.next()) {
                break;
            }

            b = stream.get();
            c = (char) b;
            builder.append(c);
        }

        System.out.println(builder);

        assertEquals("abcdf", builder.toString());

    }
}
