package com.teragrep.blf_01;

import java.util.HashSet;
import java.util.Set;

public class MinorDelimiters {

    Set<Delimiter> delimiterSet;

    MinorDelimiters() {
        this.delimiterSet = new HashSet<>();

        delimiterSet.add(new Delimiter("#"));
        delimiterSet.add(new Delimiter("$"));
        delimiterSet.add(new Delimiter("%"));
        delimiterSet.add(new Delimiter("-"));
        delimiterSet.add(new Delimiter("."));
        delimiterSet.add(new Delimiter("/"));
        delimiterSet.add(new Delimiter(":"));
        delimiterSet.add(new Delimiter("="));
        delimiterSet.add(new Delimiter("@"));
        delimiterSet.add(new Delimiter("\\"));
        delimiterSet.add(new Delimiter("_"));

    }
}
