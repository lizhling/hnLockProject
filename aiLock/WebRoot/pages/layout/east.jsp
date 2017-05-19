<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8">
</script>
<div class="easyui-layout" fit="true" border="false">
	<div region="north" border="false" style="height:180px;overflow: hidden;">
		<div id="calendar"></div>
	</div>
	<div region="center" border="false" style="overflow: hidden;">
		<div id="onlinePanel" fit="true" border="false" title="在线用户"  >
			<table id="onlineDatagrid" class="easyui-datagrid" toolbar="#tb">
				<thead>
					<th field="username" width="150">帐号</th>
		            <th field="createTime" width="150">登录时间</th>  
				</thead>
			</table>
			<div id="tb" style="padding-left:5px;height:auto">
			    <input type="text" id="q_account4a" class="easyui-validatebox"  style="width:60%;"/> 
			    <a href="javascript:searchOnline()" class="easyui-linkbutton" iconCls="icon-search"></a> 
			</div>
		</div>
	</div>

	<div id="userOnlineInfoDialog" style="display: none;width: 250px;height: 130px;">
		<table id="userOnlineInfoDataGrid">
			
		</table>
	</div>
</div>