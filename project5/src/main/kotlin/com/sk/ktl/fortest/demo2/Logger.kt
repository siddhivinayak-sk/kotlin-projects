package com.sk.ktl.fortest.demo2

import java.io.FileNotFoundException
import java.time.LocalDate
import java.util.LinkedList

class Logger(configurationManager: Configuration, private val paths: Paths, private val files: Files) {

    val logDirectoryName: String = configurationManager["logDirectory"]
    val logBaseName: String = configurationManager["logBaseName"]

    fun createLog(): String {
        return createLog(logDirectoryName, logBaseName, SystemLocalDate(LocalDate.now()))
    }

    fun createLog(logDirectoryName: String, logBaseName: String, todayDate: DateProvider): String {

        val logDirectoryPath = paths.get(logDirectoryName)

        if (!files.exists(logDirectoryPath)) throw FileNotFoundException("Invalid Log Directory")

        val filesInDirectory = getAllFilesInDirectory(logDirectoryPath)
        val logFilesInDirectory = LinkedList<File>()

        for (file in filesInDirectory) {
            if (file.name.startsWith(logBaseName)) {
                logFilesInDirectory.add(file)
            }
        }

        val logDateSuffix = String.format("%04d%02d%02d", todayDate.year, todayDate.monthValue, todayDate.dayOfMonth)

        if (logFilesInDirectory.size == 0) {
            return createLogFile(logDirectoryName, logBaseName, logDateSuffix)
        }

        val logFileBaseName = "${logBaseName}_$logDateSuffix"

        val maxFileNumber = getMaxFileNumber(logFilesInDirectory, logFileBaseName)

        return createLogFile(logDirectoryName, logBaseName, logDateSuffix, maxFileNumber)
    }

    internal fun getMaxFileNumber(logFilesInDirectory: List<File>, logFileBaseName: String): Int {
        var maxFileNumber = 0
        logFilesInDirectory.forEach { logFile ->
            val logFileName = stripExtension(logFile.name)
            if (logFileName.startsWith(logFileBaseName)) {
                var logFileSuffix = logFileName.substring(logFileBaseName.length)
                if (!logFileSuffix.isEmpty()) {
                    logFileSuffix = logFileSuffix.substring(1) // strip off the leading '_'
                    val fileNumber = Integer.parseInt(logFileSuffix)
                    if (fileNumber >= maxFileNumber) maxFileNumber = fileNumber + 1
                } else {
                    if (maxFileNumber == 0) maxFileNumber = 1
                }
            }
        }
        return maxFileNumber
    }

    private fun stripExtension(logFileName: String): String {
        val indexOfLastDot = logFileName.lastIndexOf('.')
        return logFileName.substring(0, indexOfLastDot)
    }

    private fun createLogFile(directoryName: String, logBaseName: String, logDateSuffix: String, logFileNumber: Int = 0): String {
        val fileName: String = if (logFileNumber == 0) {
            "$directoryName${File.separator}${logBaseName}_$logDateSuffix.log"
        } else {
            "$directoryName${File.separator}${logBaseName}_${logDateSuffix}_$logFileNumber.log"
        }

        val path = paths.get(fileName)
        files.createFile(path)
        return fileName
    }

    internal fun getAllFilesInDirectory(path: Path): List<File> {
        val files = LinkedList<File>()
        val directory = path.toFile()
        val filesList = directory.listFiles()
        for (f in filesList!!) {
            if (f.isFile) {
                files.add(f.absoluteFile)
            }
        }

        return files
    }
}
