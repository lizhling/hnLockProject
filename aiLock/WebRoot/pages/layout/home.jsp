<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="<%=basePath%>/javascript/jquery-easyui-1.4.2/jquery.min.js" charset="utf-8"></script>
<link id="easyuiTheme" rel="stylesheet" href="<%=basePath%>/javascript/jquery-easyui-1.4.2/themes/default/easyui.css" type="text/css"></link>
<!-- <link id="easyuiTheme" rel="stylesheet" href="<%=basePath%>/javascript/jquery-easyui-1.4.2/themes/gray/easyui.css" type="text/css"></link> -->
<link rel="stylesheet" href="<%=basePath%>/javascript/jquery-easyui-1.4.2/themes/color.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath%>/javascript/jquery-easyui-1.4.2/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/javascript/jquery-easyui-1.4.2/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

<script type="text/javascript" charset="utf-8" src="<%=basePath%>/javascript/highcharts.js" ></script>
<script type="text/javascript" charset="utf-8" src="<%=basePath%>/javascript/cxUtil.js" ></script>
<!-- <script type="text/javascript" charset="utf-8" src="<%=basePath%>/javascript/highcharts-3d.js" ></script> -->

<script type="text/javascript" charset="utf-8" src="<%=basePath%>/javascript/DataGrid.js"></script>
<style type="text/css">
.easyui-fluid .panel-header {
	border-top-width: 1px;
	border-radius: 5px 5px 0 0 !important;
	/*filter:;
	background-color: #009966 !important;*/
	
}
.panel-body {
  
}
.dottedLine {
	border-top:1px dashed #cccccc;
	height: 1px;
	overflow:hidden;
	padding: 3px 0px 3px;
	clear: both;
}
a{
	text-decoration:none;
}
.solidLine {
	border-top:1px solid #cccccc;
	padding: 0 0 5px 0;
	clear: both;
}
</style>
<script type="text/javascript" charset="utf-8">
	var basePath = "<%=basePath%>";
	var findUnlockRecordsListUrl = basePath + "UnlockRecordsAction/findUnlockRecordsList.action?dto.selectType=2&dto.ifDeal=0";
	var findSysNoticeListUrl = basePath + "SysNoticeAction/findSysNoticeList.action";
	var findAreaStatisticalLockUrl = basePath + "DevLockInfoAction/findAreaStatisticalLock.action";
	var findOrgStLockRecordsUrl = basePath + "UnlockRecordsAction/findOrgStLockRecords.action";
	
	$(function() {
		Highcharts.getOptions().colors = Highcharts.map(Highcharts.getOptions().colors, function (color) {
	        return {
	            radialGradient: {
	                cx: 0.5,
	                cy: 0.3,
	                r: 0.7
	            },
	            stops: [
	                [0, color],
	                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
	            ]
	        };
	    });
		
		findAreaSt();
		findOrgStLockRecords();
		findUnlockRecords();
		findSysNoticeList();
		
		$('#alarmDiv').panel({
			title: "最新告警<div style='float:right;font-family:Dotum;'><a href='javascript:void(0);' onclick='openLockAlarmPage()'>more</a></div>"
		})
		$('#noticeDiv').panel({
			title: "系统公告<div style='float:right;font-family:Dotum;'><a href='javascript:void(0);' onclick='openSysNoticePage()'>more</a></div>"
		})
	});
	
	var areaAeries = new Array();
	var findAreaSt = function(){
		$.get(findAreaStatisticalLockUrl,{
		},
		function(data){
			var repObj = eval("(" + data + ")");
		  	var dataArray = repObj.rows;
		  	var items = [];
		  	if (dataArray && dataArray.length > 0) {
			  	for (var x = 0; x < dataArray.length; x++) {
			  		if(x == 0){
	                    areaAeries.push({name:dataArray[x][2],y:dataArray[x][3],sliced: true,selected: true});
			  		}else{
				  		areaAeries.push({name:dataArray[x][2],y:dataArray[x][3]});
			  		}
			  	}
			  	showAreaSt();
		  	}
		});
	}
	
	var showAreaSt = function(){
	    $('#containerLock').highcharts({
	        chart: {
	            plotBackgroundColor: null,
	            plotBorderWidth: null,
	            plotShadow: false,
	            type: 'pie'
	        },
	        title: {
	            text: '门锁分布统计'
	        },
	        tooltip: {
	            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	        },
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: true,
	                    format: '<b>{point.name}</b>: {point.y:.0f} 个',
	                    style: {
	                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
	                    },
	                    connectorColor: 'silver'
	                }
	            }
	        },
	        series: [{
	            name: "占比",
	            data: areaAeries
	        }]
	    });
	}
	
	var orgCategories = new Array();
	var orgSeriesUnl = new Array();
	var orgSeriesAla = new Array();
	var orgPie = new Array();
	var findOrgStLockRecords = function(){
		$.get(findOrgStLockRecordsUrl,{
		},
		function(data){
			var repObj = eval("(" + data + ")");
		  	var dataArray = repObj.rows;
		  	var items = [];
		  	var unlSum = 0;
		  	var alaSum = 0;
		  	if (dataArray && dataArray.length > 0) {
			  	for (var x = 0; x < dataArray.length; x++) {
			  		orgCategories.push(dataArray[x][2]);
			  		orgSeriesUnl.push(dataArray[x][3]);
			  		orgSeriesAla.push(dataArray[x][4]);
			  		
			  		unlSum += dataArray[x][3];
			  		alaSum += dataArray[x][4];
			  	}
		  		orgPie.push({name:"开锁", y:unlSum,color:Highcharts.getOptions().colors[10]});
		  		orgPie.push({name:"告警", y:alaSum, color:Highcharts.getOptions().colors[8]});
			  	showOrgSt();
		  	}
		});
	}
	
	var showOrgSt = function(){
		$('#container').highcharts({
	        title: {
	            text: '开锁、告警统计'
	        },
	        subtitle: {
	            text: ''
	        },
	        xAxis: {
	            categories: orgCategories,
	            crosshair: true
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '次数'
	            }
	        },
	        labels: {
	            items: [{
	                html: '总次数',
	                style: {
	                    left: '32px',
	                    top: '-22px',
	                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'black'
	                }
	            }]
	        },
	        tooltip: {
	            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                '<td style="padding:0"><b>{point.y:.0f} 次</b></td></tr>',
	            footerFormat: '</table>',
	            shared: true,
	            useHTML: true
	        },
	        plotOptions: {
	            column: {
	                pointPadding: 0.2,
	                borderWidth: 0
	            }
	        },
	        series: [{
	        	type: 'column',
	            name: '开锁',
	            data: orgSeriesUnl,
	            color: '#7798BF'
	        }, {
	        	type: 'column',
	            name: '告警',
	            data: orgSeriesAla,
	            color: '#DF5353'
	        }, {
                type: 'pie',
                name: '总共',
                data: orgPie,
                center: [30, 4],
                size: 60,
                showInLegend: false,
                dataLabels: {
                    enabled: false
                }
            }]
	    });
	}
	
	
	var findUnlockRecords = function(){
		$.get(findUnlockRecordsListUrl,{
		},
		function(data){
			var repObj = eval("(" + data + ")");
		  	var dataArray = repObj.rows;
		  	var items = [];
		  	if (dataArray && dataArray.length > 0) {
		  		var lockName;
			  	for (var x=0;x<dataArray.length;x++) {
			  		var ulr = dataArray[x];
			  		var puttop = "";
		  			lockName = ulr.lockName;
			  		if(isUndefined(ulr.lockName)){
			  			lockName = "未知门锁名称";
			  		}
		  			items.push("<li><div style='float:left;'>"+
					        		   "<a href='javascript:void(0);' style='color: #FF0000' onclick='showAlarmRecord("+ulr.recordId+")'>门锁："+lockName+"（"+ulr.recordTpye+"）</a>"+
					        	   "</div><div style='float:right;padding:0 3px 1px 0;'>"+ulr.unlockTime.substring(0,16)+"</div></li>");
		  			items.push("<div class='dottedLine'></div>");
		  			$("#alarm").append(items.join(''));
		  			items = [];
			  	}
		  	}
		  	if(repObj.resultCode == "00"){
			  	$("#alarmCon").html("&emsp;&emsp;未处理告警：（<span style='font-weight:bold;color:#FF0033;'>"+repObj.resultMessage+"条</span>）");
		  	}else{
		  		$("#alarmCon").html("获取最新告警失败！");
		  	}
		});
	}
	
	function showAlarmRecord(recordId){
		viewLockRecords("告警记录查询",  basePath + '/pages/analysis/alarmRecords.jsp?recordId='+recordId);
	}
	
	function openLockAlarmPage(){
		viewLockRecords("告警记录查询",  basePath + '/pages/analysis/alarmRecords.jsp');
	}
	
	function openSysNoticePage(){
		viewLockRecords("系统公告查询",  basePath + '/pages/system/sys_notice_query.jsp');
	}
	
	var noticeData;
	var findSysNoticeList = function(){
		$.get(findSysNoticeListUrl,{
		},
		function(data){
			var repObj = eval("(" + data + ")");
		  	noticeData = repObj.rows;
		  	var items = [];
		  	if (noticeData && noticeData.length > 0) {
			  	for (var x = 0; x < noticeData.length; x++) {
			  		var ulr = noticeData[x];
			  		var puttop = "";
		  			items.push("<li><div style='float:left;'>"+
					        		   "<a href='javascript:void(0);' style='color: #000000' onclick='showNotice("+ulr.noticeId+")'>"+ulr.title+"</a>"+
					        	   "</div><div style='float:right;padding:0 3px 1px 0;'>"+ulr.releaseTime.substring(0,16)+"</div></li>");
		  			items.push("<div class='dottedLine'></div>");
		  			$("#notice").append(items.join(''));
		  			items = [];
			  	}
		  	}
		  	if(!repObj.resultCode == "00"){
		  		$("#notice").html("获取最新公告失败！");
		  	}
		});
	}
	
	function showNotice(noticeId){
		$('#noticeWin').dialog({
		    modal:true,
		    onClose: function() {
				$("#noticeWin").empty();
			}
		});
		$('#noticeWin').window('open');
		
	  	for (var x = 0; x < noticeData.length; x++) {
	  		var ulr = noticeData[x];
	  		if(ulr.noticeId == noticeId){
	  			var reTime = noticeData[x].releaseTime;
	  			var showReTime = reTime.substring(0,4) + " 年 " + reTime.substring(5,7) + " 月 " + reTime.substring(8,10) + " 日   " +
	  			 				 reTime.substring(11,13) + " 时 " + reTime.substring(14,16) + " 分";
	  			$("#noticeWin").append("<h3 style='text-align: center;'>"+noticeData[x].title+"</H3>");
	  			$("#noticeWin").append("<H4 style='text-align: center;'>"+showReTime+"</H4>");
	  			$("#noticeWin").append("<div class='solidLine'></div>");
	  			$("#noticeWin").append("<H7>"+noticeData[x].content.replace(/\n/g,"<br>")+"</H7>");
	  		}
	  	}
	}
	
