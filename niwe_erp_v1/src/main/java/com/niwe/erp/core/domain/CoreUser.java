package com.niwe.erp.core.domain;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder; 

@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CORE_USER")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreUser extends AbstractEntity {/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The username
	 */
	@Column(name="USERNAME",unique=true, nullable=false)
    private String username;
	/**
	 * The email
	 */
	@Column(name="EMAIL")
    private String email;
    /**
     * The password
     */
    @Column(name="PASSWORD",nullable=false)
    private String password; // encoded
    
    /**
     * The fullname
     */
    @Column(name="FULLNAME")
    private String fullname;
    /**
     * The enabled
     */
    @Column(name="ENABLED",nullable=false)
    @Default
    private boolean enabled = true;

	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "ROLE_ID", nullable = false)
	private CoreRole role;
    
	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = false)
	private CoreTaxpayer taxpayer;

}
