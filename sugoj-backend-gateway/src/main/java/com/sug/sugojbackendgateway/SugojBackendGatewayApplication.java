package com.sug.sugojbackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SugojBackendGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SugojBackendGatewayApplication.class, args);
    }

}
