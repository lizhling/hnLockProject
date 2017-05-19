<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 0);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script type="text/javascript" charset="utf-8">
	var centerTabs;
	var tabsMenu;
	$(function() {
		tabsMenu = $('#tabsMenu').menu({
			onClick : function(item) {
				var curTabTitle = $(this).data('tabTitle');
				var type = $(item.target).attr('type');
	
				if (type === 'refresh') {
					refreshTab(curTabTitle);
					return;
				}
	
				if (type === 'close') {
					var t = centerTabs.tabs('getTab', curTabTitle);
					if (t.panel('options').closable) {
						centerTabs.tabs('close', curTabTitle);
					}
					return;
				}
	
				var allTabs = centerTabs.tabs('tabs');
				var closeTabsTitle = [];
	
				$.each(allTabs, function() {
					var opt = $(this).panel('options');
					if (opt.closable && opt.title != curTabTitle && type == 'closeOther') {
						closeTabsTitle.push(opt.title);
					} else if (opt.closable && type == 'closeAll') {
						closeTabsTitle.push(opt.title);
					}
				});
	
				for ( var i = 0; i < closeTabsTitle.length; i++) {
					centerTabs.tabs('close', closeTabsTitle[i]);
				}
			}
		});
	
		centerTabs = $('#centerTabs').tabs({
			fit : true,
			border : false,
			onContextMenu : function(e, title) {
				e.preventDefault();
				tabsMenu.menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data('tabTitle', title);
			}
		});
		
		centerTabs.tabs('add', {
			title : "首页",
			content : '<iframe src="<%=basePath%>pages/layout/home.jsp;jsession=<%=session.getId()%>" frameborder="0" style="width:100%;height:100%;"></iframe>'
		});
	});
	
	function addTab(node) {
		if (centerTabs.tabs('exists', node.text)) {
			centerTabs.tabs('select', node.text);
		} else {
			if (node.state != "open") {
				return;
			}
			if (node && node.url && node.url.length > 0) {
				$.messager.progress({
					text : '页面加载中....',
					interval : 100
				});
				window.setTimeout(function() {
					try {
						$.messager.progress('close');
					} catch (e) {
					}
				}, 5000);
				
				centerTabs.tabs('add', {
					title : node.text,
					closable : true,
					content : '<iframe src="<%=basePath%>' + node.url + ';jsession=<%=session.getId()%>" frameborder="0" style="width:100%;height:100%;"></iframe>'
				});
			}
		}
	}
	
	function closeProgress(){
		$.messager.progress('close');
	}
	
	function refreshTab(title) {
		var tab = centerTabs.tabs('getTab', title);
		centerTabs.tabs('update', {
			tab : tab,
			options: tab.panel('options')
		});
	}
</script>
<div id="centerTabs">
	<%--<div title="首页" border="false" style="overflow: hidden;width:100%;height:100%;">
		<iframe src="<%=basePath%>pages/layout/home.jsp;jsession=<%=session.getId()%>" frameborder="0" style="width:100%;height:100%;"></iframe>
	</div>
--%></div>
<div id="tabsMenu" style="width: 120px;display:none;">
	<div type="refresh">刷新</div>
	<div class="menu-sep"></div>
	<div type="close">关闭</div>
	<div type="closeOther">关闭其他</div>
	<div type="closeAll">关闭所有</div>
</div>