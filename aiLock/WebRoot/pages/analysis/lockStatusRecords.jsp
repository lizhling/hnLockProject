<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include.jsp"></jsp:include>
<style type="text/css">
</style>
<script type="text/javascript" charset="utf-8">
	var recordQueryUrl = "<%=basePath%>LockStatusRecordsAction/findPageLockStatusRecords.action";
	var findSysBasicDataListUtl = "<%=basePath%>SysBasicDataAction/findSysBasicDataCombobox.action?dto.typeTag=LOCK_STATUS";
	
	var grid;
	var win = null;
	var form = null;
	
	$(function() {
		initDataGrid();
		initComboboxDate();
	});
	
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'noticeId',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{title: '所属区域',field: 'areaName',sortable: true,width: 60},
					{title: '门锁编码',field: 'lockCode',sortable: true,width: 130},
					{title: '门锁名称',field: 'lockName',sortable: true,width: 200},
					//{title: '布防状态',field: 'bufangStatus',width: 90},
					//{title: '报警状态',field: 'baojingStatus',width: 80},
					//{title: '机械钥匙',field: 'jixieyaoshiStatus',width: 80},
					{title: '锁舌状态',field: 'xiesheStatus',width: 80},
					{title: '门磁状态',field: 'menciStatus',width: 80},
					//{title: '上锁状态',field: 'shangshuoStatus',width: 80},
					{title: '门的状态',field: 'menguanhaoStatus',width: 80},
					{title: '所属组织',field: 'orgName',width: 100},
					{title: '上报时间',field: 'reportTime',width: 125}
				]],
				toolbar:null
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function initComboboxDate(){
		jsonToSelect(findSysBasicDataListUtl + '&dto.parentId=48', "--请选择--", 'q_menciStatus');
		jsonToSelect(findSysBasicDataListUtl + '&dto.parentId=53', "--请选择--", 'q_xiesheStatus');
		jsonToSelect(findSysBasicDataListUtl + '&dto.parentId=70', "--请选择--", 'q_menguanhaoStatus');
	}
	
	function search(queryType) {
		grid.datagrid('unselectAll');
		var queryParams = {
				'dto.lockCode':$("#q_lockCode").val(),
				'dto.menciStatus':$("#q_menciStatus").combobox('getValue'),
				'dto.xiesheStatus':$("#q_xiesheStatus").combobox('getValue'),
				'dto.menguanhaoStatus':$("#q_menguanhaoStatus").combobox('getValue'),
				'dto.orgId' : $("#q_orgId").combotree('getValue'),
				'dto.areaId':$("#q_areaId").combotree('getValue'),
				'dto.lockName':$("#q_lockName").val(),
				'dto.queryType':queryType
			};
			if(queryType != 1){
				grid.datagrid('load',queryParams);
			}else{
				Net.functions.excelExport(recordQueryUrl, queryParams);
			}
		}

	
</script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:100px;padding:5px;">
		<table width="100%">
		<tr>
		    <td style="text-align: right">门锁编码：</td>
			<td style="text-align: left">
				<input id="q_lockCode" name="q_lockCode" style="width:149px;"/>
			</td>
	        <td style="text-align: right">门锁名称：</td>
			<td style="text-align: left">
				<input id="q_lockName" name="q_lockName" style="width:149px;"/>
			</td>
	        <td style="text-align: right">锁舌状态：</td>
			<td style="text-align: left">
				<select id="q_xiesheStatus" name="q_xiesheStatus" class="easyui-combobox" editable="false" style="width:85px;" "></select>
	        </td>
			<td style="text-align: right">门磁状态：</td>
			<td style="text-align: left">
				<select id="q_menciStatus" name="q_menciStatus" class="easyui-combobox" panelWidth="200px" style="width:85px;"></select>
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
	        <td style="text-align: right">门的状态：</td>
			<td style="text-align: left">
				<select id="q_menguanhaoStatus" name="q_menguanhaoStatus" class="easyui-combobox" panelWidth="200px" style="width:85px;"></select>
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
</body>
</html>