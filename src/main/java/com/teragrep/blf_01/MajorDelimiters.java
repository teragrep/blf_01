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

import java.nio.ByteBuffer;
import java.util.*;

public class MajorDelimiters implements Delimiters {

    private final HashMap<ByteBuffer, Delimiter> delimiterSet;
    MajorDelimiters() {
        this.delimiterSet = new HashMap<>();

        delimiterSet.put(new Delimiter("\t").delimiterBuffer, new Delimiter("\t"));
        delimiterSet.put(new Delimiter("\n").delimiterBuffer, new Delimiter("\n"));
        delimiterSet.put(new Delimiter("\r").delimiterBuffer, new Delimiter("\r"));
        delimiterSet.put(new Delimiter(" ").delimiterBuffer, new Delimiter(" "));
        delimiterSet.put(new Delimiter("!").delimiterBuffer, new Delimiter("!"));
        delimiterSet.put(new Delimiter("\"").delimiterBuffer, new Delimiter("\""));
        delimiterSet.put(new Delimiter("%0A").delimiterBuffer, new Delimiter("%0A"));
        delimiterSet.put(new Delimiter("%20").delimiterBuffer, new Delimiter("%20"));
        delimiterSet.put(new Delimiter("%21").delimiterBuffer, new Delimiter("%21"));
        delimiterSet.put(new Delimiter("%2520").delimiterBuffer, new Delimiter("%2520"));
        delimiterSet.put(new Delimiter("%2526").delimiterBuffer, new Delimiter("%2526"));
        delimiterSet.put(new Delimiter("%26").delimiterBuffer, new Delimiter("%26"));
        delimiterSet.put(new Delimiter("%28").delimiterBuffer, new Delimiter("%28"));
        delimiterSet.put(new Delimiter("%29").delimiterBuffer, new Delimiter("%29"));
        delimiterSet.put(new Delimiter("%2B").delimiterBuffer, new Delimiter("%2B"));
        delimiterSet.put(new Delimiter("%2C").delimiterBuffer, new Delimiter("%2C"));
        delimiterSet.put(new Delimiter("%3A").delimiterBuffer, new Delimiter("%3A"));
        delimiterSet.put(new Delimiter("%3B").delimiterBuffer, new Delimiter("%3B"));
        delimiterSet.put(new Delimiter("%3D").delimiterBuffer, new Delimiter("%3D"));
        delimiterSet.put(new Delimiter("%5B").delimiterBuffer, new Delimiter("%5B"));
        delimiterSet.put(new Delimiter("%5D").delimiterBuffer, new Delimiter("%5D"));
        delimiterSet.put(new Delimiter("%7C").delimiterBuffer, new Delimiter("%7C"));
        delimiterSet.put(new Delimiter("&").delimiterBuffer, new Delimiter("&"));
        delimiterSet.put(new Delimiter("'").delimiterBuffer, new Delimiter("'"));
        delimiterSet.put(new Delimiter("|").delimiterBuffer, new Delimiter("|"));
        delimiterSet.put(new Delimiter("(").delimiterBuffer, new Delimiter("("));
        delimiterSet.put(new Delimiter(")").delimiterBuffer, new Delimiter(")"));
        delimiterSet.put(new Delimiter("*").delimiterBuffer, new Delimiter("*"));
        delimiterSet.put(new Delimiter("+").delimiterBuffer, new Delimiter("+"));
        delimiterSet.put(new Delimiter(",").delimiterBuffer, new Delimiter(","));
        delimiterSet.put(new Delimiter("--").delimiterBuffer, new Delimiter("--"));
        delimiterSet.put(new Delimiter(";").delimiterBuffer, new Delimiter(";"));
        delimiterSet.put(new Delimiter("<").delimiterBuffer, new Delimiter("<"));
        delimiterSet.put(new Delimiter(">").delimiterBuffer, new Delimiter(">"));
        delimiterSet.put(new Delimiter("?").delimiterBuffer, new Delimiter("?"));
        delimiterSet.put(new Delimiter("[").delimiterBuffer, new Delimiter("["));
        delimiterSet.put(new Delimiter("]").delimiterBuffer, new Delimiter("]"));
        delimiterSet.put(new Delimiter("{").delimiterBuffer, new Delimiter("{"));
        delimiterSet.put(new Delimiter("\\").delimiterBuffer, new Delimiter("\\"));
        delimiterSet.put(new Delimiter("}").delimiterBuffer, new Delimiter("}"));
    }

    @Override
    public HashMap<ByteBuffer, Delimiter> getDelimiters() {
        return delimiterSet;
    }
}
