package com.example.demoScheduler.controller;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchRestController {
    @Autowired
    private ApplicationContext context;

    private final JobLauncher jobLauncher;

	//@PostMapping(path =  "/schedulerTrigger", consumes = "application/json", produces = "application/json")
    //@PostMapping(path =  "/schedulerTrigger", consumes = "application/json", produces = "application/json")
    @GetMapping(path =  "/schedulerTrigger")
	public ResponseEntity<Object> schedulerTrigger(@RequestParam(name="JOB") String  job,
			Authentication authentication, HttpSession session) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("statusCode", "200");  

        Job jobBean =  (Job) context.getBean(job);
        try {
            JobExecution jobEx = jobLauncher.run(
                jobBean,
                    new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now().toString())
                    .toJobParameters()  // job parameter 설정
            );
            result.put("jobId", jobEx.getJobId());  
        } catch (JobExecutionException ex) {
            result.put("statusCode", "500");  
            log.error(ex.getMessage(), ex);
        }
		return ResponseEntity.ok(result);
	}
}
