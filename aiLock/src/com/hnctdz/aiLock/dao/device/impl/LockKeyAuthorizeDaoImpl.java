package com.hnctdz.aiLock.dao.device.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.device.LockKeyAuthorizeDao;
import com.hnctdz.aiLock.domain.device.LockKeyAuthorize;
import com.hnctdz.aiLock.dto.Combobox;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.LockKeyAuthorizeDto;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName LockKeyAuthorizeDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("LockKeyAuthorizeDao")
public class LockKeyAuthorizeDaoImpl extends GenericDaoImpl<LockKeyAuthorize, Long> implements LockKeyAuthorizeDao {
private Map<String, Object> proMap;
	
	public String queryConditions(LockKeyAuthorizeDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		boolean isAddPermissions = true;
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getAuthorizeCode())){
				conSql.append(" and authorizeCode like :authorizeCode");
				proMap.put("authorizeCode", "%"+dto.getAuthorizeCode()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getKeyCode())){
				conSql.append(" and keyCode like :keyCode");
				proMap.put("keyCode", "%"+dto.getKeyCode()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getKeyName())){
				conSql.append(" and keyName like :keyName");
				proMap.put("keyName", "%"+dto.getKeyName()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getStatusCode())){
				conSql.append(" and statusCode  = :statusCode");
				proMap.put("statusCode", dto.getStatusCode());
			}
			if(null != dto.getLockId()){
				conSql.append(" lockId = :lockId");
				proMap.put("lockId", dto.getLockId());
				isAddPermissions = false;
			}
			if(null != dto.getCuUserId()){
				conSql.append(" and cuUserId = :cuUserId");
				proMap.put("cuUserId", dto.getCuUserId());
				isAddPermissions = false;
			}
			if(null != dto.getUnlockPerId()){
				conSql.append(" and unlockPerId  = :unlockPerId");
				proMap.put("unlockPerId", dto.getUnlockPerId());
				isAddPermissions = false;
			}
			if(StringUtil.isNotEmpty(dto.getLockCode())){
				conSql.append(" and lk.lockId in (select dl.lockId from DevLockInfo dl where dl.lockCode like :lockCode)");
				proMap.put("lockCode", "%"+dto.getLockCode()+"%");
				isAddPermissions = false;
			}
			if(StringUtil.isNotEmpty(dto.getLockName())){
				conSql.append(" and lk.lockId in (select s.lockId from DevLockInfo s where s.lockName like :lockName)");
				proMap.put("lockName", "%"+dto.getLockName()+"%");
//				isAddPermissions = false;    //修改此项为限定用户组织结构来查询授权信息
			}
		}
		
		if(isAddPermissions){//添加登录人所在组织权限
			String orgSql = this.addOrgPermissionHql("d");
			if(StringUtil.isNotBlank(orgSql)){
				conSql.append(" and lk.lockId in (select d.lockId from DevLockInfo d where 1 = 1 "+orgSql+")");
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageLockKeyAuthorize(LockKeyAuthorizeDto dto, DataPackage dp){
		String hql = "from LockKeyAuthorize lk where 1=1 " + queryConditions(dto) + " order by endTime desc, authorizeCode desc";
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<LockKeyAuthorize> findLockKeyAuthorizeList(LockKeyAuthorizeDto dto){
		String hql = "from LockKeyAuthorize lk where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
	
	public List<LockKeyAuthorize> findLockKeyAuthorizeByPerId(Long perId){
		String hql = "from LockKeyAuthorize a where a.startTime <= :sysTime" +
				" and a.endTime >= :sysTime and a.statusCode = :statusCode and a.unlockPerId = :unlockPerId)";
		proMap = new HashMap<String, Object>();
		proMap.put("sysTime", new Date());
		proMap.put("statusCode", "01");
		proMap.put("unlockPerId", perId);
		return findAllByHQL(hql, proMap);
	}
	
	public List<LockKeyAuthorize> findLockKeyAuthorizeByCode(String authorizeCode){
		proMap = new HashMap<String, Object>();
		String hql = "from LockKeyAuthorize where authorizeCode = :authorizeCode";
		proMap.put("authorizeCode", authorizeCode);
		return findAllByHQL(hql, proMap);
	}
	
	public List findAuthorizeLockList(String authorizeCode){
		String hql = "from DevLockInfo where lockId in(select l.lockId from LockKeyAuthorize l where l.authorizeCode = :authorizeCode)";
		proMap = new HashMap<String, Object>();
		proMap.put("authorizeCode", authorizeCode);
		return findAllByHQL(hql, proMap);
	}
	
	public List findAuthorizePerList(String authorizeCode){
		String sql = "select CONCAT(per_id,'') as 'value', per_name as 'text' from personnel_info where per_id in(select l.unlock_Per_Id from LOCK_KEY_AUTHORIZE l where l.authorize_code = :authorizeCode)";
		proMap = new HashMap<String, Object>();
		proMap.put("authorizeCode", authorizeCode);
		return findAllBySQL(sql, proMap, Combobox.class);
	}
	
	public void saveLka(LockKeyAuthorize lockKeyAuthorize) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		session.save(lockKeyAuthorize);
		session.flush();
		session.clear();
	}
	
	public void updateLka(LockKeyAuthorize lockKeyAuthorize){
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		session.update(lockKeyAuthorize);
		session.flush();
		session.clear();
	}
	
	public void deleteOfBatch(List<LockKeyAuthorize> listForDel) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		int i = 1;
		for(LockKeyAuthorize lka : listForDel){
			session.delete(lka);
			if(i == 30){
				session.flush();
				session.clear();
			}
			i++ ;
		}
	}
	
	public void updateAuthorizeIds(LockKeyAuthorize authorizeInfo, String authorizeIds){
		String updateSql = "update LOCK_KEY_AUTHORIZE set START_TIME = :startTime, END_TIME = :endTime," +
						   " UNLOCK_NUMBER = :unlockNumber, BLUE_UNLOCK = :blueUnlock, AUTHORIZE_TYPE = :authorizeType," +
						   " STATUS_CODE = :statusCode, CU_USER_ID = :cuUserId, CU_TIME = :cuTime, SCOPE_UNLOCK = :scopeUnlock" +
						   " where authorize_code = :authorizeCode and authorize_id in("+authorizeIds+")";

		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(updateSql);
		query.setParameter("startTime",  DateUtil.getDateTime(DateUtil.dateTime, authorizeInfo.getStartTime()));
		query.setParameter("endTime",  DateUtil.getDateTime(DateUtil.dateTime, authorizeInfo.getEndTime()));
		query.setParameter("unlockNumber", authorizeInfo.getUnlockNumber());
		query.setParameter("blueUnlock", authorizeInfo.getBlueUnlock());
		query.setParameter("statusCode", authorizeInfo.getStatusCode());
		query.setParameter("cuUserId", authorizeInfo.getCuUserId());
		query.setParameter("cuTime", DateUtil.getDateTime(DateUtil.dateTime, authorizeInfo.getCuTime()));
		query.setParameter("authorizeCode", authorizeInfo.getAuthorizeCode());
		query.setParameter("authorizeType", authorizeInfo.getAuthorizeType());
		query.setParameter("scopeUnlock", authorizeInfo.getScopeUnlock());
//		query.setParameter("authorizeIds", authorizeIds);
		
		query.executeUpdate();
	}
	
	public void deleteAuthorize(LockKeyAuthorizeDto dto){
		String whele = "";
		if(null != dto.getAuthorizeId()){
			whele = " and authorizeId = "+dto.getAuthorizeId();
		}
		if(StringUtil.isNotEmpty(dto.getAuthorizeIds())){
			whele = " and authorizeId in("+dto.getAuthorizeIds()+")";
		}
		if(StringUtil.isNotEmpty(dto.getAuthorizeCode())){
			whele = " and authorizeCode = '"+dto.getAuthorizeCode()+"'";
		}
		if(StringUtil.isNotEmpty(dto.getLockIds())){
			whele = " and lockId in("+dto.getLockIds()+")";
		}
		if(null != dto.getLockId()){
			whele = " and lockId = "+dto.getLockId();
		}
		
		if(StringUtil.isNotEmpty(whele)){
			String hql = " update UpdateAuthorize u set u.changeTime = '"+DateUtil.getDateTime()+ "', u.status = 1" +
						 " where u.perId in(select unlockPerId from LockKeyAuthorize where 1=1 "+whele+")";
			this.bulkUpdate(hql);

			String deleteHql = "delete LockKeyAuthorize where 1=1 " + whele;
			this.bulkUpdate(deleteHql);
		}
	}
}
