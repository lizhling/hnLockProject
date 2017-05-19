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
	
	var recordQueryUrl = "<%=basePath%>SysResAction/findPageSysRes.action";
	var recordSaveUrl = "<%=basePath%>SysResAction/saveSysRes.action";
	var recordDeleteUrl = "<%=basePath%>SysResAction/deleteSysRes.action";
	
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
<script type="text/javascript" charset="utf-8" src="js/sys_res.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:110px;padding:10px;">
		<table width="80%">
		<tr>
		    <td style="text-align: right">资源名称：</td>
			<td style="text-align: left">
				<input id="q_resName" name="q_resName"/>
			</td>
			<td style="text-align: right">资源类型：</td>
			<td style="text-align: left">
				<select id="q_resType" name="q_resType" class="easyui-combobox" editable="false" style="width:155px;"></select>
	        </td>
			<td>
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;&nbsp;
			</td>
		</tr>
		<tr>
			<td style="text-align: right">资源状态：</td>
			<td style="text-align: left">
				<select id="q_status" name="q_status" class="easyui-combobox" editable="false" style="width:155px;"></select>
			</td>
		    <td style="text-align: right">上级资源：</td>
			<td style="text-align: left">
				<input id="q_resParentId"  name="q_resParentId" value="" class="easyui-combotree" panelWidth="200px" data-options="url:'<%=basePath%>SysResAction/findSysResTreeList.action'" style="width:155px;"></input>
			</td>
		</tr>
		</table>
	</div>

	<!-- 网格列表 -->
	<div region="center" style="overflow:hidden;border:0;">
		<table id="datagrid"></table>
	</div>
	
	<!-- 新增、修改面板。 -->
	<div id="example-window" class="easyui-window" title="系统资源信息" style="width:505px;height:280px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="resId" name="sysRes.resId">
			<table>
				<tr>
					<td style="width: 17%" align="right">资源名称：</td>
					<td>
						<input class="easyui-validatebox" id="resName" name="sysRes.resName" data-options="required:true" style="width:149px;"></input>
					</td>
					<td style="width: 18%" align="right">上级资源：</td>
					<td>
						<input id="resParentId"  name="sysRes.resParentId" value="" class="easyui-combotree" panelWidth="200px" data-options="url:'<%=basePath%>SysResAction/findSysResTreeList.action'" style="width:155px;"></input>
					</td>
				</tr>
				<tr>
					<td align="right">资源URL：</td>
					<td colspan="3">
						<input class="easyui-validatebox" type="text" id="resUrl" name="sysRes.resUrl" style="width:98.5%;"></input>
					</td>
				</tr>
				<tr>
					<td align="right">资源类型：</td>
					<td>
						<select id="resType" name="sysRes.resType" class="easyui-combobox" editable="false" style="width:155px;" data-options="required:true"></select>
			        </td>
			        <td align="right">资源状态：</td>
					<td>
						<select id="status" name="sysRes.status" class="easyui-combobox" editable="false" style="width:155px;" data-options="required:true"></select>
					</td>
				</tr>
				<tr>
					<td align="right">资源图标：</td>
					<td>
						<input class="easyui-validatebox" type="text" id="resIcon" name="sysRes.resIcon" style="width:149px;"></input>
					</td>
					<td align="right">资源排序：</td>
					<td colspan="3">
						<input class="easyui-numberbox" type="text" id="resOrder" name="sysRes.resOrder" data-options="required:true" style="width:155px;"></input>
					</td>
				</tr>
				<tr>
					<td align="right">资源描述：</td>
					<td colspan="3">
						<textarea id="note" name="sysRes.note" class="easyui-validatebox" style="width:98.5%;height:60px;"></textarea>
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