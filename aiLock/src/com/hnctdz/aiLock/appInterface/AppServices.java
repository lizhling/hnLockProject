package com.hnctdz.aiLock.appInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.analysis.UnlockRecords;
import com.hnctdz.aiLock.domain.device.DevKeyGroup;
import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.domain.device.LockKeyAuthorize;
import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.domain.system.AppMenuPermissions;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.AppLoginJson;
import com.hnctdz.aiLock.dto.Authentication;
import com.hnctdz.aiLock.dto.AuthenticationManager;
import com.hnctdz.aiLock.dto.CommandInfo;
import com.hnctdz.aiLock.dto.device.DevKeyInfoDto;
import com.hnctdz.aiLock.dto.device.DevLockInfoDto;
import com.hnctdz.aiLock.dto.device.LockKeyAuthorizeDto;
import com.hnctdz.aiLock.dto.info.PersonnelInfoDto;
import com.hnctdz.aiLock.dto.info.UpdateAuthorizeDto;
import com.hnctdz.aiLock.https.HttpDeviceManageForRest;
import com.hnctdz.aiLock.service.analysis.UnlockRecordsService;
import com.hnctdz.aiLock.service.device.DevKeyGroupService;
import com.hnctdz.aiLock.service.device.DevKeyInfoService;
import com.hnctdz.aiLock.service.device.DevLockInfoService;
import com.hnctdz.aiLock.service.device.LockKeyAuthorizeService;
import com.hnctdz.aiLock.service.info.PersonnelInfoService;
import com.hnctdz.aiLock.service.info.UpdateAuthorizeService;
import com.hnctdz.aiLock.service.system.SysUserRoleService;
import com.hnctdz.aiLock.service.system.SysUserService;
import com.hnctdz.aiLock.utils.AppMenuInfo;
import com.hnctdz.aiLock.utils.Calculate;
import com.hnctdz.aiLock.utils.CommonUtil;
import com.hnctdz.aiLock.utils.CommunCrypUtil;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.Mess;
import com.hnctdz.aiLock.utils.ResponseCommandUtil;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DisposeParameter;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.GsonUtil;
import com.hnctdz.aiLock.utils.ReturnCodeUtil;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.annotations.Before;

/** 
 * @ClassName AppServices.java
 * @Author WangXiangBo 
 */
@SuppressWarnings("serial")
@ParentPackage("app-package") 
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/AppServices")
@Controller
public class AppServices extends BaseAction{
	private static final Logger LOG = Logger.getLogger(AppServices.class);
			
