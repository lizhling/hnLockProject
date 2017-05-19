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
	
	//var findNormalLockListByComboboxUrl = basePath + "DevLockInfoAction/findNormalLockListByCombobox.action";
	var findNormalLockInfoListUtl = basePath + "DevLockInfoAction/findNormalLockInfoList.action";
	var findAuthorizeLockListUrl = basePath + "LockKeyAuthorizeAction/findAuthorizeLockList.action";
	
	var findPersonnelInfoOptionsUtl = basePath + "PersonnelInfoAction/findPersonnelInfoOptions.action?dto.status=1";
	var findAuthorizePerListUrl = basePath + "LockKeyAuthorizeAction/findAuthorizePerList.action";
	
	var groupReferUrl = basePath + "pages/commonRefer/groupRefer.jsp";
	var findLockByGroupIdsListUrl = basePath + "DevGroupAction/findLockByGroupIdsList.action";
	
	var grid;
	var win = null;
	var form = null;
	var editRow = undefined;
	var isNewRecord = true;
	
	var lockInfoOpJson = null;
	var personnelInfoOpJson = null;
	
	$(function() {
		initDataGrid();
		setTimeout('initComboboxDate()',300);
	});
	
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
			  		insertRowLock(lockInfos);
			  	}
			});
		}
		
		iframeWin.window('close');
	}
	
	function insertRowLock(lockInfos){
		var rows = $('#existingLocks').datalist('getData').rows;
		if(rows.length <= 0){
			$('#existingLocks').datalist({
				url: "",
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
	
	function searchLock(value,name){
		var jsons = JSON.stringify(lockInfoOpJson);
		var lockInfoJson = eval("(" + jsons + ")");
		
		if(isNotUndefined(value)){
			var valueLet = value.length;
			for(var i=0; i < lockInfoJson.length; i++){
				var texts = lockInfoJson[i].lockName;
				var l = false;
				for(var j = 0; j< texts.length; j++){
					if(texts.substr(j, valueLet) == value ){
						l = true;
						break;
					}
				}
				if(!l){
					lockInfoJson.splice(i,1);
					i--;
				}
			}
		}
		$('#lockList').datalist({
	  		data:lockInfoJson
	  	});
	}
	
	function searchPer(value,name){
		var jsons = JSON.stringify(personnelInfoOpJson);
		var perInfoJson = eval("(" + jsons + ")");
		
		if(isNotUndefined(value)){
			var valueLet = value.length;
			for(var i=0; i < perInfoJson.length; i++){
				var texts = perInfoJson[i].text;
				var l = false;
				for(var j = 0; j< texts.length; j++){
					if(texts.substr(j, valueLet) == value ){
						l = true;
						break;
					}
				}
				if(!l){
					perInfoJson.splice(i,1);
					i--;
				}
				/*if(perInfoJson[i].text != value ){
					perInfoJson.splice(i,1);
					i--;
				}*/
			}
		}
		$('#perList').datalist({
	  		data:perInfoJson
	  	});
	};
	
</script>
<script type="text/javascript" charset="utf-8" src="js/lockKeyAuthorize.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:85px;padding:10px;">
		<table width="100%">
		<tr>
		    <td style="text-align: right">门锁编码：</td>
			<td style="text-align: left">
				<input id="q_lockCode" name="q_lockCode" style="width:100px;"/>
			</td>
			<td style="text-align: right">门锁名称：</td>
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
	<div id="example-window" class="easyui-window" title="<div style='float:left;'>门锁授权信息</div><div style='float:left;font-family:Dotum;color:red'>（有源门锁权限设置只有截至时间有效，及有效时间为：当前时间至截至时间）</div>" style="width:880px;height:455px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="authorizeId" name="lockKeyAut.authorizeId"/>
			<input type="hidden" id="authorizeCode" name="lockKeyAut.authorizeCode"/>
			<input type="hidden" id="startTime" name="lockKeyAut.startTime"/>
			<input type="hidden" id="endTime" name="lockKeyAut.endTime"/>
			<input type="hidden" id="authorizeType" name="lockKeyAut.authorizeType">
			<input type="hidden" id="authorizeLockIds" name="lockKeyAut.authorizeLockIds">
			<input type="hidden" id="authorizePerIds" name="lockKeyAut.authorizePerIds">
			<table border="0" style="width: 100%">
				<tr>
					<td width="60px" align="right" >启用时间：</td>
					<td width="100px">
						<input id="startDate" name="startDate" class="easyui-datebox" data-options="required:true" style="width:92px;"/>
					</td>
					<td width="60px" align="right">截止时间：</td>
					<!-- <td width="150px"> -->
					<td width="100px">
						<input id="endDate" name="endDate" class="easyui-datebox" data-options="required:true" style="width:92px;"/>
					</td>
					<!-- <td width="72px" align="right">范围内开锁：</td> -->
					<td width="102px" align="right">范围内开锁：</td>
					<td width="50px">
						<select id="scopeUnlock" name="lockKeyAut.scopeUnlock" class="easyui-combobox" data-options="editable:false,required:true" style="width:50px;"></select>
					</td>
					<!-- <td width="60px" align="right">蓝牙开门：</td> -->
					<td width="90px" align="right">蓝牙开门：</td>
					<td width="55px">
						<select id="blueUnlock" name="lockKeyAut.blueUnlock"  class="easyui-combobox" data-options="editable:false,required:true" style="width:55px;"></select>
					</td>
					<!-- <td width="60px" align="right">授权状态：</td> -->
					<td width="90px" align="right">授权状态：</td>
					<td width="55px">
						<select id="statusCode" name="lockKeyAut.statusCode"  class="easyui-combobox" data-options="editable:false,required:true" style="width:55px;"></select>
					</td>
				</tr>
			</table>
			<table style="width:100%;height:326px">
				<tr>
					<td width="26%">
						<div style="height:26px;">
							门锁查询：
							<input id="parent" name="parent" class="easyui-searchbox" searcher="searchLock" style="width:147px;" />
						</div>
						<div id="lockList" style="height:271px;">
						</div>
					</td>
					<td width="7%" align="center" style="padding-top:64px;" valign="top">
						<a id="save_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="showGroupWin()" style="width:60px;"> 从组添加</a><br><br><br>
						<a id="save_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="addLocks()" style="width:60px;">＞ ＞</a><br><br><br>
						<a id="cancl_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="delLocks()" style="width:60px;">＜＜</a>
					</td>
					<td width="25%">
						<div id="existingLocks" title="已选择授权门锁" style="height:300px;">
						</div>
					</td>
					<td width="5%">
					</td>
					<td width="18%">
						<div style="height:29px;">
							人员查询：
							<input id="parent" name="parent" class="easyui-searchbox" searcher="searchPer" style="width:82px;" />
						</div>
						<div id="perList" style="height:271px;">
						</div>
					</td>
					<td width="6%">
						<a id="save_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="addPers()" style="width:50px;">＞ ＞</a><br><br><br>
						<a id="cancl_button" class="easyui-linkbutton" href="javascript:void(0)" onclick="delPers()" style="width:50px;">＜＜</a>
					</td>
					<td width="14%">
						<div id="existingPers" title="已选择授权人员" style="height:300px;">
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div style="text-align:right;height:30px;line-height:24px;padding-top:5px;">
			<a id="save_button" class="easyui-linkbutton c6" iconCls="icon-ok" href="javascript:void(0)" onclick="submitForm()">保存</a>
			<a id="cancl_button" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeWindow()">取消</a>
		</div>
	</div>
	
	<!-- 查询门锁列表 -->
	<div id="iframe-window" title="门锁列表" style="width:850px;height:465px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
			<iframe name="ifms" id="ifms"  frameBorder="0" style="width:100%;height:100%" src=""></iframe>
		</div>
	</div>
</body>
</html>