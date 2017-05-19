package com.hnctdz.aiLock.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.SysUserRoleDao;
import com.hnctdz.aiLock.domain.system.SysUserRole;

/** 
 * @ClassName SysUserRoleDaoImpl.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@Repository("SysUserRoleDao")
public class SysUserRoleDaoImpl extends GenericDaoImpl<SysUserRole, Long> implements SysUserRoleDao{
	
	public List<SysUserRole> findRoleByUserId(Long userId){
		Map<String, Object> proMap = new HashMap<String, Object>();
		String hql = " from SysUserRole where sysUser.userId = :userId";
		proMap.put("userId", userId);
		return findAllByHQL(hql, proMap);
	}
	
	public String findOrgPermissionByUserOrg(Long orgId){
//		String sql =" select distinct t.org_id from ORG_INFO t"+
//				    " start with t.org_id = "+ orgId+
//				    " connect by t.org_parent_id = prior t.org_id";
		String sql ="select getChildLst_org("+orgId+")";
		List list = findAllBySQL(sql);
		String orgIds = list.get(0).toString();
		return orgIds.replace("$,","");
	}
	
	public void saveOfBatch(List<SysUserRole> businessServiceListForSave) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		int i = 1;
		for(SysUserRole res : businessServiceListForSave){
			session.save(res);
			if(i == 30){
				session.flush();
				session.clear();
			}
			i++ ;
		}
	}

	public void deleteOfBatch(List<SysUserRole> businessServiceListForDel) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		int i = 1;
		for(SysUserRole res : businessServiceListForDel){
			session.delete(res);
			if(i == 30){
				session.flush();
				session.clear();
			}
			i++ ;
		}
	}
	
}
