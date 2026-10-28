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

import java.util.List;
import java.util.ArrayList;

import io.github.greywolfcode.espresso.errorreporting.ErrorReporter;
import io.github.greywolfcode.espresso.Token;
import io.github.greywolfcode.espresso.TokenType;

public class Parser 
{
    private final ErrorReporter errorHandeler;
    private final List<Token> tokens;
    private int location;

    public Parser(List<Token> paramTokens, ErrorReporter paramErrorHandeler)
    {
        tokens = paramTokens;
        errorHandeler = paramErrorHandeler;
        location = 0;
    }
    public List<Dclr> parse()
    {
        List<Dclr> declarations = new ArrayList<Dclr>();

        while (!isEnd())
        {
            declarations.add(processDeclaration());
        }
        return declarations;
    }
    private Dclr processDeclaration()
    {
        Token next = advance();

        switch (next.getType())
        {
            case TokenType.PACKAGE:
                return createPackage();
            case TokenType.IMPORT:
                return createImport();
        }
        return null;
    }

    private Dclr.Package createPackage()
    {
        StringBuilder packageParts = new StringBuilder();
        Token next;
        while (!isEnd() && peek().getType() != TokenType.SEMICOLON)
        {
            next = advance();
            if (next.getType() == TokenType.IDENTIFIER || next.getType() == TokenType.PERIOD)
            {

                packageParts.append(next.getToken());
            }
            else
            {
                //TODO: show error
            }
        }

        return new Dclr.Package(packageParts.toString());
    }
    private Dclr.Import createImport()
    {
        StringBuilder importParts = new StringBuilder();
        Token next;
        while (!isEnd() && peek().getType() != TokenType.SEMICOLON)
        {
            next = advance();
            if (next.getType() == TokenType.IDENTIFIER || next.getType() == TokenType.PERIOD)
            {

                importParts.append(next.getToken());
            }
            else
            {
                //TODO: show error
            }
        }

        return new Dclr.Import(importParts.toString());
    }

    private Token advance()
    {
        if (!isEnd())
        {
            location++;
        }
        return previous();
    }
    private Token previous()
    {
        return tokens.get(location - 1);
    }
    private Token peek()
    {
        //location is always on the next token
        return tokens.get(location);
    }
    private boolean isEnd()
    {
        return location >= tokens.size();
    }


}