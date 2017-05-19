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
import com.hnctdz.aiLockdm.utils.CommunCrypUtil;
import com.hnctdz.aiLockdm.utils.ErrorCode;
import com.hnctdz.aiLockdm.utils.ErrorCodeException;
import com.hnctdz.aiLockdm.utils.ReturnCodeUtil;
import com.hnctdz.aiLockdm.utils.StringUtil;

/** 
 * @ClassName SendExpressCommandServlet.java
 * @Author WangXiangBo 
 */
public class SendExpressCommandServlet extends HttpServlet {

	public SendExpressCommandServlet() {
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
		SocketHandler socketHandler = null;
		if(StringUtil.isNotEmpty(moduleIp) && StringUtil.isNotEmpty(commands)){
			try {
				socketHandler = SocketServer.getInstance().sendExpress(moduleIp, commands);
				if(StringUtil.isNotEmpty(socketHandler.getWaitTask())){
					simpleRespose = ReturnCodeUtil.exception(ErrorCode.ERROR_WAIT_TASK_TRUE);
				}else{
					socketHandler.setWaitTask(commands.substring(6, 8));//设置等待任务
					
					long waitStartTime = new Date().getTime();
					int waitingLong = CommonUtil.getIntProperty("waitingLong", 1500);
					while(true) {
						String results = socketHandler.getResults();
						if(StringUtil.isNotEmpty(results)){
							if(results.substring(0, 2).equalsIgnoreCase("EB") &&
									results.substring(40, 42).equalsIgnoreCase("EA")){
								simpleRespose = ReturnCodeUtil.success(ReturnCodeUtil.SUCCESS_RETURN_CODE, results);
							}else{
								simpleRespose = ReturnCodeUtil.exception(ErrorCode.ERROR_RESPONSE_MESS_ERROR, moduleIp);
							}
							break;
						}
						if((waitStartTime + waitingLong) <= new Date().getTime()){
							simpleRespose = ReturnCodeUtil.exception(ErrorCode.ERROR_WAIT_TIMEOUT, moduleIp);
							break;
						}
						Thread.sleep(200);
					}
				}
			} catch (ErrorCodeException e) {
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
		out.println(simpleRespose);
		out.flush();
		out.close();
	}

	public void init() throws ServletException {
	}

}
