<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8" src="<%=basePath%>/javascript/highcharts.js" ></script>

<script type="text/javascript" charset="utf-8">
$(function() {
	parent.closeProgress();
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

	    // Build the chart
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
	            data: [
	                {name: "郑州市", y: 6633,
	                    sliced: true,
	                    selected: true},
	                {name: "新乡市", y: 2403},
	                {name: "洛阳市", y: 3038},
	                {name: "信阳市", y: 1737},
	                {name: "南阳市", y: 912},
	                {name: "许昌市", y: 2432}
	            ]
	        }]
	    });
	 adssd();
});

function adssd(){
	$('#container').highcharts({
		 chart: {
	            type: 'column'
	        },
	        title: {
	            text: '开锁、告警统计'
	        },
	        subtitle: {
	            text: ''
	        },
	        xAxis: {
	            categories: [
	 		        '公共',
	                '移动',
	                '联通',
	                '电信'
	            ],
	            crosshair: true
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '次数'
	            }
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
	            name: '开锁',
	            data: [600,499, 815, 764],
	            color: '#7798BF'
	        }, {
	            name: '告警',
	            data: [10,56, 158, 105],
	            color: '#f45b5b'

	        }]
	    });
}
	
</script>
</head>
<body class="easyui-layout">
	<div style="width:48%;height:56%;padding:3px 5px 5px 8px;float:left;">
		<div id="container" class="easyui-panel" title="" style="width:100%;height:100%;padding:1px;">
	    </div>
	</div>
	<div style="width:48%;height:56%;padding:3px 8px 5px 5px;float:right;">
		<div id="containerLock" class="easyui-panel" title="" style="width:100%;height:100%;padding:1px;">
	    </div>
	</div>
</body>
</html>