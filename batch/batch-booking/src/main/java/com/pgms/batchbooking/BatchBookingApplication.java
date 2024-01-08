package com.pgms.batchbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.pgms")
public class BatchBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchBookingApplication.class, args);
	}

}
