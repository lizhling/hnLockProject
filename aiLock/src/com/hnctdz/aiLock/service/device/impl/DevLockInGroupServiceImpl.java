package com.hnctdz.aiLock.service.device.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.device.DevLockInGroupDao;
import com.hnctdz.aiLock.domain.device.DevLockInGroup;
import com.hnctdz.aiLock.domain.device.DevLockInGroupId;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevLockInGroupDto;
import com.hnctdz.aiLock.service.device.DevLockInGroupService;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName DevLockInGroupServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.device.DevLockInGroupService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class DevLockInGroupServiceImpl implements DevLockInGroupService{
	@Autowired
	private DevLockInGroupDao dao;
	
	public DataPackage findPageDevLockInGroup(DevLockInGroupDto dto, DataPackage dp){
		return null;
	}
	
	public List<DevLockInGroup> findDevLockByGroupId(Long groupId){
		return dao.findDevLockByGroupId(groupId);
	}
	
	public boolean saveDevLockInGroup(Long groupId, String lockIds)throws Exception {
		boolean flag = false;
		try {
			//存放页面传送过来的对象
			List<DevLockInGroup> devLockInGroupList = new ArrayList<DevLockInGroup>();
			//存放用于删除的对象
			List<DevLockInGroup> devLockInGroupListForDel = new ArrayList<DevLockInGroup>();
			//存放用于新增的对象
			List<DevLockInGroup> devLockInGroupListForSave = new ArrayList<DevLockInGroup>();
			//获取当前分组中的门锁对象
			List<DevLockInGroup> currentDevLockInGroupList = findDevLockByGroupId(groupId);
			
			
			DevLockInGroup devLockInGroup = null;
			DevLockInGroupId id = null;
			if(StringUtil.isNotEmpty(lockIds)){//前台传过来的是空对象
				String[] lockIdArray = lockIds.split(",");
				for(String lockId : lockIdArray){//将前台传过来的业务ID查找出来，全都添加到DevLockInGroupList
					devLockInGroup = new DevLockInGroup();
					id = new DevLockInGroupId();
					id.setGroupId(groupId);
					id.setLockId(Long.parseLong(lockId));
					devLockInGroup.setId(id);
					devLockInGroupList.add(devLockInGroup);
				}
				for(DevLockInGroup bs : devLockInGroupList){
					//当前数据库中没有记录，则全部为添加操作
					if (currentDevLockInGroupList == null || currentDevLockInGroupList.size() == 0) {
						devLockInGroupListForSave.add(bs);
					} else {
						boolean bl = true;
						for(DevLockInGroup cbs : currentDevLockInGroupList){
							DevLockInGroupId bsId = bs.getId();
							DevLockInGroupId cbsId = cbs.getId();
							//判断前端传过来的对象与数据库中的对象是否相同
							if(bsId.getLockId().equals(cbsId.getLockId())){
								bl = false;
								break;
							}
						}
						if(bl){
							devLockInGroupListForSave.add(bs);
						}
					}
				}
				
				//防止出现不一致的读
				//this.dao.flushAndClearCurrentSession();
				
				if(devLockInGroupListForSave != null && devLockInGroupListForSave.size() != 0){
					this.dao.saveOfBatch(devLockInGroupListForSave);
				}
				
				for(DevLockInGroup cbs : currentDevLockInGroupList){
					boolean bl = true;
					for(DevLockInGroup bs :devLockInGroupList){
						if(bs.getId().getLockId().equals(cbs.getId().getLockId())){
							bl = false;
							break;
						}
					}
					if(bl){
						devLockInGroupListForDel.add(cbs);
					}
				}
				
				if(devLockInGroupListForDel != null && devLockInGroupListForDel.size() != 0){
					this.dao.deleteOfBatch(devLockInGroupListForDel);
				}
			} else {//删除当前数据库中的所有对象
				if(currentDevLockInGroupList != null && currentDevLockInGroupList.size() != 0){
					this.dao.deleteOfBatch(currentDevLockInGroupList);
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
