<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include.jsp"></jsp:include>

<script type="text/javascript" charset="utf-8">
	var recordQueryUrl = window.parent.basePath + "DevGroupAction/findDevGroupList.action";
	
	var grid;
	var win = null;
	var form = null;
	var editRow = undefined;
	var isNewRecord = true;
	
	var groupLockGrid = null;
	var lockInfoWin = null;
	
	$(function() {
		initDataGrid();
	});
	
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'groupId',//表明该列是一个唯一列
				pagination:false,
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
				    {title: '分组名称',field: 'groupName',sortable: true,width: 200},
				    {title: '备注',field: 'note',sortable: true,width: 450}
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
	
	function selectConfirm(){
		var rows = grid.datagrid('getSelections');
		
		if(window.parent.receiveGroup){//判断方法是否存在
			window.parent.receiveGroup(rows);
		}
		window.close();
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.groupName':$("#q_groupName").val(),
			'dto.lockCode':$("#q_lockCode").val()
		});
	}
	
</script>
</head>
<body class="easyui-layout" style="background:#fff;border-top:1px solid #95b8e7;">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="100%">
		<tr>
			<td style="text-align: right">分组名称：</td>
			<td style="text-align: left">
				<input id="q_groupName" name="q_groupName" />
			</td>
		    <td style="text-align: right">门锁编码：</td>
			<td style="text-align: left">
				<input id="q_lockCode" name="q_lockCode"/>
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