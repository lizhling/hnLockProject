package com.hnctdz.aiLock.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.AppDataPackage;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.Response;
import com.hnctdz.aiLock.dto.TokenResponse;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName BaseAction.java
 * @Author WangXiangBo 
 */
@Entity
public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	/** serialVersionUID */
	@GeneratedValue
	private static final long serialVersionUID = -4863333241280926659L;

	/** HttpServletRequest */
	protected static HttpServletRequest request;
	
	/** HttpServletResponse */
	protected HttpServletResponse response;
	
	//请求中的分页信息
	protected String pageSize;//每页显示的记录数  
	protected String pageNum;//当前第几页
	private String rows;//jquery easy ui组件的分页
    private String page;//jquery easy ui组件的分页

	//保存数据列表及分页信息
	protected DataPackage dataPackage;
	//简单请求返回的结果
	protected Response simpleRespose = new Response();
	//APP数据集请求返回的结果
	protected AppDataPackage appDataPackage;
	//业务令牌请求返回的结果
	protected TokenResponse tokenResponse;
	
    //会在action初始化的时候被调用
	public DataPackage getDataPackage() {
		if (dataPackage == null) {
			dataPackage = new DataPackage();
			//初始化空list，防止没有查询到数据返回null，前端不会刷新问题
			List list = new ArrayList();
			dataPackage.setRows(list);
			dataPackage.setFooter(list);
		}
		if (StringUtil.isNotBlank(page) && StringUtil.isNotBlank(rows)) {
			dataPackage.setPageNo(StringUtil.convertToInt(this.page, 0) - 1);
			dataPackage.setPageSize(StringUtil.convertToInt(this.rows, 0));
		} else if (StringUtil.isNotBlank(pageSize) && StringUtil.isNotBlank(pageNum)) {
			dataPackage.setPageNo(StringUtil.convertToInt(this.pageNum, 0) - 1);
			dataPackage.setPageSize(StringUtil.convertToInt(this.pageSize, 0));
		} else {
			dataPackage.setPageNo(0);//初始化第一页
			dataPackage.setPageSize(15);//一页15条记录
		}
		
		return dataPackage;
	}
	
	//会在action初始化的时候被调用
	public AppDataPackage getAppDataPackage() {
		if (appDataPackage == null) {
			appDataPackage = new AppDataPackage();
		}
		return appDataPackage;
	}
	
	//会在action初始化的时候被调用
	public TokenResponse getTokenResponse() {
		if (tokenResponse == null) {
			tokenResponse = new TokenResponse();
		}
		return tokenResponse;
	}
	
	public void putToSession(String key, Object obj) {
		getRequest().getSession().setAttribute(key, obj);
	}
	
	public static SysUser getLoginSysUser() {
		try{
			Object obj = getRequest().getSession().getAttribute(Constants.SESSION_USER);
			if(obj != null){
				return (SysUser) obj;
			}else{
				return null;
			}
		}catch (Exception e) {
			return null;
		}
	}
	
	public void removeLoginUserSession() {
		getRequest().getSession().removeAttribute(Constants.SESSION_USER);
	}
	
	/**
	 * 获取验证码
	 */
	public String getValidaCode() {
		return (String) getRequest().getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
	}
	
	public void setdataPackage(DataPackage page) {
		this.dataPackage = page;
	}
	
	public static HttpServletRequest getRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * 返回每页条数
	 * @return
	 */
	public int getPageSize() {
		return StringUtil.convertToInt(this.pageSize, 15);
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	
	/**
	 * 返回每页条数
	 * @return
	 */
	public int getPageNum() {
		return StringUtil.convertToInt(this.pageNum, 0);
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public Response getSimpleRespose() {
		return simpleRespose;
	}
	
	public void setSimpleRespose(Response simpleRespose) {
		this.simpleRespose = simpleRespose;
	}

	public void setAppDataPackage(AppDataPackage appDataPackage) {
		this.appDataPackage = appDataPackage;
	}

}