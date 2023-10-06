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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TokenizationTest {

    @Test
    public void splitterTest() {
        DelimiterWindow delimiterWindow = new DelimiterWindow(new MajorDelimiters());

        String input = "test%20test,b.c--opr<xz-- ";
        //String input = "%20";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Stream stream = new Stream(bais);

        LinkedList<Token> decoded = delimiterWindow.findBy(stream);

        for (Token token : decoded) {
            Assertions.assertFalse(token.isStub);
        }

        /*
        for (Token token : decoded) {
            System.out.println(token);
        }

         */
        assertEquals(11, decoded.size());

        assertEquals(new Token("test"), decoded.get(0));
        assertEquals(new Token("%20"), decoded.get(1));
        assertEquals(new Token("test"), decoded.get(2));
        assertEquals(new Token(","), decoded.get(3));
        assertEquals(new Token("b.c"), decoded.get(4));
        assertEquals(new Token("--"), decoded.get(5));
        assertEquals(new Token("opr"), decoded.get(6));
        assertEquals(new Token("<"), decoded.get(7));
        assertEquals(new Token("xz"), decoded.get(8));
        assertEquals(new Token("--"), decoded.get(9));
        assertEquals(new Token(" "), decoded.get(10));


    }

    @Test
    public void splitterTest2() {
        DelimiterWindow delimiterWindow = new DelimiterWindow(new MajorDelimiters());

        String input = "a,2";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Stream stream = new Stream(bais);

        LinkedList<Token> decoded = delimiterWindow.findBy(stream);

        for (Token token : decoded) {
            Assertions.assertFalse(token.isStub);
        }

        assertEquals(3, decoded.size());

        assertEquals(new Token("a"), decoded.get(0));
        assertEquals(new Token(","), decoded.get(1));
        assertEquals(new Token("2"), decoded.get(2));


    }

    @Test
    public void splitterTest3() {
        DelimiterWindow delimiterWindow = new DelimiterWindow(new MajorDelimiters());

        String input = ",x|";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Stream stream = new Stream(bais);

        LinkedList<Token> decoded = delimiterWindow.findBy(stream);

        for (Token token : decoded) {
            Assertions.assertFalse(token.isStub);
        }

        assertEquals(3, decoded.size());

        assertEquals(new Token(","), decoded.get(0));
        assertEquals(new Token("x"), decoded.get(1));
        assertEquals(new Token("|"), decoded.get(2));


    }
}
