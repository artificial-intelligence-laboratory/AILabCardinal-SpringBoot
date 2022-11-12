package com.ailab.ailabsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ailab.ailabsystem.mapper")
@SpringBootApplication
public class AilabsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AilabsystemApplication.class, args);
    }

}
