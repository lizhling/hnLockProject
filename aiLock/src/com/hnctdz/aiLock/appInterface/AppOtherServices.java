package com.hnctdz.aiLock.appInterface;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.Authentication;
import com.hnctdz.aiLock.dto.AuthenticationManager;
import com.hnctdz.aiLock.service.info.OrgInfoService;
import com.hnctdz.aiLock.service.info.PersonnelInfoService;
import com.hnctdz.aiLock.service.system.SysUserService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DisposeParameter;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ReturnCodeUtil;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.annotations.Before;

/** 
 * @ClassName AppOtherServices.java
 * @Author WangXiangBo 
 */
@SuppressWarnings("serial")
@ParentPackage("app-package") 
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/AppOtherServices")
@Controller
public class AppOtherServices extends BaseAction{
	private static final Logger LOG = Logger.getLogger(AppCheckServices.class);

	private String queryStrings;
	private String loginUser;
	private Long loginId = null;
	private String userType = null;
	private Long orgId = null;
	private DevLockInfo lockInfo = null;
	private String responseString = null;
	
	@Autowired
	private PersonnelInfoService personnelInfoService;
	@Autowired
	private OrgInfoService orgInfoService;
	@Autowired
	private SysUserService sysUserService;
	
	@Before
    public void annoBefore(){
        String queryString = request.getQueryString();
        queryStrings = ReturnCodeUtil.decryptData(queryString);
        System.out.println("请求解密参数："+queryStrings);
        
        loginUser = DisposeParameter.getStringValueFromTag(queryStrings, "loginUser");
    }
	
	/** 验证toKen */
    public String validateAuthentication(){
    	String result = null;
		try {
			if(StringUtil.isNotEmpty(loginUser)){
				Authentication authentication = AuthenticationManager.getInstance().checkAuthentication(loginUser);
				if(StringUtil.isEmpty(authentication.getCheckResult())){
					loginId = authentication.getLoginId();
					userType = authentication.getUserType();
					orgId = authentication.getOrgId();
				}else{
					result = authentication.getCheckResult();
				}
			}else{
				result = "操作失败，请登录！";
			}
		} catch (Exception e) {
			result = "验证用户有效性失败，请登录！";
		}
		return result;
	}
    
	/** 获取人员资料信息  */
	@Action(value="getLoginUserInfo")
	@ToJson(outField="responseString")
	public String getLoginUserInfo(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		String name = "";
		String phoneNo = "";
		String orgName = "";
		String address = "";
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				if(Constants.PERSONNEL_LOGIN.equalsIgnoreCase(userType)){
					PersonnelInfo per = personnelInfoService.getPersonnelInfoById(loginId);
					if(null != per){
						name = per.getPerName();
						phoneNo = per.getPhoneNo();
						orgName = orgInfoService.getOrgInfoById(per.getOrgId()).getOrgName();
						address = per.getAddress();
					}
				}else{
					SysUser sysUser = sysUserService.getSysUserById(loginId);
					if(null != sysUser){
						name = sysUser.getName();
						phoneNo = sysUser.getPhoneNo();
						orgName = orgInfoService.getOrgInfoById(sysUser.getOrgId()).getOrgName();
					}
				}
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			if(StringUtil.isEmpty(resultMessage)){
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}
			responseString = ReturnCodeUtil.successTojsons("{\"resultCode\":\"" + resultCode + "\",\"resultMessage\":\"" + resultMessage 
					+ "\",\"name\":\"" + name + "\",\"phoneNo\":\"" + phoneNo + "\",\"orgName\":\"" + orgName + "\",\"address\":\"" + address + "\"}");
		}
		return SUCCESS;
	}
}
