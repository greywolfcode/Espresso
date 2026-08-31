import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Scanner;
import java.util.Stack;

public class HeaderInserter 
{
    private static final Logger LOGGER = Logger.getLogger(HeaderInserter.class.getName());


    private static String licenceHeader = """ 
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

                                        """;

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
             System.out.println("Folder path expected");
             System.exit(64);
        }
        else
        {
            process(args[0]);
        }
    }
    private static void process(String folderPath)
    {
        LOGGER.log(Level.INFO, "Starting Path: " + folderPath);

        Stack<File> folders = new Stack<File>();
        HashMap<String, FileStorage> files = new HashMap<String, FileStorage>();
        File toplevel = new File(folderPath);
        folders.add(toplevel);

        while (!folders.empty())
        {
            File currentFolder = folders.pop();
            File[] subFolders = currentFolder.listFiles(File::isDirectory);
            folders.addAll(Arrays.asList(subFolders));

            LOGGER.log(Level.INFO, "Searching " + currentFolder.getPath());

            for (File file : currentFolder.listFiles(File::isFile))
            {
                if (getExtension(file).equals(".java"))
                {
                    LOGGER.log(Level.INFO, "Found " + file.getPath());

                    String name = getName(file);
                    //remove the top level path to just get the package path
                    String packageName = getPackageName(file.toPath(), toplevel.toPath());

                    FileStorage currentFile = new FileStorage(name, packageName, file);

                    files.put(name, currentFile);
                }
            }

            // Process each file
            for (FileStorage file : files.values())
            {
                boolean needsHeader = true;
                int packageLine = -1;
                ArrayList<Integer> importLines = new ArrayList<Integer>();

                File fileData = file.getFile();

                LOGGER.log(Level.INFO, "Processing " + fileData.getPath());

                //check what needs to be done
                try(Scanner data = new Scanner(fileData))
                {
                    int currentLine = 0;
                    while (data.hasNext())
                    {
                        String line = data.nextLine();

                        //check if licence header is needed
                        if (line.contains("/*"))
                        {
                            needsHeader = false;
                            while (!line.contains("*/"))
                            {
                                line = data.nextLine();
                            }
                            LOGGER.log(Level.INFO, "Requires Licence Header");
                        }
                        else if (line.contains("package"))
                        {
                            packageLine = currentLine;
                            LOGGER.log(Level.INFO, "Found package at: " + packageLine);
                        }
                        else if (line.contains("import"))
                        {
                            //ignore standard library imports
                            if (!line.contains("import java.") || !line.contains("import javax.") || !line.contains("import jdk."))
                            {
                                importLines.add(currentLine);
                                LOGGER.log(Level.INFO, "Found import at " + currentLine);
                            }
                        }
                        //must have hit begining of actual code
                        else if (line.contains("class"))
                        {
                            break;
                        }

                        currentLine++;
                    }
                }
                catch (IOException e)
                {
                    break;
                }

                //rewrite file
                try
                {
                    Path tempFile = Files.createTempFile(fileData.getName(), ".temp");

                    try (BufferedReader reader = Files.newBufferedReader(fileData.toPath());
                        BufferedWriter writer = Files.newBufferedWriter(tempFile)) 
                    {
                        if (needsHeader)
                        {
                            LOGGER.log(Level.INFO, "Writing Licence Header");
                            writer.write(licenceHeader);
                        }
                        if (packageLine == -1)
                        {
                            LOGGER.log(Level.INFO, "Writing Package Line");
                            writer.write("package " + file.packageName + ";");
                        }

                        String currentLine = "";
                        int lineNumber = 0;

                        while((currentLine = reader.readLine()) != null)
                        {
                            if (lineNumber == packageLine)
                            {
                                LOGGER.log(Level.INFO, "Writing Package Line");
                                writer.write("package " + file.packageName + ";");
                            }
                            else if (importLines.contains(lineNumber))
                            {
                                String fileName = currentLine.split("\\.")[0];
                                String importPackage = files.get(fileName).packageName;
                                LOGGER.log(Level.INFO, "Writing Import: " + importPackage);
                                writer.write("import " + importPackage + "." + fileName + ";");
                            }
                            else
                            {
                                writer.write(currentLine);
                            }
                            writer.newLine();

                            lineNumber++;
                        }
                    }
                    Files.move(tempFile, fileData.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                catch (IOException e)
                {
                    break;
                }
            }
        }
    }
    private static String getExtension(File file)
    {
        if (file == null)
        {
            return "";
        }

        String name = file.getName();
        int extensionIndex = name.lastIndexOf(".");

        if (extensionIndex == -1)
        {
            return "";
        }

        return name.substring(extensionIndex);
    }
    private static String getName(File file)
    {
        if (file == null)
        {
            return "";
        }

        String name = file.getName();
        int extensionIndex = name.lastIndexOf(".");

        if (extensionIndex == -1)
        {
            return "";
        }

        return name.substring(0, extensionIndex);
    }
    private static String getPackageName(Path file, Path topLevel)
    {
        // /new StringBuilder(file.getPath().substring(toplevel.getPath().length())).reverse().toString().replace('/', '.').replace('\\', '.');
        ArrayList<String> pathData = new ArrayList<String>();

        for (Path section : file)
        {
            //don't include end of path
            if (!section.toString().contains(".java"))
            {
                pathData.add(section.toString());
            }
        }

        //remove top level path sections
        for (int i=0; i<topLevel.getNameCount(); i++)
        {
            pathData.remove(0);
        }

        pathData.reversed();

        StringBuilder packageData = new StringBuilder();

        for (String part : pathData)
        {
            packageData.append(part);
            packageData.append(".");
        }
        //remove trialing period
        packageData.setLength(packageData.length() - 1);

        return packageData.toString();
    }
}

class FileStorage
    {
        String name;
        String packageName;
        File file;

        public FileStorage(String paramName, String paramPackageName, File paramFile)
        {
            name = paramName;
            packageName = paramPackageName;
            file = paramFile;
        }
        public String getName()
        {
            return name;
        }
        public String getPackagename()
        {
            return packageName;
        }
        public File getFile()
        {
            return file;
        }
    }