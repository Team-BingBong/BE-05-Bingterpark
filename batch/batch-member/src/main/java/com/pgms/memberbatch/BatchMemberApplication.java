package com.pgms.memberbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.pgms")
@EnableBatchProcessing
public class BatchMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchMemberApplication.class, args);
	}

}
