package com.hnctdz.aiLock.action;

import java.io.InputStream;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.utils.Constants;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 下载模版Action
 * @ClassName FileDownLoadAction.java
 * @Author WangXiangBo 
 */
@Controller
@Namespace("/FileDownLoadAction")
public class FileDownLoadAction  extends ActionSupport {
	
	private static String resource = "resource";
	
	private String downLoadFilePath;
	
	private String downLoadFileName;
	
	@Action(  
		    results={@Result(type="stream",  name=ActionSupport.SUCCESS,
		                    params={"contentType","application/octet-stream",  
		                            "inputName","donwLoadFile",  
		                            "contentDisposition","attachment;filename=\"${downLoadFileName}\"",  
		                            "bufferSize","4096"  
		                    })},  
		    value="fileDownLoad"
	)
	public String fileDownLoad() {
		//   \\resources\\downloadTemp\\目录下
		downLoadFilePath = Constants.FILE_SEPARATOR+"resources"+Constants.FILE_SEPARATOR+"downloadTemp"+Constants.FILE_SEPARATOR+"" + downLoadFileName;
		return ActionSupport.SUCCESS;
	}
	
	@Action(  
		    results={@Result(type="stream",  name=ActionSupport.SUCCESS,
		                    params={"contentType","application/octet-stream",  
		                            "inputName","donwLoadFile",  
		                            "contentDisposition","attachment;filename=\"${downLoadFileName}\"",  
		                            "bufferSize","4096"  
		                    })},  
		    value="numFileDownLoad"
	)
	public String numFileDownLoad() {
		//   \\resources\\uploadtemp\\目录下
		downLoadFilePath = Constants.FILE_SEPARATOR+"resources"+Constants.FILE_SEPARATOR+"uploadtemp"+Constants.FILE_SEPARATOR+"" + downLoadFileName;
		return ActionSupport.SUCCESS;
	}
	
	@Action(  
		    results={@Result(type="stream",  name=ActionSupport.SUCCESS,
		                    params={"contentType","application/octet-stream",  
		                            "inputName","donwLoadFile",  
		                            "contentDisposition","attachment;filename=\"${downLoadFileName}\"",  
		                            "bufferSize","4096"  
		                    })},  
		    value="mouldDownLoad"
	)
	public String mouldDownLoad() {
		//   \\resources\\moulddownload\\目录下
		downLoadFilePath = Constants.FILE_SEPARATOR+"resources"+Constants.FILE_SEPARATOR+"moulddownload"+Constants.FILE_SEPARATOR+"" + downLoadFileName;
		return ActionSupport.SUCCESS;
	}
	
	@Action(  
		    results={@Result(type="stream",  name=ActionSupport.SUCCESS,
		                    params={"contentType","application/octet-stream",  
		                            "inputName","donwLoadFile",  
		                            "contentDisposition","attachment;filename=\"${downLoadFileName}\"",  
		                            "bufferSize","4096"  
		                    })},  
		    value="imgEncodeDownLoad"
	)
	public String imgEncodeDownLoad() {
		//   \\images\\menu\\imgEncode\\目录下
		downLoadFilePath = Constants.FILE_SEPARATOR+"images"+Constants.FILE_SEPARATOR+"menu"+Constants.FILE_SEPARATOR+"imgEncode"+Constants.FILE_SEPARATOR+"" + downLoadFileName;
		return ActionSupport.SUCCESS;
	}
	
	public InputStream getDonwLoadFile() {  
        return ServletActionContext.getServletContext()  
            .getResourceAsStream(downLoadFilePath);
    } 
  
    public void setDownLoadFilePath(String inputPath) {  
        this.downLoadFilePath = inputPath;  
    }

	
	/**
	 * @return the downLoadFileName
	 */
	public String getDownLoadFileName() {
		return downLoadFileName;
	}

	
	/**
	 * @param downLoadFileName the downLoadFileName to set
	 */
	public void setDownLoadFileName(String downLoadFileName) {
		this.downLoadFileName = downLoadFileName;
	}

    
}
