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
import java.util.ArrayList;

public class TokenScan {

    public final Delimiters delimiters;

    private final ByteBuffer windowBuffer;
    private final Delimiter stubDelimiter;
    private final ArrayList<Token> tokens;
    private final PartialToken partialToken;

    public TokenScan(Delimiters delimiters) {
        this.delimiters = delimiters;

        // create windowBuffer with size of longest delimiter

        int size = Integer.MIN_VALUE;
        for(Delimiter delimiter : delimiters.getDelimiters().values()) {
            if(delimiter.delimiterBuffer.capacity() > size) {
                size = delimiter.delimiterBuffer.capacity();
            }
        }
        this.windowBuffer = ByteBuffer.allocateDirect(size);
        this.stubDelimiter = new Delimiter();
        this.tokens = new ArrayList<>();
        this.partialToken = new PartialToken();
    }

    public ArrayList<Token> findBy(Stream stream) {
        windowBuffer.clear();
        tokens.clear();

        while (fillWindowBufferFrom(stream) || windowBuffer.position() != windowBuffer.limit()) {
            windowBuffer.flip();

            if (windowBuffer.limit() == 0) {
                // done
                // +++++ PartialToken stuff
                Token endToken = partialToken.completeToken();
                if (!endToken.isStub) {
                    //System.out.println("created endToken <[" + endToken + "]>");
                    tokens.add(endToken);
                }
                // -----
                break;
            }

            Delimiter delimiter = match(delimiters, windowBuffer);

            if (delimiter.isStub) {
                // take one out (insert to token)
                byte b = windowBuffer.get();
                partialToken.put(b);


            } else {
                int delimiterSize = delimiter.delimiterBuffer.capacity();
                ByteBuffer sliceBuffer = windowBuffer.slice();
                ByteBuffer limitedBuffer = (ByteBuffer) sliceBuffer.limit(delimiterSize);

                // +++++ PartialToken stuff
                Token token = partialToken.completeToken();
                if (!token.isStub) {
                    //System.out.println("created token <[" + token + "]>");
                    tokens.add(token);
                }
                // -----

                // realToken
                Token delimiterToken = new Token(limitedBuffer);
                //System.out.println("created delimiterToken <[" + delimiterToken + "]>");
                tokens.add(delimiterToken);


                windowBuffer.position(windowBuffer.position() + limitedBuffer.limit());
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
                break;
            }
        }
        return rv;
    }



    Delimiter match(Delimiters delimiters, ByteBuffer matchBuffer) {
        Delimiter rv = delimiters.getDelimiters().getOrDefault(matchBuffer, this.stubDelimiter);
        if (rv.isStub) {
            // saerch smaller delimiter
            if (matchBuffer.limit() > 1) {
                ByteBuffer sliceBuffer = matchBuffer.slice();
                ByteBuffer subMatchBuffer = (ByteBuffer) sliceBuffer.limit(matchBuffer.limit() - 1);
                rv = match(delimiters, subMatchBuffer);
            }
        }

        return rv;
    }
}


