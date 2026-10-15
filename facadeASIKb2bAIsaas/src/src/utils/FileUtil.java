package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileUtil {
    public static List<String> readLines(String fileName) {
        try {
            Path path = Paths.get(fileName);
            if (!Files.exists(path)) return new ArrayList<>();
            return Files.readAllLines(path);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void writeLines(String fileName, List<String> lines) {
        try {
            Files.write(Paths.get(fileName), lines);
        } catch (IOException e) { throw new RuntimeException(e); }
    }
}