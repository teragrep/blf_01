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

import java.util.ArrayList;
import java.util.ListIterator;

public class Entanglement {

    private final ArrayList<Token> endWindowScanTokens;
    private final ArrayList<Token> allTokens;
    private final ArrayList<Token> forwardScanTokens;
    private final ArrayList<Token> backwardsScanTokens;
    private final ConcatenatedToken concatenatedToken;
    public Entanglement() {
        this.endWindowScanTokens = new ArrayList<>(512);
        this.allTokens = new ArrayList<>(512);
        this.forwardScanTokens = new ArrayList<>(512);
        this.backwardsScanTokens = new ArrayList<>(512);
        this.concatenatedToken = new ConcatenatedToken();
    }

    public ArrayList<Token> entangle(ArrayList<Token> tokens) {
        //System.out.println("entangling> " + tokens + " results into " + rv);
        return startWindowScan(tokens);

    }


    /**
     * Iterates Token list in forward order,
     * starting from the largest subList and going to smaller ones
     * and processes reverse order ones within
     */
    private ArrayList<Token> startWindowScan(ArrayList<Token> tokenList)  {
        allTokens.clear();
        for (int i = 0; i < tokenList.size(); i++) {
            ListIterator<Token> forwardIterator = tokenList.listIterator(i);
            // +++++ task
            forwardScanTokens.clear();
            while (forwardIterator.hasNext()) {
                forwardScanTokens.add(forwardIterator.next());
            }
            // +++++ subtask endWindowScan
            allTokens.addAll(endWindowScan(forwardScanTokens));
            // -----
            Token concatenated = new Token(concatenatedToken.concatenate(forwardScanTokens));
            allTokens.add(concatenated);
            // -----
        }

        return allTokens;
    }

    /**
     * Iterates Token list in reverse order,
     * starting from the largest subList and going to smaller ones
     */
    private ArrayList<Token> endWindowScan(ArrayList<Token> tokenList) {
        endWindowScanTokens.clear();

        for (int i = tokenList.size() - 1; i > 0; i--) {
            ListIterator<Token> backwardIterator = tokenList.listIterator(i);
            // +++++ task
            backwardsScanTokens.clear();
            while (backwardIterator.hasPrevious()) {
                backwardsScanTokens.add(0, backwardIterator.previous());
            }
            endWindowScanTokens.add(new Token(concatenatedToken.concatenate(backwardsScanTokens)));
            // ----- task
        }

        return endWindowScanTokens;
    }
}
