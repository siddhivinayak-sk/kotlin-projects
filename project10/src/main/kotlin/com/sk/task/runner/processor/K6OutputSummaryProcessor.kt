package com.sk.task.runner.processor

import com.sk.task.model.K6CsvMetrics
import com.sk.task.model.K6Csvs
import com.sk.task.runner.base.BaseInput
import com.sk.task.runner.base.BaseOutput
import com.sk.task.runner.base.BaseProcessor
import com.sk.task.util.stdDev

class K6OutputSummaryProcessor: BaseProcessor {

    override fun process(input: BaseInput, config: Map<String, String>): BaseOutput {
        val csvs = input as K6Csvs
        return K6CsvMetrics(
                vus = config[VUS],
                cycle = "$CYCLE$HYPHEN${csvs.metrics.size}",
                cpu = stdDev(csvs.metrics.map { it.cpu }.toTypedArray()),
                memory = stdDev(csvs.metrics.map { it.memory }.toTypedArray()),
                network = stdDev(csvs.metrics.map { it.network }.toTypedArray()),
                network_ = stdDev(csvs.metrics.map { it.network_ }.toTypedArray()),

                autho_min = stdDev(csvs.metrics.map { it.autho_min as Double }.toTypedArray()),
                autho_med = stdDev(csvs.metrics.map { it.autho_med as Double }.toTypedArray()),
                autho_max = stdDev(csvs.metrics.map { it.autho_max as Double }.toTypedArray()),
                autho_p90 = stdDev(csvs.metrics.map { it.autho_p90 as Double }.toTypedArray()),
                autho_p95 = stdDev(csvs.metrics.map { it.autho_p95 as Double }.toTypedArray()),
                autho_avg = stdDev(csvs.metrics.map { it.autho_avg as Double }.toTypedArray()),
                autho_pas = stdDev(csvs.metrics.map { it.autho_pas as Int }.toTypedArray()).toInt(),
                autho_fai = stdDev(csvs.metrics.map { it.autho_fai as Int }.toTypedArray()).toInt(),

                login_min = stdDev(csvs.metrics.map { it.login_min as Double }.toTypedArray()),
                login_med = stdDev(csvs.metrics.map { it.login_med as Double }.toTypedArray()),
                login_max = stdDev(csvs.metrics.map { it.login_max as Double }.toTypedArray()),
                login_p90 = stdDev(csvs.metrics.map { it.login_p90 as Double }.toTypedArray()),
                login_p95 = stdDev(csvs.metrics.map { it.login_p95 as Double }.toTypedArray()),
                login_avg = stdDev(csvs.metrics.map { it.login_avg as Double }.toTypedArray()),
                login_pas = stdDev(csvs.metrics.map { it.login_pas as Int }.toTypedArray()).toInt(),
                login_fai = stdDev(csvs.metrics.map { it.login_fai as Int }.toTypedArray()).toInt(),

                token_min = stdDev(csvs.metrics.map { it.token_min as Double }.toTypedArray()),
                token_med = stdDev(csvs.metrics.map { it.token_med as Double }.toTypedArray()),
                token_max = stdDev(csvs.metrics.map { it.token_max as Double }.toTypedArray()),
                token_p90 = stdDev(csvs.metrics.map { it.token_p90 as Double }.toTypedArray()),
                token_p95 = stdDev(csvs.metrics.map { it.token_p95 as Double }.toTypedArray()),
                token_avg = stdDev(csvs.metrics.map { it.token_avg as Double }.toTypedArray()),
                token_pas = stdDev(csvs.metrics.map { it.token_pas as Int }.toTypedArray()).toInt(),
                token_fai = stdDev(csvs.metrics.map { it.token_fai as Int }.toTypedArray()).toInt(),

                refre_pas = stdDev(csvs.metrics.map { it.refre_pas as Int }.toTypedArray()).toInt(),
                refre_fai = stdDev(csvs.metrics.map { it.refre_fai as Int }.toTypedArray()).toInt(),
        )
    }
}