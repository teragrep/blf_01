package com.teragrep.blf_01;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

class MatchTask extends RecursiveTask<Delimiter> {
    private final ByteBuffer matchBuffer;

    public final Delimiters delimiters;
    public final boolean isStub;

    MatchTask() {
        this.matchBuffer = ByteBuffer.allocate(0);
        this.delimiters = HashSet::new;
        this.isStub = true;
    }

    MatchTask(Delimiters delimiters, ByteBuffer matchBuffer) {
        this.matchBuffer = matchBuffer;
        this.delimiters = delimiters;
        this.isStub = false;
    }


    @Override
    protected Delimiter compute() {
        Delimiter rv = new Delimiter();
        System.out.println("NEW TASK WITH B <[" + debugBuffer(matchBuffer) + "]>");

        MatchTask matchTask = new MatchTask();
        if (matchBuffer.limit() > 1) {
            ByteBuffer subMatchBuffer = matchBuffer.slice().limit(matchBuffer.limit() - 1);
            matchTask = new MatchTask(delimiters, subMatchBuffer);
            matchTask.fork();
        }
        for (Delimiter delimiter : delimiters.getDelimiters()) {
            // debug
            String deliString = debugBuffer(delimiter.delimiterBuffer);
            String matchString = debugBuffer(matchBuffer);
            System.out.println("matching matchString <[" + matchString + "]> " +
                    "against deliString <[" + deliString + "]>");


            if (matchBuffer.equals(delimiter.delimiterBuffer)) {
                // has match
                rv = delimiter;
            }
        }

        if (!matchTask.isStub) {
            System.out.println("subTask returns");
            try {
                Delimiter sub = matchTask.get();
                System.out.println("subReturns " + debugBuffer(sub.delimiterBuffer));
                if (sub.delimiterBuffer.capacity() > rv.delimiterBuffer.capacity()) {
                    rv = sub;
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
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
