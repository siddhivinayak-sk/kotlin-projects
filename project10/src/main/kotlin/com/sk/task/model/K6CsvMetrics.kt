package com.sk.task.model

import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvBindByPosition
import com.sk.task.runner.base.BaseOutput

data class K6CsvMetrics(
        @CsvBindByPosition(position = 0)
        @CsvBindByName(column = "VUs")
        val vus: String?,
        @CsvBindByPosition(position = 1)
        @CsvBindByName(column = "Cycles")
        val cycle: String?,
        @CsvBindByPosition(position = 2)
        @CsvBindByName(column = "CPU")
        val cpu: Double,
        @CsvBindByPosition(position = 3)
        @CsvBindByName(column = "Memory")
        val memory: Double,
        @CsvBindByPosition(position = 4)
        @CsvBindByName(column = "Network+")
        val network: Double,
        @CsvBindByPosition(position = 5)
        @CsvBindByName(column = "Network-")
        val network_: Double,

        @CsvBindByPosition(position = 6)
        @CsvBindByName(column = "Auth Min")
        val autho_min: Double?,
        @CsvBindByPosition(position = 7)
        @CsvBindByName(column = "Auth Med")
        val autho_med: Double?,
        @CsvBindByPosition(position = 8)
        @CsvBindByName(column = "Auth Max")
        val autho_max: Double?,
        @CsvBindByPosition(position = 9)
        @CsvBindByName(column = "Auth P(90)")
        val autho_p90: Double?,
        @CsvBindByPosition(position = 10)
        @CsvBindByName(column = "Auth P(95)")
        val autho_p95: Double?,
        @CsvBindByPosition(position = 11)
        @CsvBindByName(column = "Auth Avg")
        val autho_avg: Double?,
        @CsvBindByPosition(position = 12)
        @CsvBindByName(column = "Auth Passed")
        val autho_pas: Int?,
        @CsvBindByPosition(position = 13)
        @CsvBindByName(column = "Auth Failed")
        val autho_fai: Int?,

        @CsvBindByPosition(position = 14)
        @CsvBindByName(column = "Login Min")
        val login_min: Double?,
        @CsvBindByPosition(position = 15)
        @CsvBindByName(column = "Login Med")
        val login_med: Double?,
        @CsvBindByPosition(position = 16)
        @CsvBindByName(column = "Login Max")
        val login_max: Double?,
        @CsvBindByPosition(position = 17)
        @CsvBindByName(column = "Login P(90)")
        val login_p90: Double?,
        @CsvBindByPosition(position = 18)
        @CsvBindByName(column = "Login P(95)")
        val login_p95: Double?,
        @CsvBindByPosition(position = 19)
        @CsvBindByName(column = "Login Avg")
        val login_avg: Double?,
        @CsvBindByPosition(position = 20)
        @CsvBindByName(column = "Login Passed")
        val login_pas: Int?,
        @CsvBindByPosition(position = 21)
        @CsvBindByName(column = "Login Failed")
        val login_fai: Int?,

        @CsvBindByPosition(position = 22)
        @CsvBindByName(column = "Token Min")
        val token_min: Double?,
        @CsvBindByPosition(position = 23)
        @CsvBindByName(column = "Token Med")
        val token_med: Double?,
        @CsvBindByPosition(position = 24)
        @CsvBindByName(column = "Token Max")
        val token_max: Double?,
        @CsvBindByPosition(position = 25)
        @CsvBindByName(column = "Token P(90)")
        val token_p90: Double?,
        @CsvBindByPosition(position = 26)
        @CsvBindByName(column = "Token P(95)")
        val token_p95: Double?,
        @CsvBindByPosition(position = 27)
        @CsvBindByName(column = "Token Avg")
        val token_avg: Double?,
        @CsvBindByPosition(position = 28)
        @CsvBindByName(column = "Token Passed")
        val token_pas: Int?,
        @CsvBindByPosition(position = 29)
        @CsvBindByName(column = "Token Failed")
        val token_fai: Int?,

        @CsvBindByPosition(position = 30)
        @CsvBindByName(column = "Refresh Passed")
        val refre_pas: Int?,
        @CsvBindByPosition(position = 31)
        @CsvBindByName(column = "Refresh Failed")
        val refre_fai: Int?,

        @CsvBindByPosition(position = 32)
        @CsvBindByName(column = "Total Requests")
        val reqs: Int?,
        @CsvBindByPosition(position = 33)
        @CsvBindByName(column = "RPS")
        val rps: Double?,

): BaseOutput()
