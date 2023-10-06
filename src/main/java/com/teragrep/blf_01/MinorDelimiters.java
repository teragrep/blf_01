package com.teragrep.blf_01;

import java.util.HashSet;
import java.util.Set;

public class MinorDelimiters implements Delimiters {

    private final Set<Delimiter> delimiterSet;

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

    @Override
    public Set<Delimiter> getDelimiters() {
        return delimiterSet;
    }
}
