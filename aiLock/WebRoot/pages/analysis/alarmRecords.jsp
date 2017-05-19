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
	var recordQueryUrl = "<%=basePath%>UnlockRecordsAction/findPageUnlockRecords.action?dto.selectType=2";
	var recordSaveUrl = "<%=basePath%>UnlockRecordsAction/saveUnlockRecords.action";
	
	var alarmConfirmUtl = "<%=basePath%>UnlockRecordsAction/alarmConfirm.action";
	
	var grid;
	var win = null;
	var form = null;
	var editRow = undefined;
	var isNewRecord = true;
	
	var roleGrid = null;
	var roleGridWin = null;
	
	var selected = [];// 存放已添加业务
	
	$(function() {
		var recordId = getQueryString("recordId");
		if(isNotUndefined(recordId)){
			$("#q_recordId").val(recordId);
		}
		var lockCode = getQueryString("lockCode");
		if(isNotUndefined(lockCode)){
			$("#q_lockCode").val(lockCode);
		}
		initDataGrid();
		initComboboxDate();
	});
	
</script>
<script type="text/javascript" charset="utf-8" src="js/alarmRecords.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:100px;padding:5px;">
		<table width="80%" border=0>
		<input type="hidden" id="q_recordId" name="q_recordId"/>
		<tr>
			<td style="text-align: right">告警等级：</td>
			<td style="text-align: left">
				<select id="q_alarmLevel" name="q_alarmLevel" class="easyui-combobox" editable="false" style="width:155px;"></select>
	        </td>
	        <td style="text-align: right">门锁区域：</td>
			<td style="text-align: left">
				<input id="q_areaId"  name="q_areaId" value="" class="easyui-combotree" panelWidth="200px" style="width:155px;" data-options="url:'<%=basePath%>SysAreaAction/findSysAreaTree.action'"></input>
	        </td>
	        <td style="text-align: right">门锁组织：</td>
			<td style="text-align: left">
				<input id="q_orgId"  name="q_orgId" class="easyui-combotree" panelWidth="200px" style="width:155px;" data-options="url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'"></input>
	        </td>
			<%--<td style="text-align: right">告警类型：</td>
			<td style="text-align: left">
				<select id="q_recordTpye" name="q_recordTpye" class="easyui-combobox" editable="false" style="width:140px;"></select>
	        </td>
	        <td style="text-align: right">钥匙编码：</td>
			<td style="text-align: left">
				<input id="q_keyCode" name="q_keyCode" style="width:120px;"/>
			</td>--%>
		</tr>
		<tr>
	        <td style="text-align: right">门锁编码：</td>
			<td style="text-align: left">
				<input id="q_lockCode" name="q_lockCode" style="width:149px;"/>
			</td>
			<td style="text-align: right">门锁名称：</td>
			<td style="text-align: left">
				<input id="q_lockName" name="q_lockName" style="width:149px;"/>
			</td>
			<td colspan="2" align="center">
				<a href="javascript:search(0)" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:search(1)" class="easyui-linkbutton" iconCls="icon-down">导出</a> 
			</td>
		</tr>
		</table>
	</div>

	<!-- 网格列表 -->
	<div region="center" style="overflow:hidden;border:0;">
		<table id="datagrid"></table>
	</div>
	
	<div id="alarmConfirmWin" class="easyui-dialog" title="告警处理确认" style="text-align:center; width:280px;height:170px;padding:10px"
			data-options="toolbar:'#dlg-toolbar',buttons:'#dlg-buttons',resizable:true,closed:'true'">
		<div style="text-align:left;">告警处理确认备注：</div>
		<textarea id="note" name="note" class="easyui-validatebox" style="width:98.5%;height:50px;"></textarea>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="alarmConfirm()" style="width:90px;">提交确认</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#alarmConfirmWin').dialog('close')">取消</a>
	</div>
</body>
</html>