package io.github.greywolfcode.espresso.errorreporting;


public interface ErrorReporter 
{
    void report(int lineNum, String file, String type, String message, String line);
}