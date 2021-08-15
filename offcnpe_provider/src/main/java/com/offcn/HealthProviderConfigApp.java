package com.offcn;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.offcn.mapper")
@EnableDubboConfiguration //千万不要忘记
public class HealthProviderConfigApp {

    public static void main(String[] args) {
        SpringApplication.run(HealthProviderConfigApp.class,args);
    }
}
