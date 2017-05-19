package com.hnctdz.aiLock.service.info.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.info.UpdateAuthorizeDao;
import com.hnctdz.aiLock.domain.info.UpdateAuthorize;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.info.UpdateAuthorizeDto;
import com.hnctdz.aiLock.service.info.UpdateAuthorizeService;

/** 
 * @ClassName UpdateAuthorizeServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.device.UpdateAuthorizeService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class UpdateAuthorizeServiceImpl implements UpdateAuthorizeService{
	@Autowired
	private UpdateAuthorizeDao dao;
	
	public DataPackage findPageUpdateAuthorize(UpdateAuthorizeDto dto,DataPackage dp){
		return dao.findPageUpdateAuthorize(dto, dp);
	}
	
	public UpdateAuthorize getById(Long groupId){
		return dao.getById(groupId);
	}
	
	public List<UpdateAuthorize> findUpdateAuthorizeList(UpdateAuthorizeDto dto){
		return dao.findUpdateAuthorizeList(dto);
	}
	
	public int validationUpdateAuthorize(UpdateAuthorizeDto dto){
		List<UpdateAuthorize> list = this.findUpdateAuthorizeList(dto);
		if(list.size() > 0){
			UpdateAuthorize ua = list.get(0);
			ua.setStatus(0L);
			ua.setUpdateTime(new Date());
			return 1;
		}
		return 0;
	}
	
	public void saveUpdateAuthorize(UpdateAuthorize UpdateAuthorize){
		dao.save(UpdateAuthorize);
	}
	
	public String deleteUpdateAuthorizeByIds(String roleIds){
		return dao.deleteUpdateAuthorizeByIds(roleIds);
	}
}
