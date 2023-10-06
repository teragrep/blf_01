package com.teragrep.blf_01;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class DelimiterWindow {

    public final Delimiters delimiters;

    private final ByteBuffer windowBuffer;

    private final ForkJoinPool forkJoinPool;
    DelimiterWindow() {
        this.delimiters = new MajorDelimiters();

        // create windowBuffer with size of longest delimiter
        int size = Integer.MIN_VALUE;
        for (Delimiter delimiter : delimiters.getDelimiters()) {
            if (delimiter.delimiterBuffer.capacity() > size) {
                size = delimiter.delimiterBuffer.capacity();
            }
        }

        this.windowBuffer = ByteBuffer.allocateDirect(size);
        this.forkJoinPool = ForkJoinPool.commonPool();
    }

    public LinkedList<Token> findBy(Stream stream) {
        LinkedList<Token> tokens = new LinkedList<>();
        int counter = 8;

        while (fillWindowBufferFrom(stream) || windowBuffer.position() != windowBuffer.limit()) {
            windowBuffer.flip();

            if (windowBuffer.limit() == 0) {
                // full read, no more data in stream
                break;
            }
            ForkJoinTask<Delimiter> delimiterForkJoinTask = new MatchTask(delimiters, windowBuffer);
            Delimiter delimiter = forkJoinPool.invoke(delimiterForkJoinTask);

            if (delimiter.isStub) {
                // take one out (insert to token)
                byte b = windowBuffer.get();
                System.out.println("this to token > " + new String(new byte[]{ b }, StandardCharsets.UTF_8));
            } else {
                int delimiterSize = delimiter.delimiterBuffer.capacity();
                ByteBuffer slice = windowBuffer.slice().limit(delimiterSize);

                // TODO realToken
                System.out.println("this to delitoken> " + debugBuffer(slice));

                windowBuffer.position(windowBuffer.position() + slice.limit());
            }

            counter--;
            if (counter == 0) {
                //break;
            }

            if (windowBuffer.position() == 0 && windowBuffer.limit() == 0) {
                // done
                break;
            }
            else {
                // remove the read ones
                windowBuffer.compact();
            }
        }

        return tokens;
    }

    private boolean fillWindowBufferFrom(Stream stream) {
        boolean rv = false;
        while (windowBuffer.position() != windowBuffer.capacity()) {
            if (stream.next()) {
                windowBuffer.put(stream.get());
                rv = true;
            }
            else {
                break;
            }
        }
        return rv;
    }

    private String debugBuffer(ByteBuffer buffer) {
        ByteBuffer bufferSlice = buffer.slice();
        byte[] bufferBytes = new byte[bufferSlice.remaining()];
        bufferSlice.get(bufferBytes);
        return new String(bufferBytes, StandardCharsets.UTF_8);
    }
}

