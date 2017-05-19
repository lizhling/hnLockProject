package com.hnctdz.aiLock.service.system.impl;

import com.hnctdz.aiLock.dao.device.DevLockInfoDao;
import com.hnctdz.aiLock.dao.system.SysAreaDao;
import com.hnctdz.aiLock.dao.system.SysUserDao;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.domain.system.SysArea;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevLockInfoDto;
import com.hnctdz.aiLock.dto.system.SysAreaDto;
import com.hnctdz.aiLock.dto.system.SysUserDto;
import com.hnctdz.aiLock.service.system.SysAreaService;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.ExcelUtil;
import com.hnctdz.aiLock.utils.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

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

@Service("com.hnctdz.aiLock.service.system.SysAreaService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class SysAreaServiceImpl implements SysAreaService {
	@Autowired
	private SysAreaDao dao;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private DevLockInfoDao devLockInfoDao;

	public SysArea getSysAreaById(Long orgId) {
		return (SysArea) this.dao.getById(orgId);
	}

	public DataPackage findPageSysArea(SysAreaDto dto, DataPackage dp) {
		dp = this.dao.findPageSysArea(dto, dp);
		List<SysArea> perIfoList = (List<SysArea>) dp.getRows();

		for (SysArea area : perIfoList) {
			if (area.getParentId() != null) {
				SysArea perArea = (SysArea) this.dao.getById(area.getParentId());
				area.setParentName(perArea.getAreaName());
			}
		}
		return dp;
	}

	public String saveSysArea(SysArea sysArea) throws Exception {
		String results = "";
		SysAreaDto dto = new SysAreaDto();
		dto.setAreaName(sysArea.getAreaName());
		List<SysArea> list = this.dao.findSysAreaList(dto);
		for (SysArea dbOrg : list) {
			if ((!dbOrg.getAreaName().equalsIgnoreCase(sysArea.getAreaName())) 
					|| ((sysArea.getAreaId() != null) && (dbOrg.getAreaId().equals(sysArea.getAreaId())))){
				continue;
			}
			results = "该区域名称已存在，请重新输入！";
		}
		if(null != sysArea.getAreaId() && null != sysArea.getParentId()){
			if(sysArea.getAreaId().equals(sysArea.getParentId())){
				results = "上级区域不能为当前区域！";
			}else{
				List<SysArea> lowerNodesList = this.dao.findSysAreaLowerNodesList(sysArea.getAreaId());
				for (SysArea lnArea : lowerNodesList) {
					if (sysArea.getParentId().equals(lnArea.getAreaId())){
						results = "上级区域不能为当前的下级区域，请重新选择！";
						continue;
					}
				}
			}
		}
		if(StringUtil.isEmpty(results)) {
			this.dao.merge(sysArea);
		}
		return results;
	}

	public String deleteSysAreaByIds(String orgIds) {
		String results = "";
		String[] orgIdArrey = orgIds.split(",");
		for (String orgIdst : orgIdArrey) {
			Long areaId = Long.valueOf(Long.parseLong(orgIdst));
			SysArea sysArea = (SysArea) this.dao.getById(areaId);
			try {
				DevLockInfoDto liDto = new DevLockInfoDto();
				liDto.setAreaId(areaId);
				if (this.devLockInfoDao.findDevLockInfoList(liDto).size() > 0) {
					results = results + sysArea.getAreaName() + "、";
					break;
				}

				SysUserDto sudto = new SysUserDto();
				sudto.setAreaId(areaId);
				if (this.sysUserDao.findSysUserList(sudto).size() > 0) {
					results = results + sysArea.getAreaName() + "、";
					break;
				}
				this.dao.delete(sysArea);
			} catch (Exception e) {
				results = results + sysArea.getAreaName() + "、";
			}
		}
		if (StringUtil.isNotEmpty(results)) {
			results = "删除的区域中：" + results + "有关联数据，不能删除，请先清除关连数据！";
		}

		return results;
	}

	public List<SysArea> findSysAreaList(SysAreaDto dto) {
		return this.dao.findSysAreaList(dto);
	}
	
	public String importAreaInfos(File importFile) {
		String result = "";
		try {
			InputStream is = new FileInputStream(importFile);
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			HSSFSheet sheet = workbook.getSheetAt(1);//第一个工作表
			
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
					String areaId="";
					String areaName="";
					String areaType="";
					String parentId="";
					String areaOrder="";
					String areaImage="";
					String note="";
					String sqlinsert = "insert into SYS_AREA(";
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
								case 0 : areaId = cellvalueStr.trim();sqlinsert+="AREA_ID,";values+="?,"; break;
								case 1 : areaName = cellvalueStr.trim();sqlinsert+="AREA_NAME,";values+="?,"; break;
								case 2 : areaType = cellvalueStr.trim();sqlinsert+="AREA_TYPE,";values+="?,"; break;
								case 3 : parentId = cellvalueStr.trim();sqlinsert+="PARENT_ID,";values+="?,"; break;
								case 4 : areaOrder = cellvalueStr.trim();sqlinsert+="AREA_ORDER,";values+="?,"; break;
								case 5 : areaImage = cellvalueStr.trim();sqlinsert+="AREA_IMAGE,";values+="?,"; break;
								case 6 : note = cellvalueStr.trim();sqlinsert+="NOTE,";values+="?,"; break;	
							}
						}
					}
					sqlinsert=sqlinsert.substring(0,sqlinsert.length()-1)+")";
					values=values.substring(0,values.length()-1)+")";
					Object[] allargs = {areaId,areaName,areaType,parentId,areaOrder,areaImage,note};
					Object[] args= new Object[param];
					int k = 0;
					for (int i = 0; i < allargs.length; i++) {
						if(null!=allargs[i]&&StringUtil.isNotBlank(allargs[i].toString())){
							args[k] = allargs[i];
							k++;
						}
					}
					String sql = sqlinsert+values;
					dao.ExecuteBySql(sql, args);
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
			result="处理信息异常："+e1.getMessage();
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
	
	public void exportAreaInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		String templatePath = "/resources/moulddownload/org_area_model.xls";
		String excelName = DateUtil.getDateTime2()+"_"+StringUtil.getFileName(templatePath);
		response.setContentType("application/vnd.ms-excel;charset=GBK");
		response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName, "GB2312"));
		in = request.getSession().getServletContext().getResourceAsStream(templatePath);
		out = response.getOutputStream();
		
		List<SysArea> data = null ; //人员信息
		data = dao.findAll();
		String[] columName = {"areaId","areaName","areaType","parentId","areaOrder","areaImage","note"};
		ExcelUtil.getInstance().ExcelTemplateExport(request, response, data, templatePath, 1,1, columName);
		
	}
	
}