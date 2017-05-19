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
	
	var recordQueryUrl = "<%=basePath%>SysNoticeAction/findPageSysNotice.action";
	var recordSaveUrl = "<%=basePath%>SysNoticeAction/saveSysNotice.action";
	var recordDeleteUrl = "<%=basePath%>SysNoticeAction/deleteSysNotice.action";
	
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
<script type="text/javascript" charset="utf-8" src="js/sys_notice.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:110px;padding:10px;">
		<table width="80%">
		<tr>
		    <td style="text-align: right">公告标题：</td>
			<td style="text-align: left">
				<input id="q_title" name="q_title"/>
			</td>
			<td style="text-align: right">公告状态：</td>
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
	<div id="example-window" class="easyui-window" title="系统资源信息" style="width:710px;height:435px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">

		<div region="center" style="padding:10px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="noticeId" name="sysNotice.noticeId">
			<input type="hidden" id="userId" name="sysNotice.userId">
			<input type="hidden" id="releaseTime" name="sysNotice.releaseTime">
			<table style="width: 100%">
				<tr>
					<td style="width: 14%"  align="right">标题：</td>
					<td colspan="3">
						<input id="title" name="sysNotice.title" class="easyui-validatebox" style="width: 99%" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">公告状态：</td>
					<td>
						<select id="status" name="sysNotice.status" class="easyui-combobox" editable="false" style="width:149px; data-options="required:true"></select>
					</td>
					<td width="20%" align="right">是否置顶：</td>
					<td>
						<select id="isPuttop" name="sysNotice.isPuttop" class="easyui-combobox" editable="false" style="width:149px;"></select>
			        </td>
				</tr>
				<tr>
					<td align="right" valign="top">公告详细：</td>
					<td colspan="3">
						<textarea id="content" name="sysNotice.content" class="easyui-validatebox" style="width:99%;height:260px;" data-options="required:true"></textarea>
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