</script>
</head>
<body class="easyui-layout" style="width:99.8%;height:99.9%;border-color: #95b8e7;border-width: 0 1px 1px 1px;border-style:solid">
	<div style="width:48.8%;height:36%;padding:5px 5px 5px 8px;float:left;">
		<div id="alarmDiv" class="easyui-panel" title="最新告警" style="width:100%;height:100%;padding:1px; border-radius: 0 0 5px 5px !important;">
	        <p id="alarmCon" style="font-weight:bold;">&emsp;&emsp;未处理告警：0条</p>
	        <ul id="alarm">
	        </ul>
	    </div>
	</div>
	<div style="width:48.8%;height:36%;padding:5px 8px 5px 5px;float:right;">
		<div id="noticeDiv" class="easyui-panel" title="系统公告" style="width:100%;height:100%;padding:1px;border-radius: 0 0 5px 5px !important;">
	        <ul id="notice">
	        	<%--<li>
	        		<div style="float:left;">
	        			<a href="javascript:void(0);" style="color: #000000" onclick="showNotice('sdsd')">系统将于2015-12-12 20:00:00 至 2015-12-12 20:10:00 更新</a>
	        		</div>
	           		<div style="float:right;padding:0 3px 1px 0;"> 2015-12-12 20:10:00</div>
	            </li>
           		<div class='dottedLine'></div>
			--%></ul>
	    </div>
	</div>
	<div style="width:48.8%;height:60%;padding:3px 5px 5px 8px;float:left;">
		<div id="container" class="easyui-panel" title="" style="width:100%;height:100%;padding:1px;border-radius: 5px !important;">
	    </div>
	</div>
	<div style="width:48.8%;height:60%;padding:3px 8px 5px 5px;float:right;">
		<div id="containerLock" class="easyui-panel" title="" style="width:100%;height:100%;padding:1px;border-radius: 5px !important;">
	    </div>
	</div>
	<div id="noticeWin" class="easyui-dialog" title="公告内容" style="border-top-width: 0px !important;text-align:center; width:755px;height:450px;padding:1px"
			data-options="resizable:true,closed:'true'">
	</div>
  </body>
</html>