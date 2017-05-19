<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 0);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include.jsp"></jsp:include>

<script type="text/javascript" charset="utf-8">

	var recordQueryUrl = "<%=basePath%>SysRoleAction/findPageSysRole.action";
	var recordSaveUrl = "<%=basePath%>SysRoleAction/saveSysRole.action";
	var recordDeleteUrl = "<%=basePath%>SysRoleAction/deleteSysRole.action";
	
	var findSysResByRoleUrl = "<%=basePath%>SysRoleAction/findSysResByRole.action";
	var saveSysRoleUseResUrl = "<%=basePath%>SysRoleAction/saveSysRoleUseRes.action";
	
	var grid;
	var win = null;
	var roleResWin = null;
	var form = null;
	var roleResForm = null;
	var editRow = undefined;
	var isNewRecord = true;
	
	$(function() {
		initDataGrid();
		initComboboxDate();
	});
	
	
</script>
<script type="text/javascript" charset="utf-8" src="js/sys_role.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="80%">
		<tr>
		    <td style="text-align: right">角色名称：</td>
			<td style="text-align: left">
				<input id="q_roleName" name="q_roleName"/>
			</td>
			<td style="text-align: right">是否启用：</td>
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
	<div id="example-window" class="easyui-window" title="系统角色信息" style="width:350px;height:230px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="roleId" name="sysRole.roleId">
			<table border="0">
				<tr>
					<td align="right">角色名称：</td>
					<td>
						<input class="easyui-validatebox" id="roleName" name="sysRole.roleName" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">是否启用：</td>
					<td>
						<select id="status" name="sysRole.status" class="easyui-combobox"  data-options="editable:false,required:true" style="width:155px;"></select>
					</td>
				</tr>
				<tr>
					<td align="right">角色描述：</td>
					<td colspan="3">
						<textarea id="note" name="sysRole.note" class="easyui-validatebox" style="width:220px;height:60px;"></textarea>
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
	
	<!-- 资源分面板。 -->
	<div id="roleRes-window" class="easyui-window" title="资源分配" style="width:335px;height:405px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:0px;background:#fff;border:1px solid #ccc; height: 315px;overflow: auto;">
			<form method="post" >
			<table border="0">
				<tr>
					<td style="width:285px;">
						<ul id="resTree" class="easyui-tree" multiple="true" checkbox="true"
        					url='<%=basePath%>SysResAction/findSysResTreeList.action'></ul>
					</td>
				</tr>
			</table>
			</form>
		</div>
		<div style="text-align:right;height:30px;line-height:24px;padding-top:5px;">
			<a id="save_button" class="easyui-linkbutton c6" iconCls="icon-ok" href="javascript:void(0)" onclick="saveSysRoleUseRes()">保存</a>
			<a id="cancl_button" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeRoleResWin()">取消</a>
		</div>
	</div>
</body>
</html>