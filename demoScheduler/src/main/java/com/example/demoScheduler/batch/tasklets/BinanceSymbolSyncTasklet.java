package com.example.demoScheduler.batch.tasklets;

import java.util.HashMap;
import java.util.Map;

import com.example.demoScheduler.service.GoRestService;
import com.example.demoScheduler.utils.PjtUtil;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ScopedProxyMode;

@Slf4j
@Component  @Scope(scopeName = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BinanceSymbolSyncTasklet implements Tasklet {
    @Autowired
	private  PjtUtil pjtU;
    @Autowired
	private GoRestService goS;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("BinanceSymbolSyncTasklet  executed tasklet !!");
        System.out.println(this.hashCode());
        //여기서 BR을 호출하는 로직을 넣어야한다.
        Map<String, Object> inDS = new HashMap<String, Object>();
        inDS.put("brRq","");
        inDS.put("brRs","");
		HashMap<String, Object> result = new HashMap<String, Object>();
		String jsonOutString = goS.callApiBizActorMap("BR_BINANCE_syncSymbol", inDS);
        return RepeatStatus.FINISHED;
    }
}