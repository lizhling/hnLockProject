package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.SysBasicData;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysBasicDataDto;

/** 
 * @ClassName SysBasicDataService.java
 * @Author WangXiangBo 
 */
@Service
public interface SysBasicDataService {
	/**
	 * 查询满足条件的基础数据列表，带分页
	 */
	public DataPackage findPageSysBasicData(SysBasicDataDto dto,DataPackage dataPackage);
	
	/**
	 * 查询满足条件的基础数据List
	 */
	public List<SysBasicData> findSysBasicDataList(SysBasicDataDto dto);
	
	/**
	 * 查询基础数据类型 下拉框使用
	 */
	public List findSysBasicDataCombobox(SysBasicDataDto dto);
	
	/**
	 * 根据ID获取基础数据信息
	 */
	public SysBasicData getById(Long id);
	
	/**
	 * 保存基础数据信息
	 */
	public void saveSysBasicData(List<SysBasicData> sbd);
	
	/**
	 * 批量删除基础数据信息
	 */
	public String deleteSysBasicDataByIds(String ids)throws Exception;
	
}
