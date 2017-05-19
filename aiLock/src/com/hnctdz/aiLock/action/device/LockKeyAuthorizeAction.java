package com.hnctdz.aiLock.action.device;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.domain.device.LockKeyAuthorize;
import com.hnctdz.aiLock.dto.device.LockKeyAuthorizeDto;
import com.hnctdz.aiLock.security.SecurityUserHolder;
import com.hnctdz.aiLock.service.device.DevLockInfoService;
import com.hnctdz.aiLock.service.device.LockKeyAuthorizeService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName LockKeyAuthorizeAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/LockKeyAuthorizeAction")
@Controller
public class LockKeyAuthorizeAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(LockKeyAuthorizeAction.class);  
	
 	@Autowired
 	private LockKeyAuthorizeService lockKeyAuthorizeService;
 	@Autowired
 	private DevLockInfoService devLockInfoService;
 	
 	private LockKeyAuthorize lockKeyAut;
 	private LockKeyAuthorizeDto dto;
 	private String authorizeIds;
 	
 	/**
	 * 查询授权信息列表
	 */
	@Action(value="findPageLockKeyAuthorize")
	@ToJson(outField="dataPackage")
	public String findPageLockKeyAuthorize(){
		dataPackage = getDataPackage();
		try {
			dataPackage = lockKeyAuthorizeService.findPageLockKeyAuthorize(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存授权信息信息
	 */
	@Action(value="saveLockKeyAuthorize")
	@ToJson(outField="simpleRespose")
	public String saveLockKeyAuthorize(){
		String result = null;
		try{
			lockKeyAut.setCuUserId(SecurityUserHolder.getCurrentUser().getUserId());
			lockKeyAut.setCuTime(new Date());
			lockKeyAut.setAuthorizeType(Constants.AUTHORIZE_MANAGE);
			result = lockKeyAuthorizeService.saveLockKeyAuthorize(lockKeyAut);
		} catch(ErrorCodeException e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			result = e.getMessage();
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			result = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			if(StringUtil.isNotEmpty(result)){
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage(result);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 查询授权门锁列表
	 */
	@Action(value="findAuthorizeLockList")
	@ToJson(outField="dataPackage")
	public String findAuthorizeLockList(){
		dataPackage = getDataPackage();
		try {
			String authorizeCode = request.getParameter("authorizeCode");
			List<DevLockInfo> list = (List<DevLockInfo>)lockKeyAuthorizeService.findAuthorizeLockList(authorizeCode);
			for(DevLockInfo lock : list){
				if(null != lock.getLockParentId()){
					lock.setLockName("(无源)"+lock.getLockName());
				}
			}
			dataPackage.setRows(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}

	/**
	 * 查询授权人员列表
	 */
	@Action(value="findAuthorizePerList")
	@ToJson(outField="dataPackage")
	public String findAuthorizePerList(){
		dataPackage = getDataPackage();
		try {
			String authorizeCode = request.getParameter("authorizeCode");
			dataPackage.setRows(lockKeyAuthorizeService.findAuthorizePerList(authorizeCode));
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除授权信息信息
	 */
	@Action(value="deleteLockKeyAuthorize")
	@ToJson(outField="simpleRespose")
	public String deleteLockKeyAuthorize(){
		String delResult = "";
		try{
			String[] authorizeId = authorizeIds.split(",");
			String delAutIds = "", f = "", d = "";
			for(int i=0; i < authorizeId.length; i++){
				LockKeyAuthorize lka = lockKeyAuthorizeService.getById(Long.parseLong(authorizeId[i]));
				DevLockInfo lock = devLockInfoService.getById(lka.getLockId());
				boolean delAuthBl = true;
				if(lock!=null){
					if(Constants.ELECTRIC_LOCK.equals(lock.getLockType()) && 
							lock.getWheCanMatchCard() == Constants.CAN_MATCH_CARD){
						try{
							//删除有源门锁中的钥匙开门权限
							delAuthBl = lockKeyAuthorizeService.deleteCardPer(lka, null);
						}catch(Exception e) {
							delAuthBl = false;
						}
					}
				}
				if(delAuthBl){
					delAutIds += f + lka.getAuthorizeId();
					f = ",";
				}else{
					delResult += d + lka.getAuthorizeCode();
					d = "、";
				}
			}
			if(StringUtil.isNotEmpty(delAutIds)){
				LockKeyAuthorizeDto dto = new LockKeyAuthorizeDto();
				dto.setAuthorizeIds(delAutIds);
				lockKeyAuthorizeService.deleteAuthorize(dto);
			}
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}finally{
			if(StringUtil.isNotEmpty(delResult)){
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage("删除的授权中批次码为："+delResult+"未完全清除门锁内权限，删除失败！");
			}
		}
		return SUCCESS;
	}
	
	public LockKeyAuthorizeDto getDto() {
		return dto;
	}

	public void setDto(LockKeyAuthorizeDto dto) {
		this.dto = dto;
	}

	public LockKeyAuthorize getLockKeyAut() {
		return lockKeyAut;
	}

	public void setLockKeyAut(LockKeyAuthorize lockKeyAut) {
		this.lockKeyAut = lockKeyAut;
	}

	public String getAuthorizeIds() {
		return authorizeIds;
	}

	public void setAuthorizeIds(String authorizeIds) {
		this.authorizeIds = authorizeIds;
	}
}
