package com.samuelaroca.eurekamicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaMicroServiceApplication.class, args);
	}

}
