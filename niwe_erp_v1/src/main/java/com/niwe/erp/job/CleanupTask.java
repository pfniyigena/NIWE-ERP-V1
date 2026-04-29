package com.niwe.erp.job;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.core.service.ErrorLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CleanupTask {

	private final ErrorLogService errorLogService;

	// Runs every 10 minutes
	@Scheduled(fixedDelay = 600000)
	@Transactional
	public synchronized void deleteOldRecords() {
		errorLogService.deleteAll();
		log.info("Old records deleted at:{} ", LocalDateTime.now());
	}
}
