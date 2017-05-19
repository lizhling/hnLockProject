package com.hnctdz.aiLock.service.device;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevLockInfoDto;
import com.hnctdz.aiLock.utils.ErrorCodeException;

/** 
 * @ClassName DevLockInfoService.java
 * @Author WangXiangBo 
 */
@Service
public interface DevLockInfoService {
	
	public DataPackage findPageDevLockInfo(DevLockInfoDto dto, DataPackage dp);
	
	public DevLockInfo getById(Integer lockId);
	
	public List<DevLockInfo> findDevLockInfoList(DevLockInfoDto dto);
	
	public List<DevLockInfo> findNormalLockInfoList(DevLockInfoDto dto);
	
	public List findNormalLockListByCombobox();
	
	public List findAreaStatisticalLock(DevLockInfoDto dto);
	
	public boolean checkLocExist(DevLockInfo lockInfo);
	
	public void saveDevLockInfo(DevLockInfo devLockInfo) throws ErrorCodeException;
	
	public String saveImportLockInfos(File importFile);
	
	public String deleteDevLockInfoByIds(String lockIds);
	
	public String deleteRealDevLockInfoByIds(String lockIds);
	
	public List<DevLockInfoDto> findPersonnelMaitLockList(DevLockInfoDto dto);
	
	public DevLockInfo getDevLockInfoByLockCode(String lockCode);

	/**
	 * @param dto（输入参数_主要获取areaId)
	 * @return  返回该区域的所有锁信息统计数据
	 */
	public String getLockCountInfo(DevLockInfoDto dto);

	/**
	 * @param response 
	 * @param request 
	 * @param dto（lockinfoDto 输入参数)
	 * 执行锁信息导出功能
	 */
	public void exportLockInfo(HttpServletRequest request, HttpServletResponse response, DevLockInfoDto dto);

	/**
	 * 检查蓝牙名称是否存在
	 * @param lockInfo
	 * @return
	 */
	public boolean checkBluetoothExist(DevLockInfo lockInfo);
}
