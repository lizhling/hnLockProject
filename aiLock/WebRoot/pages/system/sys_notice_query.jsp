<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include.jsp"></jsp:include>
<style type="text/css">
.solidLine {
	border-top:1px solid #cccccc;
	padding: 0 0 5px 0;
	clear: both;
}
</style>
<script type="text/javascript" charset="utf-8">
	
	var recordQueryUrl = "<%=basePath%>SysNoticeAction/findPageSysNotice.action";
	
	var grid;
	var win = null;
	var form = null;
	var noticeData = undefined;
	
	$(function() {
		initDataGrid();
		initComboboxDate();
	});
	
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'noticeId',//表明该列是一个唯一列
				columns:[[
				    {title: '编号',field: 'noticeId',checkbox: true,width: 10},
					{title: '公告标题',field: 'title',sortable: true,width: 500},
					{title: '发布时间',field: 'releaseTime',width: 150}
				]],
				toolbar:null
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.title':$("#q_title").val(),
			'dto.status':$("#q_status").combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(noticeStatusGroup,'q_status');
		dataToSelect(isOrNoGroup,'q_isPuttop');
	}
	
	function edit(){
		var rows = grid.datagrid('getSelections');
		if (rows.length == 1) {
			noticeData = rows[0];
		}
		
		$('#noticeWin').dialog({
		    modal:true,
		    onClose: function() {
				$("#noticeWin").empty();
			}
		});
		$('#noticeWin').window('open');
		
		var reTime = noticeData.releaseTime;
		var showReTime = reTime.substring(0,4) + " 年 " + reTime.substring(5,7) + " 月 " + reTime.substring(8,10) + " 日   " +
		 				 reTime.substring(11,13) + " 时 " + reTime.substring(14,16) + " 分";
		$("#noticeWin").append("<h3 style='text-align: center;'>"+noticeData.title+"</H3>");
		$("#noticeWin").append("<H4 style='text-align: center;'>"+showReTime+"</H4>");
		$("#noticeWin").append("<div class='solidLine'></div>");
		$("#noticeWin").append("<H7>"+noticeData.content.replace(/\n/g,"<br>")+"</H7>");
	}
	
	function closeWindow(){
		win.window('close');
	};

	
</script>
</head>
<body class="easyui-layout">
	<div region="north" title="条件查询" iconCls="icon-search" split="true" style="height:110px;padding:10px;">
		<table width="80%">
		<tr>
		    <td style="text-align: right">公告标题：</td>
			<td style="text-align: left">
				<input id="q_title" name="q_title"/>
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
	
	<div id="noticeWin" class="easyui-dialog" title="公告内容" style="border-top-width: 0px !important;text-align:center; width:755px;height:450px;padding:1px"
			data-options="resizable:true,closed:'true'">
	</div>
</body>
</html>