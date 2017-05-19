package com.hnctdz.aiLock.security;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

import com.hnctdz.aiLock.utils.ReturnCodeUtil;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

/** 
 * @ClassName AppIntercept.java
 * @Author WangXiangBo 
 */
public class AppIntercept extends AbstractInterceptor {
    
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionContext actionContext = invocation.getInvocationContext();   
        HttpServletRequest request= (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST); 
        
        String queryString = request.getQueryString();
        System.out.println("请求参数AppIntercept："+queryString);
//        String queryString1 = ReturnCodeUtil.decryptData(queryString);
//        System.out.println("解密后参数："+queryString1);
        
        /*if(StringUtil.isNotBlank(queryString)){
        	Map<String, Object> parameters = invocation.getInvocationContext().getParameters();
        	String[] queryStringArr = queryString.split("&");
        	String[] strings = null;
        	for(int i=0; i < queryStringArr.length; i++){
        		String[] query = queryStringArr[i].split("=");
        		strings = new String[]{query[1]};
        		parameters.put(query[0], strings);
        	}
        	invocation.getInvocationContext().setParameters(parameters);
        }*/
        
        Map<String, Object> parameters = invocation.getInvocationContext().getParameters();
    	String[] strings = null;
        Set entrySet = parameters.entrySet();
        String[] values = null;
        for (Iterator it = entrySet.iterator(); it.hasNext();) {
            Entry entry = (Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String[]) {
            	values = (String[]) value;//类型转换
            	strings = new String[values.length];
                for (int i = 0; i < values.length; i++) 
                {
                	String val = values[i].trim();
                    strings[i] = val;
                }
                parameters.put((String) key, strings);
            }
        }
        parameters.put("password", new String[]{"asdasdadsads"});
        invocation.getInvocationContext().setParameters(parameters);
        return invocation.invoke();
    }
}
