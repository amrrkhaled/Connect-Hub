package backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveImage {
    private static final String IMAGES_FOLDER = "images/";
    private static int imageCounter = 1;

    public static String saveImageToFolder(String absoluteFilePath) throws IOException {
        File sourceFile = new File(absoluteFilePath);
        if (!sourceFile.exists()) {
            throw new IOException("File not found: " + absoluteFilePath);
        }
        File folder = new File(IMAGES_FOLDER);
        File[] files = folder.listFiles();
        if (files != null) {
            imageCounter = files.length + 1;
        }

        // Generate a unique name for the image to avoid conflicts
        String fileExtension = getFileExtension(sourceFile.getName());
        String newFileName = "image" + imageCounter++ + fileExtension;
        Path destinationPath = Paths.get(IMAGES_FOLDER + newFileName);

        // Copy the file to the images folder
        Files.copy(sourceFile.toPath(), destinationPath);

        // Return the relative path to the saved file
        return destinationPath.toString();
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex > 0) ? fileName.substring(lastDotIndex) : ""; // Return the extension, or empty if none
    }
}