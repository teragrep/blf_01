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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class EntanglementTest {

    @Test
    public void subTokenSplitTest() {

        String testString = "b.c.d";
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        Stream stream = new Stream(bais);

        DelimiterWindow delimiterWindow = new DelimiterWindow(new MinorDelimiters());

        LinkedList<Token> tokens = delimiterWindow.findBy(stream);

        Assertions.assertEquals(5, tokens.size());

        Assertions.assertEquals(tokens.get(0), new Token("b", false));
        Assertions.assertEquals(tokens.get(1), new Token(".", true));
        Assertions.assertEquals(tokens.get(2), new Token("c", false));
        Assertions.assertEquals(tokens.get(3), new Token(".", true));
        Assertions.assertEquals(tokens.get(4), new Token("d", false));

        Entanglement entanglement = new Entanglement(tokens);
        LinkedList<Token> subTokens = entanglement.entangle();

        System.out.println("SOUTTTIII" +  subTokens);

        Assertions.assertEquals(15, subTokens.size());

        Assertions.assertTrue(subTokens.contains(new Token("b.c.d", false)));
        Assertions.assertTrue(subTokens.contains(new Token("b.c.", false)));
        Assertions.assertTrue(subTokens.contains(new Token("b.c", false)));
        Assertions.assertTrue(subTokens.contains(new Token("b.", false)));
        Assertions.assertTrue(subTokens.contains(new Token("b", false)));
        Assertions.assertTrue(subTokens.contains(new Token(".c.d", false)));
        Assertions.assertTrue(subTokens.contains(new Token(".c.", false)));
        Assertions.assertTrue(subTokens.contains(new Token(".c", false)));
        Assertions.assertTrue(subTokens.contains(new Token(".", true)));
        Assertions.assertTrue(subTokens.contains(new Token("c.d", false)));
        Assertions.assertTrue(subTokens.contains(new Token("c.", false)));
        Assertions.assertTrue(subTokens.contains(new Token("c", false)));
        Assertions.assertTrue(subTokens.contains(new Token(".d", false)));
        Assertions.assertTrue(subTokens.contains(new Token(".", false)));
        Assertions.assertTrue(subTokens.contains(new Token("d", false)));

    }
}
