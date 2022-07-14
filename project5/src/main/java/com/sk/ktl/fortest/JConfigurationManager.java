package com.sk.ktl.fortest;

import java.util.HashMap;

public class JConfigurationManager {

    HashMap<String, String> configurationValues;

    private JConfigurationManager() {
        configurationValues = new HashMap<>();
        configurationValues.put("logDirectory", "./logs");
        configurationValues.put("logBaseName", "userlog");
    }

    public static JConfigurationManager getConfigurationManager() {
        return new JConfigurationManager();
    }

    public String get(String key) {
            return configurationValues.getOrDefault(key, "");
    }
}


