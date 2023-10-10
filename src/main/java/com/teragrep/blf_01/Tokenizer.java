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

package com.teragrep.blf_01;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class to access the tokenizer
 */
public class Tokenizer {

    /**
     * Tokenizes a string.
     *
     * @param input String that is tokenized
     * @return String set of tokens from string
     */
    public Set<String> tokenize(String input) {

        ByteArrayInputStream bais = new ByteArrayInputStream(
                input.getBytes(StandardCharsets.US_ASCII)
        );

        final Stream stream = new Stream();

        stream.setInputStream(bais);

        return getTokensAsStringSet(stream);

    }

    /**
     * tokenizes an input stream.
     *
     * @param is input stream that is tokenized
     * @return String set of tokens from input stream
     */
    public Set<String> tokenize(InputStream is) {

        final Stream stream = new Stream();

        stream.setInputStream(is);

        return getTokensAsStringSet(stream);

    }

    Set<String> getTokensAsStringSet(Stream stream) {

        TokenScan majorTokenScan = new TokenScan(new MajorDelimiters());

        ArrayList<Token> majorTokens = majorTokenScan.findBy(stream);

        ArrayList<Token> allTokens = new ArrayList<>(majorTokens);

        Delimiters minorDelimiters = new MinorDelimiters();

        TokenScan minorTokenScan = new TokenScan(minorDelimiters);

        Entanglement entanglement = new Entanglement();

        for (Token token : majorTokens) {

            ByteArrayInputStream tokenBais = new ByteArrayInputStream(token.bytes);

            stream.setInputStream(tokenBais);

            ArrayList<Token> minorTokens = minorTokenScan.findBy(stream);


            allTokens.addAll(entanglement.entangle(minorTokens));
        }

        return convertToStringSet(allTokens);

    }

    private Set<String> convertToStringSet(ArrayList<Token> tokenList) {
        Set<String> rv;

        // parallel stream when more than 10 000 tokens
        if (tokenList.size() > 10000) {

            rv = tokenList.parallelStream().map(Token::toString).collect(Collectors.toSet());

        } else {

            rv = tokenList.stream().map(Token::toString).collect(Collectors.toSet());

        }

        return rv;

    }
}
