package com.niwe.erp.common.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.niwe.erp.core.domain.EState;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4476780039321218901L;

	@JsonIgnore
	@Id
	@UuidGenerator
	@Column(name = "ID", nullable = false, unique = true, updatable = false)
	@EqualsAndHashCode.Include 
	private UUID id;

	/**
	 * This field is used for auditory and logging purposes. It is populated by the
	 * system when an entity instance is created.
	 */
	@JsonIgnore
	@Column(name = "CREATED_AT")
	@CreatedDate
	protected Instant createdAt;
	@JsonIgnore
	@Column(name = "MODIFIED_AT")
	@LastModifiedDate
	protected Instant modifiedAt;
	@CreatedBy
	@Basic(optional = true)
	@Column(name = "CREATED_BY")
	private String createdBy = "";
	@LastModifiedBy
	@Basic(optional = true)
	@Column(name = "UPDATED_BY")
	private String updatedBy = "";
	@Column(name = "STATE")
	@Enumerated(EnumType.STRING)
	@lombok.ToString.Include
	@Builder.Default
	private EState state=EState.ACTIVE;
	@JsonIgnore
	@Version
	public int version;
	

	/**
	 * This constructor is required by JPA. All subclasses of this class will
	 * inherit this constructor.
	 */
	protected AbstractEntity() {
		createdAt = Instant.now();
		modifiedAt =Instant.now();
	}

}
