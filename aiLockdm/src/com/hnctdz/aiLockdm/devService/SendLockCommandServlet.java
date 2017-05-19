package com.hnctdz.aiLockdm.devService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnctdz.aiLockdm.socket.SocketHandler;
import com.hnctdz.aiLockdm.socket.SocketServer;
import com.hnctdz.aiLockdm.utils.CommonUtil;
import com.hnctdz.aiLockdm.utils.ErrorCode;
import com.hnctdz.aiLockdm.utils.ErrorCodeException;
import com.hnctdz.aiLockdm.utils.ReturnCodeUtil;
import com.hnctdz.aiLockdm.utils.StringUtil;


/** 
 * @ClassName SendLockCommandServlet.java
 * @Author WangXiangBo 
 */
public class SendLockCommandServlet extends HttpServlet {
	
	public SendLockCommandServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String simpleRespose = "";
		String moduleIp = request.getParameter("moduleIp");
		String commands = request.getParameter("commands");
		int i = 0;
		if(StringUtil.isNotEmpty(moduleIp) && StringUtil.isNotEmpty(commands)){
			SocketHandler socketHandler = null;
			try {
				socketHandler = SocketServer.getInstance().send(moduleIp, commands);
				if(StringUtil.isNotEmpty(socketHandler.getWaitTask())){
					simpleRespose = ReturnCodeUtil.exception(ErrorCode.ERROR_WAIT_TASK_TRUE);
				}else{
					socketHandler.setWaitTask(commands.substring(6, 8));//设置等待任务
					
					long waitStartTime = new Date().getTime();
					int waitingLong = CommonUtil.getIntProperty("waitingLong", 1500);
					String results = null;
					while(true) {
						results = socketHandler.getResults();
						if(StringUtil.isNotEmpty(results)){
//							System.out.println("停止等待时间："+DateUtil.getDateMs());
							simpleRespose = ReturnCodeUtil.success(ReturnCodeUtil.SUCCESS_RETURN_CODE, results);
							break;
						}
						if((waitStartTime + waitingLong) <= new Date().getTime()){
							simpleRespose = ReturnCodeUtil.exception(ErrorCode.ERROR_WAIT_TIMEOUT, moduleIp);
							break;
						}
						Thread.sleep(200);
						i++;
					}
				}
			} catch (ErrorCodeException e) {
				e.printStackTrace();
				simpleRespose = ReturnCodeUtil.exception(e.getMessage(), moduleIp);
			} catch (IOException e) {
				simpleRespose = ReturnCodeUtil.exception(ErrorCode.ERROR_IP_CONNECTED_FAILED, moduleIp);
			} catch (Exception e) {
				simpleRespose = ReturnCodeUtil.exception(ErrorCode.ERROR_SYS_EXCEPTION);
			} finally {
				if(socketHandler != null)
					socketHandler.setWaitTask(null);//清除等待任务
			}
		}else{
			simpleRespose = ReturnCodeUtil.exception(ErrorCode.ERROR_PARAMETER);
		}
//		System.out.println("返回给管理平台："+DateUtil.getDateMs()+";i="+i);
		out.println(simpleRespose);
		out.flush();
		out.close();
	}

	public void init() throws ServletException {
	}

}
