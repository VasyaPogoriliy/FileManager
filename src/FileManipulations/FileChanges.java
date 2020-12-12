package FileManipulations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileChanges {
    private final static int LENGTH_OF_EXTENSION = 4;

    public static AddingFile getResultOfMovement(AddingFile source, AddingFile folderDestination) {
        String fileName = source.getName();
        String wayToNewFile = folderDestination.getAbsolutePath() + "/" + fileName;
        return new AddingFile(new File(wayToNewFile));
    }

    public static AddingFile getFileWithSuffix(String filePath) {
        File newFile = new File(filePath);
        int i = 0;
        while (newFile.exists()) {
            i++;
            if (filePath.substring(filePath.length() - LENGTH_OF_EXTENSION).equals(".txt")) {
                newFile = new File(filePath.substring(0, filePath.length() - 4) + " (" + i + ")" + ".txt");
            } else {
                newFile = new File(filePath + " (" + i + ")");
            }
        }
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AddingFile(newFile);
    }

    public static AddingFile resultOfRename(AddingFile source, String newName) {
        String path = source.getAbsolutePath();
        String newPath = path.substring(0, path.length() - source.getName().length()) + newName;
        return new AddingFile(new File(newPath));
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    public static void rewriteFile(File file, String newFileText) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(newFileText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
