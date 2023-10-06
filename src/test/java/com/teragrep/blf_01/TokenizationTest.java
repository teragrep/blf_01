package com.teragrep.blf_01;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenizationTest {

    @Test
    public void splitterTest() {
        DelimiterWindow delimiterWindow = new DelimiterWindow();

        String input = "test%20test,b.c--opr<xz-- ";
        //String input = "%20";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Stream stream = new Stream(bais);

        delimiterWindow.findBy(stream);

        /*
        Set<String> decoded = new HashSet<>();

        assertTrue(decoded.contains("test"));
        assertTrue(decoded.contains("%20"));
        assertTrue(decoded.contains(","));
        assertTrue(decoded.contains("b.c"));
        assertTrue(decoded.contains("--"));
        assertTrue(decoded.contains("opr"));
        assertTrue(decoded.contains("<"));
        assertTrue(decoded.contains("xz"));
        assertTrue(decoded.contains(" "));

        assertFalse(decoded.contains("xz--"));
        assertFalse(decoded.contains("test%"));
        assertFalse(decoded.contains("test,"));
        */
    }
}
