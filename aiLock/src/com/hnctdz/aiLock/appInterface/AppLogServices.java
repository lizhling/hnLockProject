package com.hnctdz.aiLock.appInterface;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.dto.Authentication;
import com.hnctdz.aiLock.dto.AuthenticationManager;
import com.hnctdz.aiLock.dto.analysis.UnlockRecordsDto;
import com.hnctdz.aiLock.service.analysis.UnlockRecordsService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.DisposeParameter;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.GsonUtil;
import com.hnctdz.aiLock.utils.ReturnCodeUtil;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.annotations.Before;

/** 
 * @ClassName AppLogServices.java
 * @Author WangXiangBo 
 */
@SuppressWarnings("serial")
@ParentPackage("app-package")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/AppLogServices")
@Controller
public class AppLogServices extends BaseAction{
	private static Log smartKeyRecordLog = LogFactory.getLog("SmartKeyRecord");
	private static final Logger LOG = Logger.getLogger(AppLogServices.class);
	
	@Autowired
	private UnlockRecordsService unlockRecordsService;
	
	private String queryStrings;
	Map<String, String[]> params = null;
	private String loginUser;
	private Long loginId = null;
	private String userType = null;
	private String responseString = null;
	
	String data = null;
	
	@Before
    public void annoBefore(){
		if("POST".equalsIgnoreCase(request.getMethod())){
			params = request.getParameterMap();
	        String[] loginUsers = params.get("loginUser");
	        loginUser = ReturnCodeUtil.decryptData(loginUsers[0]);
		}else{
			String queryString = request.getQueryString();
	        queryStrings = ReturnCodeUtil.decryptData(queryString);
	        System.out.println("请求解密参数："+queryStrings);
	        
	        loginUser = DisposeParameter.getStringValueFromTag(queryStrings, "loginUser");
		}
		
        
//        String queryString = "";  
//        for (String key : params.keySet()) {  
//            String[] values = params.get(key);  
//            for (int i = 0; i < values.length; i++) {  
//                String value = values[i];  
//                queryString += key + "=" + value + "&";  
//            }  
//        } 
//		System.out.println(queryString);
    }
    
    /**
     * 验证toKen
     */
    public String validateAuthentication(){
    	String result = null;
		try {
			if(StringUtil.isNotEmpty(loginUser)){
				Authentication authentication = AuthenticationManager.getInstance().checkAuthentication(loginUser);
				if(StringUtil.isEmpty(authentication.getCheckResult())){
					loginId = authentication.getLoginId();
					userType = authentication.getUserType();
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
    
    /**
	 * 提取锁芯ID接口
	 */
	@Action(value="extractLockId")
	@ToJson(outField="responseString")
	public String extractLockId(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		String dataLog = null;
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				String[] dataValues = params.get("data");
				dataLog = ReturnCodeUtil.decryptData(dataValues[0]);
				System.out.println(dataLog);
				resultMessage = unlockRecordsService.extractLockId(dataLog);
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}else{
				resultMessage = validateResult;
			}
		}catch (ErrorCodeException e) {
			resultMessage = e.getMessage();
			smartKeyRecordLog.info(dataLog);
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			System.out.println(resultCode + "---" + resultMessage);
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}
    
    /**
	 * 上传智能钥匙记录接口
	 */
    @Action(value="appCommitSmartKeyLog")
	@ToJson(outField="responseString")
	public String appCommitSmartKeyLog(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		String dataLog = null;
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				String[] dataValues = params.get("data");
				dataLog = ReturnCodeUtil.decryptData(dataValues[0]);
				System.out.println(dataLog);
				LOG.error(dataLog);
				unlockRecordsService.saveSmartKeyLog(loginId, dataLog);
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}else{
				resultMessage = validateResult;
			}
		}catch (ErrorCodeException e) {
			resultMessage = "记录上传失败！";
			smartKeyRecordLog.info(dataLog);
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询门锁记录列表
	 */
	@Action(value="appFindPageUnlockRecords")
	@ToJson(outField="responseString")
	public String appFindPageUnlockRecords(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		List list = null;
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				pageSize = DisposeParameter.getStringValueFromTag(queryStrings, "pageSize");
				pageNum = DisposeParameter.getStringValueFromTag(queryStrings, "pageNum");
				dataPackage = getDataPackage();
				
				UnlockRecordsDto dto = new UnlockRecordsDto();
				String selectType = DisposeParameter.getStringValueFromTag(queryStrings, "selectType");
				if("2".equalsIgnoreCase(selectType)){
					String alarmLevel = DisposeParameter.getStringValueFromTag(queryStrings, "alarmLevel");
					dto.setAlarmLevel(alarmLevel);
					dto.setIfDeal("0");
				}else{
					String recordCode = DisposeParameter.getStringValueFromTag(queryStrings, "recordCode");
					dto.setRecordCode(recordCode);
				}
				
				if(Constants.PERSONNEL_LOGIN.equalsIgnoreCase(this.userType)){//APP是人员登录
					dto.setUnlockPerId(loginId);
				}
				
				String lockName = DisposeParameter.getStringValueFromTag(queryStrings, "lockName");
				String unStartTime = DisposeParameter.getStringValueFromTag(queryStrings, "unStartTime");
				String unEndTime = DisposeParameter.getStringValueFromTag(queryStrings, "unEndTime");
				
				dto.setLockName(lockName);
				dto.setUnStartTime(unStartTime);
				dto.setUnEndTime(unEndTime);
				dto.setSelectType(Integer.parseInt(selectType));//1：查询记录；2：查询告警
				dataPackage = unlockRecordsService.findPageUnlockRecords(dto, dataPackage);
				list = (List)dataPackage.getRows();
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}else{
				resultMessage = validateResult;
			}
		} catch(Exception e) {
			e.printStackTrace();
			LOG.error(ErrorCodeException.getExceInfo(e));
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = GsonUtil.buildArrayJsonString(resultCode, resultMessage, list);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询门锁告警列表
	 */
	@Action(value="appFindPageAlarmRecords")
	@ToJson(outField="responseString")
	public String appFindPageAlarmRecords(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		List list = null;
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				pageSize = params.get("pageSize")[0];
				pageNum = params.get("pageNum")[0];
				
				dataPackage = getDataPackage();
				String timeType = params.get("timeType")[0];
				String lockCode = params.get("lockCode")[0];
				String recordCode = params.get("recordCode")[0];
				
				UnlockRecordsDto dto = new UnlockRecordsDto();
				dto.setLockCode(lockCode);
				dto.setRecordCode(recordCode);
				dataPackage = unlockRecordsService.findPageUnlockRecords(dto, dataPackage);
				list = (List)dataPackage.getRows();
			}else{
				resultMessage = validateResult;
			}
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = GsonUtil.buildArrayJsonString(resultCode, resultMessage, list);
		}
		return SUCCESS;
	}
}
