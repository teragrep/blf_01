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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MajorTokenStreamTest {

    @Test
    public void simpleMajorTokenStreamTest() {
        Set<String> tokenSet = new MajorTokenStream("1%20b.c.d.e%202").getTokenSet();
        Set<String> expectedSet = new HashSet<>(Arrays.asList("1","%20","b.c.d.e","2"));
        assertTrue(tokenSet.containsAll(expectedSet));
    }

    @Test
    public void simpleMinorTokenStreamTest() {
        Set<String> tokenSet = new MajorTokenStream("1%20b.c.d.e%202").getTokenSetWithMinorTokens();
        Set<String> expectedSet = new HashSet<>();

        // Major
        expectedSet.add("1");
        expectedSet.add("%20");
        expectedSet.add("b.c.d.e");
        expectedSet.add("2");
        // Minor
        expectedSet.add("%");
        expectedSet.add("20");
        expectedSet.add("b.c.d.");
        expectedSet.add("b.c.d");
        expectedSet.add("b.c.");
        expectedSet.add("b.c");
        expectedSet.add("b.");
        expectedSet.add("b");
        expectedSet.add(".c.d.e");
        expectedSet.add(".c.d.");
        expectedSet.add(".c.d");
        expectedSet.add(".c.");
        expectedSet.add(".c");
        expectedSet.add("c.d.e");
        expectedSet.add("c.d.");
        expectedSet.add("c.d");
        expectedSet.add("c.");
        expectedSet.add("c");
        expectedSet.add(".d.e");
        expectedSet.add(".d.");
        expectedSet.add(".d");
        expectedSet.add("d.e");
        expectedSet.add("d.");
        expectedSet.add("d");
        expectedSet.add(".e");
        expectedSet.add("e");

        assertTrue(tokenSet.containsAll(expectedSet));
        assertEquals(tokenSet.size(), expectedSet.size());
    }

    @Test
    public void testMajorSplitterAtEdges() {
        Set<String> tokenSet = new MajorTokenStream("<ab>").getTokenSet();
        Set<String> expectedSet = new HashSet<>();

        expectedSet.add("<");
        expectedSet.add("ab");
        expectedSet.add(">");

        System.out.println(expectedSet);
        System.out.println(tokenSet);

        assertTrue(tokenSet.containsAll(expectedSet));
        assertEquals(tokenSet.size(), expectedSet.size());
    }


    @Test
    public void doubleHyphenTest() {
        Set<String> tokenSet = new MajorTokenStream("-one--two---three-").getTokenSet();
        Set<String> expectedSet = new HashSet<>(Arrays.asList("--","-one","two","-three-"));
        assertTrue(tokenSet.containsAll(expectedSet));
    }

    @Test
    public void urlEncodingTest() {
        Set<String> tokenSet = new MajorTokenStream("html%20with%1234.%252010").getTokenSet();
        Set<String> expectedSet = new HashSet<>(Arrays.asList("html","%20","with%1234.","%2520","10"));
        assertTrue(tokenSet.containsAll(expectedSet));
    }
}
