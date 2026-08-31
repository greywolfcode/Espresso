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

package io.github.greywolfcode.espresso;

import io.github.greywolfcode.espresso.TokenType;

public class Token 
{
    private int offset;
    private String file;
    private String token;
    private TokenType type;

    public Token(int paramOffset, String paramFile, String paramToken, TokenType paramType)
    {
        offset = paramOffset;
        file = paramFile;
        token = paramToken;
        type = paramType;
    }
    public String toString()
    {
        return "Token: " + token + " Type: " + type;
    }

}
