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
	var recordQueryUrl = basePath + "LockKeyAuthorizeAction/findPageLockKeyAuthorize.action";
	var recordSaveUrl = basePath + "LockKeyAuthorizeAction/saveLockKeyAuthorize.action";
	var recordDeleteUrl = basePath + "LockKeyAuthorizeAction/deleteLockKeyAuthorize.action";
	
	var findPersonnelInfoOptionsUtl = basePath + "PersonnelInfoAction/findPersonnelInfoOptions.action";
	var findAuthorizeInfoUtl = "";
	
	var lockInfoReferUrl = basePath + "pages/commonRefer/lockInfoRefer.jsp";
	var groupReferUrl = basePath + "pages/commonRefer/groupRefer.jsp";
	var personnelReferUrl = basePath + "pages/commonRefer/personnelRefer.jsp";
	
	var findLockByGroupIdsListUrl = basePath + "DevGroupAction/findLockByGroupIdsList.action";
	
	var grid;
	var win = null;
	var form = null;
	var editRow = undefined;
	var isNewRecord = true;
	
	var lockGrid = null;
	var lockGridWin = null;
	
	var personnelInfoOpJson = null;
	
	$(function() {
		initDataGrid();
		//initComboboxDate();
	});
	
	var iframeWin = null;
	function showLockInfoWin(){
		iframeWin = $('#iframe-window').window({
		    width:850,
		    height:465,
		    modal:true,
		    title: '可选门锁列表'
		});
		iframeWin.css("display", "block");
		
		var ifms = document.getElementById("ifms");
		ifms.src = lockInfoReferUrl;
	};
	
	function receiveLockInfo(lockInfos){
		var rows = $('#lockGrid').datagrid('getData').rows;
		addLock(lockInfos);
		iframeWin.window('close');
	}
	
	
	var iframeWin = null;
	function showGroupWin(){
		iframeWin = $('#iframe-window').window({
		    modal:true,
		    title: '可选分组列表'
		});
		iframeWin.window('open');
		
		var ifms = document.getElementById("ifms");
		ifms.src = groupReferUrl;
	};
	
	function receiveGroup(groups){
		var groupIds = "";
		var f = "";
		for(var i=0; i < groups.length; i++){
			groupIds += f + groups[i].groupId;
			f = ","
		}
		if(groupIds != ""){
			$.get(findLockByGroupIdsListUrl, {
				'dto.groupIds':groupIds
			},
			function(data) {
				var repObj = eval("(" + data + ")");
			  	var lockInfos = repObj.rows;
			  	if (lockInfos != 0 && lockInfos.length > 0) {
			  		addLock(lockInfos);
			  	}
			});
		}
		
		iframeWin.window('close');
	}
	
	function addLock(lockInfos){
		/*var rows = $('#lockGrid').datagrid('getData').rows;
		var insertLock = true;
		for(var i=0; i < lockInfos.length; i++){
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
				$('#lockGrid').datagrid('insertRow', {
					row : lockInfos[i]
				});
			}
		}*/
		
		var rows = $('#existingLocks').datalist('getData').rows;
		if(rows.length <= 0){
			$('#existingLocks').datalist({
				lines:true,
				data: lockInfos,
				textField: "lockName",
				valueField: "lockId",
				singleSelect: false
			});
		}else{
			var insertLock = true;
			for(var i=0; i < lockInfos.length; i++){
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
					$('#existingLocks').datalist('insertRow', {
						row : lockInfos[i]
					});
				}
			}
		}
	}
	
	
	function showPersonnelWin(){
		iframeWin = $('#iframe-window').window({
		    width:850,
		    height:465,
		    modal:true,
		    title: '可选人员列表'
		});
		iframeWin.css("display", "block");
		
		var ifms = document.getElementById("ifms");
		ifms.src = personnelReferUrl;
	};
	
	function receivePersonnelInfo(personnelInfo){
		var rows = lockGrid.datagrid('getData').rows;
		for(var i=0; i < rows.length; i++){
			rows[i].unlockPerId = personnelInfo.perId;
			rows[i].unlockPerName = personnelInfo.perName;
			lockGrid.datagrid('updateRow',{index:i, row:rows[i]});
		}
		iframeWin.window('close');
	}
	
	function opens(value,name){
	};
	
