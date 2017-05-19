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
	
	var recordQueryUrl = "<%=basePath%>SysUserAction/findPageSysUser.action";
	var recordSaveUrl = "<%=basePath%>SysUserAction/saveSysUser.action";
	var recordDeleteUrl = "<%=basePath%>SysUserAction/deleteSysUser.action";
	var resetPasswordUtl = "<%=basePath%>SysUserAction/resetPassword.action";
	
	var findSysRoleUrl = "<%=basePath%>SysRoleAction/findSysRole.action";
	var saveSysUserInRoleUrl = "<%=basePath%>SysUserAction/saveSysUserInRole.action";
	
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
<script type="text/javascript" charset="utf-8" src="js/sys_user.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="80%">
		<tr>
		    <td style="text-align: right">用户姓名：</td>
			<td style="text-align: left">
				<input id="q_name" name="q_name"/>
			</td>
			<td style="text-align: right">用户状态：</td>
			<td style="text-align: left">
				<select id="q_status" name="q_status" class="easyui-combobox" editable="false" style="width:155px;"></select>
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
	<div id="example-window" class="easyui-window" title="系统用户信息" style="width:502px;height:270px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="userId" name="sysUser.userId">
			<input type="hidden" id="password" name="sysUser.password">
			<table border="0">
				<tr>
					<td width="15%"  align="right">账号：</td>
					<td>
						<input class="f1 easyui-validatebox" id="userName" name="sysUser.userName" data-options="required:true"></input>
					</td>
					<td width="17%" align="right">姓名：</td>
					<td>
						<input class="f1 easyui-validatebox" id="name" name="sysUser.name" data-options="required:true"></input>
					</td>
					<%--<td width="15%" align="right">密码：</td>
					<td>
						<input class="f1 easyui-validatebox" id="password" name="sysUser.password" data-options="required:true"></input>
					</td>
				--%>
				</tr>
				<tr>
					<td align="right">手机号码：</td>
					<td>
						<input class="f1 easyui-numberbox" id="phoneNo" name="sysUser.phoneNo" data-options="required:true,precision:0" validType="mobelType"></input>
					</td>
					<td align="right">用户状态：</td>
					<td>
						<select class="easyui-combobox" editable="false" editable="false" id=status name="sysUser.status" data-options="required:true" style="width:155px;"></select>
					</td>
				</tr>
				<tr>
					<td align="right">所属组织：</td>
					<td>
						<input id="orgId"  name="sysUser.orgId" class="easyui-combotree" panelWidth="200px" data-options="required:true,url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'"></input>
					</td>
					<td align="right">所属区域：</td>
					<td>
						<input id="areaId" name="sysUser.areaId" class="easyui-combotree" panelWidth="200px" style="width:155px;" data-options="url:'<%=basePath%>SysAreaAction/findSysAreaTree.action'"></input>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">备注：</td>
					<td colspan="3">
						<textarea id="note" name="sysUser.note" class="f1 easyui-validatebox" style="width:98.5%;height:80px;"></textarea>
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
	
	<!-- 角色列表 -->
	<div id="roleGrid-window" class="easyui-window" title="角色列表" data-options="iconCls:'icon-save',minimizable:false,closed:'true'" style="width:535px;height:300px;padding:5px;background: #fafafa;">
		<div region="center" class="easyui-layout" fit='true' style="background:#fff;border:1px solid #ccc;">

			<table id="roleDatagrid" border="0"></table>
		</div>
	</div>
</body>
</html>