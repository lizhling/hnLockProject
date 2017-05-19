package com.hnctdz.aiLock.dto.system;

/** 
 * @ClassName AppMenuPermissionsDto.java
 * @Author WangXiangBo 
 */
public class AppMenuPermissionsDto {
	private Long menuId;
	private String menuName;
	private Long parentId;
	private Long permissionsType;
	private Long menuOrder;
	private Long status;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getPermissionsType() {
		return permissionsType;
	}

	public void setPermissionsType(Long permissionsType) {
		this.permissionsType = permissionsType;
	}

	public Long getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(Long menuOrder) {
		this.menuOrder = menuOrder;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
}
