package com.teragrep.blf_01;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Delimiter {
    public final ByteBuffer delimiterBuffer;
    Delimiter(String delimiter) {
        byte[] bytes = delimiter.getBytes(StandardCharsets.UTF_8);
        this.delimiterBuffer = ByteBuffer.allocateDirect(bytes.length);
        this.delimiterBuffer.put(bytes);
        this.delimiterBuffer.flip();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delimiter delimiter = (Delimiter) o;
        return Objects.equals(delimiterBuffer, delimiter.delimiterBuffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delimiterBuffer);
    }
}
