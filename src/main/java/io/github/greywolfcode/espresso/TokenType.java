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

public enum TokenType 
{
    // Single Charachter
    PLUS,
    MINUS,
    STAR,
    SLASH,
    LEFT_PAREN,
    RIGHT_PAREN,
    SEMICOLON,
    RIGHT_CURLY_BRACKET,
    LEFT_CURLY_BRACKET,
    COMMA,
    PERIOD,
    EQUALS,
    NOT,
    GREATER,
    LESS,

    // Double Charachter
    EQUALS_EQUALS,
    NOT_EQUALS,
    GREATER_EQUALS,
    LESS_EQUALS,

    // Literals
    TRUE,
    FALSE,
    NULL,
    STRING,
    NUMBER,

    // Keywords
    ABSTRACT,
    ASSERT,
    BOOLEAN,
    BREAK,
    BYTE,
    CASE,
    CATCH,
    CHAR,
    CLASS,
    CONTINUE,
    DEFAULT,
    DO,
    DOUBLE,
    ELSE,
    ENUM,
    EXTENDS,
    FINAL,
    FINALLY,
    FLOAT,
    FOR,
    IF,
    IMPLEMENTS,
    IMPORT,
    INSTANCEOF,
    INT,
    INTERFACE,
    LONG,
    NATIVE,
    NEW,
    PACKAGE,
    PRIVATE,
    PUBLIC,
    PROTECTED,
    RETURN,
    SHORT,
    STATIC,
    STRICTFP,
    SUPER,
    SWITCH,
    SYNCHRONIZED,
    THIS,
    THROW,
    THROWS,
    TRANSIENT,
    TRY,
    VOID,
    VOLATILE,
    WHILE
}
