package io.github.greywolfcode.espresso;

public class Espresso 
{
    public static void main(String[] args) 
    {
        if (args.length == 0 || args[0].equals(""))
        {
            System.out.println("Espresso: no source files");
        }
        else if (args[0].equals("-h") || args[0].equals("--help"))
        {
            System.out.println("Espresso"); //name/desc
            System.out.println();
            System.out.println("Compiler for the Espresso Java superset, targeting the JVM"); //desc
            System.out.println();
            System.out.println("usage: Espresso [-h] files..."); //usage
            System.out.println();
            System.out.println("positional arguments:");
            System.out.println("  files               list of Espresso files to compile");
            System.out.println();
            System.out.println("options:");
            System.out.println("  -h, --help         show this help message and exit");
        }
        else
        {
            run(args)
        }
    }
    private static void run(String[] files)
    {
        
    }
}