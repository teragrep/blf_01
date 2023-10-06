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

        String input = "test%20test,b.c--jkl<jl-- ";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))
        Stream stream = new Stream(bais);

        delimiterWindow.findBy(stream);

        /*
        Set<String> decoded = new HashSet<>();

        assertTrue(decoded.contains("test"));
        assertTrue(decoded.contains("%20"));
        assertTrue(decoded.contains(","));
        assertTrue(decoded.contains("b.c"));
        assertTrue(decoded.contains("--"));
        assertTrue(decoded.contains("jkl"));
        assertTrue(decoded.contains("<"));
        assertTrue(decoded.contains("jl"));
        assertTrue(decoded.contains(" "));

        assertFalse(decoded.contains("jl--"));
        assertFalse(decoded.contains("test%"));
        assertFalse(decoded.contains("test,"));
        */
    }
}
