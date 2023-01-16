package com.sk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sk.gateway")
public class GatewaySampleApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewaySampleApplication.class, args);
	}
}
