package com.offcn;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class MobilelConfigApp {
    public static void main(String[] args) {
        SpringApplication.run(MobilelConfigApp.class,args);
    }
}
