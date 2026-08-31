package io.github.greywolfcode.espresso.errorreporting;

public class ConsoleLineReporter extends ErrorReporter
{
    public void report(int lineNum, String file, String type, String message, String line)
    {
        hadError = true;
        
        System.err.println(file + " [line " + lineNum + "] " + type + " Error: " + message);
        System.err.println(lineNum + " | " + line);
    }
}