<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

	String easyuiThemeName = "default";
	Cookie cookies[] = request.getCookies();
	if (cookies != null && cookies.length > 0) {
		for(int i=0; i<cookies.length; i++) {
			Cookie cookie = cookies[i];
			if ("easyuiThemeName".equals(cookie.getName())) {
				easyuiThemeName = cookie.getValue();
				break;
			}
		}
	}
%>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9 IE9" />
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<!-- <meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta http-equiv="content-type" content="text/html; charset=utf-8"> -->
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<!-- <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page"> -->
<script type="text/javascript">
	//如果不是在框架中打开，跳转到框架中再打开，避免打开单个页面。
	var hrefs = window.location.href;
	var jspName = "";
	var findx = hrefs.indexOf("?");
	if(findx >= 0){
		jspName = hrefs.substring(0, findx);
	}else{
		jspName = hrefs;
	}
	if ((!parent.centerTabs) && jspName.indexOf("main.jsp") < 0
			&& jspName.indexOf("commonRefer") < 0) {
		window.location.href="<%=basePath%>/pages/main.jsp";
	}
</script>
<!-- easyui -->
<script type="text/javascript" src="<%=basePath%>/javascript/jquery-easyui-1.4.2/jquery.min.js" charset="utf-8"></script>
<link id="easyuiTheme" rel="stylesheet" href="<%=basePath%>/javascript/jquery-easyui-1.4.2/themes/default/easyui.css" type="text/css"></link>
<!-- <link id="easyuiTheme" rel="stylesheet" href="<%=basePath%>/javascript/jquery-easyui-1.4.2/themes/gray/easyui.css" type="text/css"></link> -->
<link rel="stylesheet" href="<%=basePath%>/javascript/jquery-easyui-1.4.2/themes/icon.css" type="text/css"></link>
<link rel="stylesheet" href="<%=basePath%>/javascript/jquery-easyui-1.4.2/themes/color.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath%>/javascript/jquery-easyui-1.4.2/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/javascript/jquery-easyui-1.4.2/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/javascript/jquery-easyui-1.4.2/jquery.slimscroll.min.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/javascript/jquery-easyui-1.4.2/plugins/jquery.messager.js" charset="utf-8"></script>

<link rel="stylesheet" href="<%=basePath%>/css/styles.css" type="text/css"></link>
 
<!-- NetJs -->
<script type="text/javascript" src="<%=basePath%>/javascript/libs/NetJs/core/core.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/javascript/libs/NetJs/core/temple.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/javascript/libs/NetJs/core/dateHelper.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/javascript/libs/NetJs/core/functions.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/javascript/libs/NetJs/core/validate.js" charset="utf-8"></script>

<script type="text/javascript" src="<%=basePath%>/javascript/dataDictionary.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/javascript/cxUtil.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/javascript/DataGrid.js" charset="utf-8"></script>

