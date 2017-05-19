<%@ page language="java"  pageEncoding="utf-8"%>
<%
response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 0);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script type="text/javascript" charset="utf-8">
    var menuUrl = "<%=basePath%>SysResAction/queryMenuTreeData.action";
	var tree;
	var menuPanel;
	$(function() {
		$.get(menuUrl, {
		},
		function(data) {
			var menus = eval("(" + data + ")")[0].children;
			
			var menuPanel = $('#menu_list');
			var westPanel = $('#indexLayout').layout('panel', 'west');
			westPanel.attr({style: {overflow: 'hidden'}});
			westPanel.html('');
			westPanel.append(menuPanel);
			
			for(var i = 0;i < menus.length;i++){
				var accordion = menus[i];
				if(accordion.children != undefined){//如果有子菜单
					var ul = $('<div style="overflow:auto;" title="' + accordion.text + '"/>').append($('<ul/>').attr({id: accordion.id}));
					menuPanel.append(ul);
					
					//alert(JSON.stringify(node));
					//alert(JSON.stringify(node.children[0]));
					
					var nodeArray = [];
					var node = accordion.children;
					for(var j = 0;j < node.length;j++){
						nodeArray.push(node[j]);
					}
					$("#" + accordion.id).tree();
					$("#" + accordion.id).tree('loadData',nodeArray);
					$("#" + accordion.id).tree({
						checkbox: false,
						cascadeCheck: false,
						onClick : function(node) {
							addTab(node);
						},
						onDblClick : function(node) {
							if (node.state == 'closed') {
								$(this).tree('expand', node.target);
							} else {
								$(this).tree('collapse', node.target);
							}
						}
					});
				}
			}
			menuPanel.accordion();
		});
	});
</script>
<div id="menu_list" class="easyui-accordion" border="false" fit="true">
</div>