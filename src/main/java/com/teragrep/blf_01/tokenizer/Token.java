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
import java.util.Set;
import java.util.regex.Pattern;

public class Token {
    private static final String regex = "(#|\\$|%|-|\\.|/|:|=|@|\\\\|_)";
    private static final Pattern compiledRegex = Pattern.compile(regex);
    private final Set<String> minorTokens = new HashSet<>();
    private final String value;

    public Token(String value) {
        this.value = value;

        int splitIndex = 0;
        for (int i = 0; i < value.length(); i++) {
            if (match(value.charAt(i))) {
                if (i == 0) {
                    addToken(this.value.charAt(i));
                }
                if (i == value.length()-1) {
                    addToken(this.value.charAt(i));
                }
                if (splitIndex == 0) {
                    addMinorTokensFromIndex(splitIndex);
                }

                splitIndex = i;

                // TODO combine these maybe

                // add with splitter
                addMinorTokensFromIndex(splitIndex);
                // add without splitter
                addMinorTokensFromIndex(splitIndex+1);
            }
        }
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

        for(int i = subString.length()-1; i > 0; i--) {
            if (match(subString.charAt(i))) {
                addToken(subString.substring(0,i));
                addToken(subString.substring(0,i+1));
            }
        }
    }

    public Set<String> getMinorTokens() {
        return minorTokens;
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
        return String.valueOf(ch).matches(compiledRegex.pattern());
    }
 }
