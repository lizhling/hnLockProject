package com.hnctdz.aiLockdm.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.hnctdz.aiLockdm.dao.DevLockInfoDao;
import com.hnctdz.aiLockdm.utils.ErrorCodeException;

/** 
 * @ClassName DevLockInfoDaoImpl.java
 * @Author WangXiangBo 
 */
public class DevLockInfoDaoImpl extends HibernateDaoSupport implements DevLockInfoDao{
	
	public List findActiveLockByModuleCode(String moduleCode) throws ErrorCodeException{
		Session session = null;
		try{
			session = getSession();
			String sql = "select LOCK_DEVICE_NO, WHE_CAN_MATCH_CARD from DEV_LOCK_INFO d where LOCK_IN_MODULE_CODE = '"+moduleCode+"'";
			return session.createSQLQuery(sql).list();
        } catch (Exception e) {
        	throw new ErrorCodeException(e.getMessage());
		}finally{
			if(session != null){
				session.close();
			}
		}
	}
}
