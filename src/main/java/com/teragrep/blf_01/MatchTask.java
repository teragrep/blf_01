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
        //System.out.println("NEW TASK WITH B <[" + debugBuffer(matchBuffer) + "]>");

        MatchTask matchTask = new MatchTask();
        if (matchBuffer.limit() > 1) {
            ByteBuffer sliceBuffer = matchBuffer.slice();
            ByteBuffer subMatchBuffer = (ByteBuffer) sliceBuffer.limit(matchBuffer.limit() - 1);
            matchTask = new MatchTask(delimiters, subMatchBuffer);
            matchTask.fork();
        }
        for (Delimiter delimiter : delimiters.getDelimiters()) {
            // debug
            /*
            String deliString = debugBuffer(delimiter.delimiterBuffer);
            String matchString = debugBuffer(matchBuffer);

            System.out.println("matching matchString <[" + matchString + "]> " +
                    "against deliString <[" + deliString + "]>");
            */

            if (matchBuffer.equals(delimiter.delimiterBuffer)) {
                // has match
                rv = delimiter;
            }
        }

        if (!matchTask.isStub) {
            //System.out.println("subTask returns");
            try {
                Delimiter sub = matchTask.get();
                //System.out.println("subReturns " + debugBuffer(sub.delimiterBuffer));
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
