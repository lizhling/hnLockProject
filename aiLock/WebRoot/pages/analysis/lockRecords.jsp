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
	
	var recordQueryUrl = "<%=basePath%>UnlockRecordsAction/findPageUnlockRecords.action";
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
		initDataGrid();
		initComboboxDate();
	});
	
</script>
<script type="text/javascript" charset="utf-8" src="js/unlockRecords.js" ></script>
</head>
<body class="easyui-layout">
     <div id="p" data-options="region:'west'" title="区域" style="width:18%;padding:0px">
		 	<ul id="areaTree" class="easyui-tree" multiple="true"
     			url='<%=basePath%>OrgInfoAction/findOrgInfoTree.action'></ul>
     </div>
     <div data-options="region:'center'" title="监控记录">
     	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:7%;padding:10px;">
			<table width="100%">
			<tr>
			    <td style="text-align: right">门锁编码：</td>
				<td style="text-align: left">
					<input id="q_lockCode" name="q_lockCode" style="width:140px;"/>
				</td>
				<td style="text-align: right">门锁名称：</td>
				<td style="text-align: left">
					<input id="q_lockName" name="q_lockName" style="width:140px;"/>
				</td>
				<td style="text-align: right">记录类型：</td>
				<td style="text-align: left">
					<select id="q_recordTpye" name="q_recordTpye" class="easyui-combobox" panelWidth="180px" editable="false" style="width:140px;"></select>
		        </td>
				<td align="right">开锁人员：</td>
				<td>
					<select class="easyui-combobox" id="q_perId" name="q_perId" style="width:70px;"></select>
				</td>
				<td>
					<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;&nbsp;
				</td>
			</tr>
			</table>
		</div>
		<!-- 网格列表 -->
		<div region="center" style="overflow:hidden;border:0;height: 89%">
			<table id="datagrid"></table>
		</div>
     </div>
</body>
</html>