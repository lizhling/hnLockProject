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
	var recordQueryUrl = "<%=basePath%>DevLockInfoAction/findPageDevLockInfo.action";
	var grid;
	
	$(function() {
		initDataGrid();
		initComboboxDate();
	});

	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				nowrap:true,
				pageSize:10,
				idField:'lockCode',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
				    {title: '门锁编码',field: 'lockCode',sortable: true,width: 120},
				    {title: '门锁名称',field: 'lockName',sortable: true,width: 140},
				    {title: '类型',field: 'lockType',sortable: true,width: 50,
				    	formatter : function(value, rec) {
				    		return dataValueConv(lockTypeGroup,value)
				    	}
				    },
				    {title: '默认开锁人员',field: 'unlockPerName',sortable: true,width: 80},
				    {title: '所在区域',field: 'areaName',width: 70},
					{title: '所属组织',field: 'orgName',width: 100},
					{title: '所在地址',field: 'lockAddres',sortable: true,width: 220}
				]],
				toolbar:[{
					id:'btnadd',
					text:'确认选择',
					iconCls:'icon-ok',
					handler:function(){
						selectConfirm();
					}
				}]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function initComboboxDate(){
		dataToSelect(lockTypeGroup,'lockType');
	}
	
	function selectConfirm(){
		var rows = grid.datagrid('getSelections');
		
		if(window.parent.receiveLockInfo){//判断方法是否存在
			window.parent.receiveLockInfo(rows);
		}
		window.close();
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.groupName':$("#q_groupName").val(),
			'dto.lockCode':$("#q_lockCode").val(),
			'dto.lockName':$("#q_lockName").val(),
			'dto.orgId' : $("#q_orgId").combotree('getValue'),
			'dto.areaId':$("#q_areaId").combotree('getValue'),
			'dto.lockType':$("#lockType").combotree('getValue')
		});
	}
	
</script>
</head>
<body class="easyui-layout" style="background:#fff;border-top:1px solid #95b8e7;">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:80px;padding:10px;">
		<table width="100%">
		<tr>
		    <td style="text-align: right">门锁编码：</td>
			<td style="text-align: left">
				<input id="q_lockCode" name="q_lockCode" style="width:70px;"/>
			</td>
			<td style="text-align: right">门锁名称：</td>
			<td style="text-align: left">
				<input id="q_lockName" name="q_lockName" style="width:70px;"/>
			</td>
			<td style="text-align: right">门锁类型：</td>
			<td style="text-align: left">
				<select id="lockType" name="lockKeyAut.lockType"  class="easyui-combobox" data-options="editable:false,required:true" style="width:80px;"></select>
			</td>
	        <td align="right">所在区域：</td>
			<td>
				<input id="q_areaId"  name="q_areaId" value="" class="easyui-combotree" panelWidth="200px" data-options="url:'<%=basePath%>SysAreaAction/findSysAreaTree.action'" style="width:80px;"></input>
			</td>
			<td align="right">所在组织：</td>
			<td>
				<input id="q_orgId"  name="q_orgId" class="easyui-combotree" panelWidth="200px" data-options="url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'" style="width:105%;"></input>
			</td>
			<td>
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
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