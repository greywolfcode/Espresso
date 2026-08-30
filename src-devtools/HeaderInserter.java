import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class HeaderInserter 
{
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
        else{
            process(args[0]);
        }
    }
    private static void process(String folderPath)
    {
        Stack<File> folders = new Stack<File>();
        HashMap<String, FileStorage> files = new HashMap<String, FileStorage>();
        File toplevel = new File(folderPath);
        folders.add(toplevel);

        while (!folders.empty())
        {
            File currentFolder = folders.pop();
            File[] subFolders = currentFolder.listFiles(File::isDirectory);
            folders.addAll(Arrays.asList(subFolders));

            for (File file : currentFolder.listFiles(File::isFile))
            {
                if (getExtension(file).equals(".java"))
                {
                    String name = getName(file);
                    //remove the top level path to just get the package path
                    String packageName = new StringBuilder(file.getPath().substring(toplevel.getPath().length())).reverse().toString().replace('/', '.').replace('\\', '.');

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
                        }
                        else if (line.contains("package"))
                        {
                            packageLine = currentLine;
                        }
                        else if (line.contains("import"))
                        {
                            //ignore standard library imports
                            if (!line.contains("import java.") || !line.contains("import javax.") || !line.contains("import jdk."))
                            {
                                importLines.add(currentLine);
                            }
                        }
                        //must have hit begining of actual code
                        else if (line.contains("class"))
                        {
                            break;
                        }
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
                            writer.write(licenceHeader);
                        }
                        if (packageLine == -1)
                        {
                            writer.write("package " + file.packageName + ";");
                        }

                        String currentLine = "";
                        int lineNumber = 0;

                        while((currentLine = reader.readLine()) != null)
                        {
                            if (lineNumber == packageLine)
                            {
                                writer.write("package " + file.packageName + ";");
                            }
                            else if (importLines.contains(lineNumber))
                            {
                                String fileName = currentLine.split(".")[0];
                                String importPackage = files.get(fileName).packageName;
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