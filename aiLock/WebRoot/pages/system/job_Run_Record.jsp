<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include.jsp"></jsp:include>

<script type="text/javascript" charset="utf-8">
	
	var recordQueryUrl = "<%=basePath%>StatisticalQueryAction/findPageJobRunRecord.action";
	
	var grid;
	var win = null;
	var form = null;
	$(function() {
		initDataGrid();
	});
	
</script>
<script type="text/javascript" charset="utf-8" src="js/job_Run_Record.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:110px;padding:10px;">
		<table width="80%">
		<tr>
			<td style="text-align: right">执行日期：</td>
			<td style="text-align: left">
				<input id="q_runStartDate" name="q_runStartDate" class="easyui-datebox"/>
			</td>
			<td style="text-align: right">服务器IP：</td>
			<td style="text-align: left">
				<input id="q_pcServerIp" name="q_pcServerIp"/>
			</td>
			<td>
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;&nbsp;
			</td>
		</tr>
		<tr>
			<td style="text-align: right">任务名称：</td>
			<td style="text-align: left">
				<input id="q_jobName" name="q_jobName"/>
			</td>
		</tr>
		</table>
	</div>
	
	<!-- 网格列表 -->
	<div region="center" style="overflow:hidden;border:0;">
		<table id="datagrid"></table>
	</div>
	
	<!-- 新增、修改面板。 -->
	<div id="example-window" title="定时任务失败原因"  modal="true" iconCls="icon-save" style="display:none;padding:5px;background: #fafafa;">
		<div region="center" style="padding:10px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<table style="width: 100%">
				<tr>
					<td align="right" style="width:10px;">失<br>败<br>原<br>因<br>：</td>
					<td>
						<textarea id="exceptionInfo" name="exceptionInfo" class="easyui-validatebox" style="width:99%;height:400px;"></textarea>
					</td>
				</tr>
			</table>
			</form>
		</div>
		<div style="text-align:right;height:30px;line-height:24px;padding-top:5px;">
			<a id="cancl_button" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeWindow()">取消</a>
		</div>
	</div>
</body>
</html>