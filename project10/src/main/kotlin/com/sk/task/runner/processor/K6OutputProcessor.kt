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
                cpu = k6JsonMetrics.infra?.cpu ?: 0.0,
                memory = k6JsonMetrics.infra?.memory ?: 0.0,
                network = k6JsonMetrics.infra?.network ?: 0.0,

                autho_min = k6JsonMetrics.metrics?.http_metrics_authorize_api?.min,
                autho_med = k6JsonMetrics.metrics?.http_metrics_authorize_api?.med,
                autho_max = k6JsonMetrics.metrics?.http_metrics_authorize_api?.max,
                autho_p90 = k6JsonMetrics.metrics?.http_metrics_authorize_api?.p90,
                autho_p95 = k6JsonMetrics.metrics?.http_metrics_authorize_api?.p95,
                autho_avg = k6JsonMetrics.metrics?.http_metrics_authorize_api?.avg,
                autho_pas = k6JsonMetrics.metrics?.rscua_success_auth_request?.passes,
                autho_fai = k6JsonMetrics.metrics?.rscua_success_auth_request?.fails,

                login_min = k6JsonMetrics.metrics?.http_metrics_login_api?.min,
                login_med = k6JsonMetrics.metrics?.http_metrics_login_api?.med,
                login_max = k6JsonMetrics.metrics?.http_metrics_login_api?.max,
                login_p90 = k6JsonMetrics.metrics?.http_metrics_login_api?.p90,
                login_p95 = k6JsonMetrics.metrics?.http_metrics_login_api?.p95,
                login_avg = k6JsonMetrics.metrics?.http_metrics_login_api?.avg,
                login_pas = k6JsonMetrics.metrics?.rscua_success_login_request?.passes,
                login_fai = k6JsonMetrics.metrics?.rscua_success_login_request?.fails,

                token_min = k6JsonMetrics.metrics?.http_metrics_token_api?.min,
                token_med = k6JsonMetrics.metrics?.http_metrics_token_api?.med,
                token_max = k6JsonMetrics.metrics?.http_metrics_token_api?.max,
                token_p90 = k6JsonMetrics.metrics?.http_metrics_token_api?.p90,
                token_p95 = k6JsonMetrics.metrics?.http_metrics_token_api?.p95,
                token_avg = k6JsonMetrics.metrics?.http_metrics_token_api?.avg,
                token_pas = k6JsonMetrics.metrics?.rscua_success_token_request?.passes,
                token_fai = k6JsonMetrics.metrics?.rscua_success_token_request?.fails,

                refre_pas = k6JsonMetrics.metrics?.rscua_success_refresh_token_request?.passes,
                refre_fai = k6JsonMetrics.metrics?.rscua_success_refresh_token_request?.fails,
                )
        return k6Output
    }
}
