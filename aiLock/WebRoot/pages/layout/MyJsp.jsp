<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
  </head>
  <body>
	<div id="centerTabs">
		<div title="首页" border="false" style="width:1100px;height:600px;overflow: hidden;">
			<iframe src="<%=basePath%>pages/layout/home.jsp;jsession=<%=session.getId()%>" frameborder="0" style="width:100%;height:100%;"></iframe>
		</div> 
	</div>
  </body>
</html>
