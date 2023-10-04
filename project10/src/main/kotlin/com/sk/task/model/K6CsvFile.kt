package com.sk.task.model

import com.sk.task.runner.base.BaseOutput

data class K6CsvFile(
    val metrics: List<K6CsvMetrics>
): BaseOutput()
