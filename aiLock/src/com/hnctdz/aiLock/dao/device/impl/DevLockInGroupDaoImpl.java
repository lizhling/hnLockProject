package com.hnctdz.aiLock.dao.device.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.device.DevLockInGroupDao;
import com.hnctdz.aiLock.domain.device.DevLockInGroup;

/** 
 * @ClassName DevLockInGroupDaoImpl.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@Repository("DevLockInGroupDao")
public class DevLockInGroupDaoImpl extends GenericDaoImpl<DevLockInGroup, Long> implements DevLockInGroupDao{
	
	public List<DevLockInGroup> findDevLockByGroupId(Long groupId){
		Map<String, Object> proMap = new HashMap<String, Object>();
		String hql = " from DevLockInGroup where id.groupId = :groupId";
		proMap.put("groupId", groupId);
		return findAllByHQL(hql, proMap);
	}
	
	public void saveOfBatch(List<DevLockInGroup> listForSave) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		int i = 1;
		for(DevLockInGroup lig : listForSave){
			session.save(lig);
			if(i == 30){
				session.flush();
				session.clear();
			}
			i++ ;
		}
	}

	public void deleteOfBatch(List<DevLockInGroup> listForDel) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		int i = 1;
		for(DevLockInGroup lig : listForDel){
			session.delete(lig);
			if(i == 30){
				session.flush();
				session.clear();
			}
			i++ ;
		}
	}
	
}
