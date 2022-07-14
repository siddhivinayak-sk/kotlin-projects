package com.sk.ktl.fortest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static java.util.Calendar.*;

public class JLogger {

    private String logDirectoryName;
    private String logBaseName;

    public JLogger() {
        JConfigurationManager configurationManager = JConfigurationManager.getConfigurationManager();

        logDirectoryName = configurationManager.get("logDirectory");
        logBaseName = configurationManager.get("logBaseName");
    }

    public String createLog() throws IOException {
        return createLog(logDirectoryName, logBaseName);
    }

    public String createLog(String logDirectoryName, String logBaseName) throws IOException {

        Path logDirectoryPath = Paths.get(logDirectoryName);

        if (!Files.exists(logDirectoryPath)) throw new FileNotFoundException("Invalid Log Directory");

        List<File> filesInDirectory = getAllFilesInDirectory(logDirectoryPath);
        List<File> logFilesInDirectory = new LinkedList<>();

        for (File file : filesInDirectory) {
            if (file.getName().startsWith(logBaseName)) {
                logFilesInDirectory.add(file);
            }
        }

        Calendar todayDate = Calendar.getInstance();
        String logDateSuffix = String.format("%04d%02d%02d", todayDate.get(YEAR), todayDate.get(MONTH) + 1, todayDate.get(DAY_OF_MONTH));

        if (logFilesInDirectory.size() == 0) {
            return createLogFile(logDirectoryName, logBaseName, logDateSuffix);
        }

        String logFileBaseName = String.format("%s_%s", logBaseName, logDateSuffix);

        int maxFileNumber = getMaxFileNumber(logFilesInDirectory, logFileBaseName);

        return createLogFile(logDirectoryName, logBaseName, logDateSuffix, maxFileNumber);
    }

    private int getMaxFileNumber(List<File> logFilesInDirectory, String logFileBaseName) {

        int maxFileNumber = 0;
        for (File logFile : logFilesInDirectory) {
            String logFileName = logFile.getName();
            logFileName = stripExtension(logFileName);
            if (logFileName.startsWith(logFileBaseName)) {
                String logFileSuffix = logFileName.substring(logFileBaseName.length());
                if (!logFileSuffix.isEmpty()) {
                    logFileSuffix = logFileSuffix.substring(1); // strip off the leading '_'
                    int fileNumber = Integer.parseInt(logFileSuffix);
                    if(fileNumber >= maxFileNumber) maxFileNumber = fileNumber + 1;
                } else {
                    if (maxFileNumber == 0) maxFileNumber = 1;
                }
            }
        }
        return maxFileNumber;
    }

    private String stripExtension(String logFileName) {
        int indexOfLastDot = logFileName.lastIndexOf('.');
        return logFileName.substring(0, indexOfLastDot);
    }

    private String createLogFile(String directoryName, String logBaseName, String logDateSuffix) throws IOException {
        return createLogFile(directoryName, logBaseName, logDateSuffix, 0);
    }

    private String createLogFile(String directoryName, String logBaseName, String logDateSuffix, int logFileNumber) throws IOException {
        String fileName;
        if (logFileNumber == 0) {
            fileName = String.format("%s%s%s_%s.log", directoryName, File.separator, logBaseName, logDateSuffix);
        } else {
            fileName = String.format("%s%s%s_%s_%d.log", directoryName, File.separator, logBaseName, logDateSuffix, logFileNumber);
        }

        Path path = Paths.get(fileName);
        Files.createFile(path);
        return fileName;
    }

    private List<File> getAllFilesInDirectory(Path path) {

        List<File> files = new LinkedList<>();

        File directory = path.toFile();
        File[] filesList = directory.listFiles();
        for (File f : filesList) {
            if (f.isDirectory())
                continue;
            if (f.isFile()) {
                files.add(f.getAbsoluteFile());
            }
        }

        return files;
    }
}
