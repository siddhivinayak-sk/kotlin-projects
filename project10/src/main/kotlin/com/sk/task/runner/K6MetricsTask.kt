package com.sk.task.runner

import com.sk.task.runner.base.BaseRunner
import com.sk.task.runner.processor.K6OutputProcessor
import com.sk.task.runner.reader.K6JsonOutputReader
import com.sk.task.runner.writer.K6CSVOutputWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments

class K6MetricsTask(reader: K6JsonOutputReader, processor: K6OutputProcessor, writer: K6CSVOutputWriter):
        BaseRunner() {

    @Value("\${k6.json.input.path}") lateinit var inputPath: String
    @Value("\${k6.csv.output.path}") lateinit var outputPath: String
    @Value("\${k6.csv.output.filename}") lateinit var outputFileName: String

    override fun run(args: ApplicationArguments?) {
        println(inputPath)
        println(outputPath)
        println(outputFileName)
    }
}