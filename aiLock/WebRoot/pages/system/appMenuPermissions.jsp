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
	
	var recordQueryUrl = "<%=basePath%>AppMenuPermissionsAction/findPageAppMenuPermissions.action";
	var recordSaveUrl = "<%=basePath%>AppMenuPermissionsAction/saveAppMenuPermissions.action";
	var recordDeleteUrl = "<%=basePath%>AppMenuPermissionsAction/deleteAppMenuPermissions.action";
	var findAppMenuPermissionsOptionsUrl = "<%=basePath%>AppMenuPermissionsAction/findAppMenuPermissionsOptions.action";
	
	var grid;
	var win = null;
	var form = null;
	var editRow = undefined;
	var isNewRecord = true;
	
	var selected = [];// 存放已添加业务
	
	$(function() {
		initDataGrid();
		initComboboxDate();
	});
	
</script>
<script type="text/javascript" charset="utf-8" src="js/appMenuPermissions.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:110px;padding:10px;">
		<table width="80%">
		<tr>
		    <td style="text-align: right">菜单名称：</td>
			<td style="text-align: left">
				<input id="q_menuName" name="q_menuName"/>
			</td>
			<td style="text-align: right">菜单权限类型：</td>
			<td style="text-align: left">
				<select id="q_permissionsType" name="q_permissionsType" class="easyui-combobox" editable="false" style="width:155px;"></select>
	        </td>
			<td>
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;&nbsp;
			</td>
		</tr>
		<tr>
			<td style="text-align: right">菜单状态：</td>
			<td style="text-align: left">
				<select id="q_status" name="q_status" class="easyui-combobox" editable="false" style="width:155px;"></select>
			</td>
		</tr>
		</table>
	</div>

	<!-- 网格列表 -->
	<div region="center" style="overflow:hidden;border:0;">
		<table id="datagrid"></table>
	</div>
	
	<!-- 新增、修改面板。 -->
	<div id="example-window" class="easyui-window" title="APP菜单信息" style="width:305px;height:230px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="menuId" name="appMenuPermissions.menuId">
			<table>
				<tr>
					<td align="right">菜单名称：</td>
					<td>
						<input class="f1 easyui-validatebox" id="menuName" name="appMenuPermissions.menuName" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">菜单权限类型：</td>
					<td>
						<select id="permissionsType" name="appMenuPermissions.permissionsType" class="easyui-combobox" editable="false" style="width:155px;" data-options="required:true"></select>
			        </td>
				</tr>
				<tr>
					<td align="right">上级菜单：</td>
					<td>
						<input id="parentId"  name="appMenuPermissions.parentId" value="" class="easyui-combobox" style="width:155px;"></input>
					</td>
				</tr>
				<tr>
					<td align="right">菜单排序：</td>
					<td colspan="3">
						<input class="f1 easyui-validatebox" type="text" id="menuOrder" name="appMenuPermissions.menuOrder" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">菜单状态：</td>
					<td>
						<select id="status" name="appMenuPermissions.status" class="easyui-combobox" editable="false" style="width:155px;" data-options="required:true"></select>
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