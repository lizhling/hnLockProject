package com.hnctdz.aiLock.dao.device.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.device.DevKeyInfoDao;
import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.dto.Combobox;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyInfoDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName DevKeyInfoDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("DevKeyInfoDao")
public class DevKeyInfoDaoImpl extends GenericDaoImpl<DevKeyInfo, Long> implements DevKeyInfoDao {
	private Map<String, Object> proMap;
	
	public String queryConditions(DevKeyInfoDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getKeyCode())){
				conSql.append(" and keyCode = :keyCode");
				proMap.put("keyCode", dto.getKeyCode());
			}
			if(StringUtil.isNotEmpty(dto.getKeyName())){
				conSql.append(" and keyName like :keyName");
				proMap.put("keyName", "%"+dto.getKeyName()+"%");
			}
			if(null != dto.getPerId()){
				conSql.append(" and perId = :perId)");
				proMap.put("perId", dto.getPerId());
			}
			if(null != dto.getKeyType()){
				conSql.append(" and keyType = :keyType)");
				proMap.put("keyType", dto.getKeyType());
			}
			if(StringUtil.isNotBlank(dto.getOrgids())){
				conSql.append(" and orgId in("+dto.getOrgids()+")");
			}else if(null != dto.getOrgId()){
				String sql ="select getChildLst_org("+dto.getOrgId()+")";
				List list = findAllBySQL(sql);
				String orgIds = list.get(0).toString();
				
				conSql.append(" and orgId in("+orgIds+")");
				
//				conSql.append(" and orgId = :orgId");
//				proMap.put("orgId", dto.getOrgId());
			}
			if(null != dto.getStatus()){
				conSql.append(" and status = :status");
				proMap.put("status", dto.getStatus());
			}
		}
		conSql.append(this.addOrgPermissionHql());//添加登录人所在组织权限
		return conSql.toString();
	}
	
	public DataPackage findPageDevKeyInfo(DevKeyInfoDto dto, DataPackage dp){
		String hql = "from DevKeyInfo where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<DevKeyInfo> findDevKeyInfoList(DevKeyInfoDto dto){
		String hql = "from DevKeyInfo where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
	
	public DevKeyInfo getDevKeyInfoByKeyCode(String keyCode){
		if(StringUtil.isNotEmpty(keyCode)){
			String hql = "from DevKeyInfo where keyCode = :keyCode";
			proMap = new HashMap<String, Object>();
			proMap.put("keyCode", keyCode);
			List<DevKeyInfo> list = findAllByHQL(hql, proMap);
			if(list.size() > 0){
				return list.get(0);
			}
		}
		return null;
	}
	
	public List findKeyInfoOptions(DevKeyInfoDto dto){
		String sql = "select key_Code value, key_Name || '(' || key_Code || ')' text from Dev_Key_Info where 1=1 ";
		return findAllBySQL(sql, proMap, Combobox.class);
	}
	
	public List getSecretKeyToDevkey(String devKeyCode){
		String sql = "select GROUP_SECRET_KEY from DEV_KEY_GROUP where GROUP_ID = (select GROUP_ID from Dev_Key_Info where key_Code = :keyCode)";
		proMap = new HashMap<String, Object>();
		proMap.put("keyCode", devKeyCode);
		return findAllBySQL(sql);
	}
}
