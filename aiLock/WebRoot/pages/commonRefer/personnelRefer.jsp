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
	var recordQueryUrl = "<%=basePath%>PersonnelInfoAction/findPagePersonnelInfo.action";
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
				singleSelect:true,
				pageSize:10,
				idField:'perId',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
				    {title: '人员姓名',field: 'perName',sortable: true,width: 100},
					{title: '手机号码',field: 'phoneNo',width: 100},
					{title: '所属组织',field: 'orgName',width: 150},
					{title: '人员状态',field: 'status',sortable: true,width: 100,
						formatter : function(value, rec) {
							return dataValueConv(userStatusGroup,value)
						}
					}
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
	}
	
	function selectConfirm(){
		var rows = grid.datagrid('getSelections');
		
		if(window.parent.receivePersonnelInfo){//判断方法是否存在
			window.parent.receivePersonnelInfo(rows[0]);
		}
		window.close();
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.perAccounts':$("#q_perAccounts").val(),
			'dto.orgId':$("#q_orgId").combotree("getValue"),
			'dto.perName':$("#q_perName").val()
		});
	}
	
</script>
</head>
<body class="easyui-layout" style="background:#fff;border-top:1px solid #95b8e7;">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:80px;padding:10px;">
		<table width="100%">
		<tr>
			<td style="text-align: right">人员姓名：</td>
			<td style="text-align: left">
				<input id="q_perName" name="q_perName" style="width:70px;"/>
			</td>
			<td style="text-align: right">账号：</td>
			<td style="text-align: left">
				<input id="q_perAccounts" name="q_perAccounts" style="width:70px;"/>
			</td>
			<td align="right">所在组织：</td>
			<td>
				<input id="q_orgId"  name="q_orgId" class="easyui-combotree" panelWidth="200px" style="width:100px;" data-options="url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'"></input>
			</td>
			<td>
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;&nbsp;
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