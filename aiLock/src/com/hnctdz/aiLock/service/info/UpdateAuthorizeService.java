package com.hnctdz.aiLock.service.info;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.info.UpdateAuthorize;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.info.UpdateAuthorizeDto;

/** 
 * @ClassName UpdateAuthorizeService.java
 * @Author WangXiangBo 
 */
@Service
public interface UpdateAuthorizeService {
	
	/**
	 * 查询满足条件的分组列表，带分页
	 */
	public DataPackage findPageUpdateAuthorize(UpdateAuthorizeDto dto, DataPackage dp);
	
	public UpdateAuthorize getById(Long groupId);
	
	public List<UpdateAuthorize> findUpdateAuthorizeList(UpdateAuthorizeDto dto);
	
	public int validationUpdateAuthorize(UpdateAuthorizeDto dto);
	
	/**
	 * 保存分组信息
	 */
	public void saveUpdateAuthorize(UpdateAuthorize UpdateAuthorize);
	
	/**
	 * 删除分组信息
	 */
	public String deleteUpdateAuthorizeByIds(String roleIds);
	
	
}
