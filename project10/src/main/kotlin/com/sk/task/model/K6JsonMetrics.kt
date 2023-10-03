package com.sk.task.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.sk.task.runner.base.BaseInput

data class K6JsonMetrics(
    val checks: K6Checks,
    val data_received: K6DataReceived,
    val data_sent: K6DataSent,
    val group_duration: K6GroupDuration,
    val http_metrics_authorize_api: K6HttpMetricsAuthorizeApi,
    val http_metrics_login_api: K6HttpMetricsLoginApi,
    val http_metrics_token_api: K6HttpMetricsTokenApi,
    val http_req_blocked: K6HttpReqBlocked,
    val http_req_connecting: K6HttpReqConnecting,
    val http_req_duration: K6HttpReqDuration,
    @JsonProperty("http_req_duration{expected_response:true}")
    val http_req_duration_rt: K6HttpReqDurationexpectedResponseTrue,
    val http_req_failed: K6HttpReqFailed,
    val http_req_receiving: K6HttpReqReceiving,
    val http_req_sending: K6HttpReqSending,
    val http_req_tls_handshaking: K6HttpReqTlsHandshaking,
    val http_req_waiting: K6HttpReqWaiting,
    val http_reqs: K6HttpReqs,
    val iteration_duration: K6IterationDuration,
    val iterations: K6Iterations,
    val rscua_success_auth_request: K6RscuaSuccessAuthRequest,
    val rscua_success_login_request: K6RscuaSuccessLoginRequest,
    val rscua_success_refresh_token_request: K6RscuaSuccessRefreshTokenRequest,
    val rscua_success_token_request: K6RscuaSuccessTokenRequest,
    val vus: K6Vus,
    val vus_max: K6VusMax
): BaseInput

data class K6Checks(
    val fails: Int,
    val passes: Int,
    val value: Int
)

data class K6DataReceived(
    val count: Int,
    val rate: Double
)

data class K6DataSent(
    val count: Int,
    val rate: Double
)

data class K6GroupDuration(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpMetricsAuthorizeApi(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpMetricsLoginApi(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpMetricsTokenApi(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpReqBlocked(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpReqConnecting(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpReqDuration(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpReqDurationexpectedResponseTrue(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpReqFailed(
    val fails: Int,
    val passes: Int,
    val value: Double
)

data class K6HttpReqReceiving(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpReqSending(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpReqTlsHandshaking(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpReqWaiting(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6HttpReqs(
    val count: Int,
    val rate: Double
)

data class K6IterationDuration(
    val avg: Double,
    val max: Double,
    val med: Double,
    val min: Double,
    @JsonProperty("p(90)")
    val p90: Double,
    @JsonProperty("p(95)")
    val p95: Double
)

data class K6Iterations(
    val count: Int,
    val rate: Double
)

data class K6RscuaSuccessAuthRequest(
    val fails: Int,
    val passes: Int,
    val thresholds: K6Thresholds,
    val value: Double
)

data class K6RscuaSuccessLoginRequest(
    val fails: Int,
    val passes: Int,
    val thresholds: K6Thresholds,
    val value: Double
)

data class K6RscuaSuccessRefreshTokenRequest(
    val fails: Int,
    val passes: Int,
    val thresholds: K6Thresholds,
    val value: Int
)

data class K6RscuaSuccessTokenRequest(
    val fails: Int,
    val passes: Int,
    val thresholds: K6Thresholds,
    val value: Int
)

data class K6Vus(
    val max: Int,
    val min: Int,
    val value: Int
)

data class K6VusMax(
    val max: Int,
    val min: Int,
    val value: Int
)

data class K6Thresholds(
    @JsonProperty("rate >= 0.9")
    val rate: Boolean
)
