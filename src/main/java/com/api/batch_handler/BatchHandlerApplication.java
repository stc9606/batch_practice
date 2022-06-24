package com.api.batch_handler;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class BatchHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchHandlerApplication.class, args);
	}

}
