package com.niwe.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.core.domain.ErrorLog;
import com.niwe.erp.core.domain.ErrorLogType;
import com.niwe.erp.core.repository.ErrorLogRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ErrorLogService {
	private final ErrorLogRepository errorLogRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(String errorName, String errorDescription, ErrorLogType type) {

		ErrorLog errorLog = errorLogRepository.findByErrorName(errorName).orElseGet(() -> {
			ErrorLog e = new ErrorLog();
			e.setErrorName(errorName);
			e.setErrorDescription(errorDescription);
			e.setErrorType(type);
			return errorLogRepository.save(e);
		});
		log.info("========{}", errorLog);

	}

	public void deleteItemById(String logId) {
		errorLogRepository.deleteById(UUID.fromString(logId));
	}
	public List<ErrorLog> findAll() {
		return errorLogRepository.findAll();
	}
	public ErrorLog findById(String id) {
		return errorLogRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Log not found with id " + id));

	}


}