	@Autowired
	private PersonnelInfoService personnelInfoService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private DevLockInfoService devLockInfoService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private LockKeyAuthorizeService lockKeyAuthorizeService;
	@Autowired
	private DevKeyInfoService devKeyInfoService;
	@Autowired
	private UnlockRecordsService unlockRecordsService;
	@Autowired
	private DevKeyGroupService devKeyGroupService;
	@Autowired
	private UpdateAuthorizeService updateAuthorizeService;
	
	
	private String queryStrings;
	private String loginUser;
	private Long loginId = null;
	private String userType = null;
	private Long orgId = null;
	private DevLockInfo lockInfo = null;
	private String responseString = null;
	
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
				System.out.println("authenticat_result:"+authentication.getCheckResult());
				if(StringUtil.isEmpty(authentication.getCheckResult())){
					System.out.println("authentication.getUserType();"+authentication.getUserType());
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
   
	
	/** 登录APP客户端,并生成Token令牌返回 */
	@Action(value="loginApp")
	@ToJson(outField="responseString")
	public String loginApp(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		boolean loginWay = false;
		List<AppMenuPermissions> list = null;
		AppLoginJson json = new AppLoginJson();
		try{
			String password = DisposeParameter.getStringValueFromTag(queryStrings, "password");
			String userType = DisposeParameter.getStringValueFromTag(queryStrings, "userType");
			if(StringUtil.isNotEmpty(loginUser) && StringUtil.isNotEmpty(password)){
				Long loginId = null, orgId = null;
				if(StringUtil.isNotEmpty(userType)){
					if(Constants.PERSONNEL_LOGIN.equalsIgnoreCase(userType)){
						PersonnelInfo personnelInfo = personnelInfoService.getPersonnelByAccounts(loginUser);
						if(personnelInfo != null && personnelInfo.getPerPassword().equalsIgnoreCase(password)){
							loginId = personnelInfo.getPerId();
							orgId = personnelInfo.getOrgId();
							list = AppMenuInfo.getInstance().getPerMenuList();
							json.setSmartKeyPerId(personnelInfo.getSmartKeyPerId());
							json.setSmartKeyPassw(personnelInfo.getSmartKeyPassw());
							loginWay = true;
						}else{
							resultMessage = "用户名或密码错误！";
						}
					}else{
						SysUser sysUser =  sysUserService.getSysUserByName(loginUser);
						if(sysUser != null && password.equalsIgnoreCase(sysUser.getPassword())){
							loginId = sysUser.getUserId();
							orgId = sysUser.getOrgId();
							String orgPermission = sysUserRoleService.findOrgPermissionByUserOrg(sysUser.getOrgId());
							sysUser.setOrgPermissionIds(orgPermission);
							putToSession(Constants.SESSION_USER, sysUser);
							
							list = AppMenuInfo.getInstance().getSysUserMenuList();
							json.setSmartKeyPerId("000001");
							json.setSmartKeyPassw(12341234L);
							loginWay = true;
						}else{
							resultMessage = "用户名或密码错误！";
						}
					}
				}else{
					resultMessage = "登录失败，请重新打开APP登录！";
				}
				if(loginWay){
					Authentication authentication = AuthenticationManager.getInstance().generateToken(loginUser, loginId, userType, orgId);
					resultMessage = authentication.getUserToken();
					resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
				}
			}else{
				resultMessage = "请输入用户名或密码！";
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = "登录失败，请重新登录！";
		}finally{
			json.setResultCode(resultCode);
			json.setResultMessage(resultMessage);
			json.setDatas(list);
			responseString = GsonUtil.buildArrayJsonString(json);
		}
		return SUCCESS;
	}
	
	/** 验证钥匙是否可用 */
	@Action(value="verificationKeyValid")
	@ToJson(outField="responseString")
	public String verificationKeyValid(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		String secretKey = "";
		Integer lockingTime = null;
		int updateAuthorize = 0;
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				String keyCode = DisposeParameter.getStringValueFromTag(queryStrings, "keyCode");
				String phoneImei = DisposeParameter.getStringValueFromTag(queryStrings, "phoneImei");
				DevKeyInfo key = devKeyInfoService.getDevKeyInfoByKeyCode(keyCode);
				if(null != key){
					DevKeyGroup dkg = devKeyGroupService.getById(key.getGroupId());
					secretKey = dkg.getGroupSecretKey();
					lockingTime = key.getLockingTime();
					
					if(!key.getStatus().equals(1L)){
						resultMessage = "该钥匙不可以使用！";
					}else{
						if(!Constants.SYSUSER_LOGIN.equalsIgnoreCase(userType)){
							if(Constants.KEY_PER_PAINING == 1 && !key.getPerId().equals(loginId)){
								resultMessage = "您没有权限使用该钥匙！";
							}else if(StringUtil.isEmpty(key.getPhoneImei())){
								key.setPhoneImei(phoneImei);
								devKeyInfoService.saveDevKeyInfo(key);
							}else{
								if(!key.getPhoneImei().equalsIgnoreCase(phoneImei)){
									resultMessage = "该手机不是钥匙绑定手机，不能连接该钥匙！";
								}
							}
							
							if(StringUtil.isBlank(resultMessage)){
								UpdateAuthorizeDto dto = new UpdateAuthorizeDto();
								dto.setPerId(loginId);
								dto.setStatus(1L);
								updateAuthorize = updateAuthorizeService.validationUpdateAuthorize(dto);
							}
						}else{
							SysUser loginUser = getLoginSysUser();
							String orgIds = loginUser.getOrgPermissionIds();
							boolean bl = false;
							for(String orgId : orgIds.split(",")){
								if(key.getOrgId().equals(Long.parseLong(orgId))){
									bl = true;
								}
							}
							if(!bl){
								resultMessage = "您没有权限管理该钥匙！";
							}
						}
					}
				}else{
					resultMessage = "该钥匙不存在！";
				}
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			if(StringUtil.isEmpty(resultMessage)){
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
				resultMessage = DateUtil.getDateTime();
			}
			responseString = ReturnCodeUtil.successTojsons("{\"resultCode\":\"" + resultCode + "\",\"resultMessage\":\"" + resultMessage 
					+ "\",\"secretKey\":\"" + secretKey + "\",\"lockingTime\":" + lockingTime+ ",\"updateAuthorize\":" + updateAuthorize + "}");
		}
		return SUCCESS;
	}
	
	/** 获取门锁列表，包涵权限信息 */
	@Action(value="findMyManageLock")
	@ToJson(outField="responseString")
	public String findMyManageLock(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		List list = null;
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				DevLockInfoDto dto = new DevLockInfoDto();
				if(Constants.PERSONNEL_LOGIN.equalsIgnoreCase(userType)){
					dto.setUnlockPerId(loginId);
					list = devLockInfoService.findPersonnelMaitLockList(dto);
				}else{
					dto.setOrgId(orgId);
					list = devLockInfoService.findDevLockInfoList(dto);
				}
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			//e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = GsonUtil.buildArrayJsonString(resultCode, resultMessage, list);
		}
		return SUCCESS;
	}
	
