/*
 * Teragrep Bloom Filter Library BLF-01
 * Copyright (C) 2019, 2020, 2021, 2022, 2023  Suomen Kanuuna Oy
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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenTest {

    @Test
    public void permutationsTest() {
        ByteBuffer buffer =
                ByteBuffer.wrap("b.c.d".getBytes(StandardCharsets.US_ASCII));
        Token token = new Token(buffer);

        final Set<ByteBuffer> resultSet = token.getPermutations();
        final HashSet<ByteBuffer> expectedSet = new HashSet<>();

        expectedSet.add(ByteBuffer.wrap("b.c.d".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap("b.c.".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap("b.c".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap("b.".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap("b".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap(".c.d".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap(".c.".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap(".c".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap("c.d".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap("c.".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap("c".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap(".d".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap("d".getBytes(StandardCharsets.US_ASCII)));

        assertEquals(expectedSet.size(), resultSet.size());
        assertTrue(expectedSet.containsAll(resultSet));
    }

    @Test
    public void noSplitterTest() {
        final Token token =
                new Token(
                        ByteBuffer.wrap("abcdefghijklmn".getBytes(StandardCharsets.US_ASCII)));
        final Set<ByteBuffer> set = token.getPermutations();

        assertEquals(1, set.size());
    }

    @Test
    public void splitterAtStartTest() {
        final Token token =
                new Token(
                        ByteBuffer.wrap(".20".getBytes(StandardCharsets.US_ASCII)));
        final Set<ByteBuffer> set = token.getPermutations();

        assertEquals(3, set.size());

    }

    @Test
    public void splitterAtEndTest() {
        final Token token =
                new Token(
                        ByteBuffer.wrap("20.".getBytes(StandardCharsets.US_ASCII)));
        final Set<ByteBuffer> resultSet = token.getPermutations();

        final HashSet<ByteBuffer> expectedSet = new HashSet<>();

        expectedSet.add(ByteBuffer.wrap(".".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap("20".getBytes(StandardCharsets.US_ASCII)));
        expectedSet.add(ByteBuffer.wrap("20.".getBytes(StandardCharsets.US_ASCII)));

        assertTrue(expectedSet.containsAll(resultSet));

    }

    private String generateRandomString(int size, int splitterFreq) {
        Random random = new Random();
        String language = "123456789abcdefghijklmnopqrstuvwxyz";
        String splitters = ".";
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i <size; i++ ) {
            if (i % splitterFreq == 0) {
                sb.append(splitters.charAt(random.nextInt(splitters.length())));
            } else {
                sb.append(language.charAt(random.nextInt(language.length())));
            }
        }
        return sb.toString();
    }

    private void printBufferSet(Set<ByteBuffer> set) {
        System.out.print("Result : [");
        set.forEach(buf -> {
            for(byte b: buf.array()) {
                System.out.print((char) b);
            }
            System.out.print(", ");
        });
        System.out.print("]\n");
    }
}
