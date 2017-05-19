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
import com.hnctdz.aiLock.domain.system.SysVersion;
import com.hnctdz.aiLock.service.system.SysVersionService;
import com.hnctdz.aiLock.utils.DisposeParameter;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ReturnCodeUtil;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.annotations.Before;

/** 
 * @ClassName AppCheckServices.java
 * @Author WangXiangBo 
 */
@SuppressWarnings("serial")
@ParentPackage("app-package") 
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/AppCheckServices")
@Controller
public class AppCheckServices extends BaseAction{
	private static final Logger LOG = Logger.getLogger(AppCheckServices.class);
	
	@Autowired
	private SysVersionService sysVersionService;
	
	private String queryStrings;
	private String responseString = null;
	
	@Before
    public void annoBefore(){
        String queryString = request.getQueryString();
        queryStrings = ReturnCodeUtil.decryptData(queryString);
        System.out.println("请求解密参数："+queryStrings);
    }
	
	/** 检查APP版本 */
	@Action(value="checkAppVersion")
	@ToJson(outField="responseString")
	public String checkAppVersion(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		String resultVersion = "";
		try{
			String versionOs = DisposeParameter.getStringValueFromTag(queryStrings, "versionOs");
			String versionCode = DisposeParameter.getStringValueFromTag(queryStrings, "versionCode");
			SysVersion newSysVersion  = sysVersionService.getNewSysVersion(Integer.parseInt(versionOs));
			String newVersion = "0";
			if(!versionCode.equalsIgnoreCase(newSysVersion.getVersionCode())){
				newVersion = "1";
				resultVersion = ",\"versionCode\":\"" + newSysVersion.getVersionCode() + "\",\"versionName\":\"" + newSysVersion.getVersionName() + "\",\"donwloadUrl\":\"" + newSysVersion.getDonwloadUrl() + 
								"\",\"versionInfo\":\"" + newSysVersion.getVersionInfo() + "\",\"updateType\":" + newSysVersion.getUpdateType();
			}
			resultVersion += ",\"newVersion\":" + newVersion;
		}catch (Exception e) {
			LOG.error(e);
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			if(StringUtil.isEmpty(resultMessage)){
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
				responseString = ReturnCodeUtil.successTojsons("{\"resultCode\":\"" + resultCode + "\",\"resultMessage\":\"" + resultMessage + "\""
						+ resultVersion + "}");
			}
		}
		return SUCCESS;
	}
}
