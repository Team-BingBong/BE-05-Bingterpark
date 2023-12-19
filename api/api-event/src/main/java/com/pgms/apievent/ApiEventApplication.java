package com.pgms.apievent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.pgms")
public class ApiEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiEventApplication.class, args);
    }

}
