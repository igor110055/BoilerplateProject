package com.example.demoScheduler;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling     // 스케쥴러 기능 활성화
@EnableBatchProcessing  // 배치 기능 활성화
@SpringBootApplication
public class SpringBatchApplication {

    //https://deeplify.dev/back-end/spring/batch-tutorial

	public static void main(String[] args) {

		SpringApplication.run(SpringBatchApplication.class, args);
	}
}
