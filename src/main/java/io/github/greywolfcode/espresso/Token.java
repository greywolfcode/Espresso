package io.github.greywolfcode.espresso;

import io.github.greywolfcode.espresso.TokenType;

public class Token 
{
    private int offset;
    private String file;
    private String token;
    private TokenType type;

    public Token(int paramOffset, String paramFile, String paramToken, TokenType type)
    {
        offset = paramOffset;
        file = paramFile;
        token = paramToken;
    }
    public String toString()
    {
        return "Token from "
    }

}