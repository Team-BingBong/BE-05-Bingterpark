package com.pgms.memberbatch;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.pgms.coredomain.domain.member.Admin;
import com.pgms.coredomain.domain.member.repository.AdminRepository;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LockAdminConfig {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final EntityManagerFactory entityManagerFactory;
	private final AdminRepository adminRepository;

	@Bean
	public Job lockAdminJob() {
		return new JobBuilder("lockAdminJob", jobRepository)
			.start(step())
			.build();
	}

	@Bean
	public Step step() {
		return new StepBuilder("csv-step", jobRepository)
			.<Admin, Admin>chunk(100, transactionManager)
			.reader(lockAdminReader())
			.processor(lockAdminProcessor())
			.writer(lockAdminWriter())
			.build();
	}

	@Bean
	public ItemReader<Admin> lockAdminReader() {
		LocalDateTime aYearAgo = LocalDateTime.now().minusYears(1);
		List<Admin> allTargetAdmins = adminRepository.findAll();
		allTargetAdmins.stream()
			.filter(admin -> admin.getLastLoginAt().isBefore(aYearAgo))
			.forEach(admin -> {
				log.info("잠금처리 대상 어드민 ===> {}", admin.getName());
				admin.updateToLocked();
			});
		return new ListItemReader<>(allTargetAdmins);
	}

	@Bean
	public ItemProcessor<Admin, Admin> lockAdminProcessor() {
		return admin -> {
			admin.updateToLocked();
			return admin;
		};
	}

	@Bean
	public ItemWriter<Admin> lockAdminWriter() {
		return adminRepository::saveAll;
	}
}
