package com.sk.task.runner.reader

import com.fasterxml.jackson.databind.ObjectMapper
import com.sk.task.model.K6JsonMetrics
import com.sk.task.runner.base.BaseInput
import com.sk.task.runner.base.BaseReader
import java.io.File

class K6JsonOutputReader: BaseReader {

    private val objectMapper = ObjectMapper()

    override fun read(config: Map<String, String>): BaseInput {
        val file = config[K6_JSON_FILE] ?: throw IllegalArgumentException("file is required")
        return objectMapper.readValue(File(file), K6JsonMetrics::class.java)

    }
}
