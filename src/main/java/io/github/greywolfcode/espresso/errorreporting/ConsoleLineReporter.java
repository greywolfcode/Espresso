/*
 * Espresso Compiler
 * Copyright (C) 2026  greywolfcode
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package io.github.greywolfcode.espresso.errorreporting;

public class ConsoleLineReporter extends ErrorReporter
{
    public void report(int offset, String fileData, String fileName, String message)
    {
        hadError = true;

        LineData data = getLineData(fileData, offset);
        
        System.err.println(fileName + " [line " + data.lineNum() + "] Error: " + message);
        System.err.println(data.lineNum() + " | " + data.line());
        System.err.println(" ".repeat(String.valueOf(data.lineNum()).length()) + "   " + "~".repeat(data.offset()) + "^");
    }
}
