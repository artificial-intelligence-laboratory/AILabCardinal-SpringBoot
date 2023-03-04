package com.ailab.ailabsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.ailab.ailabsystem.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class AilabsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AilabsystemApplication.class, args);
//        CorsFilter bean = (CorsFilter) run.getBean("corsFilter");
//        System.out.println(bean);
    }

}
