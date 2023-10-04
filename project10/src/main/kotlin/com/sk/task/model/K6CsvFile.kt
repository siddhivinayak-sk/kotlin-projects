package com.sk.task.model

import com.sk.task.runner.base.BaseInput
import com.sk.task.runner.base.BaseOutput

data class K6CsvFile(
    val metrics: List<K6CsvMetrics>
): BaseOutput()

data class K6Csvs(
        val metrics: List<K6CsvMetrics>
): BaseInput
