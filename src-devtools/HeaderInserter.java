import java.io.File;
import java.util.Arrays;
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
}