package com.teragrep.blf_01;

import java.util.HashSet;
import java.util.Set;

public class MajorDelimiters {

    Set<Delimiter> delimiterSet;
    MajorDelimiters() {
        this.delimiterSet = new HashSet<>();

        delimiterSet.add(new Delimiter("\t"));
        delimiterSet.add(new Delimiter("\n"));
        delimiterSet.add(new Delimiter("\r"));
        delimiterSet.add(new Delimiter(" "));
        delimiterSet.add(new Delimiter("!"));
        delimiterSet.add(new Delimiter("\""));
        delimiterSet.add(new Delimiter("%0A"));
        delimiterSet.add(new Delimiter("%20"));
        delimiterSet.add(new Delimiter("%21"));
        delimiterSet.add(new Delimiter("%2520"));
        delimiterSet.add(new Delimiter("%2526"));
        delimiterSet.add(new Delimiter("%26"));
        delimiterSet.add(new Delimiter("%28"));
        delimiterSet.add(new Delimiter("%29"));
        delimiterSet.add(new Delimiter("%2B"));
        delimiterSet.add(new Delimiter("%2C"));
        delimiterSet.add(new Delimiter("%3A"));
        delimiterSet.add(new Delimiter("%3B"));
        delimiterSet.add(new Delimiter("%3D"));
        delimiterSet.add(new Delimiter("%5B"));
        delimiterSet.add(new Delimiter("%5D"));
        delimiterSet.add(new Delimiter("%7C"));
        delimiterSet.add(new Delimiter("&"));
        delimiterSet.add(new Delimiter("'"));
        delimiterSet.add(new Delimiter("|"));
        delimiterSet.add(new Delimiter("("));
        delimiterSet.add(new Delimiter(")"));
        delimiterSet.add(new Delimiter("*"));
        delimiterSet.add(new Delimiter("+"));
        delimiterSet.add(new Delimiter(","));
        delimiterSet.add(new Delimiter("--"));
        delimiterSet.add(new Delimiter(";"));
        delimiterSet.add(new Delimiter("<"));
        delimiterSet.add(new Delimiter(">"));
        delimiterSet.add(new Delimiter("?"));
        delimiterSet.add(new Delimiter("["));
        delimiterSet.add(new Delimiter("]"));
        delimiterSet.add(new Delimiter("{"));
        delimiterSet.add(new Delimiter("\\"));
        delimiterSet.add(new Delimiter("}"));
    }
}
