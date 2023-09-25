package com.teragrep.blf_01.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

public class Stream implements Supplier<Byte> {

    final InputStream inputStream;

    private final byte[] buffer = new byte[256 * 1024];
    private int pointer = -1;
    private int bytesInBuffer = -1;
    private byte b;
    private int mark;

    Stream(InputStream inputStream) {
        this.inputStream=inputStream;
    }

    public Byte get() {
        return b;
    }

    public void mark() {
        mark = pointer;
    }

    public int getMark() {
        return mark;
    }

    public void reset() {
        pointer = mark;
    }

    public void skip(int ammount) {
        System.out.println("Stream skipping: " + ammount);
        pointer = pointer + ammount;
    }

    boolean next() {
        if (pointer == bytesInBuffer) {
            int read;
            try {
                read = inputStream.read(buffer, 0 , buffer.length);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }

            if (read <= 0) {
                pointer = bytesInBuffer;
                return false;

            }

            bytesInBuffer = read;
            pointer = 0;
        }

        b = buffer[pointer++];
        return true;
    }
}
