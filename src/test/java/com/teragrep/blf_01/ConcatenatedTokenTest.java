package com.teragrep.blf_01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class ConcatenatedTokenTest {

    @Test
    public void testConcatenation() {
        LinkedList<Token> tokenLinkedList = new LinkedList<>();
        Token token1 = new Token("ab", false);
        tokenLinkedList.add(token1);
        Token token2 = new Token(".", true);
        tokenLinkedList.add(token2);
        Token token3 = new Token("cd", false);
        tokenLinkedList.add(token3);

        ConcatenatedToken concatenatedToken = new ConcatenatedToken(tokenLinkedList);

        byte[] expected = "ab.cd".getBytes(StandardCharsets.UTF_8);
        Assertions.assertArrayEquals(expected,concatenatedToken.concatenate());
    }
}
