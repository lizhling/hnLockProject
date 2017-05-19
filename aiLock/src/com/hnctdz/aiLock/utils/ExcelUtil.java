package com.hnctdz.aiLock.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.hnctdz.aiLock.domain.device.DevLockInGroup;
import com.hnctdz.aiLock.domain.device.DevLockInGroupId;
import com.hnctdz.aiLock.service.info.impl.PersonnelInfoServiceImpl.DBTABLE;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/** 
 * @ClassName ExcelUtil.java
 * @Author WangXiangBo 
 */
public class ExcelUtil<T> {
	private static final ExcelUtil instance = new ExcelUtil();
	
	public static ExcelUtil getInstance(){
		return instance;
	}
	
	public ExcelUtil(){}
	/**
	 * 通过对应模版导出Excel数据
	 * @param request
	 * @param response
	 * @param data List数据
	 * @param templatePath 对应模版URL
	 * @param startRow 模版表头占用行数
	 * @param columName 模版列对应数据bean属性
	 */
	public void ExcelTemplateExport(HttpServletRequest request, HttpServletResponse response, 
			List<T> data, String templatePath, int startRow, String[] columName){
		InputStream in = null;
		OutputStream out = null;
		try {
			String excelName = DateUtil.getDateTime2()+"_"+StringUtil.getFileName(templatePath);
			//String excelName = templatePath.substring(templatePath.lastIndexOf("/") + 1, templatePath.lastIndexOf(".xls"));
			response.setContentType("application/vnd.ms-excel;charset=GBK");
			response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName, "GB2312"));
			
			in = request.getSession().getServletContext().getResourceAsStream(templatePath);
			out = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(out, Workbook.getWorkbook(in));
			
			WritableFont bodyFont = new WritableFont(WritableFont.TIMES, 10);//字体：10
			WritableCellFormat bodyFormat = new WritableCellFormat(bodyFont);// 设置数据
			bodyFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
			bodyFormat.setAlignment(Alignment.LEFT); // 水平对齐
			
			WritableSheet sheet = book.getSheet(0);//获取Excel模版中第一个表格
			
