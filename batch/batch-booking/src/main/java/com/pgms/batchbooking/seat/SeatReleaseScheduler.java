package com.pgms.batchbooking.seat;

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

/*
 * 테스트를 위한 스케줄러
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SeatReleaseScheduler {

	private final JobLauncher jobLauncher;
	private final Job lockAdminJob;

	@Scheduled(cron = "0 0 0 * * ?")
	public void runJob() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("executedTime", System.currentTimeMillis())
				.toJobParameters();

			jobLauncher.run(lockAdminJob, jobParameters);
		} catch (JobExecutionException e) {
			log.error("SeatReleaseScheduler error : ", e);
		}
	}
}
