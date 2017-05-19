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

	var recordQueryUrl = "<%=basePath%>OrgInfoAction/findPageOrgInfo.action";
	var recordSaveUrl = "<%=basePath%>OrgInfoAction/saveOrgInfo.action";
	var recordDeleteUrl = "<%=basePath%>OrgInfoAction/deleteOrgInfo.action";
	var findSysUserOptionsUrl = "<%=basePath%>SysUserAction/findSysUserOptions.action";
	
	var exportOrgInfo = "<%=basePath%>SysUserAction/exportOrgInfo.action";
	var importOrgInfo = "<%=basePath%>SysUserAction/importOrgInfo.action";
	
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
<script type="text/javascript" charset="utf-8" src="js/orgInfo.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="80%">
		<tr>
		    <td style="text-align: right">组织名称：</td>
			<td style="text-align: left">
				<input id="q_orgName" name="q_orgName"/>
			</td>
			<td align="right">上级组织：</td>
			<td>
				<input id="q_orgParentId"  name="q_orgParentId" class="easyui-combotree" panelWidth="200px" data-options="url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'"></input>
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
	<div id="example-window" class="easyui-window" title="组织架构信息" style="width:305px;height:225px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="orgId" name="orgInfo.orgId">
			<input type="hidden" id="userId" name="orgInfo.userId">
			<table border="0">
				<tr>
					<td align="right">组织名称：</td>
					<td>
						<input class="f1 easyui-validatebox" id="orgName" name="orgInfo.orgName" data-options="required:true"></input>
					</td>
				</tr>
				<%--<tr>
					<td align="right">管理员：</td>
					<td>
						<select id="userId" name="orgInfo.userId" class="easyui-combobox" editable="false" style="width:155px;" data-options="required:true"></select>
					</td>
				</tr>
				--%><tr>
					<td align="right">上级组织：</td>
					<td>
						<input id="orgParentId" name="orgInfo.orgParentId" class="easyui-combotree" panelWidth="200px" style="width:155px;" data-options="url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'"></input>
					</td>
				</tr>
				<tr>
					<td align="right">备注：</td>
					<td>
						<textarea id="note" name="orgInfo.note" class="f1 easyui-validatebox" style="width:98.5%;height:60px;"></textarea>
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
	<!-- 导入面板。 -->
	<div id="import-window" class="easyui-dialog" title="导入人员信息" style="width:410px;height:167px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:10px;background:#fff;border:1px solid #ccc;">

			<form method="post" enctype="multipart/form-data">
			<table style="width: 100%">
				<tr>
					<td align="right">导入文件：</td>
					<td>
						<input type="file" id="importFile" name="importFile" style="width:97.5%;" >
					</td>
				</tr>
			</table>
			<br>
			</form>
		</div>
		<div style="text-align:right;height:30px;line-height:24px;padding-top:5px;">
			<a id="save_button" class="easyui-linkbutton c6" iconCls="icon-ok" href="javascript:void(0)" onclick="importData()">导入</a>
			<a id="cancl_button" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeImportWin()">取消</a>
		</div>
	</div>
</body>
</html>