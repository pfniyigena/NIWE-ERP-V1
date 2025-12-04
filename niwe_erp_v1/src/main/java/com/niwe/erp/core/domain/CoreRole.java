package com.niwe.erp.core.domain;

import java.util.HashSet;
import java.util.Set;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.util.annotation.ValidRoleName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
@Table(name = "CORE_ROLE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreRole extends AbstractEntity {/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The name
	 */
	@Column(name = "NAME",unique = true, nullable = false)
	@ValidRoleName
	@NotBlank(message = "{role.name.required}")
    @Size(min = 3, max = 50, message = "{role.name.size}")
    private String name; 
	/**
	 * The description
	 */
	@Column(name = "DESCRIPTION")
    private String description;
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CORE_ROLE_PERMISSION",
        joinColumns = @JoinColumn(name = "ROLE_ID"),
        inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID"))
	@Default
	@NotEmpty(message = "{role.permissions.required}")
    private Set<CorePermission> permissions = new HashSet<>();

}
