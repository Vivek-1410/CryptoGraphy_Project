package util;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    public static void writeToFile(String filePath, String content) throws Exception {
        FileWriter writer = new FileWriter(filePath);
        writer.write(content);
        writer.close();
    }

    public static String readFromFile(String filePath) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}