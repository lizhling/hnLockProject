package com.hnctdz.aiLock.domain.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * AppMenuPermissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "app_menu_permissions")
public class AppMenuPermissions implements java.io.Serializable {

	// Fields
	@Expose
	private Long menuId;
	@Expose
	private String menuName;
	@Expose
	private Long parentId;
	private Long permissionsType;
	private Long menuOrder;
	private Long status;

	// Constructors

	/** default constructor */
	public AppMenuPermissions() {
	}

	/** full constructor */
	public AppMenuPermissions(String menuName, Long parentId,
			Long permissionsType, Long menuOrder, Long status) {
		this.menuName = menuName;
		this.parentId = parentId;
		this.permissionsType = permissionsType;
		this.menuOrder = menuOrder;
		this.status = status;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "MENU_ID", unique = true, nullable = false)
	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	@Column(name = "MENU_NAME", length = 20)
	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "PERMISSIONS_TYPE")
	public Long getPermissionsType() {
		return this.permissionsType;
	}

	public void setPermissionsType(Long permissionsType) {
		this.permissionsType = permissionsType;
	}

	@Column(name = "MENU_ORDER")
	public Long getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(Long menuOrder) {
		this.menuOrder = menuOrder;
	}

	@Column(name = "STATUS")
	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

}