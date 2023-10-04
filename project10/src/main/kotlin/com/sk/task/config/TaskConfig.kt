package com.sk.task.config

import com.sk.task.runner.reader.K6JsonOutputReader
import com.sk.task.runner.K6MetricsTask
import com.sk.task.runner.processor.K6OutputProcessor
import com.sk.task.runner.processor.K6OutputSummaryProcessor
import com.sk.task.runner.writer.K6CSVOutputWriter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.task.configuration.EnableTask
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableTask
@Configuration
class TaskConfig {

    companion object {
        const val K6 = "k6"
        const val ENABLED = "enabled"
        const val TRUE = "true"
    }

    @Bean
    @ConditionalOnProperty(prefix = K6, name = [ENABLED], havingValue = TRUE)
    fun k6JsonOutputReader() = K6JsonOutputReader()

    @Bean
    @ConditionalOnProperty(prefix = K6, name = [ENABLED], havingValue = TRUE)
    fun k6CSVOutputWriter() = K6CSVOutputWriter()

    @Bean
    @ConditionalOnProperty(prefix = K6, name = [ENABLED], havingValue = TRUE)
    fun k6OutputProcessor() = K6OutputProcessor()

    @Bean
    @ConditionalOnProperty(prefix = K6, name = [ENABLED], havingValue = TRUE)
    fun k6OutputSummaryProcessor() = K6OutputSummaryProcessor()

    @Bean
    @ConditionalOnProperty(prefix = K6, name = [ENABLED], havingValue = TRUE)
    fun k6OutputAggregator(
            reader: K6JsonOutputReader,
            processor: K6OutputProcessor,
            summaryProcessor: K6OutputSummaryProcessor,
            writer: K6CSVOutputWriter
    ) = K6MetricsTask(reader, processor, summaryProcessor, writer)
}
