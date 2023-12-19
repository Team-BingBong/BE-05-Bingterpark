package com.pgms.apipayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.pgms")
public class ApiPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiPaymentApplication.class, args);
    }

}
