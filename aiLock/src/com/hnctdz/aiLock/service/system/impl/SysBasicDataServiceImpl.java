package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.SysBasicDataDao;
import com.hnctdz.aiLock.domain.system.SysBasicData;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysBasicDataDto;
import com.hnctdz.aiLock.service.system.SysBasicDataService;

/** 
 * @ClassName SysBasicDataServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.system.SysBasicDataService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class SysBasicDataServiceImpl implements SysBasicDataService{
	
	@Autowired
	private SysBasicDataDao sysBasicDataDao;
	
	public DataPackage findPageSysBasicData(SysBasicDataDto dto,DataPackage dp){
		return sysBasicDataDao.findPageSysBasicData(dto, dp);
	}
	
	public List<SysBasicData> findSysBasicDataList(SysBasicDataDto dto){
		return sysBasicDataDao.findSysBasicDataList(dto);
	}
	
	public List findSysBasicDataCombobox(SysBasicDataDto dto){
		return sysBasicDataDao.findSysBasicDataCombobox(dto);
	}
	
	public SysBasicData getById(Long id){
		return sysBasicDataDao.getById(id);
	}
	
	public void saveSysBasicData(List<SysBasicData> sbd){
		//保存父类型基础数据
		SysBasicData parentSbd = sbd.get(0);
		sysBasicDataDao.save(parentSbd);
		
		SysBasicDataDto dto = new SysBasicDataDto();
		dto.setParentId(parentSbd.getBasicDataId());
		List<SysBasicData> sonList = findSysBasicDataList(dto);//查询该父类型下的原有子类型
		
		if(sonList != null && sonList.size() > 0){
			for(int i= 0; i < sonList.size(); i++){
				boolean delete = true;
				SysBasicData sysBasicData = null;
				
				for(int j= 1; j < sbd.size(); j++){
					sysBasicData = sbd.get(j);
					//判断原有的子类型是否该删除
					if(sysBasicData != null && sysBasicData.getBasicDataId() != null){
						if(sonList.get(i).getBasicDataId().equals(sysBasicData.getBasicDataId())){
							delete = false;
							break;
						}
					}
				}
				
				if(delete){
					sysBasicDataDao.delete(sonList.get(i));//删除原有子类型
				}else if(sysBasicData != null){
					//不需要删除的子类型 可直接保存
					sysBasicData.setParentId(parentSbd.getBasicDataId());
					sysBasicData.setTypeTag(parentSbd.getTypeTag());
					sysBasicDataDao.merge(sysBasicData);//session对象合并
					sysBasicDataDao.save(sonList.get(i));//保存合并（修改）后对象
					
					//从list中清除，无需再进行多余的循环判断
					sbd.remove(sysBasicData);
				}
			}
		}
		
		//剩余的子类型保存
		for(int i= 1; i < sbd.size(); i++){
			if(sbd.get(i) != null){
				sbd.get(i).setParentId(parentSbd.getBasicDataId());
				sbd.get(i).setTypeTag(parentSbd.getTypeTag());
				sysBasicDataDao.save(sbd.get(i));
			}
		}

	}
	
	public String deleteSysBasicDataByIds(String ids)throws Exception{
		return sysBasicDataDao.deleteSysBasicDataByIds(ids);
	}
}