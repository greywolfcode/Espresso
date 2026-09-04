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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.greywolfcode.espresso.errorreporting.ErrorReporter;

import io.github.greywolfcode.espresso.Token;
import io.github.greywolfcode.espresso.TokenType;

public class Lexer 
{
    private static final Map<String, TokenType> keywords = Map.ofEntries(
        Map.entry("abstract", TokenType.ABSTRACT),
        Map.entry("assert", TokenType.ASSERT),
        Map.entry("boolean", TokenType.BOOLEAN),
        Map.entry("break", TokenType.BREAK),
        Map.entry("byte", TokenType.BYTE),
        Map.entry("case", TokenType.CASE),
        Map.entry("catch", TokenType.CATCH),
        Map.entry("char", TokenType.CHAR),
        Map.entry("class", TokenType.CLASS),
        Map.entry("continue", TokenType.CONTINUE),
        Map.entry("default", TokenType.DEFAULT),
        Map.entry("do", TokenType.DO),
        Map.entry("double", TokenType.DOUBLE),
        Map.entry("else", TokenType.ELSE),
        Map.entry("enum", TokenType.ENUM),
        Map.entry("extends", TokenType.EXTENDS),
        Map.entry("final", TokenType.FINAL),
        Map.entry("finally", TokenType.FINALLY),
        Map.entry("float", TokenType.FLOAT),
        Map.entry("for", TokenType.FOR),
        Map.entry("if", TokenType.IF),
        Map.entry("implements", TokenType.IMPLEMENTS),
        Map.entry("import", TokenType.IMPORT),
        Map.entry("instanceof", TokenType.INSTANCEOF),
        Map.entry("int", TokenType.INT),
        Map.entry("interface", TokenType.INTERFACE),
        Map.entry("long", TokenType.LONG),
        Map.entry("native", TokenType.NATIVE),
        Map.entry("new", TokenType.NEW),
        Map.entry("package", TokenType.PACKAGE),
        Map.entry("private", TokenType.PRIVATE),
        Map.entry("public", TokenType.PUBLIC),
        Map.entry("protected", TokenType.PROTECTED),
        Map.entry("return", TokenType.RETURN),
        Map.entry("short", TokenType.SHORT),
        Map.entry("static", TokenType.STATIC),
        Map.entry("strictfp", TokenType.STRICTFP),
        Map.entry("super", TokenType.SUPER),
        Map.entry("switch", TokenType.SWITCH),
        Map.entry("synchronized", TokenType.SYNCHRONIZED),
        Map.entry("this", TokenType.THIS),
        Map.entry("throw", TokenType.THROW),
        Map.entry("throws", TokenType.THROWS),
        Map.entry("transient", TokenType.TRANSIENT),
        Map.entry("try", TokenType.TRY),
        Map.entry("void", TokenType.VOID),
        Map.entry("volatile", TokenType.VOLATILE),
        Map.entry("while", TokenType.WHILE)
    );

    private String source;
    private String sourceName;
    private int start = 0;
    private int offset = 0;
    private final ErrorReporter errorHandeler;
    private final ArrayList<Token> tokens;