			int numberRows = data.size();
			if(numberRows + startRow >= 500000){
				sheet.mergeCells(0, startRow, 10, startRow);//合并第arg0列第arg1行到第arg2列第arg3行的所有单元格 
				sheet.addCell(createLabel(0, startRow, "需要导出的数据有"+data.size()+"行，导出最大限额"+(500000 - startRow - 1)+"行，剩下的请通过查询条件分别导出！", bodyFormat));
				startRow++;
				numberRows = 500000 - startRow;
			}
			for (int i = 0; i < numberRows; i++) {
				int setRow = startRow + i;
				sheet.setRowView(setRow, 400);//设置行高
				T value = data.get(i);
				BeanWrapper wrapper = new BeanWrapperImpl(value);
				int col = 0;
				for (String colum : columName) {
					sheet.addCell(createLabel(col++, setRow, wrapper.getPropertyValue(colum), bodyFormat));
				}
			}
			book.write();
			try {
				book.close();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 通过对应模版导出Excel数据
	 * @param request
	 * @param response
	 * @param data List数据
	 * @param templatePath 对应模版URL
	 * @param startRow 模版表头占用行数
	 * @param SheetNum 导出第几个表格中   第一个表格为0
	 * @param columName模版列对应数据bean属性
	 */
	public void ExcelTemplateExport(HttpServletRequest request, HttpServletResponse response, 
			List<T> data, String templatePath, int startRow,int SheetNum, String[] columName){
		InputStream in = null;
		OutputStream out = null;
		try {
			String excelName = DateUtil.getDateTime2()+"_"+StringUtil.getFileName(templatePath);
			//String excelName = templatePath.substring(templatePath.lastIndexOf("/") + 1, templatePath.lastIndexOf(".xls"));
			response.setContentType("application/vnd.ms-excel;charset=GBK");
			response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName, "GB2312"));
			
			in = request.getSession().getServletContext().getResourceAsStream(templatePath);
			out = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(out, Workbook.getWorkbook(in));
			
			WritableFont bodyFont = new WritableFont(WritableFont.TIMES, 10);//字体：10
			WritableCellFormat bodyFormat = new WritableCellFormat(bodyFont);// 设置数据
			bodyFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
			bodyFormat.setAlignment(Alignment.LEFT); // 水平对齐
			
			WritableSheet sheet = book.getSheet(SheetNum);//获取Excel模版中第一个表格
			
			int numberRows = data.size();
			if(numberRows + startRow >= 500000){
				sheet.mergeCells(0, startRow, 10, startRow);//合并第arg0列第arg1行到第arg2列第arg3行的所有单元格 
				sheet.addCell(createLabel(0, startRow, "需要导出的数据有"+data.size()+"行，导出最大限额"+(500000 - startRow - 1)+"行，剩下的请通过查询条件分别导出！", bodyFormat));
				startRow++;
				numberRows = 500000 - startRow;
			}
			for (int i = 0; i < numberRows; i++) {
				int setRow = startRow + i;
				sheet.setRowView(setRow, 400);//设置行高
				T value = data.get(i);
				BeanWrapper wrapper = new BeanWrapperImpl(value);
				int col = 0;
				for (String colum : columName) {
					sheet.addCell(createLabel(col++, setRow, wrapper.getPropertyValue(colum), bodyFormat));
				}
			}
			book.write();
			try {
				book.close();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * (跟据人员区域导出数据库层数据-专用)
	 * 通过对应模版导出Excel数据
	 * @param request
	 * @param response
	 * @param data List数据
	 * @param templatePath 对应模版URL
	 * @param startRow 模版表头占用行数
	 * @pramm writeTable 写第几个表格 第一个表格为0
	 * @param columName 模版列对应数据bean属性
	 */
	public void ExcelTemplateExport(InputStream in, OutputStream out,HashMap<DBTABLE, List> propertyMap){
		
		try {
			WritableWorkbook book = Workbook.createWorkbook(out, Workbook.getWorkbook(in));
			WritableFont bodyFont = new WritableFont(WritableFont.TIMES, 10);//字体：10
			WritableCellFormat bodyFormat = new WritableCellFormat(bodyFont);// 设置数据
			bodyFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
			bodyFormat.setAlignment(Alignment.LEFT); // 水平对齐
			
			Iterator it = propertyMap.entrySet().iterator();
			DBTABLE dbtable;
			List data = null;
			while (it.hasNext()) {
				 Map.Entry<DBTABLE, List> entry = (Map.Entry<DBTABLE, List>) it.next();
				 dbtable = entry.getKey();
				 data = propertyMap.get(entry.getKey());
				switch (dbtable) {
				case PERSONTABLE:
					WritableSheet sheet = book.getSheet(0);//获取Excel模版中第几个表格 开始从0
					String[] columName ={"perName","perAccounts","orgId","phoneNo","address","note","perId","perPassword","status","smartKeyPerId","smartKeyPassw","cuTime"};
					writeContent(book, sheet, data, 1, columName, bodyFormat);
					break;
				case KEYTABLE:
					WritableSheet sheet1 = book.getSheet(1);
					String[] columName1 ={"keyId","keyCode","keyName","keyType","perId","orgId","status","note","groupId","lockingTime","phoneImei","blueName"};
					writeContent(book, sheet1, data, 1, columName1, bodyFormat);
					break;
				case KEYGROUPTAPE:
					WritableSheet sheet2 = book.getSheet(2);
					String[] columName2 ={"groupId","groupName","groupSecretKey","orgId","note"};
					writeContent(book, sheet2, data, 1, columName2, bodyFormat);
					break;			
				case LOCKKEYAUTHTABLE:
					WritableSheet sheet3 = book.getSheet(3);
					String[] columName3 ={"authorizeId","authorizeCode","lockId","unlockPerId","startTime","endTime","unlockNumber","lockInModuleCode","lockDeviceNo","blueUnlock","statusCode","cuUserId","cuTime","authorizeType","scopeUnlock"};
					writeContent(book, sheet3, data, 1, columName3, bodyFormat);
					break;
				case LOCKTABLE:
					WritableSheet sheet4 = book.getSheet(4);
					String[] columName4 ={"lockId","lockCode","lockName","unlockPassword","lockType","lockAddres","longitude","latitude","areaId","orgId","status","note","lockDeviceNo","lockInModuleCode","ipAddress","lockInBlueCode","wheCanMatchCard","vicePassiveLockCode","lockParentId","blueMac","privateKey"};
					writeContent(book, sheet4, data, 1, columName4, bodyFormat);
					break;
				case DEVLOCKGROUPTABLE:
					WritableSheet sheet5 = book.getSheet(5);
					String[] columName5 ={"lockId","groupId"};
					writeContent(book, sheet5, data, 1, columName5, bodyFormat);
					break;
				default:
					break;
				}
			}
			try {
				book.write();
				book.close();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param book  excel对像
	 * @param sheet 单格参数
	 * @param data  List数据值
	 * @param startRow 写入开始行
	 * @param columName 导入数据列名
	 * @param bodyFormat excel 样式
	 * @throws Exception  
	 * @throws WriteException
	 */
	public void writeContent(WritableWorkbook book,WritableSheet sheet,List<T> data,int startRow,String[] columName,WritableCellFormat bodyFormat) throws Exception, WriteException{
		int numberRows = data.size();
		if(numberRows + startRow >= 500000){
			sheet.mergeCells(0, startRow, 10, startRow);//合并第arg0列第arg1行到第arg2列第arg3行的所有单元格 
			sheet.addCell(createLabel(0, startRow, "需要导出的数据有"+data.size()+"行，导出最大限额"+(500000 - startRow - 1)+"行，剩下的请通过查询条件分别导出！", bodyFormat));
			startRow++;
			numberRows = 500000 - startRow;
		}
		for (int i = 0; i < numberRows; i++) {
			int setRow = startRow + i;
			sheet.setRowView(setRow, 400);//设置行高
			T value = data.get(i);
			BeanWrapper wrapper = new BeanWrapperImpl(value);
			int col = 0;
			for (String colum : columName) {
				sheet.addCell(createLabel(col++, setRow, wrapper.getPropertyValue(colum), bodyFormat));
			}
		}
	}
	
	
	public static Label createLabel(int col, int row, Object data, CellFormat format) {
		Label label = new Label(col, row, convertToString(data), format);
		return label;
	}
	
	public static String convertToString(Object data) {
		if (data == null) {
			return "";
		} else {
			return data.toString();
		}
	}
	
}
