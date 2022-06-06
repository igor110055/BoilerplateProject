package com.example.demoScheduler.batch.jobs;

import com.example.demoScheduler.batch.tasklets.BinanceSymbolSyncTasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BinanceSymbolSyncConfig {

    private final JobBuilderFactory jobBuilderFactory; // Job 빌더 생성용
    private final StepBuilderFactory stepBuilderFactory; // Step 빌더 생성용

    private final BinanceSymbolSyncTasklet binanceSymbolSyncTasklet; // Step 빌더 생성용

    // JobBuilderFactory를 통해서 tutorialJob을 생성
    @Bean
    public Job binanceSymbolSyncJob() {
        return jobBuilderFactory.get("binanceSymbolSyncJob")
                .start(binanceSymbolSyncStep())  // Step 설정
                .build();
    }

    // StepBuilderFactory를 통해서 tutorialStep을 생성
    @Bean
    public Step binanceSymbolSyncStep() {
        return stepBuilderFactory.get("binanceSymbolSyncStep")
                .tasklet(binanceSymbolSyncTasklet) // Tasklet 설정
                .build();
    }
}