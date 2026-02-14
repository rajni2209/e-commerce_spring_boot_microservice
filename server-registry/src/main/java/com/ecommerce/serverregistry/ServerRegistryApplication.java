package com.ecommerce.serverregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ServerRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerRegistryApplication.class, args);
	}

}
