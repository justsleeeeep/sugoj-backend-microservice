package com.sug.sugojbackendjudgeservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.sug")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.sug.sugojbackendserviceclient.service"})
public class SugojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SugojBackendJudgeServiceApplication.class, args);
    }

}
