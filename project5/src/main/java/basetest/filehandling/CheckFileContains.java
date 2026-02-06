package basetest.filehandling;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CheckFileContains {

    public List<String> selected(String directoryPath, int level) {
        List<String> result = new ArrayList<>();
        File root = new File(directoryPath);
        if (root.exists() && root.isDirectory()) {
            traverse(root, 0, level, result);
        }
        return result;
    }

    private void traverse(File current, int currentLevel, int maxLevel, List<String> result) {
        if (!current.isDirectory()) return;

        result.add(current.getAbsolutePath());

        if (maxLevel >= 0 && currentLevel >= maxLevel) return;

        File[] files = current.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    traverse(file, currentLevel + 1, maxLevel, result);
                }
            }
        }
    }

    public String loadFileContent(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            return "";
        }
    }

    private void provideNotFoundDirectory(String directoryPath, String filePath) {
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Directory not found: " + directoryPath);
            return;
        }
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.err.println("File not found: " + filePath);
            return;
        }
        String content = loadFileContent(filePath);
        var dirs = selected(directoryPath, 1);
        dirs.forEach(cDir -> {
            File temp = new File(cDir);
            if(!content.contains(temp.getName())) {
                System.out.println(temp.getName());
            }
        });
   }

    public static void main(String...args) {
        CheckFileContains c1 = new CheckFileContains();
        c1.provideNotFoundDirectory(".", "");
    }
}
