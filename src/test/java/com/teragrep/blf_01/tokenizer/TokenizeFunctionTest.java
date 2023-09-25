package com.teragrep.blf_01.tokenizer;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

public class TokenizeFunctionTest {

    @Test
    public void tokenFunctionTest() {
        String input = "te%20st--test,";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII));
        Stream stream = new Stream(bais);
        ByteBuffer buffer = ByteBuffer.allocateDirect(input.length());

        BiFunction<Stream, ByteBuffer, ByteBuffer> fn = new TokenizeFunction();
        ByteBuffer result = fn.apply(stream, buffer);

        String written = StandardCharsets.UTF_8.decode(result).toString();

        System.out.println("Result: " + written);
    }
}

