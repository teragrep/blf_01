package com.teragrep.blf_01.tokenizer;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenTest {

    @Test
    public void splitterAtStartTest() {
        final Token token = new Token("%20");
        final Set<String> set = token.getMinorTokens();
        final HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("%");
        expectedSet.add("20");

        assertTrue(set.containsAll(expectedSet));
        assertEquals(expectedSet.size(), set.size());
    }

    @Test
    public void splitterAtEndTest() {
        final Token token = new Token("20.");
        final Set<String> set = token.getMinorTokens();
        final HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("20");
        expectedSet.add(".");

        assertTrue(set.containsAll(expectedSet));
        assertEquals(expectedSet.size(), set.size());
    }

    @Test
    public void multipleSplitterTypesTest() {
        final Token token = new Token("b/c:d=e");
        final Set<String> set = token.getMinorTokens();
        final HashSet<String> expectedSet = new HashSet<>();

        expectedSet.add("b/c:d=");
        expectedSet.add("b/c:d");
        expectedSet.add("b/c:");
        expectedSet.add("b/c");
        expectedSet.add("b/");
        expectedSet.add("b");
        expectedSet.add("/c:d=e");
        expectedSet.add("/c:d=");
        expectedSet.add("/c:d");
        expectedSet.add("/c:");
        expectedSet.add("/c");
        expectedSet.add("c:d=e");
        expectedSet.add("c:d=");
        expectedSet.add("c:d");
        expectedSet.add("c:");
        expectedSet.add("c");
        expectedSet.add(":d=e");
        expectedSet.add(":d=");
        expectedSet.add(":d");
        expectedSet.add("d");
        expectedSet.add("d=e");
        expectedSet.add("d=");
        expectedSet.add("=e");
        expectedSet.add("e");

        assertTrue(set.containsAll(expectedSet));
        assertEquals(expectedSet.size(), set.size());

    }

    @Test
    public void noSplitterTest() {
        final Token token = new Token("abcdefghijklmn");
        final Set<String> set = token.getMinorTokens();

        assertEquals(0, set.size());
    }
    @Test
    public void multipleSplitTest() {
        final Token token = new Token("b.c.d.e");
        final Set<String> set = token.getMinorTokens();
        final HashSet<String> expectedSet = new HashSet<>();

        expectedSet.add("b.c.d.");
        expectedSet.add("b.c.d");
        expectedSet.add("b.c.");
        expectedSet.add("b.c");
        expectedSet.add("b.");
        expectedSet.add("b");
        expectedSet.add(".c.d.e");
        expectedSet.add(".c.d.");
        expectedSet.add(".c.d");
        expectedSet.add(".c.");
        expectedSet.add(".c");
        expectedSet.add("c.d.e");
        expectedSet.add("c.d.");
        expectedSet.add("c.d");
        expectedSet.add("c.");
        expectedSet.add("c");
        expectedSet.add(".d.e");
        expectedSet.add(".d.");
        expectedSet.add(".d");
        expectedSet.add("d.e");
        expectedSet.add("d.");
        expectedSet.add("d");
        expectedSet.add(".e");
        expectedSet.add("e");

        assertTrue(set.containsAll(expectedSet));
        assertEquals(expectedSet.size(), set.size());

    }
}
