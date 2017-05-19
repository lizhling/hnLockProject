package com.hnctdz.aiLock.dao.info.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.info.PersonnelInfoDao;
import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.info.PersonnelInfoDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName PersonnelInfoDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("PersonnelInfoDao")
public class PersonnelInfoDaoImpl extends GenericDaoImpl<PersonnelInfo, Long> implements PersonnelInfoDao {
private Map<String, Object> proMap;
	
	public String queryConditions(PersonnelInfoDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getPerAccounts())){
				conSql.append(" and perAccounts = :perAccounts");
				proMap.put("perAccounts", dto.getPerAccounts());
			}
			if(StringUtil.isNotEmpty(dto.getPerPassword())){
				conSql.append(" and perPassword = :perPassword");
				proMap.put("perPassword", dto.getPerPassword());
			}
			if(StringUtil.isNotEmpty(dto.getPerName())){
				conSql.append(" and perName like :perName");
				proMap.put("perName", "%"+dto.getPerName()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getPhoneNo())){
				conSql.append(" and phoneNo = :phoneNo");
				proMap.put("phoneNo", dto.getPhoneNo());
			}
			if(StringUtil.isNotEmpty(dto.getSmartKeyPerId())){
				conSql.append(" and smartKeyPerId = :smartKeyPerId");
				proMap.put("smartKeyPerId", dto.getSmartKeyPerId());
			}
			if(null != dto.getStatus()){
				conSql.append(" and status = :status");
				proMap.put("status", dto.getStatus());
			}
			if(null!= dto.getPerIds()&&StringUtil.isNotBlank(dto.getPerIds())){
				conSql.append(" and perId in("+dto.getPerIds()+")");
			}
		}
		if(dto != null && StringUtil.isNotBlank(dto.getOrgIds())){
			conSql.append(" and orgId in("+dto.getOrgIds()+")");
		}else if(dto != null && dto.getOrgId() != null){
			String sql ="select getChildLst_org("+dto.getOrgId()+")";
			List list = findAllBySQL(sql);
			String orgIds = list.get(0).toString();
			
			conSql.append(" and orgId in("+orgIds+")");
		}
		else{
			conSql.append(this.addOrgPermissionHql());//添加登录人所在组织权限
		}
		return conSql.toString();
	}
	
	public DataPackage findPagePersonnelInfo(PersonnelInfoDto dto, DataPackage dp){
		String hql = "from PersonnelInfo where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<PersonnelInfo> findPersonnelInfoList(PersonnelInfoDto dto){
		String hql = "from PersonnelInfo where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
	
	public PersonnelInfo getPersonnelInfoByKeyCode(String keyCode){
		String hql = "from PersonnelInfo p where p.perId in(select d.perId from DevKeyInfo d where d.keyCode = :keyCode)";
		proMap = new HashMap<String, Object>();
		proMap.put("keyCode", keyCode);
		List<PersonnelInfo> list = findAllByHQL(hql, proMap);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public List findPersonnelInfoOptions(PersonnelInfoDto dto){
		String hql = "select perId, perName from PersonnelInfo where 1=1 " + queryConditions(dto) + " order by status, perName";
		return findAllByHQL(hql, proMap);
	}
	public List<PersonnelInfo> getPersonnelInfos(PersonnelInfoDto dto) {
//		String hql = "select perId,perAccounts,perPassword,perName,phoneNo,address,orgId,status,note,smartKeyPerId,smartKeyPassw,cuTime " +
		String hql = "from PersonnelInfo where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
		}
	
	public PersonnelInfo getPersonnelByAccounts(String perAccounts){
		String hql = "from PersonnelInfo where perAccounts = :perAccounts";
		proMap = new HashMap<String, Object>();
		proMap.put("perAccounts", perAccounts);
		List<PersonnelInfo> list = findAllByHQL(hql, proMap);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public List findAccountBySysUserAndPer(String account){
		String sql = "SELECT 1, s.user_id, s.user_name FROM sys_user s where s.user_name = :userName" +
			   " UNION SELECT 2, p.per_id, p.per_accounts FROM personnel_info p where p.per_accounts = :perAccounts";
		proMap = new HashMap<String, Object>();
		proMap.put("userName", account);
		proMap.put("perAccounts", account);
		return findAllBySQL(sql, proMap);
	}
	
	public void updateResetPerPassword(Long perId, String password){
		String hql = "update PersonnelInfo set perPassword = '" +password+ "' where perId = "+ perId;
		bulkUpdate(hql);
	}
	
	public String getMaxSmartKeyPerId(){
		String sql = "SELECT max(smart_key_per_id) FROM personnel_info";
		List list = findAllBySQL(sql);
		if(list.get(0) != null){
			return list.get(0).toString();
		}
		return null;
	}
}
