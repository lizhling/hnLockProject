package com.hnctdz.aiLockdm.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnctdz.aiLockdm.utils.CommonUtil;

public class ServletFilter implements Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		boolean bl = this.getIpAddr((HttpServletRequest)request, CommonUtil.getPropertyValue("allowedIP"));
//		boolean bl = this.getIpAddr((HttpServletRequest)request, "112.74.88.103");
		
		if(bl){
			((HttpServletResponse) response).setHeader("Cache-Control", "no-cache");
			((HttpServletResponse) response).setHeader("Pragma", "no-cache");
			((HttpServletResponse) response).setDateHeader("Expires", -1);
			
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			try{
				filterChain.doFilter(request, response);
			}catch (Exception e) {
				System.out.println("无效请求！");
			}
		}else{
			System.out.println("拦截非法请求");
			response.setContentType("text/plain; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("Illegal request");
			out.flush();
			out.close();
		}
	}

	private boolean getIpAddr(HttpServletRequest request, String admitIp) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || !admitIp.equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || !admitIp.equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || !admitIp.equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || !admitIp.equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || !admitIp.equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }
        if (ip != null && admitIp.equalsIgnoreCase(ip)) {  
        	return true;
        }
        return false;  
    }
	
	public void init(FilterConfig arg0) throws ServletException {

	}

}
