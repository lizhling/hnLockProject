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

	var recordQueryUrl = "<%=basePath%>SysVersionAction/findPageSysVersion.action";
	var recordSaveUrl = "<%=basePath%>SysVersionAction/saveSysVersion.action";
	var recordDeleteUrl = "<%=basePath%>SysVersionAction/deleteSysVersion.action";
	
	var grid;
	var win = null;
	var roleResWin = null;
	var form = null;
	var editRow = undefined;
	var isNewRecord = true;
	
	$(function() {
		initDataGrid();
		initComboboxDate();
	});
	
	
</script>
<script type="text/javascript" charset="utf-8" src="js/sys_version.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:110px;padding:10px;">
		<table width="80%">
		<tr>
		    <td style="text-align: right">版本号：</td>
			<td style="text-align: left">
				<input id="q_versionCode" name="q_versionCode"/>
			</td>
			<td align="right">版本名称：</td>
			<td>
				<input id="q_versionName" name="q_versionName"></input>
			</td>
			<td align="right">版本平台：</td>
			<td>
				<select id="q_versionOs" name="q_versionOs" class="easyui-combobox" editable="false" style="width:155px;"></select>
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
	<div id="example-window" class="easyui-window" title="版本信息" style="width:515px;height:370px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="versionId" name="sysVersion.versionId">
			<input type="hidden" id="createTime" name="sysVersion.createTime">
			<table border="0" style="width: 100%">
				<tr>
					<td width="19%" align="right">版本号：</td>
					<td>
						<input class="easyui-validatebox f1" id="versionCode" name="sysVersion.versionCode" data-options="required:true"></input>
					</td>
					<td width="21%" align="right">版本名称：</td>
					<td>
						<input class="easyui-validatebox f1" id="versionName" name="sysVersion.versionName" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">版本平台：</td>
					<td>
						<select id="versionOs" name="sysVersion.versionOs" class="easyui-combobox f1" editable="false" data-options="required:true" style="width:155px;"></select>
					</td>
					<td align="right">更新类型：</td>
					<td>
						<select id="updateType" name="sysVersion.updateType" class="easyui-combobox f1" editable="false" style="width:155px;"></select>
					</td>
				</tr>
				<tr>
					<td align="right">更新地址：</td>
					<td colspan="4">
						<input class="easyui-validatebox f1" id="donwloadUrl" name="sysVersion.donwloadUrl" data-options="required:true"style="width:98.5%;"></input>
					</td>
				</tr>
				<tr>
					<td align="right">版本信息：</td>
					<td colspan="4">
						<textarea id="versionInfo" name="sysVersion.versionInfo" class="f1 easyui-validatebox" style="width:98.5%;height:180px;"></textarea>
					</td>
				</tr>
			</table>
			</form>
		</div>
		<div style="text-align:right;height:30px;line-height:24px;padding-top:5px;">
			<a id="save_button" class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="submitForm()">保存</a>
			<a id="cancl_button" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeWindow()">取消</a>
		</div>
	</div>
</body>
</html>