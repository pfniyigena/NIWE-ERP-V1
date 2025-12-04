package com.niwe.erp.web.api.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.niwe.erp.web.api.domain.PosApiLog;
import com.niwe.erp.web.api.domain.PosApiLogRepository;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PosRequestLoggingFilter implements Filter {

	@Autowired
	private PosApiLogRepository logRepository;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// Extract clean path without context path
		String path = req.getRequestURI();
		String contextPath = req.getContextPath();
		if (contextPath != null && !contextPath.isEmpty()) {
			path = path.substring(contextPath.length());
		}
		log.info("=====================PATH:{}", path);
		// Only log POS-related APIs
		if (!path.startsWith("/api/")) {
			chain.doFilter(request, response);
			return;
		}

		/// Extract device ID
		String deviceId = req.getHeader("X-Device-ID");
		if (deviceId == null || deviceId.trim().isEmpty()) {
			res.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing X-Device-ID");
			return;
		}

		long startTime = System.currentTimeMillis();
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(req);
		ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(res);

		try {
			chain.doFilter(wrappedRequest, wrappedResponse);
		} finally {
			long duration = System.currentTimeMillis() - startTime;

			PosApiLog log = new PosApiLog();
			log.setDeviceId(deviceId);
			log.setEndpoint(req.getRequestURI());
			log.setMethod(req.getMethod());
			log.setDurationMs(duration);
			log.setResponseStatus(String.valueOf(res.getStatus()));

			// Safely read request body (only once)
			String requestBody = getPayload(wrappedRequest.getContentAsByteArray());
			log.setRequestBody(truncate(requestBody, 10000)); // limit size

			// Response body (optional)
			String responseBody = getPayload(wrappedResponse.getContentAsByteArray());
			log.setResponseBody(truncate(responseBody, 10000));

			logRepository.save(log);

			// Important: copy response back to original
			wrappedResponse.copyBodyToResponse();
		}
	}

	private String getPayload(byte[] buf) {
		if (buf.length > 0) {
			try {
				return new String(buf, StandardCharsets.UTF_8);
			} catch (Exception e) {
				return "[binary or corrupted data]";
			}
		}
		return "";
	}

	
	private String truncate(String value, int maxLength) {
	    if (value == null) return null;
	    if (value.length() <= maxLength) return value;
	    return value.substring(0, maxLength) + "\n... [truncated " 
	         + (value.length() - maxLength) + " chars]";
	}
}
