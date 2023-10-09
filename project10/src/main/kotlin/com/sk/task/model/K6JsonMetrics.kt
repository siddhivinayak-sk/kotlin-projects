package com.sk.task.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.sk.task.runner.base.BaseInput

@JsonIgnoreProperties(ignoreUnknown = true)
class K6JsonMetrics: BaseInput {
    @JsonProperty val metrics: K6Metrics? = null
    @JsonProperty val infra: InfraMetrics? = null
}

class K6Metrics() {
    @JsonProperty val checks: K6Checks? = null
    @JsonProperty val data_received: K6DataReceived? = null
    @JsonProperty val data_sent: K6DataSent? = null
    @JsonProperty val group_duration: K6GroupDuration? = null
    @JsonProperty val http_metrics_authorize_api: K6HttpMetricsAuthorizeApi? = null
    @JsonProperty val http_metrics_login_api: K6HttpMetricsLoginApi? = null
    @JsonProperty val http_metrics_token_api: K6HttpMetricsTokenApi? = null
    @JsonProperty val http_req_blocked: K6HttpReqBlocked? = null
    @JsonProperty val http_req_connecting: K6HttpReqConnecting? = null
    @JsonProperty val http_req_duration: K6HttpReqDuration? = null
    @JsonProperty("http_req_duration{expected_response:true}") val http_req_duration_rt: K6HttpReqDurationexpectedResponseTrue? = null
    @JsonProperty val http_req_failed: K6HttpReqFailed? = null
    @JsonProperty val http_req_receiving: K6HttpReqReceiving? = null
    @JsonProperty val http_req_sending: K6HttpReqSending? = null
    @JsonProperty val http_req_tls_handshaking: K6HttpReqTlsHandshaking? = null
    @JsonProperty val http_req_waiting: K6HttpReqWaiting? = null
    @JsonProperty val http_reqs: K6HttpReqs? = null
    @JsonProperty val iteration_duration: K6IterationDuration? = null
    @JsonProperty val iterations: K6Iterations? = null
    @JsonProperty val rscua_success_auth_request: K6RscuaSuccessAuthRequest? = null
    @JsonProperty val rscua_success_login_request: K6RscuaSuccessLoginRequest? = null
    @JsonProperty val rscua_success_refresh_token_request: K6RscuaSuccessRefreshTokenRequest? = null
    @JsonProperty val rscua_success_token_request: K6RscuaSuccessTokenRequest? = null
    @JsonProperty val vus: K6Vus? = null
    @JsonProperty val vus_max: K6VusMax? = null
}

class K6Checks() {
    @JsonProperty val fails: Int? = null
    @JsonProperty val passes: Int? = null
    @JsonProperty val value: Int? = null
}

class K6DataReceived() {
    @JsonProperty val count: Int? = null
    @JsonProperty val rate: Double? = null
}

class K6DataSent{
    @JsonProperty val count: Int? = null
    @JsonProperty val rate: Double? = null
}

class K6GroupDuration {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpMetricsAuthorizeApi {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpMetricsLoginApi{
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpMetricsTokenApi {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpReqBlocked {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpReqConnecting {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpReqDuration {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpReqDurationexpectedResponseTrue {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpReqFailed {
    @JsonProperty val fails: Int? = null
    @JsonProperty val passes: Int? = null
    @JsonProperty val value: Double? = null
}

class K6HttpReqReceiving {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpReqSending {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpReqTlsHandshaking {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpReqWaiting {
    @JsonProperty val avg: Double? = null
    @JsonProperty val max: Double? = null
    @JsonProperty val med: Double? = null
    @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6HttpReqs {
        @JsonProperty val count: Int? = null
        @JsonProperty val rate: Double? = null
}

class K6IterationDuration {
        @JsonProperty val avg: Double? = null
        @JsonProperty val max: Double? = null
        @JsonProperty val med: Double? = null
        @JsonProperty val min: Double? = null
    @JsonProperty("p(90)") val p90: Double? = null
    @JsonProperty("p(95)") val p95: Double? = null
}

class K6Iterations {
        @JsonProperty val count: Int? = null
        @JsonProperty val rate: Double? = null
}

class K6RscuaSuccessAuthRequest {
        @JsonProperty val fails: Int? = null
        @JsonProperty val passes: Int? = null
        @JsonProperty val thresholds: K6Thresholds? = null
        @JsonProperty val value: Double? = null
}

class K6RscuaSuccessLoginRequest {
        @JsonProperty val fails: Int? = null
        @JsonProperty val passes: Int? = null
        @JsonProperty val thresholds: K6Thresholds? = null
        @JsonProperty val value: Double? = null
}

class K6RscuaSuccessRefreshTokenRequest {
        @JsonProperty val fails: Int? = null
        @JsonProperty val passes: Int? = null
        @JsonProperty val thresholds: K6Thresholds? = null
        @JsonProperty val value: Int? = null
}

class K6RscuaSuccessTokenRequest {
        @JsonProperty val fails: Int? = null
        @JsonProperty val passes: Int? = null
        @JsonProperty val thresholds: K6Thresholds? = null
        @JsonProperty val value: Int? = null
}

class K6Vus {
        @JsonProperty val max: Int? = null
        @JsonProperty val min: Int? = null
        @JsonProperty val value: Int? = null
}

class K6VusMax {
        @JsonProperty val max: Int? = null
        @JsonProperty val min: Int? = null
        @JsonProperty val value: Int? = null
}

class K6Thresholds {
    @JsonProperty("rate >= 0.9") val rate: Boolean? = null
}

class InfraMetrics {
    @JsonProperty val cpu: Double? = null
    @JsonProperty val memory: Double? = null
    @JsonProperty val network: Double? = null
    @JsonProperty val network_: Double? = null
}
