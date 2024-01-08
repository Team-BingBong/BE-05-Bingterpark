package com.pgms.batchbooking.seat;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.pgms.coredomain.domain.booking.repository.TicketRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SeatReleaseConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final TicketRepository ticketRepository;

	@Bean
	public Job seatReleaseJob() {
		return new JobBuilder("seatReleaseJob", jobRepository)
			.start(seatReleaseStep())
			.build();
	}

	@Bean
	public Step seatReleaseStep(){
		return  new StepBuilder("seatReleaseStep", jobRepository)
			.tasklet(seatReleaseTasklet(), transactionManager)
			.build();
	}

	@Bean
	public Tasklet seatReleaseTasklet(){
		return new SeatReleaseTasklet(ticketRepository);
	}
}
