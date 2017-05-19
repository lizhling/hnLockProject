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
	var recordQueryUrl = basePath + "SysBasicDataAction/findPageSysBasicData.action";
	var recordSaveUrl = basePath + "SysBasicDataAction/saveSysBasicData.action";
	var recordDeleteUrl = basePath + "SysBasicDataAction/deleteSysBasicData.action";
	
	var findSysBasicDataListUrl = basePath + "SysBasicDataAction/findSysBasicDataList.action";
	
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
<script type="text/javascript" charset="utf-8" src="js/sys_basic_data.js" ></script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:110px;padding:10px;">
		<table width="80%">
		<tr>
		    <td style="text-align: right">类型名称：</td>
			<td style="text-align: left">
				<input id="q_typeName" name="q_typeName"/>
			</td>
			<td style="text-align: right">类型编码：</td>
			<td style="text-align: left">
				<input id="q_typeCode" class="easyui-validatebox" name="q_typeCode"/>
	        </td>
			<td>
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;&nbsp;
			</td>
		</tr>
		<tr>
			<td style="text-align: right">类型标识：</td>
			<td style="text-align: left">
				<input id="q_typeTag" name="q_typeTag"/>
			</td>
			<td style="text-align: right">上级类型名称：</td>
			<td style="text-align: left">
				<input id="q_parentName" name="q_parentName"/>
			</td>
		</tr>
		</table>
	</div>

	<!-- 网格列表 -->
	<div region="center" style="overflow:hidden;border:0;">
		<table id="datagrid"></table>
	</div>
	
	<!-- 新增、修改面板。 -->
	<div id="example-window" class="easyui-window" title="系统基础数据信息" style="width:655px;height:330px;padding:5px;background: #fafafa;" data-options="iconCls:'icon-save',minimizable:false,closed:'true'">
		<div region="center" style="padding:5px;background:#fff;border:1px solid #ccc;">
			<form method="post" >
			<input type="hidden" id="basicDataId" name="sbd[0].basicDataId"/>
			<input type="hidden" id="parentId" name="sbd[0].parentId"/>
			<fieldset id="id_field_base" style="padding:5;" align="center">
			<legend><font color="#0000FF">类型信息配置 </font></legend> 
				<table width="100%">
					<tr>
						<td align="right" width="10%">类型名称：</td>
						<td width="14%">
							<input id="typeName" name="sbd[0].typeName" class="easyui-validatebox" data-options="required:true"style="width:100%"></input>
						</td>
						<td align="right" width="9%">编码：</td>
						<td width="15%">
							<input id="typeCode" name="sbd[0].typeCode" class="easyui-validatebox" style="width:100%"></input>
						</td>
						<td align="right" width="12%">类型标识：</td>
						<td width="15%">
							<input id="typeTag" name="sbd[0].typeTag" class="easyui-validatebox" data-options="required:true" style="width:100%" readonly="readonly"></input>
						</td>
						<td width="4%">
						</td>
					</tr>
					<tr>
						<td align="right">备注：</td>
						<td colspan="6">
							<input id="memo" name="sbd[0].memo" class="easyui-validatebox" style="width:94%"></input>
						</td>
					</tr>
				</table>
		    </fieldset>
			<br/>
			<fieldset id="id_field_base" style="padding:5;" align="center">
			<legend><font color="#0000FF">子类型信息配置 </font></legend>
				<div id="sonDiv" style="width:100%;height:110px;">
					<table id="levelTABID" width="100%">
					</table>
				</div>
			</fieldset>
			</form>
		</div>
		<div style="text-align:right;height:30px;line-height:24px;padding-top:5px;">
			<a id="add_button"class="easyui-linkbutton c6" iconCls="icon-add" href="javascript:void(0)" onclick="addOnclick()" style="width:100px;">添加子类</a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a id="save_button" class="easyui-linkbutton c6" iconCls="icon-ok" href="javascript:void(0)" onclick="submitForm()">保存</a>
			<a id="cancl_button" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeWindow()">取消</a>
		</div>
	</div>
</body>
</html>