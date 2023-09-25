package com.teragrep.blf_01.tokenizer;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamTest {

    @Test
    public void getBytesTest() {
        String input = "testString";
        InputStream is = new ByteArrayInputStream(input.getBytes());
        Stream stream = new Stream(is);
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
        InputStream is = new ByteArrayInputStream(input.getBytes());
        Stream stream = new Stream(is);
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
        InputStream is = new ByteArrayInputStream(input.getBytes());
        Stream stream = new Stream(is);
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
}
