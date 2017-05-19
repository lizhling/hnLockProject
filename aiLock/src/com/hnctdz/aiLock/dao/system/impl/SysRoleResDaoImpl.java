package com.hnctdz.aiLock.dao.system.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.SysRoleResDao;
import com.hnctdz.aiLock.domain.system.SysRoleRes;

/** 
 * @ClassName SysRoleResDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("SysRoleResDao")
public class SysRoleResDaoImpl extends GenericDaoImpl<SysRoleRes, Long> implements SysRoleResDao{
	
	public void saveOfBatch(List<SysRoleRes> list) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		int i = 1;
		for(SysRoleRes res : list){
			session.save(res);
			if(i == 30){
				session.flush();
				session.clear();
			}
			i++ ;
		}
	}
	
	public void deleteOfBatch(List<SysRoleRes> list) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		int i = 1;
		for(SysRoleRes res : list){
			session.delete(res);
			if(i == 30){
				session.flush();
				session.clear();
			}
			i++ ;
		}
	}
}