    public Lexer(String paramSource, String paramSourceName, ErrorReporter paramErrorHandeler)
    {
        source = paramSource;
        sourceName = paramSourceName;
        tokens = new ArrayList<Token>();
        errorHandeler = paramErrorHandeler;
    }
    public List<Token> scan()
    {
        while (!isEnd())
        {
            processToken();
        }

       return tokens;
    }
    private void processToken()
    {
        start = offset;
        char nextChar = getNext();
        switch (nextChar)
        {
            case '+':
                appendToken(TokenType.PLUS);
                break;
            case '-':
                appendToken(TokenType.MINUS);
                break;
            case '*':
                appendToken(TokenType.STAR);
                break;
            case '(':
                appendToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                appendToken(TokenType.RIGHT_PAREN);
                break;
            case ';':
                appendToken(TokenType.SEMICOLON);
                break;
            case '{':
                appendToken(TokenType.LEFT_CURLY_BRACKET);
                break;
            case '}':
                appendToken(TokenType.RIGHT_CURLY_BRACKET);
                break;
            case ',':
                appendToken(TokenType.COMMA);
                break;
            case '.':
                appendToken(TokenType.PERIOD);
                break;
            case '=':
                appendToken(match('=') ? TokenType.EQUALS_EQUALS : TokenType.EQUALS);
                break;
            case '!':
                appendToken(match('=') ? TokenType.NOT_EQUALS : TokenType.NOT);
                break;
            case '>':
                appendToken(match('=') ? TokenType.GREATER_EQUALS : TokenType.GREATER);
                break;
            case '<':
                appendToken(match('=') ? TokenType.LESS_EQUALS : TokenType.LESS);
                break;
            case '"':
                parseString();
                break;
            //ignore whitespace
            case ' ', '\n', '\t', '\r':
                break;
            default:
                if (isDigit(nextChar))
                {
                    parseNumber();
                }
                else if (isAlphaNumeric(nextChar))
                {
                    parseIdentifier();
                }
                else
                {
                    //TODO: Report Error Here
                }
                break;
        }
    }
    private void parseNumber()
    {
        while(isDigit(peek()))
        {
            getNext();
        }

        //No trailing decimal points
        if (peek() == '.' && isDigit(peekTwo()))
        {
            while(isDigit(peek()))
            {
                getNext();
            }
        }

        appendToken(TokenType.NUMBER);
    }
    private void parseString()
    {
        while (!match('"') && !isEnd())
        {
            if (check('\n'))
            {
                //TODO: throw error for new line in String
                break;
            }
            offset++;
        }

        //strip of quote charachters
        String token = source.substring(start + 1, offset - 1);
        token = parseEscapeChars(token);
        appendToken(TokenType.STRING, token);
    }
    private String parseEscapeChars(String source)
    {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < source.length(); i++)
        {
            if (source.charAt(i) != '\\')
            {
                output.append(source.charAt(i));
            }
            else
            {
                switch (source.charAt(i+1))
                {
                    case 't': // tab
                        output.append('\t');
                        break;
                    case 'b': // backspace
                        output.append('\b');
                        break;
                    case 'n': // newline
                        output.append('\n');
                        break;
                    case 'r': // carrige return
                        output.append('\r');
                        break;
                    case 'f': // form feed
                        output.append('\f');
                        break;
                    case '\'': // single quote
                        output.append('\'');
                        break;
                    case '\"': // double quote
                        output.append('\"');
                        break;
                    case ('\\'): // backslash
                        output.append('\\');
                        break;
                    case ('u'): //unicode escape code
                        char[] unicodeNumber = new char[4];
                        unicodeNumber[0] = source.charAt(i+2);
                        unicodeNumber[0] = source.charAt(i+3);
                        unicodeNumber[0] = source.charAt(i+4);
                        unicodeNumber[0] = source.charAt(i+5);

                        boolean invalid = false;
                        for (char c : unicodeNumber)
                        {
                            if (isHexadecimal(c))
                            {
                                invalid = true;
                                //TODO: Throw error here
                                break;
                            }
                        }
                        if (invalid)
                        {
                            break;
                        }

                        int hexVal = Integer.parseInt(new String(unicodeNumber));
                        char charachter= (char)hexVal;
                        
                        output.append(charachter);
                        i+=3; //move passed unicode escape
                        break;
                    default:
                        //handle octal escape codes
                        if (isDigit(source.charAt(i+1)))
                        {
                            StringBuilder octalNumber = new StringBuilder();
                            octalNumber.append(source.charAt(i+1));

                            if (isDigit(source.charAt(i+2)))
                            {
                                octalNumber.append(source.charAt(i+2));
                            }
                            if (isDigit(source.charAt(i+3)))
                            {
                                octalNumber.append(source.charAt(i+2));
                            }

                            //first charachter bounds
                            if (!(octalNumber.charAt(0) >= '0' && octalNumber.charAt(0) <= '3'))
                            {
                                //TODO: Throw error here
                                break;
                            }
                            //second charchter bounds
                            if (octalNumber.length() > 1)
                            {
                                if (!isOctal(octalNumber.charAt(1)))
                                {
                                    //TODO: Throw error here
                                    break;
                                }
                                i++; //need to increment extra
                            }
                            //third charchter bounds
                            if (octalNumber.length() > 2)
                            {
                                if (!isOctal(octalNumber.charAt(2)))
                                {
                                    //TODO: Throw error here
                                    break;
                                }
                                i++;
                            }

                            int octalVal = Integer.parseInt(octalNumber.toString(), 8);
                            char octalChar = (char)octalVal;

                            output.append(octalChar);
                        }

                        //TODO: Throw error here
                        break;
                }

                //consume charachter
                i++;
            }
        }

        return output.toString();
    }
    private void parseIdentifier()
    {
        while (isAlphaNumeric(peek()))
        {
            getNext();
        }

        String identifier = source.substring(start, offset);

        if (keywords.containsKey(identifier))
        {
            appendToken(keywords.get(identifier));
        }
        else
        {
            appendToken(TokenType.IDENTIFIER);
        }
    }
    private boolean isEnd()
    {
        if (offset >= source.length())
        {
            return true;
        }
        return false;
    }
    private boolean isEndTwo()
    {
        if (offset + 1 >= source.length())
        {
            return true;
        }
        return false;
    }
    private boolean isDigit(char token)
    {
        return token >= '0' && token <= '9';
    }
    private boolean isOctal(char token)
    {
        return token >= '0' && token <= '7';
    }
    private boolean isHexadecimal(char token)
    {
        return (token >= '0' && token <= '9') 
            || (token >= 'a' && token <= 'f') 
            || (token >= 'A' && token <= 'F');
    }
    private boolean isAlphaNumeric(char token)
    {
        return (token >= '0' && token <= '9') 
            || (token >= 'a' && token <= 'z') 
            || (token >= 'A' && token <= 'Z');
    }
    private boolean match(char token)
    {
        if (isEnd())
        {
            return false;
        }
        if (!(source.charAt(offset + 1) == token))
        {
            return false;
        }
        offset++;
        return true;
    }
    private char peek()
    {
        if (isEnd())
        {
            return '\0';
        }
        return source.charAt(offset + 1);
    }
    private char peekTwo()
    {
        if (isEndTwo())
        {
            return '\0';
        }
        return source.charAt(offset + 1);
    }
    private char getNext()
    {
        return source.charAt(offset++);
    }
    private boolean check(char token)
    {
        if (isEnd())
        {
            return false;
        }
        if (!(source.charAt(offset) == token))
        {
            return false;
        }
        return true;
    }
    private void appendToken(TokenType type)
    {
        String lexeme = source.substring(start, offset);
        appendToken(type, lexeme);
    }
    private void appendToken(TokenType type, String lexeme)
    {
        tokens.add(new Token(start, sourceName, lexeme, type));
    }
}
