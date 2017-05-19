package com.hnctdz.aiLock.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnctdz.aiLock.utils.CommonUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName CheckCodeImageFilter.java
 * @Author WangXiangBo 
 */
public class CheckCodeImageFilter implements Filter{

	public void destroy() {  
    }  
  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {  
        HttpServletRequest hsRequest = (HttpServletRequest) request;
        HttpServletResponse hsResponse = (HttpServletResponse) response;
          
        String vcCode = request.getParameter("j_captcha");
        String j_username = request.getParameter("j_username");
        
        String checkCodeSession = (String)hsRequest.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY); //内存中
    	
    	String rn = CommonUtil.getRandomNumberString(4);
		Cookie cuser = new Cookie("cookie_user",j_username);
        Cookie cpass = new Cookie("cookie_random",rn);
        hsResponse.addCookie(cuser);
        hsResponse.addCookie(cpass);
        
    	if(StringUtil.isNotBlank(vcCode) && vcCode.equals(checkCodeSession)){  
    		chain.doFilter(hsRequest, hsResponse);
        } else {
        	String path = hsRequest.getContextPath();
        	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        	hsResponse.sendRedirect(basePath+"login.jsp?errorCode=1"); 
        }
    }
  
    public void init(FilterConfig arg0) throws ServletException {  
    }  
}
