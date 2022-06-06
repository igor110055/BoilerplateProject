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
public class BinanceSyncScheduler {

    @Autowired
    @Qualifier("binanceSymbolSyncJob") 
    private Job binanceSymbolSyncJob;  // upbitMarketSyncJob
    private final JobLauncher jobLauncher;
   
    //매일아침 8시, 저녁 6시 33
    @Scheduled(cron = "0 33 8,18 * * *")
    public void executeㅠinanceSymbolSyncJob () {
        try {
            jobLauncher.run(
                binanceSymbolSyncJob,
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