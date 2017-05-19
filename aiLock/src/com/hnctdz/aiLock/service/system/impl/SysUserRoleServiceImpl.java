package com.hnctdz.aiLock.service.system.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.info.OrgInfoDao;
import com.hnctdz.aiLock.dao.system.SysUserRoleDao;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.domain.system.SysUserRole;
import com.hnctdz.aiLock.domain.system.SysUserRoleId;
import com.hnctdz.aiLock.service.system.SysUserRoleService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.ExcelUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName SysUserRoleServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.system.SysUserRoleService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class SysUserRoleServiceImpl implements SysUserRoleService{
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Autowired
 	private OrgInfoDao orgInDao;
	
	public List<SysUserRole> findRoleByUserId(Long userId){
		return sysUserRoleDao.findRoleByUserId(userId);
	}
	
	public String findOrgPermissionByUserOrg(Long orgId){
		return sysUserRoleDao.findOrgPermissionByUserOrg(orgId);
	}
	
	public boolean saveSysUserRole(Long userId, String serviceIds)throws Exception {
		boolean flag = false;
		try {
			//存放页面传送过来的对象
			List<SysUserRole> SysUserRoleList = new ArrayList<SysUserRole>();
			//存放用于删除的对象
			List<SysUserRole> SysUserRoleListForDel = new ArrayList<SysUserRole>();
			//存放用于新增的对象
			List<SysUserRole> SysUserRoleListForSave = new ArrayList<SysUserRole>();
			//获取当前用户ID下的对象
			List<SysUserRole> currentSysUserRoleList = findRoleByUserId(userId);
			
			SysUserRole SysUserRole = null;
			SysUserRoleId id = null;
			if(StringUtil.isNotEmpty(serviceIds)){//前台传过来的是空对象
				String[] serviceIdArray = serviceIds.split(",");
				for(String serviceId : serviceIdArray){//将前台传过来的业务ID查找出来，全都添加到SysUserRoleList
					Long sId = Long.parseLong(serviceId);
					SysUserRole = new SysUserRole();
					id = new SysUserRoleId();
					id.setRoleId(sId);
					id.setUserId(userId);
					SysUserRole.setId(id);
					SysUserRoleList.add(SysUserRole);
				}
				for(SysUserRole bs : SysUserRoleList){
					//当前数据库中没有记录，则全部为添加操作
					if (currentSysUserRoleList == null || currentSysUserRoleList.size() == 0) {
						SysUserRoleListForSave.add(bs);
					} else {
						int i = 1;
						for(SysUserRole cbs : currentSysUserRoleList){
							SysUserRoleId bsId = bs.getId();
							SysUserRoleId cbsId = cbs.getId();
							//判断前端传过来的对象与数据库中的对象是否相同
							if(Long.toString(bsId.getUserId()).equals(Long.toString(cbsId.getUserId())) && 
									Long.toString(bsId.getRoleId()).equals(Long.toString(cbsId.getRoleId()))){
								break;
							}
							if(i == currentSysUserRoleList.size()){
								SysUserRoleListForSave.add(bs);
							}
							i++;
						}
					}
					
				}
				
				if(SysUserRoleListForSave != null && SysUserRoleListForSave.size() != 0){
					this.sysUserRoleDao.saveOfBatch(SysUserRoleListForSave);
				}
				
				for(SysUserRole cbs : currentSysUserRoleList){
					boolean bl = true;
					for(SysUserRole bs :SysUserRoleList){
						if(Long.toString(bs.getId().getUserId()).equals(Long.toString(cbs.getId().getUserId())) && 
								Long.toString(bs.getId().getRoleId()).equals(Long.toString(cbs.getId().getRoleId()))){
							bl = false;
							break;
						}
					}
					if(bl){
						SysUserRoleListForDel.add(cbs);
					}
				}
				if(SysUserRoleListForDel != null && SysUserRoleListForDel.size() != 0){
					this.sysUserRoleDao.deleteOfBatch(SysUserRoleListForDel);
				}
			} else {//删除当前数据库中的所有对象
				if(currentSysUserRoleList != null && currentSysUserRoleList.size() != 0){
					this.sysUserRoleDao.deleteOfBatch(currentSysUserRoleList);
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
		}
	public String importOrgInfos(File importFile) {
		String result = "";
		try {
			InputStream is = new FileInputStream(importFile);
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			HSSFSheet sheet = workbook.getSheetAt(0);//第一个工作表
			
			String dataLack = "";
			String orgNotExist = "";
			String perExist ="";
			String numberFormat = "";
			String dateError = "";
	
			Date date = new Date();
			for (int j = 2; j < sheet.getPhysicalNumberOfRows() + 1; j++) {//从第二行开始读
				try {
					HSSFRow rowline = sheet.getRow(j - 1);
					if(rowline == null) continue;
					String orgId="";
					String orgName="";
					String orgParentId="";
					String userId="";
					String note="";
					String sqlinsert = "insert into ORG_INFO(";
					String values ="values (";
					int param = 0;
					for (int i = 0; i < rowline.getLastCellNum(); i++) {// 循环遍历所有列
						HSSFCell cell = rowline.getCell((short) i); // 取得当前Cell
						String cellvalueStr = null;
						if (cell != null) {
							switch (cell.getCellType()) {// 判断当前Cell的Type
								case HSSFCell.CELL_TYPE_NUMERIC : {// 如果当前Cell的Type为NUMERIC
									Double value = cell.getNumericCellValue();// 取得当前Cell的数值
									BigDecimal bigDecimal = new BigDecimal(value);//格式转换，避免以科学计数法表示
									cellvalueStr = bigDecimal.toString();
									break;
								}
								case HSSFCell.CELL_TYPE_STRING : // 如果当前Cell的Type为STRING
									cellvalueStr = cell.getStringCellValue(); // 取得当前的Cell字符串
									break;
							}
						}
						if(StringUtil.isNotEmpty(cellvalueStr)){
							param++;
							switch (i) {
								case 0 :  orgId = cellvalueStr.trim();sqlinsert+="ORG_ID,";values+="?,"; break;
								case 1 :  orgName = cellvalueStr.trim();sqlinsert+="ORG_NAME,";values+="?,"; break;
								case 2 :  orgParentId = cellvalueStr.trim();sqlinsert+="ORG_PARENT_ID,";values+="?,"; break;
								case 3 :  userId = cellvalueStr.trim();sqlinsert+="USER_ID,";values+="?,"; break;
								case 4 :  note = cellvalueStr.trim();sqlinsert+="NOTE,";values+="?,"; break;
							}
						}
					}
					
					sqlinsert=sqlinsert.substring(0,sqlinsert.length()-1)+")";
					values=values.substring(0,values.length()-1)+")";
					Object[] allargs = {orgId,orgName,orgParentId,userId,note};
					Object[] args= new Object[param];
					int k = 0;
					for (int i = 0; i < allargs.length; i++) {
						if(null!=allargs[i]&&StringUtil.isNotBlank(allargs[i].toString())){
							args[k] = allargs[i];
							k++;
						}
					}
					String sql = sqlinsert+values;
					sysUserRoleDao.ExecuteBySql(sql, args);
					
				} catch(NumberFormatException e) {
					numberFormat += j + "、";
				} catch(Exception e) {
					e.printStackTrace();
					dateError += j + "、";
				}
			}
			result=logicProcess(dataLack,perExist, numberFormat, orgNotExist, dateError, result);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 处量并返回导入数据中存在的错识吴信息
	 * @param dataLack
	 * @param numberFormat
	 * @param orgNotExist
	 * @param dateError
	 * @param result
	 * @param perExist 
	 * @return
	 */
	private String logicProcess(String dataLack, String perExist,String numberFormat,String orgNotExist,String dateError,String result){
		if(!dataLack.equalsIgnoreCase("")){
			result = "第"+dataLack+"行数据不全！<br>";
		}
		if(!"".equalsIgnoreCase(perExist)){
			result += "第"+perExist+"行名字或帐号已存在！<br>";
		}
		if(!"".equalsIgnoreCase(numberFormat)){
			result += "第"+numberFormat+"行数字类型错误！<br>";
		}
		if(!"".equalsIgnoreCase(orgNotExist)){
			result += "第"+orgNotExist+ "行组织名称不存在！<br>";
		}
		if(!"".equalsIgnoreCase(dateError)){
			result += "第"+dateError+"行导入出现异常，请检查该行数据！<br>";
		}
		if(StringUtil.isNotEmpty(result)){
			result = "导入的文件中导入失败的有：<br>"+result;
		}
		return result;
	}
	
	public void exportOrgInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		String templatePath = "/resources/moulddownload/org_area_model.xls";
		String excelName = DateUtil.getDateTime2()+"_"+StringUtil.getFileName(templatePath);
		response.setContentType("application/vnd.ms-excel;charset=GBK");
		response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName, "GB2312"));
		in = request.getSession().getServletContext().getResourceAsStream(templatePath);
		out = response.getOutputStream();
		
		List<OrgInfo> data = null ; //人员信息
		data = orgInDao.findAll();
		String[] columName = {"orgId","orgName","orgParentId","userId","note"};
		ExcelUtil.getInstance().ExcelTemplateExport(request, response, data, templatePath, 1,0, columName);
		
	}
}
