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

public abstract class ErrorReporter 
{
    protected boolean hadError = false;

    public abstract void report(int offset, String fileData, String fileName, String message);

    public boolean getHadError()
    {
        return hadError;
    }
    
    protected LineData getLineData(String fileData, int offset)
    {
        int newlines = 0;
        int lineStart = 0;

        for (int i=0; i<offset; i++)
        {
           if (fileData.charAt(i) == '\n')
           {
               newlines++;

               //there should never be a case where this
               //could throw an out of bounds error because
               //the newline must always before the offset
               lineStart = i + 1;
           } 
        }

        return new LineData(newlines, offset - lineStart, fileData.substring(lineStart, offset));
    }
}
