package com.hnctdz.aiLock.service.system;

import com.hnctdz.aiLock.domain.system.SysArea;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysAreaDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public abstract interface SysAreaService {
	public abstract SysArea getSysAreaById(Long paramLong);

	public abstract DataPackage findPageSysArea(SysAreaDto paramSysAreaDto,DataPackage paramDataPackage);

	public abstract String saveSysArea(SysArea paramSysArea) throws Exception;

	public abstract String deleteSysAreaByIds(String paramString);

	public abstract List<SysArea> findSysAreaList(SysAreaDto paramSysAreaDto);

	/**
	 * 从excel文件中导入区域信息
	 * @param importFile
	 * @return
	 */
	public abstract String importAreaInfos(File importFile);

	/**
	 * 导出区域信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public abstract void exportAreaInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException;
}