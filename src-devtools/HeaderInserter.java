import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
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