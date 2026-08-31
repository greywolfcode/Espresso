package io.github.greywolfcode.espresso.errorreporting;

public abstract class ErrorReporter 
{
    protected boolean hadError = false;

    public abstract void report(int lineNum, String file, String type, String message, String line);

    public boolean getHadError()
    {
        return hadError;
    }
}