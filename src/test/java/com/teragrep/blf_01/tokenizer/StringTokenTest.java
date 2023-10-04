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

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Disabled
public class StringTokenTest {

    @Test
    public void printTest() {
        final StringToken token = new StringToken("%20");

        assertEquals("Token: '%20', minor tokens [%, 20]", token.toString());
    }

    @Test
    public void splitterAtStartTest() {
        final StringToken token = new StringToken("%20");
        final Set<String> set = token.getMinorTokens();
        final HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("%");
        expectedSet.add("20");

        assertTrue(set.containsAll(expectedSet));
        assertEquals(expectedSet.size(), set.size());
    }

    @Test
    public void splitterAtEndTest() {
        final StringToken token = new StringToken("20.");
        final Set<String> set = token.getMinorTokens();
        final HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("20");
        expectedSet.add(".");

        assertTrue(set.containsAll(expectedSet));
        assertEquals(expectedSet.size(), set.size());
    }

    @Test
    public void multipleSplitterTypesTest() {
        final StringToken token = new StringToken("b/c:d=e");
        final Set<String> set = token.getMinorTokens();
        final HashSet<String> expectedSet = new HashSet<>();

        expectedSet.add("b/c:d=");
        expectedSet.add("b/c:d");
        expectedSet.add("b/c:");
        expectedSet.add("b/c");
        expectedSet.add("b/");
        expectedSet.add("b");
        expectedSet.add("/c:d=e");
        expectedSet.add("/c:d=");
        expectedSet.add("/c:d");
        expectedSet.add("/c:");
        expectedSet.add("/c");
        expectedSet.add("c:d=e");
        expectedSet.add("c:d=");
        expectedSet.add("c:d");
        expectedSet.add("c:");
        expectedSet.add("c");
        expectedSet.add(":d=e");
        expectedSet.add(":d=");
        expectedSet.add(":d");
        expectedSet.add("d");
        expectedSet.add("d=e");
        expectedSet.add("d=");
        expectedSet.add("=e");
        expectedSet.add("e");

        assertTrue(set.containsAll(expectedSet));
        assertEquals(expectedSet.size(), set.size());

    }

    @Test
    public void noSplitterTest() {
        final StringToken token = new StringToken("abcdefghijklmn");
        final Set<String> set = token.getMinorTokens();

        assertEquals(0, set.size());
    }
    @Test
    public void multipleSplitTest() {
        final StringToken token = new StringToken("b.c.d.e");
        final Set<String> set = token.getMinorTokens();
        final HashSet<String> expectedSet = new HashSet<>();

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

        assertTrue(set.containsAll(expectedSet));
        assertEquals(expectedSet.size(), set.size());

    }
}