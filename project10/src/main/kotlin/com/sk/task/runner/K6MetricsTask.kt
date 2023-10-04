package com.sk.task.runner

import com.sk.task.model.K6CsvFile
import com.sk.task.model.K6CsvMetrics
import com.sk.task.model.K6File
import com.sk.task.model.K6Object
import com.sk.task.runner.base.BaseRunner
import com.sk.task.runner.processor.CYCLE
import com.sk.task.runner.processor.K6OutputProcessor
import com.sk.task.runner.processor.VUS
import com.sk.task.runner.reader.K6JsonOutputReader
import com.sk.task.runner.reader.K6_JSON_FILE
import com.sk.task.runner.writer.K6CSVOutputWriter
import com.sk.task.runner.writer.K6_CSV_FILENAME
import com.sk.task.runner.writer.K6_CSV_FILEPATH
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import reactor.util.function.Tuple2
import java.io.File
import java.util.stream.Collectors

class K6MetricsTask(val reader: K6JsonOutputReader, val processor: K6OutputProcessor, val writer: K6CSVOutputWriter):
        BaseRunner() {

    @Value("\${k6.json.input.path}") lateinit var inputPath: String
    @Value("\${k6.csv.output.path}") lateinit var outputPath: String
    @Value("\${k6.csv.output.filename}") lateinit var outputFileName: String

    override fun run(args: ApplicationArguments?) {
        val csvMetrics = inputPath.listFiles()
                .stream()
                .map { K6Object(it.vus, it.cycle, reader.read(mutableMapOf(
                        K6_JSON_FILE to it.file,
                ))) }
                .map { processor.process(it.input, mutableMapOf(
                        VUS to it.vus,
                        CYCLE to it.cycle,
                )) }
                .map { it as K6CsvMetrics }
                .collect(Collectors.toList())
        writer.write(K6CsvFile(csvMetrics), mapOf(
                K6_CSV_FILEPATH to outputPath,
                K6_CSV_FILENAME to outputFileName
        ))
    }

    private fun String.listFiles(): List<K6File> {
        val files = mutableListOf<K6File>()
        val dir = File(this)
        dir.walk().forEach {
            if (it.isFile && it.name == K6_JSON_OUTPUT_FILENAME) {
                files.add(K6File(it.absolutePath, it.parentFile.parentFile.name, it.parentFile.name))
            }
        }
        return files
    }


}