	/** 获取人员列表
	@Action(value="appFindPerInfo")
	@ToJson(outField="responseString")
	public String appFindPerInfo(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		List<PersonnelInfo> perList = new ArrayList<PersonnelInfo>();
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				PersonnelInfoDto dto = new PersonnelInfoDto();
				dto.setOrgId(orgId);
				dto.setStatus(1L);
				List list = personnelInfoService.findPersonnelInfoOptions(dto);
				
				for(int i = 0; i < list.size(); i++){
					Object[] obj = (Object[])list.get(i);
					PersonnelInfo per = new PersonnelInfo();
					per.setPerId(Long.parseLong(obj[0].toString()));
					per.setPerName(obj[1].toString());
					perList.add(per);
				}
				
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = GsonUtil.buildArrayJsonString(resultCode, resultMessage, perList);
		}
		return SUCCESS;
	} */
	
	@Action(value="appFindPerInfo")
	@ToJson(outField="responseString")
	public String appFindPerInfo(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		List<PersonnelInfo> perList = new ArrayList<PersonnelInfo>();
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				String searchValue = DisposeParameter.getStringValueFromTag(queryStrings, "searchValue");
				if(StringUtil.isNotBlank(searchValue)){
					PersonnelInfoDto dto = new PersonnelInfoDto();
					dto.setOrgId(orgId);
					dto.setStatus(1L);
					if(StringUtil.isNumeric(searchValue)){
						dto.setPhoneNo(searchValue);
					}else{
						dto.setPerName(searchValue);
					}
					
					List list = personnelInfoService.findPersonnelInfoOptions(dto);
					for(int i = 0; i < list.size(); i++){
						Object[] obj = (Object[])list.get(i);
						PersonnelInfo per = new PersonnelInfo();
						per.setPerId(Long.parseLong(obj[0].toString()));
						per.setPerName(obj[1].toString());
						perList.add(per);
					}
				}
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = GsonUtil.buildArrayJsonString(resultCode, resultMessage, perList);
		}
		return SUCCESS;
	}
	
	/** 获取转授权记录列表 */
	@Action(value="findTurnAuthorization")
	@ToJson(outField="responseString")
	public String findTurnAuthorization(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		List list = null;
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				pageSize = DisposeParameter.getStringValueFromTag(queryStrings, "pageSize");
				pageNum = DisposeParameter.getStringValueFromTag(queryStrings, "pageNum");
				dataPackage = getDataPackage();
				
				LockKeyAuthorizeDto dto = new LockKeyAuthorizeDto();
				dto.setCuUserId(loginId);
				list = lockKeyAuthorizeService.findTurnAuthorization(dto, dataPackage);
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = GsonUtil.buildArrayJsonString(resultCode, resultMessage, list);
		}
		return SUCCESS;
	}
	
	/** 转授权 */
	@Action(value="perTurnAuthorization")
	@ToJson(outField="responseString")
	public String perTurnAuthorization(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				String lockId = DisposeParameter.getStringValueFromTag(queryStrings, "lockId");
				String startTime = DisposeParameter.getStringValueFromTag(queryStrings, "startTime");
				String endTime = DisposeParameter.getStringValueFromTag(queryStrings, "endTime");
				String blueUnlock = DisposeParameter.getStringValueFromTag(queryStrings, "blueUnlock");
				String unlockPerId = DisposeParameter.getStringValueFromTag(queryStrings, "unlockPerId");
				String lockType = DisposeParameter.getStringValueFromTag(queryStrings, "lockType");
				
				LockKeyAuthorize lka = new LockKeyAuthorize();
				lka.setAuthorizeCode(CommonUtil.getRandomLettersUp(4) + DateUtil.getDateTime2());
				lka.setAuthorizeLockIds(lockId);
				lka.setStartTime(DateUtil.convertStringToDate(startTime));
				lka.setEndTime(DateUtil.convertStringToDate(endTime));
				lka.setBlueUnlock(Long.parseLong(blueUnlock));
				lka.setAuthorizePerIds(unlockPerId);
				lka.setCuUserId(loginId);
				lka.setCuTime(new Date());
				lka.setAuthorizeType(Constants.AUTHORIZE_PER_TURN);
				lka.setStatusCode("01");
				System.out.println("authorize --- lockType:"+lockType);
				if(null==lockType||StringUtil.isBlank(lockType)){
					lka.setLockType(2);	
					System.out.println("authorize1 --- lockType:"+lka.getLockType());
				}
				
				lockKeyAuthorizeService.saveLockKeyAuthorize(lka);
				
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}
	
	/** 取消转授权 */
	@Action(value="cancelTurnAuthorization")
	@ToJson(outField="responseString")
	public String cancelTurnAuthorization(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				String authorizeId = DisposeParameter.getStringValueFromTag(queryStrings, "authorizeId");
				
				if(StringUtil.isNotBlank(authorizeId)){
					LockKeyAuthorize lka = lockKeyAuthorizeService.getById(Long.parseLong(authorizeId));
					Date sysDate = new Date();
					if(null != lka && lka.getEndTime().getTime() >= sysDate.getTime()){
						lka.setCuUserId(loginId);
						lka.setCuTime(sysDate);
						lka.setStatusCode("00");
						lockKeyAuthorizeService.saveLockKeyAuthorize(lka);
						resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
					}else{
						resultMessage = "授权时间已过，不能取消！";
					}
				}else{
					resultMessage = "操作失败，请刷新数据再试！";
				}
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}
	
	
	/** 操作门锁前检查相关信息 */
	private String checkLockInfo(){
		String resultMessage = "";
		String validateResult = validateAuthentication();
		if(validateResult == null){
			String lockId = DisposeParameter.getStringValueFromTag(queryStrings, "lockId");
			if(StringUtil.isNotEmpty(lockId)){
				DevLockInfo dli = devLockInfoService.getById(Integer.parseInt(lockId));
				if(dli.getLockType() == 2){
					if(StringUtil.isNotEmpty(dli.getLockDeviceNo())){
						if("1".equalsIgnoreCase(dli.getStatus())){
							lockInfo = dli;
						}else{
							resultMessage = "门锁处于故障等状态中,不能对该门锁进行操作！";
						}
					}else{
						resultMessage = "该门锁没有配置对应机号！";
					}
				}else{
					resultMessage = "该门锁不支持远程操作！";
				}
			}else{
				resultMessage = "请求参数不全，请重新进入该界面操作！";
			}
		}else{
			resultMessage = validateResult;
		}
		return resultMessage;
	}
	
	/**
	 * APP远程开锁
	 */
	@Action(value="appRemoteUnlock")
	@ToJson(outField="responseString")
	public String appRemoteUnlock(){
		String returnCode = "", resultMsg = "";
		UnlockRecords ur = new UnlockRecords();
		try{
			resultMsg = checkLockInfo();
			if(StringUtil.isEmpty(resultMsg)){
				ur.setLockType(Constants.ACTIVE_LOCK);
				if(Constants.PERSONNEL_LOGIN.equalsIgnoreCase(userType)){
					ur.setUnlockPerId(loginId);
				}else{
					ur.setPerId(loginId);
				}
				ur.setUnlockTime(DateUtil.getDateTime());
				ur.setLockInModuleCode(lockInfo.getLockInModuleCode());
				ur.setLockDeviceNo(lockInfo.getLockDeviceNo());
				ur.setLockCode(lockInfo.getLockCode());
				ur.setRecordCode(ResponseCommandUtil.REMOTE_UNLOCK_RECORD_CODE);
				ur.setRecordTpye(ResponseCommandUtil.getPassiveLockRecord(ur.getRecordCode()));
				
				Mess mess = new Mess();
				mess.setModuleCode(lockInfo.getLockInModuleCode());
				mess.setDeviceNo(lockInfo.getLockDeviceNo());
				
				String returnJson = HttpDeviceManageForRest.getInstance()
						.httpRequestForLockdm(mess.remoteUnlock());
				
				CommandInfo commandInfo = GsonUtil.fromJson(returnJson, CommandInfo.class);
				if(!ReturnCodeUtil.SUCCESS_RESULT_CODE.equalsIgnoreCase(commandInfo.getReturnCode())){
					returnCode = commandInfo.getReturnCode();
				}else{
					if(!ResponseCommandUtil.SUCCESS_COMMAND
							.equalsIgnoreCase(commandInfo.getReturnMessage().substring(8, 10))){
						returnCode = ErrorCode.SYS_UNLOCK_FAILED;;
					}
				}
				ur.setMessage(commandInfo.getReturnMessage());
			}
		}catch (ErrorCodeException ec) {
			resultMsg = ec.getMessage();
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMsg = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			if(StringUtil.isNotEmpty(resultMsg)) {
				responseString = ReturnCodeUtil.success(ReturnCodeUtil.ERROR_RESULT_CODE, resultMsg);
			}else if(StringUtil.isNotEmpty(returnCode)){
				resultMsg = ErrorCode.getRemoteOperationResults(returnCode);
				responseString = ReturnCodeUtil.success(ReturnCodeUtil.ERROR_RESULT_CODE, resultMsg);
			}else{
				returnCode = ErrorCode.SYS_UNLOCK_COMMAND;
				resultMsg = ErrorCode.getRemoteOperationResults(returnCode);
				responseString = ReturnCodeUtil.success(ReturnCodeUtil.SUCCESS_RESULT_CODE, resultMsg);
			}
			ur.setRemoteUnlockResults(returnCode);
			ur.setNote(resultMsg+"(APP远程开门)");
			ur.setUploadTime(new Date());
			unlockRecordsService.saveUnlockRecords(ur);
		}
		return SUCCESS;
	}
	
	
	/**
	 * APP蓝牙开门
	 */
	@Action(value="appBluetoothUnLock")
	@ToJson(outField="responseString")
	public String appBluetoothUnLock(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		try{
			resultMessage = checkLockInfo();
			if(StringUtil.isEmpty(resultMessage)){
				String blueId = DisposeParameter.getStringValueFromTag(queryStrings, "blueId");
					   blueId = (StringUtil.isNotEmpty(blueId)) ? blueId : DisposeParameter.getStringValueFromTag(
					        this.queryStrings, "blueCode");
			    String phoneMac = DisposeParameter.getStringValueFromTag(
					        this.queryStrings, "phoneMac");
			    String blueMac = DisposeParameter.getStringValueFromTag(
					        this.queryStrings, "blueMac");
			    System.out.println("蓝牙名称："+blueId+" phoneMac:"+phoneMac+" BlueMac:"+blueMac);
			    if (StringUtil.isNotBlank(blueMac) && StringUtil.isNotEmpty(blueId) && (blueId.subSequence(0, 2).toString().equalsIgnoreCase("HC"))){
			    	 String id = "00000000000000000000";
			         byte index = 1;
			         if ((this.lockInfo.getPrivateKey() != null) || (this.lockInfo.getBlueMac() != null)) {
			           if (this.lockInfo.getBlueMac().equals(blueMac)) {
			             resultCode = "00";
			             byte[] retmsg = Calculate.getLockCommound(id, index, blueMac, phoneMac, this.lockInfo.getPrivateKey(), true);

			             byte[] retmsg1 = Calculate.getCallNameCommound(id, index, blueMac, phoneMac, this.lockInfo.getPrivateKey());

			             resultMessage = CommunCrypUtil.toHexStr(retmsg) + "|" + CommunCrypUtil.toHexStr(retmsg1);
			             System.out.println("输出新蓝牙开锁指令：" + resultMessage); 
			           }else{
			        	   resultMessage = "新蓝牙连接错识破,请核查蓝牙MAC配置信息是否正常！"; 
			           }
			         }else{
			        	 resultCode = "-1";
				         resultMessage = "新蓝牙后台锁信息配置不正常！"; 
			         }
			         System.out.println("新蓝牙后台锁信息! --当前信息：privateKey=" + this.lockInfo.getPrivateKey() + "  btMac=" + this.lockInfo.getBlueMac());
			         System.out.println("入参信息：phoneMac=" + phoneMac + "  btMac:" + blueMac);
			    }else{
					if(StringUtil.isNotEmpty(lockInfo.getLockInBlueCode())){
						if(lockInfo.getLockInBlueCode().substring(3, lockInfo.getLockInBlueCode().length())
								.equalsIgnoreCase(blueId)){
							
							Mess mess = new Mess();
							mess.setDeviceNo(lockInfo.getLockDeviceNo());
							String msg = mess.remoteUnlockEncryption();
							resultMessage = CommunCrypUtil.toHexStr(CommunCrypUtil.getCrypCommand(msg));
							resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
	
							/*UnlockRecords ur = new UnlockRecords();
							ur.setLockType(Constants.ACTIVE_LOCK);
							if(Constants.PERSONNEL_LOGIN.equalsIgnoreCase(userType)){
								ur.setUnlockPerId(loginId);
							}else{
								ur.setPerId(loginId);
							}
							ur.setUnlockTime(DateUtil.getDateTime());
							ur.setLockInModuleCode(lockInfo.getLockInModuleCode());
							ur.setLockDeviceNo(lockInfo.getLockDeviceNo());
							ur.setLockCode(lockInfo.getLockCode());
							ur.setRecordCode(ResponseCommandUtil.BLUETOOTH_UNLOCK_RECORD_CODE);
							ur.setRecordTpye(ResponseCommandUtil.getPassiveLockRecord(ur.getRecordCode()));
							
							ur.setRemoteUnlockResults(ErrorCode.SYS_UNLOCK_COMMAND);
							ur.setUploadTime(new Date());
							unlockRecordsService.saveUnlockRecords(ur);*/
						}else{
							resultMessage = "蓝牙连接错误,请选择正确的蓝牙连接！";
						}
					}else{
						resultMessage = "该门锁不支持蓝牙开门！";
					}
			    }
			}
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
	 * APP蓝牙开门结果
	 */
	@Action(value="appBlueUnLockResult")
	@ToJson(outField="responseString")
	public String appBlueUnLockResult(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		UnlockRecords ur = null;
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				String lockId = DisposeParameter.getStringValueFromTag(queryStrings, "lockId");
				String unLockResult = DisposeParameter.getStringValueFromTag(queryStrings, "unLockResult");
				if(StringUtil.isNotEmpty(lockId)){
					DevLockInfo lockInfo = devLockInfoService.getById(Integer.parseInt(lockId));
					ur = new UnlockRecords();
					ur.setLockInModuleCode(lockInfo.getLockInModuleCode());
					ur.setLockDeviceNo(lockInfo.getLockDeviceNo());
					ur.setLockCode(lockInfo.getLockCode());
					ur.setUnlockTime(DateUtil.getDateTime());
					ur.setRecordCode(ResponseCommandUtil.BLUETOOTH_UNLOCK_RECORD_CODE);
					ur.setRecordTpye(ResponseCommandUtil.getPassiveLockRecord(ur.getRecordCode()));
					
					ur.setLockType(Constants.ACTIVE_LOCK);
					ur.setUnlockPerId(loginId);
					
					if(StringUtil.isNotEmpty(unLockResult) && unLockResult.length() == 146){
						byte[] b = CommunCrypUtil.dataToByte(unLockResult);
						byte[] contentByte = CommunCrypUtil.commEncryption(b);
						if(contentByte[0] == -21 && contentByte[3] == 7 && contentByte[4] == -86 && contentByte[72] == -22){
							//resultMessage = "开门成功";
							resultMessage = "开门指令已发送";
							resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
							ur.setRemoteUnlockResults(ErrorCode.BLUE_UNLOCK_COMMAND);
						}else{
							resultMessage = "开门失败";
							ur.setRemoteUnlockResults(ErrorCode.BLUE_UNLOCK_FAILED);
						}
						ur.setMessage(CommunCrypUtil.toHexStr(contentByte));
					}else{
						ur.setMessage(unLockResult);
						resultMessage = "开门指令已发送";
					}
				}else{
					resultMessage = "请求参数不全，请重新进入该界面操作！";
				}
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
			resultMessage = "开门结果解析失败";
		}finally{
			if(ur != null){
				ur.setNote(resultMessage);
				ur.setUploadTime(new Date());
				unlockRecordsService.saveUnlockRecords(ur);
			}
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}
	
	
	/**
	 * APP获取门锁状态
	 */
	@Action(value="appGetLockState")
	@ToJson(outField="responseString")
	public String appGetLockState(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		try{
			resultMessage = checkLockInfo();
			if(StringUtil.isEmpty(resultMessage)){
				Mess mess = new Mess();
				mess.setModuleCode(lockInfo.getLockInModuleCode());
				mess.setDeviceNo(lockInfo.getLockDeviceNo());
				
				String returnJson = HttpDeviceManageForRest.getInstance()
						.httpRequestForLockdm(mess.getLockState());
				
				CommandInfo commandInfo = GsonUtil.fromJson(returnJson, CommandInfo.class);
				if(!ReturnCodeUtil.SUCCESS_RESULT_CODE.equalsIgnoreCase(commandInfo.getReturnCode())){
					resultMessage = ErrorCode.getRemoteOperationResults(commandInfo.getReturnCode());
				}else{
					resultMessage = ResponseCommandUtil.getHandLockStateInfo(commandInfo.getReturnMessage());
					resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
				}
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}
	

	/** 提交门锁经纬度信息 */
	@Action(value="submitLongAndLatItude")
	@ToJson(outField="responseString")
	public String submitLongAndLatItude(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				String lockId = DisposeParameter.getStringValueFromTag(queryStrings, "lockId");
				String longitude = DisposeParameter.getStringValueFromTag(queryStrings, "longitude");
				String latitude = DisposeParameter.getStringValueFromTag(queryStrings, "latitude");
				DevLockInfo lock = devLockInfoService.getById(Integer.parseInt(lockId));
				if(null != lock){
					if(lock.getLongitude() == null && lock.getLatitude() == null){
						lock.setLongitude(Double.parseDouble(longitude));
						lock.setLatitude(Double.parseDouble(latitude));
						devLockInfoService.saveDevLockInfo(lock);
						resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
					}else{
						resultMessage = "上传失败，门锁已有经纬度！";
					}
				}else{
					resultMessage = "系统中找不到该锁信息，请联系管理员！";
				}
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}
	
	/** 修改门锁信息 */
	@Action(value="submitLockInfo")
	@ToJson(outField="responseString")
	public String submitLockInfo(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				String lockCode = DisposeParameter.getStringValueFromTag(queryStrings, "lockCode");
				String lockName = DisposeParameter.getStringValueFromTag(queryStrings, "lockName");
				String lockAddres = DisposeParameter.getStringValueFromTag(queryStrings, "lockAddres");
				DevLockInfoDto dto = new DevLockInfoDto();
				dto.setLockCode(lockCode);
				List<DevLockInfo> list = devLockInfoService.findDevLockInfoList(dto);
				if(list.size() > 0){
					DevLockInfo lock = list.get(0);
					lock.setLockName(lockName);
					lock.setLockAddres(lockAddres);
					devLockInfoService.saveDevLockInfo(lock);
					resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
				}else{
					resultMessage = "系统中找不到该锁的信息，请联系管理员！";
				}
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}	

	/** 修改人员登录密码及智能钥匙密码 */
	@Action(value="updatePerPassword")
	@ToJson(outField="responseString")
	public String updatePerPassword(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				String upType = DisposeParameter.getStringValueFromTag(queryStrings, "upType");
				String oldPassw = DisposeParameter.getStringValueFromTag(queryStrings, "oldPassw");
				String newPassw = DisposeParameter.getStringValueFromTag(queryStrings, "newPassw");
				if("1".equalsIgnoreCase(upType)){
					PersonnelInfo per = personnelInfoService.getPersonnelInfoById(loginId);
					if(!per.getPerPassword().equalsIgnoreCase(oldPassw)){
						resultMessage = "当前密码错误，请重新输入！";
					}else{
						per.setPerPassword(newPassw);
						personnelInfoService.savePersonnelInfo(per);
					}
				}else if("2".equalsIgnoreCase(upType)){
					PersonnelInfo per = personnelInfoService.getPersonnelInfoById(loginId);
					if(!per.getSmartKeyPassw().equals(Long.parseLong(oldPassw))){
						resultMessage = "当前密码错误，请重新输入！";
					}else{
						per.setSmartKeyPassw(Long.parseLong(newPassw));
						personnelInfoService.savePersonnelInfo(per);
					}
				}
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			if(StringUtil.isEmpty(resultMessage)){
				resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
			}
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}
	
	/** 获取钥匙组列表 */
	@Action(value="findKeyGroupList")
	@ToJson(outField="responseString")
	public String findKeyGroupList(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		List list = null;
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				if(Constants.SYSUSER_LOGIN.equalsIgnoreCase(userType)){
					list = devKeyGroupService.findDevKeyGroupList(null);
					resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
				}else{
					resultMessage = "您没有权限操作此功能！";
				}
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = GsonUtil.buildArrayJsonString(resultCode, resultMessage, list);
		}
		return SUCCESS;
	}
	
	/** 初始化智能钥匙 */
	@Action(value="initializeSmartKey")
	@ToJson(outField="responseString")
	public String initializeSmartKey(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				if(Constants.SYSUSER_LOGIN.equalsIgnoreCase(userType)){
					String keyCode = DisposeParameter.getStringValueFromTag(queryStrings, "keyCode");
					String keyGroupId = DisposeParameter.getStringValueFromTag(queryStrings, "keyGroupId");
					String note = DisposeParameter.getStringValueFromTag(queryStrings, "note");
					if(StringUtil.isNotBlank(keyCode) && StringUtil.isNotBlank(keyGroupId)){
						
						DevKeyInfo dbData = devKeyInfoService.getDevKeyInfoByKeyCode(keyCode);
						if(dbData != null && dbData.getStatus() != 0L ){//钥匙是挂失状态
							resultMessage = "该智能钥匙不能被初始化！";
						}else{
							DevKeyGroup group = devKeyGroupService.getById(Long.parseLong(keyGroupId));
							if(group != null){
								DevKeyInfo devKeyInfo = new DevKeyInfo();
								if(dbData != null){
									devKeyInfo = dbData;
								}
								devKeyInfo.setKeyCode(keyCode.toUpperCase());
								devKeyInfo.setKeyType(1L);
								devKeyInfo.setGroupId(group.getGroupId());
								devKeyInfo.setOrgId(orgId);
								devKeyInfo.setLockingTime(24);
								devKeyInfo.setStatus(0L);
								devKeyInfo.setNote(note);
								devKeyInfoService.saveDevKeyInfo(devKeyInfo);
								
								resultMessage = group.getGroupSecretKey();
								resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
							}else{
								resultMessage = "钥匙组错误，请重新选择！";
							}
						}
					}else{
						resultMessage = "初始化信息不全，请重新录入！";
					}
				}else{
					resultMessage = "您没有权限操作此功能！";
				}
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}

	/** 提交无源锁芯信息  */
	@Action(value="initializePassiveLock")
	@ToJson(outField="responseString")
	public String initializePassiveLock(){
		String resultCode = ReturnCodeUtil.ERROR_RESULT_CODE;
		String resultMessage = "";
		try{
			String validateResult = validateAuthentication();
			if(validateResult == null){
				if(Constants.PERSONNEL_LOGIN.equalsIgnoreCase(userType)){
					String lockCode = DisposeParameter.getStringValueFromTag(queryStrings, "lockCode");
					String lockName = DisposeParameter.getStringValueFromTag(queryStrings, "lockName");
					String note = DisposeParameter.getStringValueFromTag(queryStrings, "note");
					if(StringUtil.isNotBlank(lockCode) && StringUtil.isNotBlank(lockName)){
						DevLockInfo dbData = devLockInfoService.getDevLockInfoByLockCode(lockCode);
						if(dbData == null){
							System.out.println("5");
							DevLockInfo devLockInfo = new DevLockInfo();
							devLockInfo.setLockCode(lockCode.toUpperCase());
							devLockInfo.setLockName(lockName);
							devLockInfo.setLockType(1);
							devLockInfo.setOrgId(orgId);
							devLockInfo.setStatus("0");
							devLockInfo.setNote(note);
							devLockInfoService.saveDevLockInfo(devLockInfo);
							resultCode = ReturnCodeUtil.SUCCESS_RESULT_CODE;
						}else{
							resultMessage = "该锁芯编码已存在系统中！";
						}
					}else{
						resultMessage = "提交信息不全，请重新录入！";
					}
				}else{
					resultMessage = "您没有权限操作此功能！";
				}
			}else{
				resultMessage = validateResult;
			}
		}catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			responseString = ReturnCodeUtil.success(resultCode, resultMessage);
		}
		return SUCCESS;
	}

	/** 注销，清除客户端令牌 */
	@Action(value="removeAuthentication")
	@ToJson(outField="responseString")
	public String removeAuthentication(){
		if(StringUtil.isNotEmpty(loginUser)){
			AuthenticationManager.getInstance().removeAuthentication(loginUser);
			removeLoginUserSession();
			responseString = ReturnCodeUtil.success(ReturnCodeUtil.SUCCESS_RESULT_CODE);
		}else{
			responseString = ReturnCodeUtil.success(ReturnCodeUtil.ERROR_RESULT_CODE, "注销失败！");
		}
		return SUCCESS;
	}
	
   /* @After  
    public void annoAfter(){  
        System.out.println("方法执行后");  
    }  
    @BeforeResult  
    public void annoBeforeResult(){  
        System.out.println("返回result之前执行");  
    } */
}
