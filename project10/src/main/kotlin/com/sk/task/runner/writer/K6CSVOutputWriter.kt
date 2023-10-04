package com.sk.task.runner.writer

import com.opencsv.CSVWriter
import com.opencsv.bean.StatefulBeanToCsvBuilder
import com.sk.task.model.K6CsvFile
import com.sk.task.model.K6CsvMetrics
import com.sk.task.runner.base.BaseOutput
import com.sk.task.runner.base.BaseWriter
import java.io.FileWriter

class K6CSVOutputWriter: BaseWriter {

    override fun write(output: BaseOutput, config: Map<String, String>) {
        val csvOutputPath = config[K6_CSV_FILEPATH] ?: throw IllegalArgumentException("output filepath is required")
        val csvOutputFile = config[K6_CSV_FILENAME] ?: throw IllegalArgumentException("output filename is required")
        val k6CsvMetrics = output as K6CsvFile
        val writer  = FileWriter("$csvOutputPath/$csvOutputFile")
        val sbc = StatefulBeanToCsvBuilder<K6CsvMetrics>(writer)
                .withQuotechar('\'')
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build()
        sbc.write(k6CsvMetrics.metrics)
        writer.flush()
    }
}
