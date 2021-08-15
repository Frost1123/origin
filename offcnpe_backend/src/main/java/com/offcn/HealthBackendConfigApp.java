package com.offcn;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class HealthBackendConfigApp {

    public static void main(String[] args) {
        // Dashboard
        SpringApplication.run(HealthBackendConfigApp.class,args);
    }
}
