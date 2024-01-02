package com.pgms.memberbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class LockAdminScheduler {

	private final JobLauncher jobLauncher;
	private final Job lockAdminJob;

	@Scheduled(cron = "0 0 0 * * ?")
	public void runJob() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();

			jobLauncher.run(lockAdminJob, jobParameters);
		} catch (JobExecutionException e) {
			log.warn("LockAdminScheduler error : ", e);
		}
	}
}
