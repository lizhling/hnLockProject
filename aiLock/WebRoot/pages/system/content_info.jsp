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
	
	var recordQueryUrl = "<%=basePath%>ContentInfoAction/findPageContentInfo.action";
	var recordSaveUrl = "<%=basePath%>ContentInfoAction/saveContentInfo.action";
	var recordDeleteUrl = "<%=basePath%>ContentInfoAction/deleteContentInfo.action";
	
	var findVersionCodeToOsUrl = "<%=basePath%>VersionAction/findVersionCodeToOs.action";
	
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
<script type="text/javascript" charset="utf-8" src="js/content_info.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:110px;padding:10px;">
		<table width="80%">
		<tr>
		    <td style="text-align: right">公告标题：</td>
			<td style="text-align: left">
				<input id="q_title" name="q_title"/>
			</td>
			<td style="width: 15%" align="right">副标题：</td>
			<td>
				<input id="q_subtitle" name="q_subtitle"/>
			</td>
			<td>
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;&nbsp;
			</td>
		</tr>
		<tr>
			<td style="text-align: right">公告状态：</td>
			<td style="text-align: left">
				<select id="q_status" name="q_status" class="easyui-combobox" editable="false" style="width:155px;"></select>
	        </td>
			<td align="right">分类：</td>
			<td style="text-align: left">
				<select id="q_contentType" name="q_contentType" class="easyui-combobox" editable="false" style="width:155px;"></select>
			</td>
		</tr>
		</table>
	</div>

	<!-- 网格列表 -->
	<div region="center" style="overflow:hidden;border:0;">
		<table id="datagrid"></table>
	</div>
	
	<!-- 新增、修改面板。 -->
	<div id="example-window" title="系统资源信息"  modal="true" iconCls="icon-save" style="display:none;padding:5px;background: #fafafa;">
		<div region="center" style="padding:10px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="contentId" name="contentInfo.contentId">
			<input type="hidden" id="ceataUser" name="contentInfo.ceataUser">
			<input type="hidden" id="ceataTime" name="contentInfo.ceataTime">
			<input type="hidden" id="suportVersion" name="contentInfo.suportVersion">
			<table style="width: 100%">
				<tr>
					<td style="width: 14%"  align="right">标题：</td>
					<td>
						<input id="title" name="contentInfo.title" class="easyui-validatebox" style="width: 97%" data-options="required:true"></input>
					</td>
					<td style="width: 15%" align="right">副标题：</td>
					<td>
						<input id="subtitle" name="contentInfo.subtitle" class="easyui-validatebox" style="width: 97%" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">分类：</td>
					<td>
						<select id="contentType" name="contentInfo.contentType" class="easyui-combobox" editable="false" style="width: 207%" data-options="required:true"></select>
					</td>
					<td align="right">内容状态：</td>
					<td>
						<select id="status" name="contentInfo.status" class="easyui-combobox" editable="false" style="width: 207%" data-options="required:true"></select>
					</td>
					<!-- <td width="20%" align="right">是否置顶：</td>
					<td>
						<select id="isPuttop" name="contentInfo.isPuttop" class="easyui-combobox" editable="false" style="width:135px;"></select>
			        </td> -->
				</tr>
				<tr>
					<td align="right">ios支持&nbsp;&nbsp;&nbsp;&nbsp;<br>最低版本：</td>
					<td>
						<select id="iosVersion" name="contentInfo.iosVersion" class="easyui-combobox" editable="false" style="width: 207%" data-options="required:true"></select>
					</td>
					<td align="right">Android支&nbsp;&nbsp;&nbsp;&nbsp;<br>持最低版本：</td>
					<td>
						<select id="androidVersion" name="contentInfo.androidVersion" class="easyui-combobox" editable="false" style="width: 207%" data-options="required:true"></select>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">内容详细：</td>
					<td colspan="3">
						<textarea id="contentText" name="contentInfo.contentText" class="easyui-validatebox" style="width:99%;height:240px;" data-options="required:true"></textarea>
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