package com.teragrep.blf_01;

import java.util.LinkedList;

public class ConcatenatedToken {

    LinkedList<Token> tokens;
    public ConcatenatedToken(LinkedList<Token> tokens) {
        this.tokens = tokens;
    }

    byte[] concatenate() {
        int size = 0;
        for (Token token : tokens) {
            size = size + token.bytes.length;
        }
        byte[] bytes = new byte[size];

        int pos = 0;
        for (Token token : tokens) {
            System.arraycopy(token.bytes, 0, bytes, pos, token.bytes.length);
            pos = pos + token.bytes.length;
        }

        return bytes;
    }
}
