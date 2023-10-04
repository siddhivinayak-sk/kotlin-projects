package com.sk.task.runner.writer

import com.opencsv.CSVWriter
import com.opencsv.bean.BeanField
import com.opencsv.bean.ColumnPositionMappingStrategy
import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.StatefulBeanToCsvBuilder
import com.opencsv.exceptions.CsvRequiredFieldEmptyException
import com.sk.task.model.K6CsvFile
import com.sk.task.model.K6CsvMetrics
import com.sk.task.runner.base.BaseOutput
import com.sk.task.runner.base.BaseWriter
import org.apache.commons.lang3.StringUtils
import java.io.FileWriter

class K6CSVOutputWriter: BaseWriter {

    override fun write(output: BaseOutput, config: Map<String, String>) {
        val csvOutputPath = config[K6_CSV_FILEPATH] ?: throw IllegalArgumentException("output filepath is required")
        val csvOutputFile = config[K6_CSV_FILENAME] ?: throw IllegalArgumentException("output filename is required")
        val mappingStrategy = CustomMappingStrategy<K6CsvMetrics>()
        mappingStrategy.type = K6CsvMetrics::class.java
        val k6CsvMetrics = output as K6CsvFile
        val writer  = FileWriter("$csvOutputPath/$csvOutputFile")
        val sbc = StatefulBeanToCsvBuilder<K6CsvMetrics>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withMappingStrategy(mappingStrategy)
                .build()
        sbc.write(k6CsvMetrics.metrics)
        writer.close()
    }
}

internal class CustomMappingStrategy<T> : ColumnPositionMappingStrategy<T>() {

    @Throws(CsvRequiredFieldEmptyException::class)
    override fun generateHeader(bean: T): Array<String?>? {
        val numColumns = fieldMap.values().size
        super.generateHeader(bean)
        val header = arrayOfNulls<String>(numColumns)
        var beanField: BeanField<T, Int>?
        for (i in 0 until numColumns) {
            beanField = findField(i)
            val columnHeaderName = extractHeaderName(beanField)
            header[i] = columnHeaderName
        }
        return header
    }
    private fun extractHeaderName(beanField: BeanField<T, Int>?): String {
        if (null == beanField || beanField.field == null || beanField.field.getDeclaredAnnotationsByType(CsvBindByName::class.java).isEmpty()) {
            return StringUtils.EMPTY
        }
        val bindByNameAnnotation: CsvBindByName = beanField.field.getDeclaredAnnotationsByType(CsvBindByName::class.java)[0]
        return bindByNameAnnotation.column
    }
}