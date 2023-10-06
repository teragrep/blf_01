/*
 * Teragrep Bloom Filter Library BLF-01
 * Copyright (C) 2019, 2020, 2021, 2022, 2023 Suomen Kanuuna Oy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://github.com/teragrep/teragrep/blob/main/LICENSE>.
 *
 *
 * Additional permission under GNU Affero General Public License version 3
 * section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it
 * with other code, such other code is not for that reason alone subject to any
 * of the requirements of the GNU Affero GPL version 3 as long as this Program
 * is the same Program as licensed from Suomen Kanuuna Oy without any additional
 * modifications.
 *
 * Supplemented terms under GNU Affero General Public License version 3
 * section 7
 *
 * Origin of the software must be attributed to Suomen Kanuuna Oy. Any modified
 * versions must be marked as "Modified version of" The Program.
 *
 * Names of the licensors and authors may not be used for publicity purposes.
 *
 * No rights are granted for use of trade names, trademarks, or service marks
 * which are in The Program if any.
 *
 * Licensee must indemnify licensors and authors for any liability that these
 * contractual assumptions impose on licensors and authors.
 *
 * To the extent this program is licensed as part of the Commercial versions of
 * Teragrep, the applicable Commercial License may apply to this file if you as
 * a licensee so wish it.
 */

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

        Token token = new Token();
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


                if (token.isStub) {
                    token = new Token(b);
                }
                else {
                    token.put(b);
                }

            } else {
                int delimiterSize = delimiter.delimiterBuffer.capacity();
                ByteBuffer slice = windowBuffer.slice().limit(delimiterSize);

                if (!token.isStub) {
                    tokens.add(token);
                    token = new Token();
                    //System.out.println(token);

                }

                // realToken
                Token delimiterToken = new Token(slice);
                tokens.add(delimiterToken);
                //System.out.println(delimiterToken);


                windowBuffer.position(windowBuffer.position() + slice.limit());
            }

            if (windowBuffer.position() == 0 && windowBuffer.limit() == 0) {
                // done
                if (!token.isStub) {
                    tokens.add(token);
                    System.out.println(token);
                }
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

