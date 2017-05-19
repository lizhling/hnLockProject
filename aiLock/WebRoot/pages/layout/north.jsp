<%@ page language="java" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 0);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script type="text/javascript" charset="utf-8">
	function logout(b) {
		window.location.href = '<%=basePath%>j_spring_security_logout';
	}
	
	function remoteUnlock(){
		$('#upPaswWin').window({
		    modal:true
		});
		$('#upPaswWin').window('open');
	}
	
	function confirmUnlock(){
		var oldPassword = $('#oldPassword').val();
		var password = $('#password').val();
		var confirmPassword = $('#confirmPassword').val();
		
		if(isNotUndefined(oldPassword) && isNotUndefined(password) && isNotUndefined(confirmPassword)){
			if(password.length < 6){
				$.messager.show({
					title : '错误', msg : '密码长度不能少于6位！'
				});
				return;
			}
			if(confirmPassword != password){
				$.messager.show({
					title : '错误', msg : '新密码和确认密码不一致，请重新输入！'
				});
				return;
			}
		}else{
			$.messager.show({
				title : '错误', msg : '请输入完相关信息！'
			});
			return;
		}
		
		$.get("<%=basePath%>SysUserAction/updateSysUserPasw.action", {
			'dto.oldPassword':oldPassword,
			'dto.password':password
		},
		function(data) {
			var repObj = eval("(" + data + ")");
			var resultCode = repObj.resultCode;
			if(resultCode == 0){
				$.messager.show({
					title : '提示', msg : '密码修改成功！'
				});
				$('#upPaswWin').window('close');
				$('#oldPassword').val("");
				$('#password').val("");
				$('#confirmPassword').val("");
			}else{
				$.messager.alert('错误', repObj.resultMessage, 'error');
			}
		});
	}
</script>

<div style="position: absolute; right: 10px; bottom: 15px; ">
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="remoteUnlock();" data-options="plain:true">修改密码</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true" onclick="logout(true);">退出系统</a>
	<%--<a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_zxMenu" iconCls="icon-back">注销</a>--%>
</div>
<div id="upPaswWin" class="easyui-dialog" title="修改登录密码" style="text-align:center; width:280px;height:200px;padding:10px"
			data-options="toolbar:'#dlg-toolbar',buttons:'#dlg-buttons',resizable:true,closed:'true'">
	当前密码：<input class="easyui-validatebox" type="password" id="oldPassword" data-options="required:true" style="width:149px;"></input><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;新密码：<input class="easyui-validatebox" type="password" id="password" data-options="required:true" style="width:149px;"></input><br><br>
确认密码：<input class="easyui-validatebox" type="password" id="confirmPassword" data-options="required:true" style="width:149px;"></input>
</div>
<div id="dlg-buttons">
	<a href="#" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="confirmUnlock()" style="width:90px;">确认修改</a>
	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#upPaswWin').dialog('close')">取消</a>
</div>
	
<%--<div id="layout_north_zxMenu" style="width: 100px; display: none;">
	<div class="menu-sep"></div>
	<div onclick="logout(true);">退出系统</div>
</div>
--%>