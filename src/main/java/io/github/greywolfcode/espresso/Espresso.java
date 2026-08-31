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

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import io.github.greywolfcode.espresso.Lexer;
import io.github.greywolfcode.espresso.Token;
import io.github.greywolfcode.espresso.errorreporting.ConsoleLineReporter;
import io.github.greywolfcode.espresso.errorreporting.ErrorReporter;

public class Espresso 
{
    private static ErrorReporter errorHandeler;

    public static void main(String[] args) 
    {
        errorHandeler = new ConsoleLineReporter();

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
            run(args);
        }
    }
    private static void run(String[] files)
    {


        for (String path : files)
        {
            try
            {
                Path filePath = Path.of(path);
                String fileName = filePath.getFileName().toString();
                String fileData = Files.readString(filePath);
                Lexer lexer = new Lexer(fileData, fileName, errorHandeler);
                List<Token> tokens = lexer.scan();
                System.out.println(tokens);
                
            }
            catch (NoSuchFileException e)
            {
                System.err.println("Espresso: file (" + path + ") could not be found");
                System.exit(66);
            }
            catch (IOException e)
            {
                System.err.println("Espresso: IO Error (" + path + ")");
                System.exit(74);
            }
        }
    }
}
