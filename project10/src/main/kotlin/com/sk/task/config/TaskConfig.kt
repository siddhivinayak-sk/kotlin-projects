package com.sk.task.config

import com.sk.task.runner.reader.K6JsonOutputReader
import com.sk.task.runner.K6OutputAggregatorTask
import com.sk.task.runner.processor.K6OutputProcessor
import com.sk.task.runner.writer.K6CSVOutputWriter
import org.springframework.cloud.task.configuration.EnableTask
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableTask
@Configuration
class TaskConfig {

    @Bean
    fun k6JsonOutputReader() = K6JsonOutputReader()

    @Bean
    fun k6CSVOutputWriter() = K6CSVOutputWriter()

    @Bean
    fun k6OutputProcessor() = K6OutputProcessor()

    @Bean
    fun k6OutputAggregator(reader: K6JsonOutputReader, processor: K6OutputProcessor, writer: K6CSVOutputWriter) =
            K6OutputAggregatorTask(reader, processor, writer)
}
