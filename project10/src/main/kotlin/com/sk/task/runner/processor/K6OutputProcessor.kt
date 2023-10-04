package com.sk.task.runner.processor

import com.sk.task.model.K6CsvMetrics
import com.sk.task.model.K6JsonMetrics
import com.sk.task.runner.base.BaseInput
import com.sk.task.runner.base.BaseOutput
import com.sk.task.runner.base.BaseProcessor

class K6OutputProcessor: BaseProcessor {

    override fun process(input: BaseInput, config: Map<String, String>): BaseOutput {
        val k6JsonMetrics = input as K6JsonMetrics
        val k6Output = K6CsvMetrics(
                vus = config[VUS],
                cycle = config[CYCLE],
        )
        return k6Output
    }
}
