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
	var recordQueryUrl = "<%=basePath%>UnlockRecordsAction/findPageUnlockRecords.action?dto.selectType=1";
	var recordSaveUrl = "<%=basePath%>UnlockRecordsAction/saveUnlockRecords.action";
	
	var findPersonnelInfoOptionsUtl = "<%=basePath%>PersonnelInfoAction/findPersonnelInfoOptions.action";
	
	var grid;
	var win = null;
	var form = null;
	var editRow = undefined;
	var isNewRecord = true;
	
	var roleGrid = null;
	var roleGridWin = null;
	
	var selected = [];// 存放已添加业务
	
	$(function() {
		var lockCode = getQueryString("lockCode");
		if(isNotUndefined(lockCode)){
			$("#q_lockCode").val(lockCode);
		}
		initDataGrid();
		initComboboxDate();
	});
	
</script>
<script type="text/javascript" charset="utf-8" src="js/unlockRecords.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:100px;padding:5px;">
		<table width="100%" border=0>
		<tr>
		    <td style="text-align: right">门锁编码：</td>
			<td style="text-align: left">
				<input id="q_lockCode" name="q_lockCode" style="width:149px;"/>
			</td>
			<td style="text-align: right">门锁名称：</td>
			<td style="text-align: left">
				<input id="q_lockName" name="q_lockName" style="width:149px;"/>
			</td>
			<td align="right">开锁时间：</td>
			<td>
				<input id="q_unStartTime" name="q_unStartTime" class="easyui-datebox" style="width:100px;"/>
				至 <input id="q_unEndTime" name="q_unEndTime" class="easyui-datebox" style="width:100px;"/>
			</td>
			<td align="right">开锁人员：</td>
			<td>
				<select class="easyui-combobox" id="q_unlockPerId" name="q_unlockPerId" style="width:100px;"></select>
			</td>
		</tr>
		<tr>
			<td style="text-align: right">门锁区域：</td>
			<td style="text-align: left">
				<input id="q_areaId"  name="q_areaId" value="" class="easyui-combotree" panelWidth="200px" style="width:155px;" data-options="url:'<%=basePath%>SysAreaAction/findSysAreaTree.action'"></input>
	        </td>
	        <td style="text-align: right">门锁组织：</td>
			<td style="text-align: left">
				<input id="q_orgId"  name="q_orgId" class="easyui-combotree" panelWidth="200px" style="width:155px;" data-options="url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'"></input>
	        </td>
			<td style="text-align: right">记录类型：</td>
			<td style="text-align: left">
				<select id="q_lockType" name="q_lockType" class="easyui-combobox" editable="false" style="width:108px;"></select>
				<select id="q_recordCode" name="q_recordCode" class="easyui-combobox" editable="false" style="width:108px;"></select>
	        </td>
			<td colspan="2">
				<a href="javascript:search(0)" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:search(1)" class="easyui-linkbutton" iconCls="icon-down">导出</a> 
			</td>
		</tr>
		</table>
	</div>

	<!-- 网格列表 -->
	<div region="center" style="overflow:hidden;border:0;">
		<table id="datagrid"></table>
</body>
</html>