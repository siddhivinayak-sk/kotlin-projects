package com.sk.project6

interface ContractTestInitializer {

    fun initTest()
    fun cleanUpTest() {
        //No clean up required by default
    }

}