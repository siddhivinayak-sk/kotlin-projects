package com.sk.task.model

import com.sk.task.runner.base.BaseInput

data class K6Object(
    val vus: String,
    val cycle: String,
    val input: BaseInput,
)