package com.pgms.apibooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.pgms")
public class ApiBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiBookingApplication.class, args);
    }

}
