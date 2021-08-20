package com.offcn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling //springboot整合了quartz，
public class OffcnpeJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(OffcnpeJobApplication.class, args);
    }
}
