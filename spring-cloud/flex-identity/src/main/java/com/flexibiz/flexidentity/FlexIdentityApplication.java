package com.flexibiz.flexidentity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FlexIdentityApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlexIdentityApplication.class, args);
	}

}
