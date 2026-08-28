import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class HeaderInserter 
{
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
                    String packageName = file.getPath().substring(toplevel.getPath().length());

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
                            importLines.add(currentLine);
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