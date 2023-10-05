package com.teragrep.blf_01;

import java.nio.ByteBuffer;

public class DelimiterWindow {

    public final MajorDelimiters majorDelimiters;

    private final ByteBuffer windowBuffer;


    DelimiterWindow() {
        this.majorDelimiters = new MajorDelimiters();

        int size = Integer.MIN_VALUE;
        for (Delimiter delimiter : majorDelimiters.delimiterSet) {
            if (delimiter.delimiterBuffer.capacity() > size) {
                size = delimiter.delimiterBuffer.capacity();
            }
        }

        windowBuffer = ByteBuffer.allocateDirect(size);
    }

    public void findBy(Stream stream) {
        fillWindowBufferFrom(stream);

        for (Delimiter delimiter : majorDelimiters.delimiterSet) {
            if (windowBuffer.equals(delimiter.delimiterBuffer)) {
                // TODO has match
            }
        }
        // TODO no match, take slice -1 size of windowBuffer and try again
        // perhaps forkJoin?

        // take one out (insert to token)

        // compact
        windowBuffer.compact();

        // refill
    }

    private void fillWindowBufferFrom(Stream stream) {
        while (windowBuffer.position() != windowBuffer.capacity()) {
            if (stream.next()) {
                windowBuffer.put(stream.get());
            }
            else {
                break;
            }
        }
    }

    private void matchAtSize(final int size) {

    }
}
