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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MajorTokenStreamTest {
    MajorTokenStream stream;
    public MajorTokenStreamTest() {
        stream = new MajorTokenStream();
    }

    @Test
    public void MajorTokenStreamTest() throws IOException {
        Set<String> tokenSet = stream.split("1%20b.c.d.e%202");
        Set<String> expectedSet = new HashSet<>(Arrays.asList("1","%20","b.c.d.e","2"));
        System.out.println(tokenSet);
        System.out.println(expectedSet);
        assertTrue(tokenSet.containsAll(expectedSet));
    }

    @Test
    public void doubleHyphenTest() throws IOException {
        Set<String> tokenSet = stream.split("-one--two---three-");
        Set<String> expectedSet = new HashSet<>(Arrays.asList("--","-one","two","-three-"));
        assertTrue(tokenSet.containsAll(expectedSet));
    }

    @Test
    public void urlEncodingTest() throws IOException {
        Set<String> tokenSet = stream.split("html%20with%1234.%252010");
        Set<String> expectedSet = new HashSet<>(Arrays.asList("html","%20","with%1234.","%2520","10"));
        assertTrue(tokenSet.containsAll(expectedSet));
    }
}
