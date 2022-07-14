package com.sk.ktl.fortest;

import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.System.out;

public class JProgram {

    public static void main(String[] args) throws IOException {
        JLogger logger = new JLogger();

//        logger.createLog("./logs", "userlog");
        String filename = logger.createLog();
        out.println("Created: " + filename);
    }
}
