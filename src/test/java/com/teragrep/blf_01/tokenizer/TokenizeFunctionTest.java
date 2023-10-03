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

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TokenizeFunctionTest {

    @Test
    public void splitterTest() {
        String input = "test%20test,b.c--jkl<jl-- ";
        Set<String> decoded = getBytesDecoded(input);

        assertTrue(decoded.contains("test"));
        assertTrue(decoded.contains("%20"));
        assertTrue(decoded.contains(","));
        assertTrue(decoded.contains("b.c"));
        assertTrue(decoded.contains("--"));
        assertTrue(decoded.contains("jkl"));
        assertTrue(decoded.contains("<"));
        assertTrue(decoded.contains("jl"));
        assertTrue(decoded.contains(" "));

        assertFalse(decoded.contains("jl--"));
        assertFalse(decoded.contains("test%"));
        assertFalse(decoded.contains("test,"));

    }

    @Test
    public void tokenBenchmarkTest() {
        int inputSize = 10000000;
        String input = generateRandomString(inputSize, 6);
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        ByteBuffer buffer = ByteBuffer.allocateDirect(input.length());
        BiFunction<Stream, ByteBuffer, Set<Token>> fn = new TokenizeFunction();

        long start = System.currentTimeMillis();

        fn.apply(stream, buffer);

        long end = System.currentTimeMillis();

        System.out.println("Input of: " + input.length());
        System.out.println("Took: " + ((end - start)) + "ms");

    }

    private String generateRandomString(int size, int splitterFreq) {
        Random random = new Random();
        String language = "123456789abcdefghijklmnopqrstuvwxyz#$%-./:=@_)";
        String splitters = ",;<>";
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

    private static Set<String> getBytesDecoded(String input) {
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        ByteBuffer buffer = ByteBuffer.allocate(input.length());

        BiFunction<Stream, ByteBuffer, Set<Token>> fn = new TokenizeFunction();
        Set<Token> result = fn.apply(stream, buffer);

        Set<String> decoded = new HashSet<>();

        for (Token buf : result) {
            decoded.addAll(buf.asStringSet());
        }
        return decoded;
    }
}

