package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.SysRoleRes;

/** 
 * @ClassName SysRoleUseResDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface SysRoleResDao extends GenericDAO<SysRoleRes, Long>{
	
	public void saveOfBatch(List<SysRoleRes> list);
	
	public void deleteOfBatch(List<SysRoleRes> list);
}
