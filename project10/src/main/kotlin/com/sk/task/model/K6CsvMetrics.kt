package com.sk.task.model

import com.sk.task.runner.base.BaseOutput

data class K6CsvMetrics(
        val vus: String?,
        val cycle: String?,
): BaseOutput()
