<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include.jsp"></jsp:include>

<!-- 弹出框样式 -->
 <style type="text/css">
     *{ margin:0px; padding:0px;}
     #popWin{ width:150px; height:70px; border:1px solid #000; position:fixed; bottom:2px; right:2px; display:none; background-color:White;}
     #popWin a{ position:absolute; top:2px; right:2px; font-size:12px; text-decoration:none; color:Blue;}
     #popWin span{position:absolute; top:10px; left:4px;}
     #reshow{position:fixed;right:2px;bottom:2px;font-size:14px; display:none;background-color:White; cursor:pointer;border:2px solid #000;}
</style>
<script type="text/javascript" charset="utf-8">
	var basePath = "<%=basePath%>";
	var recordQueryUrl = basePath + "DevLockInfoAction/findPageDevLockInfo.action";
	var recordSaveUrl = basePath + "DevLockInfoAction/saveDevLockInfo.action";
	var recordDeleteUrl = basePath + "DevLockInfoAction/deleteDevLockInfo.action";
	
	var findPersonnelInfoOptionsUtl = basePath + "PersonnelInfoAction/findPersonnelInfoOptions.action";
	var remoteUnlockUtl = basePath + "DevLockInfoAction/remoteUnlock.action";
	var setLockTimeUtl = basePath + "DevLockInfoAction/setLockTime.action";
	var getLockStateUtl = basePath + "DevLockInfoAction/getLockState.action";
	
	var importFileUrl = basePath+"DevLockInfoAction/importLockInfos.action";
	var mouldDownLoadUrl = basePath+"FileDownLoadAction/mouldDownLoad.action";
	
	var menuUrl = "<%=basePath%>SysAreaAction/findSysAreaTree.action";
	var getDevLockCountUtl = "<%=basePath%>DevLockInfoAction/getLockCountInfo.action";
	var exportQueryUrl = "<%=basePath%>DevLockInfoAction/exportLockInfo.action";
	
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

