/*
 * Teragrep Bloom Filter Library BLF-01
 * Copyright (C) 2019, 2020, 2021, 2022, 2023 Suomen Kanuuna Oy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://github.com/teragrep/teragrep/blob/main/LICENSE>.
 *
 *
 * Additional permission under GNU Affero General Public License version 3
 * section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it
 * with other code, such other code is not for that reason alone subject to any
 * of the requirements of the GNU Affero GPL version 3 as long as this Program
 * is the same Program as licensed from Suomen Kanuuna Oy without any additional
 * modifications.
 *
 * Supplemented terms under GNU Affero General Public License version 3
 * section 7
 *
 * Origin of the software must be attributed to Suomen Kanuuna Oy. Any modified
 * versions must be marked as "Modified version of" The Program.
 *
 * Names of the licensors and authors may not be used for publicity purposes.
 *
 * No rights are granted for use of trade names, trademarks, or service marks
 * which are in The Program if any.
 *
 * Licensee must indemnify licensors and authors for any liability that these
 * contractual assumptions impose on licensors and authors.
 *
 * To the extent this program is licensed as part of the Commercial versions of
 * Teragrep, the applicable Commercial License may apply to this file if you as
 * a licensee so wish it.
 */

package com.teragrep.blf_01.tokenizer;

import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class Token {
    private static final String regex = "(#|\\$|%|-|\\.|/|:|=|@|\\\\|_)";
    private static final Pattern compiledRegex = Pattern.compile(regex);
    private final Set<String> minorTokens = new HashSet<>();
    private final String value;
    private final TreeSet<Integer> indexes = new TreeSet<>();
    private NavigableSet<Integer> reversedIndexes;

    public Token(String value) {
        this.value = value;
    }

    public Set<String> getMinorTokens() {

        // Find splitter indexes
        for (int i = 0; i < value.length(); i++) {
            if (match(value.charAt(i))) {
                indexes.add(i);
            }
        }

        this.reversedIndexes = indexes.descendingSet();

        addMinorTokensFromIndex(0);

        for(int i: indexes) {
            if (i == 0) {
                addToken(this.value.charAt(i));
            }
            if (i == value.length()-1) {
                addToken(this.value.charAt(i));
            }

            // add with splitter
            addMinorTokensFromIndex(i);
            // add without splitter
            addMinorTokensFromIndex(i+1);
        }

        return minorTokens;
    }

    private void addMinorTokensFromIndex(int index) {
        String subString = value.substring(index);

        if(subString.isEmpty() || subString.equals(" ")) {
            return;
        }

        // Add substring
        if (!subString.equals(value)) {
            addToken(subString);
        }

        for (int i : reversedIndexes) {
            if (i <= index) {
                break;
            }
            addToken(value.substring(index,i));
            addToken(value.substring(index,i+1));
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token: '" + value + "', minor tokens " + getMinorTokens();
    }

    private void addToken(char c) {
        if (!String.valueOf(c).equals(value)) {
            minorTokens.add(String.valueOf(c));
        }
    }
    private void addToken(String s) {
        if (!s.equals(value)) {
            minorTokens.add(s);
        }
    }

    private static boolean match(char ch) {
        return compiledRegex.matcher(String.valueOf(ch)).matches();
    }
 }
