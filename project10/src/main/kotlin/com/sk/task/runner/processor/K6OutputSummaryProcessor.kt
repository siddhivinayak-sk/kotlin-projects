package com.sk.task.runner.processor

import com.sk.task.model.K6CsvMetrics
import com.sk.task.model.K6Csvs
import com.sk.task.runner.base.BaseInput
import com.sk.task.runner.base.BaseOutput
import com.sk.task.runner.base.BaseProcessor
import com.sk.task.util.mean
import com.sk.task.util.stdDev

class K6OutputSummaryProcessor: BaseProcessor {

    override fun process(input: BaseInput, config: Map<String, String>): BaseOutput {
        val csvs = input as K6Csvs
        val aggregator = config[AGGREGATOR] ?: "avg"
        return K6CsvMetrics(
                vus = config[VUS],
                cycle = "$CYCLE$HYPHEN${csvs.metrics.size}",
                cpu = aggregator.calculate(csvs.metrics.map { it.cpu }.toTypedArray()),
                memory = aggregator.calculate(csvs.metrics.map { it.memory }.toTypedArray()),
                network = aggregator.calculate(csvs.metrics.map { it.network }.toTypedArray()),
                network_ = aggregator.calculate(csvs.metrics.map { it.network_ }.toTypedArray()),

                autho_min = aggregator.calculate(csvs.metrics.map { it.autho_min as Double }.toTypedArray()),
                autho_med = aggregator.calculate(csvs.metrics.map { it.autho_med as Double }.toTypedArray()),
                autho_max = aggregator.calculate(csvs.metrics.map { it.autho_max as Double }.toTypedArray()),
                autho_p90 = aggregator.calculate(csvs.metrics.map { it.autho_p90 as Double }.toTypedArray()),
                autho_p95 = aggregator.calculate(csvs.metrics.map { it.autho_p95 as Double }.toTypedArray()),
                autho_avg = aggregator.calculate(csvs.metrics.map { it.autho_avg as Double }.toTypedArray()),
                autho_pas = aggregator.calculate(csvs.metrics.map { it.autho_pas as Int }.toTypedArray()).toInt(),
                autho_fai = aggregator.calculate(csvs.metrics.map { it.autho_fai as Int }.toTypedArray()).toInt(),

                login_min = aggregator.calculate(csvs.metrics.map { it.login_min as Double }.toTypedArray()),
                login_med = aggregator.calculate(csvs.metrics.map { it.login_med as Double }.toTypedArray()),
                login_max = aggregator.calculate(csvs.metrics.map { it.login_max as Double }.toTypedArray()),
                login_p90 = aggregator.calculate(csvs.metrics.map { it.login_p90 as Double }.toTypedArray()),
                login_p95 = aggregator.calculate(csvs.metrics.map { it.login_p95 as Double }.toTypedArray()),
                login_avg = aggregator.calculate(csvs.metrics.map { it.login_avg as Double }.toTypedArray()),
                login_pas = aggregator.calculate(csvs.metrics.map { it.login_pas as Int }.toTypedArray()).toInt(),
                login_fai = aggregator.calculate(csvs.metrics.map { it.login_fai as Int }.toTypedArray()).toInt(),

                token_min = aggregator.calculate(csvs.metrics.map { it.token_min as Double }.toTypedArray()),
                token_med = aggregator.calculate(csvs.metrics.map { it.token_med as Double }.toTypedArray()),
                token_max = aggregator.calculate(csvs.metrics.map { it.token_max as Double }.toTypedArray()),
                token_p90 = aggregator.calculate(csvs.metrics.map { it.token_p90 as Double }.toTypedArray()),
                token_p95 = aggregator.calculate(csvs.metrics.map { it.token_p95 as Double }.toTypedArray()),
                token_avg = aggregator.calculate(csvs.metrics.map { it.token_avg as Double }.toTypedArray()),
                token_pas = aggregator.calculate(csvs.metrics.map { it.token_pas as Int }.toTypedArray()).toInt(),
                token_fai = aggregator.calculate(csvs.metrics.map { it.token_fai as Int }.toTypedArray()).toInt(),

                refre_pas = aggregator.calculate(csvs.metrics.map { it.refre_pas as Int }.toTypedArray()).toInt(),
                refre_fai = aggregator.calculate(csvs.metrics.map { it.refre_fai as Int }.toTypedArray()).toInt(),

                reqs = aggregator.calculate(csvs.metrics.map { it.reqs as Int }.toTypedArray()).toInt(),
                rps = aggregator.calculate(csvs.metrics.map { it.rps as Double }.toTypedArray()),
        )
    }

    private fun String.calculate(numbers: Array<Double>) = if(this == "avg")
        mean(numbers)
    else if(this == "stddev") {
        mean(numbers)
    } else {
        stdDev(numbers)
    }

    private fun String.calculate(numbers: Array<Int>) = if(this == "avg")
        mean(numbers)
    else if(this == "stddev") {
        mean(numbers)
    } else {
        stdDev(numbers)
    }
}
