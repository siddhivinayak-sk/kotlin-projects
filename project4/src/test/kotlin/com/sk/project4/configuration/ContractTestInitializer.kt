package com.sk.project4.configuration

interface ContractTestInitializer {
    fun initTest()
    fun cleanUpTest() {
        //No clean up required by default
    }
}