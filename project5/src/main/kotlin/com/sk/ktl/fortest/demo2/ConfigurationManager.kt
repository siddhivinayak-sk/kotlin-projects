package com.sk.ktl.fortest.demo2


object ConfigurationManager  {

    private var configurationValues = HashMap<String, String>()

    init {
        configurationValues["logDirectory"] = "./logs"
        configurationValues["logBaseName"] = "userlog"
    }

    operator fun get(key: String): String {
        return configurationValues.getOrDefault(key, "")
    }
}
