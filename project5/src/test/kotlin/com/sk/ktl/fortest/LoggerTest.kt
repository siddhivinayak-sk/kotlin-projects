@file:Suppress("ClassName")

package com.sk.ktl.fortest

import com.sk.ktl.fortest.demo2.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LoggerTest {

    @MockK
    lateinit var configuration: Configuration

    @MockK
    lateinit var paths: Paths

    @MockK
    lateinit var files: Files

    @MockK
    lateinit var date: DateProvider

    init {
        MockKAnnotations.init(this)
    }

    @Nested
    inner class `on construction` {


        @Test
        fun `the logDirectoryName should be set`() {

            every { configuration.get("logDirectory") } returns "logDirectoryName"
            every { configuration.get("logBaseName") } returns "baseName"

            val logger = Logger(configuration, paths, files)

            logger.logDirectoryName `should be` "logDirectoryName"
        }

        @Test
        fun `the logBase should be set`() {

            every { configuration.get("logDirectory") } returns "logDirectoryName"
            every { configuration.get("logBaseName") } returns "baseName"

            val logger = Logger(configuration, paths, files)

            logger.logBaseName `should be` "baseName"
        }
    }

    @Nested
    inner class getMaxFileName {

        val baseName = "baseName"

        init {
            every { configuration.get("logDirectory") } returns "directory"
            every { configuration.get("logBaseName") } returns baseName
        }

        @Test
        fun `should return 0 when there are no files`() {
            val logFileBaseName = ""
            val logFilesInDirectory = listOf<IoFile>()
            val logger = Logger(configuration, paths, files)

            logger.getMaxFileNumber(logFilesInDirectory, logFileBaseName) `should be` 0
        }

        @Test
        fun `should return 1 when there is one file`() {
            val logFilesInDirectory = listOf(IoFile(java.io.File("${baseName}.log")))
            val logFileBaseName = baseName
            val logger = Logger(configuration, paths, files)
            logger.getMaxFileNumber(logFilesInDirectory, logFileBaseName) `should be` 1
        }

        @Test
        fun `should return 2 when there is a file with the suffix 1`() {
            val logFilesInDirectory = listOf(
                IoFile(java.io.File("${baseName}.log")),
                IoFile(java.io.File("${baseName}_1.log"))
            )
            val logFileBaseName = baseName
            val logger = Logger(configuration, paths, files)
            logger.getMaxFileNumber(logFilesInDirectory, logFileBaseName) `should be` 2
        }

        @Test
        fun `should return increment when there is are files with many suffixes`() {
            val logFilesInDirectory = listOf(
                IoFile(java.io.File("${baseName}.log")),
                IoFile(java.io.File("${baseName}_3.log")),
                IoFile(java.io.File("${baseName}_5.log")),
                IoFile(java.io.File("${baseName}_1.log"))
            )
            val logFileBaseName = baseName
            val logger = Logger(configuration, paths, files)
            logger.getMaxFileNumber(logFilesInDirectory, logFileBaseName) `should be` 6
        }
    }

    @Nested
    inner class getAllFilesInDirectory {
        val baseName = "baseName"

        @MockK
        lateinit var path: Path

        @MockK
        lateinit var file: File

        init {

            MockKAnnotations.init(this)

            every { configuration.get("logDirectory") } returns "directory"
            every { configuration.get("logBaseName") } returns baseName

            every { path.toFile() } returns file
        }

        @Test
        fun `should return an empty list if there are no files`() {
            val logger = Logger(configuration, paths, files)

            every { file.listFiles() } returns listOf()

            logger.getAllFilesInDirectory(path)
        }

        @Test
        fun `should return a list if there are files`() {

            val mockFile = mockk<File>()

            every { mockFile.isFile } returns true
            every { mockFile.absoluteFile } returns IoFile(java.io.File("${baseName}.log"))
            every { file.listFiles() } returns listOf(mockFile)

            val logger = Logger(configuration, paths, files)
            val files = logger.getAllFilesInDirectory(path)
            files.shouldNotBeEmpty()
            files.count() `should be` 1
        }
    }

    @Nested
    inner class createLog {

        val baseName = "baseName"

        @MockK
        lateinit var path: Path

        @MockK
        lateinit var file: File

        @MockK
        lateinit var date: DateProvider


        init {

            MockKAnnotations.init(this)

            every { configuration.get("logDirectory") } returns "directory"
            every { configuration.get("logBaseName") } returns "baseName"

            every { path.toFile() } returns file
        }

        @Test
        fun `should create a file with the correct date`() {
            val directoryName = "logDirectoryName"
            val expectedFileName = "${directoryName}${File.separator}baseName_20190807.log"

            //every { paths.get }
            every { paths.get(directoryName) } returns path
            every { paths.get(expectedFileName) } returns path
            every { files.exists(path) } returns true
            every { files.createFile(path) } returns path

            every { file.listFiles() } returns listOf()

            every { date.year } returns 2019
            every { date.monthValue } returns 8
            every { date.dayOfMonth } returns 7


            val logger = Logger(configuration, paths, files)

            val fileName = logger.createLog(directoryName, "baseName", date)

            fileName `should equal` expectedFileName
        }
    }
}