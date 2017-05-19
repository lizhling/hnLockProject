package com.hnctdz.aiLock.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security的工具类.
 */
public class SpringSecurityUtils {

	private SpringSecurityUtils() {}

	/**
	 * 取得当前用户的登录名,如果当前用户未登录则返回null.
	 */
	public static String getCurrentUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null ) {
			return null;
		}
		AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
		if (trustResolver.isAnonymous(auth)){
			return null;
		}
		return auth.getName();
	}

	/**
	 * 获取已经验证的用户。
	 * @return
	 */
	public static Authentication currentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 设置临时已经登陆的用户
	 * @param authentication
	 */
	public static void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	public static boolean isAdministrator(){
		Authentication auth = currentAuthentication();
		if(auth != null){
			return auth.getAuthorities().contains(new GrantedAuthorityImpl("1"));
		}
		return false;
	}
	
	/**
	 * 判断是不是标签管理员
	 * 
	 */
	public static boolean isLabelAdministrator(){
		Authentication auth = currentAuthentication();
		if(auth != null){
			return auth.getAuthorities().contains(new GrantedAuthorityImpl("label_admin"));
		}
		return false;
	}
	
	/**
	 * 获取已经验证用户的所有角色。
	 * 
	 */
	public static List<String> getRoles(){
		Authentication auth = currentAuthentication();
		Collection<GrantedAuthority> grantedAuthoritys=auth.getAuthorities();
		Iterator<GrantedAuthority> iterators=grantedAuthoritys.iterator();
		List<String> grantedAuthorityLists=new ArrayList<String>();
		while(iterators.hasNext()){
			GrantedAuthority trantedAuthority=iterators.next();
			grantedAuthorityLists.add(trantedAuthority.getAuthority());
		}
		return grantedAuthorityLists;
	}
	
	public static String getRoleNam(){
		Authentication auth = currentAuthentication();
		Collection<GrantedAuthority> grantedAuthoritys=auth.getAuthorities();
		Iterator<GrantedAuthority> iterators=grantedAuthoritys.iterator();
		String role_id="";
		while(iterators.hasNext()){
			GrantedAuthority trantedAuthority=iterators.next();
			role_id=trantedAuthority.getAuthority();
			System.out.println("roleid=="+role_id);
			break;
		}
		return role_id;
	}
	
	
	
}