<script type="text/javascript" charset="utf-8" src="<%=basePath%>pages/device/js/lockInfo.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:110px;padding:10px;">
		<table width="100%">
		<tr>
		    <td style="text-align: right">锁编码：</td>
			<td style="text-align: left">
				<input id="q_lockCode" name="q_lockCode"/>
			</td>
			<td style="text-align: right">锁名称：</td>
			<td style="text-align: left">
				<input id="q_lockName" name="q_lockName"/>
			</td>
			<td style="text-align: right">蓝牙名称：</td>
			<td style="text-align: left">
				<input id="q_BtName" name="q_BtName"/>
	        </td>
			<td style="text-align: right">类型：</td>
			<td style="text-align: left">
				<select id="q_lockType" name="q_lockType" class="easyui-combobox" editable="false" style="width:80px;"></select>
	        </td>
	        <td style="text-align: right">状态：</td>
			<td style="text-align: left">
				<select id="q_status" name="q_status" class="easyui-combobox" editable="false" style="width:80px;"></select>
	        </td>
        </tr>
        <tr>
	        <td align="right">所在区域：</td>
			<td>
				<input id="q_areaId" name="q_areaId" class="easyui-combotree" panelWidth="200px" panelHeight="350px" style="width:155px;" data-options="url:'<%=basePath%>SysAreaAction/findSysAreaTree.action'"></input>
			</td>
			<td align="right">所属组织：</td>
			<td>
				<input id="q_orgId"  name="q_orgId" class="easyui-combotree" panelWidth="200px" panelHeight="350px" style="width:155px;" data-options="url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'"></input>
			</td>
			<td style="text-align: right">蓝牙配置：</td>
			<td style="text-align: left">
				<select id="q_isBlueConfig" name="q_isBlueConfig" class="easyui-combobox" editable="false" style="width:80px;"></select>
	        </td>
	        <td style="text-align: right">副锁配置：</td>
			<td style="text-align: left">
				<select id="q_isVicePassive" name="q_isVicePassive" class="easyui-combobox" editable="false" style="width:80px;"></select>
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
					<div id="treePanel" class="easyui-panel" fit="true" border="false" title="区域树" style="padding: 5px;"> 
						<ul id="areaTree" class="easyui-tree"></ul>
					</div>
				</td>
				<td width=85%>
					<table id="datagrid"></table>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- 新增、修改面板。 -->
	<div id="example-window" class="easyui-window" title="门锁信息" style="width:540px;height:370px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="lockId" name="lockInfo.lockId"/>
			<input type="hidden" id="lockParentId" name="lockInfo.lockParentId"/>
			<table border="0">
				<tr>
					<td width="17%" align="right">门锁类型：</td>
					<td>
						<select class="easyui-combobox" id="lockType" name="lockInfo.lockType" data-options="editable:false,required:true" style="width:155px;"></select>
					</td>
					<td width="21%" align="right">门锁状态：</td>
					<td>
						<select class="easyui-combobox"  id="status" name="lockInfo.status" editable="false" data-options="required:true" style="width:155px;"></select>
					</td>
				</tr>
				<tr>
					<td align="right">门锁编码：</td>
					<td>
						<input class="easyui-validatebox" id="lockCode" name="lockInfo.lockCode" data-options="required:true" style="width:149px;"></input>
					</td>
					<td align="right">门锁名称：</td>
					<td>
						<input class="easyui-validatebox" id="lockName" name="lockInfo.lockName" data-options="required:true" style="width:149px;"></input>
					</td>
				</tr>
				<tr>
					<td align="right">网关编码：</td>
					<td>
						<input class="easyui-validatebox f1" id="lockInModuleCode" name="lockInfo.lockInModuleCode" data-options="required:false" validType="HexadecimalCheck[12]"></input>
					</td>
					<td align="right">门锁机号：</td>
					<td>
						<input class="easyui-validatebox f1" id="lockDeviceNo" name="lockInfo.lockDeviceNo" data-options="required:false"></input>
					</td>
				</tr>
				<tr>
					<td align="right">网关IP：</td>
					<td>
						<input class="easyui-validatebox f1" id="ipAddress" name="lockInfo.ipAddress" data-options=""></input>
					</td>
					<td align="right">蓝牙连接名称：</td>
					<td>
						<input class="easyui-validatebox f1" id="lockInBlueCode" name="lockInfo.lockInBlueCode" data-options="required:false"></input>
					</td>
				</tr>
				<tr>
					<td align="right">蓝牙MAC：</td>
					<td>
						<input class="easyui-validatebox f1" id="blueMac" name="lockInfo.blueMac" data-options=""></input>
					</td>
					<td align="right">蓝牙KEY：</td>
					<td>
						<input class="easyui-validatebox f1" id="privateKey" name="lockInfo.privateKey" data-options="required:false"></input>
					</td>
				</tr>
				<tr>
					<td align="right">是否可配卡：</td>
					<td>
						<select class="easyui-combobox" id="wheCanMatchCard" name="lockInfo.wheCanMatchCard" editable="false" required="true" style="width:155px;"></select>
					</td>
					<td align="right">副无源锁编码：</td>
					<td>
						<input class="easyui-validatebox f1" id="vicePassiveLockCode" name="lockInfo.vicePassiveLockCode" data-options="" validType="HexadecimalCheck[8]"></input>
					</td>
				</tr>
				<tr>
					<td align="right">所属组织：</td>
					<td>
						<input id="orgId"  name="lockInfo.orgId" class="easyui-combotree" panelWidth="200px" data-options="required:true,url:'<%=basePath%>OrgInfoAction/findOrgInfoTree.action'" style="width:155px;"></input>
					</td>
					<td align="right">所属区域：</td>
					<td>
						<input id="areaId" name="lockInfo.areaId" class="easyui-combotree" panelWidth="200px" style="width:155px;" data-options="url:'<%=basePath%>SysAreaAction/findSysAreaTree.action'"></input>
					</td>
				</tr>
				<tr>
					<td align="right">经度：</td>
					<td>
						<input class="easyui-validatebox" id="longitude" name="lockInfo.longitude" validType="NumberFloatCheckToLongitude[1,12]" style="width:149px;"></input>
					</td>
					<td align="right">纬度：</td>
					<td>
						<input class="easyui-validatebox" id="latitude" name="lockInfo.latitude" validType="NumberFloatCheckToLongitude[1,13]" style="width:149px;"></input>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">所在地址：</td>
					<td colspan="3">
						<input class="easyui-validatebox" id="lockAddres" name="lockInfo.lockAddres" style="width:98.5%;"></input>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">备注：</td>
					<td colspan="3">
						<textarea id="note" name="lockInfo.note" class="easyui-validatebox" style="width:98.5%;height:60px;"></textarea>
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
	<div id="unLockWin" class="easyui-dialog" title="开锁确认" style="text-align:center; width:280px;height:150px;padding:10px"
			data-options="toolbar:'#dlg-toolbar',buttons:'#dlg-buttons',resizable:true,closed:'true'">
		请输入管理员远程开锁密码，确认开锁<br><br>
		开锁密码：<input class="easyui-validatebox" type="password" id="unLockPassw" name="unLockPassw" data-options="required:true" style="width:149px;"></input>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="confirmUnlock()" style="width:90px;">确认开锁</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#unLockWin').dialog('close')">取消</a>
	</div>
	
	<!-- 导入面板。 -->
	<div id="import-window" class="easyui-dialog" title="导入人员信息" style="width:410px;height:147px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
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
			<a id="save_button" class="easyui-linkbutton c6" iconCls="icon-ok" href="javascript:void(0)" onclick="importGroup()">导入</a>
			<a id="cancl_button" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeImportWin()">取消</a>
		</div>
	</div>
	<div id="reshow">信息</div>
     <div id="popWin">         
	     <a href="javaScript:void(0)" id="close">关闭</a>         
	     <span style=" line-height:20px;">
	            无源锁：<apan id="unlineLockNum"></apan> <br />
	     		有源锁：<apan id="onlineLockNum"></apan><br />
	           蓝牙锁： <apan id="btLockNum"></apan>
	     </span>
     </div>
	<iframe id="downloadIframe" src="" style="display:none;"></iframe>
</body>
</html>