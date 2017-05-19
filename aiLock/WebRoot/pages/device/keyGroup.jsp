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
	
	var recordQueryUrl = "<%=basePath%>DevKeyGroupAction/findPageDevKeyGroup.action";
	var recordSaveUrl = "<%=basePath%>DevKeyGroupAction/saveDevKeyGroup.action";
	var recordDeleteUrl = "<%=basePath%>DevKeyGroupAction/deleteDevKeyGroup.action";
	
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
<script type="text/javascript" charset="utf-8" src="js/keyGroup.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="100%">
		<tr>
			<td style="text-align: right">钥匙母钥名称：</td>
			<td style="text-align: left">
				<input id="q_groupName" name="q_groupName" />
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
	
	<!-- 新增、修改面板。 -->
	<div id="example-window" class="easyui-window" title="钥匙组信息" style="width:460px;height:245px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="groupId" name="devKeyGroup.groupId"/>
			<table border="0">
				<tr>
					<td width="21%" align="right">钥匙母钥名称：</td>
					<td>
						<input class="easyui-validatebox" id="groupName" name="devKeyGroup.groupName" data-options="required:true" style="width:99%;"></input>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">母钥通信码：</td>
					<td>
						<input id="groupSecretKey" name="devKeyGroup.groupSecretKey" type="password" class="easyui-validatebox" style="width:99%;"></input>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">备注：</td>
					<td>
						<textarea id="note" name="devKeyGroup.note" class="easyui-validatebox" style="width:99%;height:80px;"></textarea>
					</td>
				</tr>
			</table>
			</form>
		</div>
		<div style="text-align:right;height:30px;line-height:24px;padding-top:5px;">
			<a id="save_button" class="easyui-linkbutton c6" iconCls="icon-ok" href="javascript:void(0)" onclick="submitForm()">保存</a>
			<a id="cancl_button" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeWindow()">取消</a>
		</div>
	</div>
</body>
</html>