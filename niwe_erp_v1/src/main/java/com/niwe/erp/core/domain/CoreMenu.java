package com.niwe.erp.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "CORE_MENU")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreMenu extends AbstractEntity{/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	 /**
	 * The title
	 */
	@Column(name = "TITLE")
    private String title;             // e.g. "Products"
    /**
     * The icon
     */
    @Column(name = "ICON")
    private String icon;              // optional, e.g. "fa-box"
    /**
     * THe url
     */
    @Column(name = "URL")
    private String url;               // e.g. "/products"
    /**
     * The sortOrder
     */
    @Column(name = "SORT_ORDER")
    @Default
    private Integer sortOrder = 0;    // display order

    /**
     * The parent
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private CoreMenu parent;              // for nested menus (optional)
    /**
     * The children
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Column(name = "CHILDREN", nullable = false)
    @Default
    private List<CoreMenu> children = new ArrayList<>();
    /**
     * The requiredPermission
     */
    @Column(name = "REQUIRED_PERMISSION", nullable = false)
    private String requiredPermission; // e.g. "PRODUCT_VIEW"
    /**
     * The active
     */
    @Column(name = "ACTIVE")
    @Default
    private boolean active = true;
}
