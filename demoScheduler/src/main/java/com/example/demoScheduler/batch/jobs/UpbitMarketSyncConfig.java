package com.example.demoScheduler.batch.jobs;

import com.example.demoScheduler.batch.tasklets.TutorialTasklet;
import com.example.demoScheduler.batch.tasklets.UpbitMarketSyncTasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UpbitMarketSyncConfig {

    private final JobBuilderFactory jobBuilderFactory; // Job 빌더 생성용
    private final StepBuilderFactory stepBuilderFactory; // Step 빌더 생성용

    private final UpbitMarketSyncTasklet upbitMarketSyncTasklet; // Step 빌더 생성용

    // JobBuilderFactory를 통해서 tutorialJob을 생성
    @Bean
    public Job upbitMarketSyncJob() {
        return jobBuilderFactory.get("upbitMarketSyncJob")
                .start(upbitMarketSyncStep())  // Step 설정
                .build();
    }

    // StepBuilderFactory를 통해서 tutorialStep을 생성
    @Bean
    public Step upbitMarketSyncStep() {
        return stepBuilderFactory.get("upbitMarketSyncStep")
                .tasklet(upbitMarketSyncTasklet) // Tasklet 설정
                .build();
    }
}