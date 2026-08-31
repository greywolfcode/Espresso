package io.github.greywolfcode.espresso;

import java.util.ArrayList;
import java.util.List;

import io.github.greywolfcode.espresso.errorreporting.ErrorReporter;

import io.github.greywolfcode.espresso.Token;
import io.github.greywolfcode.espresso.TokenType;

public class Lexer 
{
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
        char nextChar = getNext();
        switch (nextChar)
        {
            case '+':
                addToken(TokenType.PLUS);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '{':
                addToken(TokenType.LEFT_CURLY_BRACKET);
                break;
            case '}':
                addToken(TokenType.RIGHT_CURLY_BRACKET);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.PERIOD);
                break;
            case '=':
                addToken(TokenType.EQUALS);
                break;
            case '!':
                addToken(TokenType.NOT);
                break;
            case '>':
                addToken(TokenType.GREATER);
                break;
            case '<':
                addToken(TokenType.LESS);
                break;
            default:
                //TODO: Report Error Here
                break;
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
    private char getNext()
    {
        return source.charAt(offset++);
    }
    private void addToken(TokenType type)
    {
        String lexeme = source.substring(start, offset);
        tokens.add(new Token(start, sourceName, lexeme, type));
    }
}