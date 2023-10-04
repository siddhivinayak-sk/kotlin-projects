package com.sk.task.model

import com.sk.task.runner.base.BaseInput

data class K6File(
        val file: String,
        val vus: String,
        val cycle: String,
)
