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
	var recordQueryUrl = "<%=basePath%>DevGroupAction/findPageDevGroup.action";
	var recordSaveUrl = "<%=basePath%>DevGroupAction/saveDevGroup.action";
	var recordDeleteUrl = "<%=basePath%>DevGroupAction/deleteDevGroup.action";
	
	var findGroupLockUtl = "<%=basePath%>DevGroupAction/findLockByGroupIdsList.action";
	
	var lockInfoReferUrl = "<%=basePath%>pages/commonRefer/lockInfoRefer.jsp";
	
	var grid;
	var win = null;
	var form = null;
	var editRow = undefined;
	var isNewRecord = true;
	
	var groupLockGrid = null;
	var lockInfoWin = null;
	
	$(function() {
		initDataGrid();
		initComboboxDate();
	});
	
	var objName = "";
	function showLockInfoWin(){
		objName = name;
		
		lockInfoWin = $('#lockInfo-window').window({
		    modal:true
		});
		lockInfoWin.window('open');
		
		var ifms = document.getElementById("ifms");
		ifms.src = lockInfoReferUrl;
	};
	
	function receiveLockInfo(lockInfos){
		var rows = $('#groupLockGrid').datagrid('getData').rows;
		var insertLock = true;
		for(var i=0; i<lockInfos.length; i++){
			insertLock = true;
			if(rows.length > 0){
				for(var j=0; j<rows.length; j++){
					if(lockInfos[i].lockCode == rows[j].lockCode){
						insertLock = false;
						break;
					}
				}
			}
			if(insertLock == true){
				$('#groupLockGrid').datagrid('insertRow', {
					row : lockInfos[i]
				});
			}
		}
		
		lockInfoWin.window('close');
	}
	
</script>
<script type="text/javascript" charset="utf-8" src="js/lockInGroup.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="100%">
		<tr>
			<td style="text-align: right">分组名称：</td>
			<td style="text-align: left">
				<input id="q_groupName" name="q_groupName" />
			</td>
		    <td style="text-align: right">门锁编码：</td>
			<td style="text-align: left">
				<input id="q_lockCode" name="q_lockCode"/>
			</td>
			<td style="text-align: right">门锁名称：</td>
			<td style="text-align: left">
				<input id="q_lockName" name="q_lockName"/>
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
	<div id="example-window" class="easyui-window" title="分组信息" style="width:700px;height:465px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="groupId" name="devGroup.groupId">
			<input type="hidden" id="lockIds" name="devGroup.lockIds">
			<table border="0" style="width: 100%">
				<tr>
					<td width="70px" align="right">分组名称：</td>
					<td width="75px">
						<input class="easyui-validatebox" id="groupName" name="devGroup.groupName" data-options="required:true" style="width:75px;"></input>
					</td>
					<td width="50px" align="right">备注：</td>
					<td width="0%">
						<input id="note" name="devGroup.note" style="width:450px;"></input>
					</td>
				</tr>
				<tr height="345">
					<td colspan="4" style="width: 100%">
						<table id="groupLockGrid"></table>
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
	
	<!-- 查询门锁列表 -->
	<div id="lockInfo-window" class="easyui-window" title="门锁列表" style="width:850px;height:465px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<iframe name="ifms" id="ifms"  frameBorder="0" style="width:100%;height:100%" src=""></iframe>
	</div>
</body>
</html>