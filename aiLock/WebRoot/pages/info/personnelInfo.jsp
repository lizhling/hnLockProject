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
	var basePath = "<%=basePath%>";
	var recordQueryUrl = "<%=basePath%>PersonnelInfoAction/findPagePersonnelInfo.action";
	var recordSaveUrl = "<%=basePath%>PersonnelInfoAction/savePersonnelInfo.action";
	var recordDeleteUrl = "<%=basePath%>PersonnelInfoAction/deletePersonnelInfo.action";
	var resetPerPasswordUtl = "<%=basePath%>PersonnelInfoAction/resetPerPassword.action";
	
	var findOrgInfoOptionsUtl = "<%=basePath%>OrgInfoAction/findOrgInfoOptions.action";
	
	var importFileUrl = "<%=basePath%>PersonnelInfoAction/importPersonnelInfos.action";
	var mouldDownLoadUrl = "<%=basePath%>FileDownLoadAction/mouldDownLoad.action";
	
	var exportQueryUrl = "<%=basePath%>PersonnelInfoAction/exportPersonInfo.action";
	
	var menuUrl = "<%=basePath%>OrgInfoAction/findOrgInfoTree.action";
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
<script type="text/javascript" charset="utf-8" src="js/personnelInfo.js" ></script>

</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="80%">
		<tr>
			<td style="text-align: right">人员姓名：</td>
			<td style="text-align: left">
				<input id="q_perName" name="q_perName"/>
			</td>
			<td style="text-align: right">账号：</td>
			<td style="text-align: left">
				<input id="q_perAccounts" name="q_perAccounts"/>
			</td>
			<td style="text-align: right">人员状态：</td>
			<td style="text-align: left">
				<select id="q_status" name="q_status" class="easyui-combobox" editable="false" style="width:155px;"></select>
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
					<div id="treePanel" fit="true" border="false"  title="区域树" style="padding: 5px;">
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
	<div id="example-window" class="easyui-window" title="人员资料信息" style="width:510px;height:300px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="perId" name="perInfo.perId">
			<input type="hidden" id="smartKeyPerId" name="perInfo.smartKeyPerId">
			<input type="hidden" id="perPassword" name="perInfo.perPassword">
			<input type="hidden" id="cuTime" name="perInfo.cuTime">
			<table border="0">
				<tr>
					<td width="16%" align="right">姓名：</td>
					<td>
						<input class="f1 easyui-validatebox" id="perName" name="perInfo.perName" data-options="required:true"></input>
					</td>
					<td width="16%" align="right">APP账号：</td>
					<td>
						<input class="f1 easyui-validatebox" id="perAccounts" name="perInfo.perAccounts" data-options="required:true"></input>
					</td>
				</tr>
				<tr>
					<td align="right">手机号码：</td>
					<td>
						<input id="phoneNo" name="perInfo.phoneNo" class="f1 easyui-numberbox" data-options="required:true,precision:0" validType="mobelType"></input>
					</td>
					<td align="right">所属组织：</td>
					<td>
						<input id="orgId"  name="perInfo.orgId" class="easyui-combotree" panelWidth="200px" data-options="required:true,url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'" style="width:155px;"></input>
					</td>
				</tr>
				<tr>
					<td align="right">智能钥匙&nbsp;&nbsp;&nbsp;&nbsp;<br>密码：</td>
					<td>
						<input id="smartKeyPassw" name="perInfo.smartKeyPassw" class="f1 easyui-numberbox" validType="ValueOneToFourCheck[8]"></input>
					</td>
					<td align="right">人员状态：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false,required:true" id="status" name="perInfo.status" style="width:155px;"></select>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">居住地址：</td>
					<td colspan="3">
						<input class="f1 easyui-validatebox" id="address" name="perInfo.address" style="width:98.5%;"></input>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">备注：</td>
					<td colspan="3">
						<textarea id="note" name="perInfo.note" class="easyui-validatebox" style="width:98.5%;height:80px;"></textarea>
					</td>
				</tr>
			</table>
			</form>
		</div>
		<div style="text-align:right;height:30px;line-height:24px;padding-top:5px;">
			<a id="save_button" class="easyui-linkbutton c7" iconCls="icon-add" href="javascript:void(0)" onclick="addKeyInfo()" style="width:100px;">新配钥匙</a>
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
				<tr>
					<td align="right">导入类型：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false,required:true" id="importType" name="dto.importType" style="width:155px;"></select>
					</td>
				</tr>
			</table>
			<br>
			</form>
		</div>
		<div style="text-align:right;height:30px;line-height:24px;padding-top:5px;">
			<a id="save_button" class="easyui-linkbutton c6" iconCls="icon-ok" href="javascript:void(0)" onclick="importGroup()">导入</a>
			<a id="cancl_button" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeImportWin()">取消</a>
		</div>
	</div>
	<iframe id="downloadIframe" src="" style="display:none;"></iframe>
</body>
</html>