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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Disabled
public class SplitterMatcherTest {

    @Test
    public void singleCharTest() {
        assertTrue(SplitterMatcher.singleMatch("\t".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("\n".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("\r".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch(" ".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("!".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("\\".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("'".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("(".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch(")".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("*".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch(",".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch(";".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("<".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch(">".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("?".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("[".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("]".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("{".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.singleMatch("}".getBytes(StandardCharsets.US_ASCII)[0]));

        assertFalse(SplitterMatcher.singleMatch("a".getBytes(StandardCharsets.US_ASCII)[0]));
        assertFalse(SplitterMatcher.singleMatch("3".getBytes(StandardCharsets.US_ASCII)[0]));
    }

    @Test
    public void minorCharTest() {
        assertTrue(SplitterMatcher.minorMatch("#".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.minorMatch("$".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.minorMatch("%".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.minorMatch("-".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.minorMatch(".".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.minorMatch("/".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.minorMatch(":".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.minorMatch("=".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.minorMatch("@".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.minorMatch("\\\\".getBytes(StandardCharsets.US_ASCII)[0]));
        assertTrue(SplitterMatcher.minorMatch("_".getBytes(StandardCharsets.US_ASCII)[0]));

        assertFalse(SplitterMatcher.minorMatch("2".getBytes(StandardCharsets.US_ASCII)[0]));
        assertFalse(SplitterMatcher.minorMatch(" ".getBytes(StandardCharsets.US_ASCII)[0]));
    }

    @Test
    public void multiCharTest() {

        assertTrue(SplitterMatcher.multiMatch("%20".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%0A".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%21".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%2520".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%2526".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%26".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%28".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%29".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%2B".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%2C".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%3A".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%3B".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%3D".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%5B".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%5D".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("%7C".getBytes(StandardCharsets.US_ASCII)));
        assertTrue(SplitterMatcher.multiMatch("--".getBytes(StandardCharsets.US_ASCII)));


        assertFalse(SplitterMatcher.multiMatch("%111".getBytes(StandardCharsets.US_ASCII)));
        assertFalse(SplitterMatcher.multiMatch("111".getBytes(StandardCharsets.US_ASCII)));
        assertFalse(SplitterMatcher.multiMatch("%".getBytes(StandardCharsets.US_ASCII)));
        assertFalse(SplitterMatcher.multiMatch(" ".getBytes(StandardCharsets.US_ASCII)));
    }
}
