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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public class Token {

    public final byte[] bytes;

    public final boolean isStub;

    public final boolean isDelimiter;

    Token() {
        this.isStub = true;
        this.bytes = new byte[]{};
        this.isDelimiter = false;
    }

    Token(byte[] bytes, boolean isDelimiter) {
        this.bytes = bytes;
        this.isStub = false;
        this.isDelimiter = isDelimiter;
    }

    Token(ByteBuffer buffer, boolean isDelimiter) {
        this.bytes = new byte[buffer.remaining()];
        buffer.get(this.bytes);
        this.isStub = false;
        this.isDelimiter = isDelimiter;
    }

    Token(String string, boolean isDelimiter) {
        this.bytes = string.getBytes(StandardCharsets.UTF_8);
        this.isStub = false;
        this.isDelimiter = isDelimiter;
    }




    @Override
    public String toString() {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return isStub == token.isStub && isDelimiter == token.isDelimiter && Arrays.equals(bytes, token.bytes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(isStub, isDelimiter);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }
}
