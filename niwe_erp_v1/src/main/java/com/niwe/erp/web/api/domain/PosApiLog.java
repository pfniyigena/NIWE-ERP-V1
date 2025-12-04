package com.niwe.erp.web.api.domain;

import java.time.LocalDateTime;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "POS_API_LOG", indexes = { @Index(name = "idx_pos_device_id", columnList = "deviceId"),
		@Index(name = "idx_request_time", columnList = "requestTime") })
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PosApiLog extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "DEVICE_ID")
	private String deviceId; // from POS header or JWT
	@Column(name = "ENDPOINT")
	private String endpoint; // /api/sales, /api/stock, etc.
	@Column(name = "METHOD")
	private String method; // GET, POST, etc.
	@Lob
	@Column(name = "REQUEST_BODY", columnDefinition = "TEXT")
	private String requestBody; // JSON payload (truncated if too long)
	@Column(name = "RESPONSE_STATUS")
	private String responseStatus; // 200, 400, etc.
	@Lob
	@Column(name = "RESPONSE_BODY", columnDefinition = "TEXT")
	private String responseBody; // optional, truncate if huge
	@Column(name = "DURATION_MS")
	private Long durationMs;
	@Column(name = "REQUEST_TIME")
	@Builder.Default
	private LocalDateTime requestTime = LocalDateTime.now();

}
