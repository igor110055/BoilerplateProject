package com.example.demoScheduler.batch.tasklets;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.ScopedProxyMode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component  @Scope(scopeName = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TutorialTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println(this.hashCode());
        log.info("TutorialTasklet  executed tasklet !!");
        System.out.println("----test---");
        return RepeatStatus.FINISHED;
    }
}