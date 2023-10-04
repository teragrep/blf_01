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

import com.teragrep.blf_01.tokenizer.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Disabled
public class StreamTest {

    @Test
    public void getBytesTest() {
        String input = "testString";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        StringBuilder builder = new StringBuilder();
        int max = input.length();
        byte b;

        stream.next();

        b = stream.get();

        while(max > 0) {
            max--;

            char c = (char) b;
            builder.append(c);

            if (max == 2) {
                stream.mark();
            }

            stream.next();

            b = stream.get();
        }

        assertEquals(input, builder.toString());

    }

    @Test
    public void streamMarkTest() {
        String input = "test";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        int max = input.length();

        stream.next();

        stream.get();

        while(max > 0) {
            max--;

            if (max == 2) {
                stream.mark();
            }

            stream.next();

            stream.get();
        }

        assertEquals(2, stream.getMark());
    }

    @Test
    public void streamTestReset() {

        String input = "abcd";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        StringBuilder builder = new StringBuilder();
        char c;
        int max = input.length();

        byte b;

        stream.next();

        b = stream.get();
        c = (char) b;
        builder.append(c);

        while(max > 0) {
            max--;

            if (max == 2) {
                stream.mark();
            }

            if (!stream.next()) {
                max = stream.getMark();
                break;
            }

            b = stream.get();
            c = (char) b;
            builder.append(c);
        }

        stream.reset();

        while(max > 0) {
            max--;

            if (!stream.next()) {
                break;
            }

            b = stream.get();
            c = (char) b;
            builder.append(c);
        }

        assertEquals("abcdcd", builder.toString());

    }

    @Test
    public void streamSkipTest() {
        String input = "abcdef";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        StringBuilder builder = new StringBuilder();
        char c;
        int max = input.length();

        byte b;
        stream.next();

        b = stream.get();
        c = (char) b;
        builder.append(c);

        while(max > 0) {
            max--;

            if (max == 2) {
                stream.skip(1);
                max--;
            }

            if (!stream.next()) {
                break;
            }

            b = stream.get();
            c = (char) b;
            builder.append(c);
        }

        assertEquals("abcdf", builder.toString());
    }

    @Test
    public void streamSkipOutOfBoundsTest() {
        String input = "abcdef";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        StringBuilder builder = new StringBuilder();
        char c;
        int max = input.length();
        int skipAmmount = 10;

        byte b;
        stream.next();

        b = stream.get();
        c = (char) b;
        builder.append(c);
        stream.skip(skipAmmount);

        stream.next();

        b = stream.get();
        if (b != 0) {
            c = (char) b;
            builder.append(c);
        }

        assertEquals("a", builder.toString());
        assertEquals(1, builder.length());

    }

    @Test
    public void testIgnoreNegativeSkips() {
        String input = "abcdef";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        StringBuilder builder = new StringBuilder();
        char c;
        int max = input.length();

        byte b;
        stream.next();

        b = stream.get();
        c = (char) b;
        builder.append(c);

        while(max > 0) {
            max--;
            stream.skip(-100);

            if (!stream.next()) {
                break;
            }

            b = stream.get();
            c = (char) b;
            builder.append(c);
        }

        assertEquals(input, builder.toString());
    }
}