</script>
<script type="text/javascript" charset="utf-8" src="js/lockKeyAuthorize.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="100%">
		<tr>
		    <td style="text-align: right">编码：</td>
			<td style="text-align: left">
				<input id="q_lockCode" name="q_lockCode" class="easyui-textbox" style="width:100px;"/>
			</td>
			<td style="text-align: right">名称：</td>
			<td style="text-align: left">
				<input id="q_lockName" name="q_lockName" style="width:100px;"/>
			</td>
	        <td style="text-align: right">状态：</td>
			<td style="text-align: left">
				<select id="q_statusCode" name="q_statusCode" class="easyui-combobox" editable="false" style="width:80px;"></select>
	        </td>
	        <td align="right">开锁人员：</td>
			<td>
				<select class="easyui-combobox" id="q_unlockPerId" name="q_unlockPerId" style="width:80px;"></select>
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
	<div id="example-window" class="easyui-window" title="门锁授权信息" style="width:850px;height:455px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="authorizeId" name="lockKeyAut.authorizeId"/>
			<input type="hidden" id="authorizeCode" name="lockKeyAut.authorizeCode"/>
			<input type="hidden" id="startTime" name="lockKeyAut.startTime"/>
			<input type="hidden" id="endTime" name="lockKeyAut.endTime"/>
			<input type="hidden" id="lockAuthorizeInfos" name="lockKeyAut.lockAuthorizeInfos">
			<table border="0" style="width: 100%">
				<tr>
					<td width="12%" align="right">开锁启用时间：</td>
					<td width="22%">
						<input id="startDate" name="startDate" class="easyui-datebox" style="width:92px;"/>
						<select id="startHours" name="startHours" class="easyui-combobox" editable="false" style="width:55px;"></select>
					</td>
					<td width="12%" align="right">开锁截止时间：</td>
					<td width="22%">
						<input id="endDate" name="endDate" class="easyui-datebox" style="width:92px;"/>
						<select id="endHours" name="endHours" class="easyui-combobox" editable="false" style="width:55px;"></select>
					</td>
					<td width="9%" align="right">开锁次数：</td>
					<td width="5%">
						<input id="unlockNumber" name="lockKeyAut.unlockNumber" class="easyui-numberbox" data-options="min:1,max:99999,precision:0" style="width:76%;"></input>
					</td>
					<td width="10%" align="right">授权状态：</td>
					<td width="10%">
						<select id="statusCode" name="lockKeyAut.statusCode"  class="easyui-combobox" editable="false" style="width:82%;"></select>
					</td>
				</tr>
			</table>
			<table border="0" style="width:100%;height:326px">
				<tr height="326px">
					<td width="20%">
						<!-- <table id="lockGrid"></table> -->
						<div style="height:29px;">
							门锁查询：
							<input id="parent" name="parent" class="easyui-searchbox" searcher="opens" style="width:120px;" />
						</div>
						<div class="easyui-datalist" id="lockGrid" style="height:271px;">
						</div>
					</td>
					<td width="7%" align="center">
						<a id="save_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="showGroupWin()" style="width:60px;"> 从组添加</a><br><br><br>
						<a id="save_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="" style="width:50px;"> ＞ ＞</a><br><br><br>
						<a id="cancl_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="" style="width:50px;">＜＜</a>
					</td>
					<td width="20%">
						<!-- <table id="lockGrid"></table>
						<div class="panel-header" style="width: 155px;">
							<div class="panel-title">
								已选择门锁
								<a id="cancl_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="closeWindow()">取消</a>
							</div>
						</div> -->
						<div class="easyui-datalist" id="existingLocks" title="已选择授权门锁" style="height:300px;">
						</div>
					</td>
					<td width="5%">
					</td>
					<td width="16%">
						<div style="height:29px;">
							人员查询：
							<input id="parent" name="parent" class="easyui-searchbox" searcher="opens" style="width:80px;" />
						</div>
						<div class="easyui-datalist" id="perList" style="height:271px;">
						</div>
					</td>
					<td width="5%">
						<a id="save_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="" style="width:50px;"> ＞ ＞</a><br><br><br>
						<a id="cancl_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="" style="width:50px;">＜＜</a>
					</td>
					<td width="14%">
						<!-- <table id="lockGrid"></table> -->
						<div class="easyui-datalist" id="" title="已选择授权人员" style="height:300px;">
						</div>
					</td>
				</tr>
			</table>
			<%--<table style="width:100%">
				<tr height="325">
					<td colspan="6">
						<table id="lockGrid" style="width:700px;"></table>
					</td>
				</tr>
			</table>
			--%></form>
		</div>
		<div style="text-align:right;height:30px;line-height:24px;padding-top:5px;">
			<a id="save_button" class="easyui-linkbutton c6" iconCls="icon-ok" href="javascript:void(0)" onclick="submitForm()">保存</a>
			<a id="cancl_button" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeWindow()">取消</a>
		</div>
	</div>
	
	<!-- 查询门锁列表 -->
	<div id="iframe-window" class="easyui-window" title="门锁列表" style="width:850px;height:465px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
			<iframe name="ifms" id="ifms"  frameBorder="0" style="width:100%;height:100%" src=""></iframe>
		</div>
	</div>
</body>
</html>