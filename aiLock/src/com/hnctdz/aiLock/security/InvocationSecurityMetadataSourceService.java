package com.hnctdz.aiLock.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;
import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.AppMenuPermissions;
import com.hnctdz.aiLock.domain.system.SysRes;
import com.hnctdz.aiLock.domain.system.SysRole;
import com.hnctdz.aiLock.domain.system.SysRoleRes;
import com.hnctdz.aiLock.dto.system.AppMenuPermissionsDto;
import com.hnctdz.aiLock.service.system.AppMenuPermissionsService;
import com.hnctdz.aiLock.service.system.SysRoleService;
import com.hnctdz.aiLock.service.system.SysRoleResService;
import com.hnctdz.aiLock.utils.AppMenuInfo;
import com.hnctdz.aiLock.utils.Constants;

 
/*
 * 
 * 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。
 * 注意，我例子中使用的是AntUrlPathMatcher这个path matcher来检查URL是否与资源定义匹配，
 * 事实上你还要用正则的方式来匹配，或者自己实现一个matcher。
 * 
 * 此类在初始化时，应该取到所有资源及其对应角色的定义
 * 
 * 说明：对于方法的spring注入，只能在方法和成员变量里注入，
 * 如果一个类要进行实例化的时候，不能注入对象和操作对象，
 * 所以在构造函数里不能进行操作注入的数据。
 */
@Service("InvocationSecurityMetadataSourceService")
public class InvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(InvocationSecurityMetadataSourceService.class);
	
	private static SysRoleService sysRoleService ;
	private static SysRoleResService sysRoleResService;
	private static AppMenuPermissionsService appMenuPermissionsService;
 
	
	private UrlMatcher urlMatcher = new AntUrlPathMatcher();
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	private static Map<String, Collection<ConfigAttribute>> tagResourceMap = null;
	
	public static void loadResourceDefine() throws Exception  {
		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		tagResourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		
		//先查询出所有角色
		List<SysRole> sysRoleList = sysRoleService.findSysRole(null);
		for (SysRole item : sysRoleList){
			ConfigAttribute ca = new SecurityConfig(item.getRoleId().toString());
			
			//根据权限查询出所有资源
			List<SysRoleRes> tActionList = sysRoleResService.findSysResByRole(item.getRoleId());
			//把资源放入到spring security的resouceMap中
			for(SysRoleRes sysRoleRes : tActionList){
				SysRes tAction = sysRoleRes.getFrwSysRes();
				//保存URL－roleId对象信息
				if (resourceMap.get(tAction.getResUrl()) != null) {//如果本资源已有role，把当前的角色添加进去
					Collection<ConfigAttribute> oldAtts = resourceMap.get(tAction.getResUrl());
					if (!oldAtts.contains(ca)) {
						oldAtts.add(ca);
					}
					resourceMap.put(tAction.getResUrl(), oldAtts);
				} else {
					Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
					atts.add(ca);
					resourceMap.put(tAction.getResUrl(), atts);
				}
				System.out.println("Url权限过滤明细是："+tAction.getResUrl()+"--" + resourceMap.get(tAction.getResUrl()));
				
				//保存TAG－roleId对象信息
//				if (StringUtils.isNotBlank(tAction.getResTag()) && !"#".equalsIgnoreCase(tAction.getResTag())) {
//					if (tagResourceMap.get(tAction.getResTag()) != null) {//如果本资源已有role，把当前的角色添加进去
//						Collection<ConfigAttribute> oldAtts = tagResourceMap.get(tAction.getResTag());
//						if (!oldAtts.contains(ca)) {
//							oldAtts.add(ca);
//						}
//						tagResourceMap.put(tAction.getResTag(), oldAtts);
//					} else {
//						tagResourceMap.put(tAction.getResTag(), atts);
//					}
//					System.out.println("Tag权限过滤明细是："+tAction.getResTag()+"--" + tagResourceMap.get(tAction.getResTag()));
//				}
			}
		}
		appMenuUpdateTask();
	}
	
	public static void appMenuUpdateTask(){
		AppMenuPermissionsDto dto = new AppMenuPermissionsDto();
		dto.setStatus(1L);
		List<AppMenuPermissions> list = appMenuPermissionsService.findAppMenuPermissionsList(dto);
		
		AppMenuInfo appMenuInfo = AppMenuInfo.getInstance();
		appMenuInfo.initializeList();
		
		for(AppMenuPermissions amp : list){
			if(amp.getParentId() == null){
				amp.setParentId(0L);//表示最上级菜单
			}
			if(amp.getPermissionsType().equals(Long.parseLong(Constants.PERSONNEL_LOGIN))){
				appMenuInfo.addPerMenuList(amp);
			}else if(amp.getPermissionsType().equals(Long.parseLong(Constants.SYSUSER_LOGIN))){
				appMenuInfo.addSysUserMenuList(amp);
			}else{
				appMenuInfo.addPerMenuList(amp);
				appMenuInfo.addSysUserMenuList(amp);
			}
		}
	}
 
 	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
 		String url = ((FilterInvocation) object).getRequestUrl();
 		
 		int firstQuestionMarkIndex = url.indexOf("?");
 		if (firstQuestionMarkIndex != -1) {
 			url = url.substring(0, firstQuestionMarkIndex);
 		}
 
		Iterator<String> ite = resourceMap.keySet().iterator();
		while (ite.hasNext()) {
			String resURL = ite.next();
			if (StringUtils.isNotBlank(resURL) && urlMatcher.pathMatchesUrl(url, resURL)) {
				Collection<ConfigAttribute> returnCollection = resourceMap.get(resURL);
				return returnCollection;
			}
		}
		return null;
	}
	public boolean supports(Class<?> clazz) {
		return true;
	}
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
		for (String key : resourceMap.keySet()) {
			atts.addAll(resourceMap.get(key));
		}
		return atts;
	}

	/**
	 * 重新加载资源列表，应与上面的LoadResourceDefine相同
	 */
	public static void reLoadResourceDefine(HttpServletRequest request) {
		ServletContext servletContext = request.getSession().getServletContext();
//		roleManagerService = (RoleManagerService) WebApplicationContextUtils.getWebApplicationContext(
//				servletContext).getBean("roleManagerService");

		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		tagResourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		
		try {
			loadResourceDefine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	

	
	/**
	 * @return the resourceMap
	 */
	public static Map<String, Collection<ConfigAttribute>> getResourceMap() {
		return resourceMap;
	}

	
	/**
	 * @return the tagResourceMap
	 */
	public static Map<String, Collection<ConfigAttribute>> getTagResourceMap() {
		return tagResourceMap;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		InvocationSecurityMetadataSourceService.sysRoleService = sysRoleService;
	}

	public void setSysRoleResService(
			SysRoleResService sysRoleResService) {
		InvocationSecurityMetadataSourceService.sysRoleResService = sysRoleResService;
	}

	public AppMenuPermissionsService getAppMenuPermissionsService() {
		return appMenuPermissionsService;
	}

	public void setAppMenuPermissionsService(
			AppMenuPermissionsService appMenuPermissionsService) {
		InvocationSecurityMetadataSourceService.appMenuPermissionsService = appMenuPermissionsService;
	}
 
}