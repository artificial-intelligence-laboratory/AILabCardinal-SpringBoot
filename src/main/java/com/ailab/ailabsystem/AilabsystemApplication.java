package com.ailab.ailabsystem;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ailab.ailabsystem.mapper")
@Slf4j
@SpringBootApplication
public class AilabsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AilabsystemApplication.class, args);
    }

}
