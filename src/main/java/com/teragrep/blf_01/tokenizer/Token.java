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

package com.teragrep.blf_01.tokenizer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Token {
    private final ByteBuffer majorToken;
    private final int capacity;
    private final Set<ByteBuffer> minorTokens = new HashSet<>();
    private final TreeSet<Integer> indexes = new TreeSet<>();

    public Token(ByteBuffer buffer) {
        this.majorToken = buffer;
        this.capacity = buffer.capacity();
    }

    public Set<ByteBuffer> getPermutations() {
        collectPermutations();
        return minorTokens;
    }

    public ByteBuffer getToken() {
        return majorToken;
    }

    public Set<String> asStringSet() {
        collectPermutations();

        Set<String> resultSet = new HashSet<>();

        for(ByteBuffer bf: minorTokens) {
            String decoded = StandardCharsets.US_ASCII.decode(bf).toString();
            resultSet.add(decoded);
        }

        return resultSet;
    }

    private void collectPermutations() {
        majorToken.clear();

        // Get indexes of splitters
        while(majorToken.hasRemaining()) {
            byte b = majorToken.get();
            if (SplitterMatcher.minorMatch(b)) {
                indexes.add(majorToken.position()-1);
            }
        }

        addPermutationsFromIndex(0);

        // Add permutations from each splitter index
        for (int i: indexes) {
            // Add splitter if first
            if (i == 0) {
                ByteBuffer start = ByteBuffer.wrap(getSubArray(i,i+1));
                minorTokens.add(start);
            }

            addPermutationsFromIndex(i);
            addPermutationsFromIndex(i+1);
        }

    }

    private void addPermutationsFromIndex(int index) {

        byte[] current = getSubArray(index, capacity);

        addToken(current);

        for(int i: indexes.descendingSet()) {
            if (i <= index) {
                break;
            }

            addToken(getSubArray(index, i+1));
            addToken(getSubArray(index, i));

        }
    }

    private byte[] getSubArray(int start, int end) {
        majorToken.position(start);
        try {
            byte[] data = new byte[end - start];
            majorToken.get(data, 0, data.length);
            return data;
        } catch (OutOfMemoryError e) {
            throw new
                    RuntimeException("Tokenizer run out of memory trying to create sub array[ "
                    + (end-start)+"]. Max memory: "
                    + Runtime.getRuntime().maxMemory() );
        }
    }

    private void addToken(byte [] array) {
        if (array.length > 0) {
            minorTokens.add(ByteBuffer.wrap(array));
        }
    }
}
