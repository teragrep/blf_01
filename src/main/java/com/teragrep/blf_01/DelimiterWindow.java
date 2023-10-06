package com.teragrep.blf_01;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

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

        while (fillWindowBufferFrom(stream) || windowBuffer.position() != 0) {
            windowBuffer.flip();

            if (windowBuffer.limit() == 0) {
                // full read, no more data in stream
                break;
            }
            ForkJoinTask<Delimiter> delimiterForkJoinTask = new MatchTask(windowBuffer);
            Delimiter delimiter = forkJoinPool.invoke(delimiterForkJoinTask);

            if (delimiter.isStub) {
                // take one out (insert to token)
                byte b = windowBuffer.get();
                System.out.println("this to token > " + new String(String.valueOf(b)));
            } else {
                int delimiterSize = delimiter.delimiterBuffer.capacity();
                ByteBuffer slice = windowBuffer.slice().limit(delimiterSize);

                byte[] bytes = new byte[slice.remaining()];
                slice.get(bytes);
                String deliToken = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("this to delitoken> " + deliToken);
            }

            // remove the read ones
            windowBuffer.compact();
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
                rv = false;
                break;
            }
        }
        return rv;
    }

    private class MatchTask extends RecursiveTask<Delimiter> {
        private final ByteBuffer matchBuffer;

        public final boolean isStub;
        MatchTask() {
            this.matchBuffer = ByteBuffer.allocate(0);
            this.isStub = true;
        }

        MatchTask(ByteBuffer matchBuffer) {
            this.matchBuffer = matchBuffer;
            this.isStub = false;
        }


        @Override
        protected Delimiter compute() {
            Delimiter rv = new Delimiter();

            MatchTask matchTask = new MatchTask();
            if (matchBuffer.capacity() > 1) {
                matchTask = new MatchTask(matchBuffer.slice().limit(matchBuffer.limit()-1));
                matchTask.fork();
            }
            for (Delimiter delimiter : delimiters.getDelimiters()) {
                if (windowBuffer.equals(delimiter.delimiterBuffer)) {
                    // has match
                    rv = delimiter;
                }
            }

            if (!matchTask.isStub) {
                try {
                    Delimiter sub =  matchTask.get();
                    if (sub.delimiterBuffer.capacity() > rv.delimiterBuffer.capacity()) {
                        rv = sub;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }

            return rv;
        }
    }
}
