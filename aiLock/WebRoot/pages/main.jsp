<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.hnctdz.aiLock.utils.CommonUtil"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String userAccIp = (String)request.getSession(true).getAttribute("userAccIp"); //内存中
	String curUserIp = CommonUtil.getIpAddr(request);
	
	Cookie c[] = request.getCookies();
	String cuser=null;
    String crand=null;
    if(c != null){
        for(int i=0;i<c.length;i++){
            //cookie_user 固定名
            if(c[i].getName().equals("cookie_user")){
                cuser = c[i].getValue(); //cookie_user的值　就是保存的用户名
            }
            if(c[i].getName().equals("cookie_random")){
                crand = c[i].getValue();
            }
        }
        if(crand==null || "".equals(crand) || cuser==null || "".equals(cuser)){
            response.sendRedirect(basePath+"login.jsp");
            return;
        }
    }
    
    if(!userAccIp.equals(curUserIp)){
    	response.sendRedirect(basePath+"login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<title>传通云锁管理平台</title>
<jsp:include page="include.jsp"></jsp:include>
</head>
<body id="indexLayout" class="easyui-layout">
	<div data-options="region:'north'" class="north_bg" style="height:60px;overflow: hidden;" >
		<div style="height:60px;line-height:50px;font-size:25px;font-family:微软雅黑;">&nbsp;&nbsp;传通云锁管理平台</div>
		<jsp:include page="layout/north.jsp"></jsp:include>
	</div>
    <div data-options="region:'center'" style="overflow:hidden; border:0 solid !important;" href="layout/center.jsp"></div>
    <div id="function_panel" data-options="region:'west',split:true" title="功能菜单" style="width:180px;overflow:hidden;" href="layout/west.jsp"></div>
	<div data-options="region:'south',split:true" style="height:25px;overflow: hidden;border-style:none !important;text-align: center;">版权所有&nbsp;河南传通电子科技有限公司</div>  
</body>
</html>