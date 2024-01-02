package com.pgms.memberbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("com.pgms")
public class BatchMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchMemberApplication.class, args);
	}

}
