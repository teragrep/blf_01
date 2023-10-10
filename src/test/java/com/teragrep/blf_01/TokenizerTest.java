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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenizerTest {

    @Test
    public void testTokenization() {
        Tokenizer tokenizer = new Tokenizer();
        String input = "[20/Feb/2022:03.456]";
        Set<String> resultSet = tokenizer.tokenize(input);

        Set<String> expectedSet = new HashSet<>(
                Arrays.asList(
                        "/2022", "/Feb", "03", "20/Feb/2022:03",
                        "2022:03", "20/Feb/2022:03.456", "20/Feb/", ":03.",
                        "20/Feb/2022", "/2022:03.456", "03.",
                        "Feb/", ":03.456", "456", "Feb/2022:03", "2022:03.456",
                        "/2022:03.", "[", "2022:03.", "]", "20/Feb", "20/Feb/2022:03.",
                        "Feb", "/Feb/2022:03", "03.456", "/Feb/2022:03.456", "/2022:",
                        "2022", "Feb/2022:", "2022:", "/Feb/", "20/", ".456", "Feb/2022:03.",
                        "20/Feb/2022:", "/Feb/2022:03.", "/Feb/2022:", "/Feb/2022", "Feb/2022",
                        ":03", "20", "Feb/2022:03.456", "/2022:03",".",":","/"
                ));

        System.out.println(expectedSet);
        System.out.println(resultSet);

        assertTrue(expectedSet.containsAll(resultSet));
        assertEquals(resultSet.size(), expectedSet.size());

    }

    @Test
    @Benchmark
    @Disabled
    public void tokenizeFileInput() throws FileNotFoundException {
        Instant start = Instant.now();

        FileInputStream bais = new FileInputStream("src/test/resources/base64.txt");
        Set<String> rv = new Tokenizer().tokenize(bais);
        System.out.println(rv.size());

        Instant end = Instant.now();
        float duration = (float) ChronoUnit.MILLIS.between(start, end)/1000;
        System.out.println("Time taken: " + duration + " seconds");
        System.out.println("Tokens: " + rv.size() + " (" + rv.size()/duration + "/s)");

    }

    @Test
    @Benchmark
    @Disabled
    public void tokenizeStringInput() {
        String input = generateRandomString(1000000,20);

        Instant start = Instant.now();

        Set<String> rv = new Tokenizer().tokenize(input);

        System.out.println(rv.size());

        Instant end = Instant.now();

        float duration = (float) ChronoUnit.MILLIS.between(start, end)/1000;

        System.out.println("Time taken: " + duration + " seconds");

        System.out.println("Tokens: " + rv.size() + " (" + rv.size()/duration + "/s)");

    }

    private String generateRandomString(int size, int splitterFreq) {
        Random random = new Random();
        String language = "123456789abcdefghijklmnopqrstuvwxyz#$%-./:=@_)";
        String majorSplitters = ",;<>";
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i <size; i++ ) {
            if (i % splitterFreq == 0) {
                sb.append(majorSplitters.charAt(random.nextInt(majorSplitters.length())));
            } else {
                sb.append(language.charAt(random.nextInt(language.length())));
            }
        }

        return sb.toString();

    }
}
