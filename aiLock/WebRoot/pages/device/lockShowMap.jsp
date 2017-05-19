<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../include.jsp"></jsp:include>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<style type="text/css">
		body, html {width: 100%;height: 100%;margin:0;}
		#allmap{width:100%;height:470px;}
		p{margin-left:5px; font-size:14px;}
	</style>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.2&ak=vv2nPYmz0C3KnqG6j7zgy7ZYs1Gghx3o"></script>
	<!--加载鼠标绘制工具-->
	<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
	
	<script type="text/javascript" charset="utf-8" src="js/lockShowMap.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="100%">
		<tr>
			<td align="right">所在区域：</td>
			<td>
				<select id="q_basicDataId"  name="q_basicDataId" value="" class="easyui-combotree" panelWidth="200px" data-options="url:'<%=basePath%>SysAreaAction/findSysAreaTree.action'" style="width:100px;"></select>
			</td>
		    <%--<td style="text-align: right">城市名：</td>
			<td style="text-align: left">
				<input id="q_cityName" name="q_cityName" style="width:100px;"/>
			</td>--%>
			<td style="text-align: right">搜索名称：</td>
			<td style="text-align: left">
				<input id="q_searchName" name="q_searchName" style="width:100px;"/>
			</td>
			<td>
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search" style="width:100px;">地图搜索</a> &nbsp;&nbsp;
			</td>
			<td style="text-align: right">门锁编码：</td>
			<td style="text-align: left">
				<input id="q_lockCode" name="q_lockCode" style="width:100px;"/>
			</td>
			<td style="text-align: right">门锁名称：</td>
			<td style="text-align: left">
				<input id="q_lockName" name="q_lockName" style="width:100px;"/>
			</td>
			<td>
				<a href="javascript:searchLock()" class="easyui-linkbutton" iconCls="icon-search" style="width:100px;">查找门锁</a> &nbsp;&nbsp;
			</td>
		</tr>
		</table>
	</div>
	<div region="center" style="overflow:hidden;border:0;">
		<div id="allmap"></div>
	</div>
</body>
</html>
<script type="text/javascript">
var findDevLockInfoListUtl = "<%=basePath%>DevLockInfoAction/findNormalLockInfoList.action";
	$(function() {
		initializeMapInfo();
		parent.closeProgress();
	});
</script>
