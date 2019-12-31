package com.xwtec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableScheduling
@PropertySource(value = {"classpath:province.properties"}, ignoreResourceNotFound = false,encoding = "UTF-8")
public class BigScreenApplication {
    public static void main(String[] args) {
    	SpringApplication.run(BigScreenApplication.class, args);
    }
}