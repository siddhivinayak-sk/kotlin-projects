package com.sk.task.model

import com.sk.task.runner.base.BaseOutput

data class K6CsvMetrics(
        val vus: Int,
        val cycle: Int,
): BaseOutput()
