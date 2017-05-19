/**
 * 
 */
package com.hnctdz.aiLock.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.SysUserDetail;


/**
 * @classname	SecurityUserHolder.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2011-1-22 下午02:18:33
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */
public class SecurityUserHolder {
	
	private static final Logger logger = Logger.getLogger(AccessDecisionManager.class);
	
	/** 
     * Returns the current user 
     *  
     * @return 
     */  
    public static SysUserDetail getCurrentUserDetail() {
    	SysUserDetail sysUser = null;
    	if (SecurityContextHolder.getContext().getAuthentication() != null) {
    		sysUser = (SysUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();  
    	}
        return sysUser;
    }
    
    /**
     * @return
     */
    public static SysUser getCurrentUser() {  
    	SysUser sysUser = null;
    	if (getCurrentUserDetail() != null) {
    		SysUserDetail SysUserDetail = getCurrentUserDetail();
    		sysUser = SysUserDetail.getSysUser();  
    	}
        return sysUser;
    }
    
    /**
     * return all role owner by current user
     * @return
     */
    public static List<Long> getAllRoleInCurrentUser() {
    	List<Long> userRoleList = new ArrayList<Long>();
    	try {
    		if (SecurityContextHolder.getContext().getAuthentication() != null) {
	    		Collection<? extends GrantedAuthority> userAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		    	for (GrantedAuthority authority : userAuthorities) {
		    		userRoleList.add(Long.valueOf(authority.getAuthority()));
		    	}
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return userRoleList;
    }
    
    /**
     * 根据按钮tag设置按钮是否可能
     * @param bttonTag
     */
    public static String setEnableButtonByTag(String bttonTag) {
    	logger.debug("^^^^^^^^bttonTag is：" + bttonTag);
    	String returnTagEnableSetting = "";
    	Map<String, Collection<ConfigAttribute>> tagResourceMap = InvocationSecurityMetadataSourceService.getTagResourceMap();
    	Collection<ConfigAttribute> configAttributeList = tagResourceMap.get(bttonTag);
    	if (configAttributeList != null && configAttributeList.size() > 0) {
    		//需要进行权限判断
    		returnTagEnableSetting = "disabled='false'";
    		//获取用户权限列表，只有用户拥有资源按钮所需要的权限，才显示为可用
    		SysUserDetail sysUserDetail = SecurityUserHolder.getCurrentUserDetail();
    		for (ConfigAttribute ca : configAttributeList) {
    			String needRole = ((SecurityConfig) ca).getAttribute();
    			logger.debug("^^^^^^^^Tag needRole is：" + needRole);
    			for (GrantedAuthority ga : sysUserDetail.getAuthorities()) {
    				logger.debug("^^^^^^^^授权信息是："+ga.getAuthority());
    				if (needRole.equals(ga.getAuthority())) { //ga is user's role.
    					returnTagEnableSetting = "";
    					break;
    				}
    			}
    		}
    	}
    	logger.debug("^^^^^^^^ button tag enable：" + returnTagEnableSetting);
    	return returnTagEnableSetting;
    }
    
    public static String getAllRoleFroStringInCurrentUser() {
    	String roles = "";
    	List<Long> roleIdList = getAllRoleInCurrentUser();
    	for(Long roleId:roleIdList){
    		roles = roles + roleId + ",";
    	}
    	if(roles.length()!=0){
    		roles = roles.substring(0,roles.length()-1);
    	}
    	System.out.println("roles="+roles);
    	return roles;
    }
}
