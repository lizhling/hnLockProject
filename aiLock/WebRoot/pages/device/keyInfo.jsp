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
	
	var recordQueryUrl = "<%=basePath%>DevKeyInfoAction/findPageDevKeyInfo.action";
	var recordSaveUrl = "<%=basePath%>DevKeyInfoAction/saveDevKeyInfo.action";
	var recordDeleteUrl = "<%=basePath%>DevKeyInfoAction/deleteDevKeyInfo.action";
	
	var findPersonnelInfoOptionsUtl = "<%=basePath%>PersonnelInfoAction/findPersonnelInfoOptions.action";
	var findKeyGroupOptionsUtl = "<%=basePath%>DevKeyGroupAction/findKeyGroupOptions.action";
	var keyNnbundlingUtl = "<%=basePath%>DevKeyInfoAction/keyNnbundling.action";
	
	var menuUrl = "<%=basePath%>OrgInfoAction/findOrgInfoTree.action";
//	var menuUrl = "<%=basePath%>SysAreaAction/findSysAreaTree.action";
	
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
<script type="text/javascript" charset="utf-8" src="js/keyInfo.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="100%">
		<tr>
		    <td style="text-align: right">编码：</td>
			<td style="text-align: left">
				<input id="q_keyCode" name="q_keyCode" style="width:100px;"/>
			</td>
			<td style="text-align: right">名称：</td>
			<td style="text-align: left">
				<input id="q_keyName" name="q_keyName" style="width:100px;"/>
			</td>
			<td style="text-align: right">类型：</td>
			<td style="text-align: left">
				<select id="q_keyType" name="q_keyType" class="easyui-combobox" editable="false" style="width:80px;"></select>
	        </td>
	        <td style="text-align: right">状态：</td>
			<td style="text-align: left">
				<select id="q_status" name="q_status" class="easyui-combobox" editable="false" style="width:80px;"></select>
	        </td>
	        <td align="right">持有人：</td>
			<td>
				<select class="easyui-combobox" id="q_perId" name="q_perId" style="width:80px;"></select>
			</td>
			<td align="right">所属组织：</td>
			<td>
				<input id="q_orgId"  name="q_orgId" class="easyui-combotree" panelWidth="200px" data-options="url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'"></input>
			</td>
			<td>
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;&nbsp;
			</td>
		</tr>
		</table>
	</div>
	<!-- 组织树和表单数据 -->
	<div region="center" style="overflow:hidden;border:0;">
		<table width="100%" height="100%">
			<tr>
				<td width=15%>
					<div id="treePanel" class="easyui-panel" fit="true" border="false"  title="区域树" style="padding: 5px;">
						<ul id="orgTree"></ul>
					</div>
				</td>
				<td width=85%>
					<table id="datagrid"></table>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- 新增、修改面板。 -->
	<div id="example-window" class="easyui-window" title="钥匙信息" style="width:520px;height:295px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="keyId" name="keyInfo.keyId"/>
			<input type="hidden" id="phoneImei" name="keyInfo.phoneImei"/>
			<table border="0">
				<tr>
					<td width="17%" align="right">类型：</td>
					<td>
						<select class="easyui-combobox" id="keyType" name="keyInfo.keyType" data-options="editable:false,required:true" style="width:155px;"></select>
					</td>
					<td width="17%" align="right">状态：</td>
					<td>
						<select class="easyui-combobox" id="status" name="keyInfo.status" data-options="editable:false,required:true" style="width:155px;"></select>
					</td>
				</tr>
				<tr>
					<td id="keyCodeTd" align="right">钥匙编码：</td>
					<td width="33%">
						<input class="easyui-validatebox" id="keyCode" name="keyInfo.keyCode" data-options="required:true" validType="HexadecimalCheck[8]" style="width:149px;"></input>
					</td>
					<td id="keyNameTd" align="right">钥匙名称：</td>
					<td width="33%">
						<input class="easyui-validatebox" id="keyName" name="keyInfo.keyName" data-options="required:true" style="width:149px;"></input>
					</td>
				</tr>
				<tr>
					<td align="right">所属组织：</td>
					<td>
						<input id="orgId"  name="keyInfo.orgId" class="easyui-combotree" panelWidth="200px" data-options="required:true,url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'" style="width:155px;"></input>
					</td>
					<td align="right">持有人员：</td>
					<td>
						<select class="easyui-combobox" id="perId" name="keyInfo.perId" style="width:155px;"></select>
					</td>
				</tr>
				<tr>
					<td align="right">智能钥匙组：</td>
					<td>
						<select class="easyui-combobox" id="groupId" name="keyInfo.groupId" style="width:155px;" data-options="editable:false,required:true"></select>
					</td>
					<!-- <td width="16%" align="right">蓝牙名称：</td>
					<td>
						<input class="easyui-validatebox" id="blueName" name="keyInfo.blueName" data-options="editable:false,required:true,validType:'ValueBlueName[8,16]'" style="width:149px;"></input>
					</td> -->
					<td align="right">智能钥匙&nbsp;&nbsp;&nbsp;&nbsp;<br>在线时长：</td>
					<td>
						<input id="lockingTime" name="keyInfo.lockingTime" class="easyui-numberbox" style="width:126px;" data-options="required:true,precision:0,min:1,max:99999"></input>
						小时
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">备注：</td>
					<td colspan="3">
						<textarea id="note" name="keyInfo.note" class="easyui-validatebox" style="width:98.5%;height:80px;"></textarea>
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