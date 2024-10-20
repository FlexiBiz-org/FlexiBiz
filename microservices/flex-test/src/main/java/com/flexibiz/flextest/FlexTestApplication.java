package com.flexibiz.flextest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FlexTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlexTestApplication.class, args);
    }

}
