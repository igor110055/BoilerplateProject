package com.example.demoScheduler.batch.schedulers;

import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FxCalculateUpbitToBinanceScheduler {

    @Autowired
    @Qualifier("fxCalculateUpbitToBinanceJob") 
    private Job fxCalculateUpbitToBinanceJob;  // upbitMarketSyncJob
    private final JobLauncher jobLauncher;
   
    //매 10분 마다
    @Scheduled(cron = "0 */10 * * * *")
    public void executeㅠinanceSymbolSyncJob () {
        try {
            jobLauncher.run(
                fxCalculateUpbitToBinanceJob,
                    new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now().toString())
                    .toJobParameters()  // job parameter 설정
            );
        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

}