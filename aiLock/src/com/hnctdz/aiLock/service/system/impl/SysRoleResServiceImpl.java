package com.hnctdz.aiLock.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.SysRoleResDao;
import com.hnctdz.aiLock.domain.system.SysRoleRes;
import com.hnctdz.aiLock.domain.system.SysRoleResId;
import com.hnctdz.aiLock.service.system.SysRoleResService;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName SysRoleResServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.system.SysRoleResService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class SysRoleResServiceImpl implements SysRoleResService{
	
	@Autowired
	private SysRoleResDao sysRoleResDao;
	
	public List<SysRoleRes> findSysResByRole(Long roleId){
		SimpleExpression jobIdEQ = Restrictions.eq("frwSysRole.roleId", roleId);
		return sysRoleResDao.findByCriteria(jobIdEQ);
	}
	
	public boolean saveSysRoleRes(Long roleId, String resIds)throws Exception {
		boolean flag = false;
		try {
			//存放页面传送过来的对象
			List<SysRoleRes> sysRoleResList = new ArrayList<SysRoleRes>();
			//存放用于删除的对象
			List<SysRoleRes> sysRoleResListForDel = new ArrayList<SysRoleRes>();
			//存放用于新增的对象
			List<SysRoleRes> sysRoleResListForSave = new ArrayList<SysRoleRes>();
			//获取当前角色ID下的对象
			List<SysRoleRes> currentSysRoleResList = findSysResByRole(roleId);
			
			SysRoleRes SysRoleRes = null;
			SysRoleResId id = null;
			if(StringUtil.isNotEmpty(resIds)){//前台传过来的是空对象
				String[] serviceIdArray = resIds.split(",");
				for(String serviceId : serviceIdArray){//将前台传过来的业务ID查找出来，全都添加到sysRoleResList
					Long sId = Long.parseLong(serviceId);
					SysRoleRes = new SysRoleRes();
					id = new SysRoleResId();
					id.setResId(sId);
					id.setRoleId(roleId);
					SysRoleRes.setId(id);
					sysRoleResList.add(SysRoleRes);
				}
				
				for(SysRoleRes bs : sysRoleResList){
					if (currentSysRoleResList == null
							|| currentSysRoleResList.size() == 0) {//当前数据库中没有记录，则全部为添加操作
						sysRoleResListForSave.add(bs);
					} else {
						int i = 1;
						for(SysRoleRes cbs : currentSysRoleResList){
							
							SysRoleResId bsId = bs.getId();
							SysRoleResId cbsId = cbs.getId();
							//判断前端传过来的对象与数据库中的对象是否相同
							if(bsId.getResId().equals(cbsId.getResId()) && 
									bsId.getRoleId().equals(cbsId.getRoleId())){
								break;
							}
							if(i == currentSysRoleResList.size()){
								sysRoleResListForSave.add(bs);
							}
							i++;
						}
					}
				}
				
				//防止出现不一致的读
				//this.SysRoleResDao.flushAndClearCurrentSession();
				
				if(sysRoleResListForSave != null && sysRoleResListForSave.size() != 0){
//					System.out.println("需要保存的对象为："+sysRoleResListForSave.size());
					this.sysRoleResDao.saveOfBatch(sysRoleResListForSave);
				}
				
				for(SysRoleRes cbs : currentSysRoleResList){
					boolean bl = true;
					for(SysRoleRes bs :sysRoleResList){
						if(bs.getId().getResId().equals(cbs.getId().getResId()) && 
								bs.getId().getRoleId().equals(cbs.getId().getRoleId())){
							bl = false;
							break;
						}
					}
					if(bl){
						sysRoleResListForDel.add(cbs);
					}
				}
				if(sysRoleResListForDel != null && sysRoleResListForDel.size() != 0){
//					System.out.println("需要删除的对象为："+sysRoleResListForDel.size());
					this.sysRoleResDao.deleteOfBatch(sysRoleResListForDel);
				}
			} else {//删除当前数据库中的所有对象
				if(currentSysRoleResList != null && currentSysRoleResList.size() != 0){
//					System.out.println("删除当前数据库中的所有对象");
					this.sysRoleResDao.deleteOfBatch(currentSysRoleResList);
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
