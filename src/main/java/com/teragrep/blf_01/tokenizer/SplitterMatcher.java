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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SplitterMatcher {
    private static final Set<ByteBuffer> multiSplitters =
            Arrays.stream(new String[]{"%0A", "%20", "%21", "%2520", "%2526", "%26", "%28", "%29", "%2B", "%2C", "%3A", "%3B", "%3D", "%5B", "%5D", "%7C", "--"})
            .map(str -> ByteBuffer.wrap(str.getBytes(StandardCharsets.US_ASCII))).collect(Collectors.toSet());
    private static final Set<Byte> majorSplitters =
            Arrays.stream(new String[]{"\t","\n","\r"," ","!","\\","&","'","(",")","*","+",",",";","<",">","?","[","]","{","|","}"})
                    .map(str -> str.getBytes(StandardCharsets.US_ASCII)[0]).collect(Collectors.toSet());

    private static final Set<Byte> minorSplitters =
            Arrays.stream(new String[]{"#","$","%","-",".","/",":","=","@","\\\\","_"})
                    .map(str -> str.getBytes(StandardCharsets.US_ASCII)[0]).collect(Collectors.toSet());

    public static boolean singleMatch(byte compared) {
        return majorSplitters.contains(compared);
    }

    public static boolean minorMatch(byte compared) {
        return minorSplitters.contains(compared);
    }

    public static boolean multiMatch(byte[] compared) {
        if (compared[0] != 37 && compared[0] != 45) {
            return false;
        }
        return multiSplitters.contains(ByteBuffer.wrap(compared));
    }
}
