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
import java.util.Objects;

public class Token {

    private ByteBuffer byteBuffer;

    public final boolean isStub;

    Token() {
        this.isStub = true;
        this.byteBuffer = ByteBuffer.allocate(0);
    }

    Token(byte b) {
        this.byteBuffer = ByteBuffer.allocateDirect(256);
        this.put(b);
        this.isStub = false;
    }

    Token(ByteBuffer buffer) {
        this.byteBuffer = ByteBuffer.allocateDirect(buffer.capacity());
        this.byteBuffer.put(buffer);
        this.isStub = false;
    }

    Token(String string) {
        byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
        this.byteBuffer = ByteBuffer.allocateDirect(stringBytes.length);
        this.byteBuffer.put(stringBytes);
        this.isStub = false;
    }

    void put(byte b) {
        if (byteBuffer.position() == byteBuffer.capacity()) {
            extendBuffer(256);
        }
        byteBuffer.put(b);
    }

    private void extendBuffer(int size) {
        ByteBuffer newBuffer = ByteBuffer.allocateDirect(byteBuffer.capacity() + size);
        ByteBuffer originalBuffer = byteBuffer.slice();
        originalBuffer.flip();
        newBuffer.put(originalBuffer);
        byteBuffer = newBuffer;
    }

    @Override
    public String toString() {
        return debugBuffer(byteBuffer);
    }

    private String debugBuffer(ByteBuffer buffer) {
        ByteBuffer bufferSlice = buffer.duplicate().flip();
        byte[] bufferBytes = new byte[bufferSlice.remaining()];
        bufferSlice.get(bufferBytes);
        return new String(bufferBytes, StandardCharsets.UTF_8);
    }

    byte[] toBytes() {
        ByteBuffer bufferSlice = byteBuffer.duplicate().flip();
        byte[] bufferBytes = new byte[bufferSlice.remaining()];
        bufferSlice.get(bufferBytes);
        return bufferBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return isStub == token.isStub && Objects.equals(byteBuffer, token.byteBuffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(byteBuffer, isStub);
    }
}
