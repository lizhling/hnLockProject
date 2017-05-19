package com.hnctdz.aiLock.dao.info;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.info.UpdateAuthorize;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.info.UpdateAuthorizeDto;

/** 
 * @ClassName UpdateAuthorizeDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface UpdateAuthorizeDao extends GenericDAO<UpdateAuthorize, Long>{
	
	public DataPackage findPageUpdateAuthorize(UpdateAuthorizeDto dto,DataPackage dp);
	
	public List<UpdateAuthorize> findUpdateAuthorizeList(UpdateAuthorizeDto dto);
	
	public Boolean UpdateAuthorizeToChange(Long perId);
	
	public void UpdateAuthorizeToUpdate(Long perId);
	
	public String deleteUpdateAuthorizeByIds(String roleIds);
}
