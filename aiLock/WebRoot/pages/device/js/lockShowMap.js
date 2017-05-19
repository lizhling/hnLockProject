	//初始化数据列表
	var map;
	var local;
	var myCity;
	var tempAreaId;
	function initializeMapInfo() {
		// 百度地图API功能
		map = new BMap.Map("allmap"); // 创建地图实例
		var point = new BMap.Point(116.403694, 39.927552); // 创建点坐标
		map.centerAndZoom(point, 12); // 初始化地图，设置中心点坐标和地图级别
		map.enableScrollWheelZoom();
		map.addControl(new BMap.NavigationControl()); //添加默认缩放平移控件
		
		myCity = new BMap.LocalCity();//根据IP定位
		myCity.get(myFun);
		
		local = new BMap.LocalSearch(map, {
			renderOptions:{map: map}
		});
		
		$('#q_basicDataId').combotree({
			onClick: function(node){
				var city = node.text;
				map.centerAndZoom(city,11);      // 用城市名设置地图中心点
				
				var str = JSON.stringify(node);
				var jsonobj=eval('('+str+')');
				tempAreaId=jsonobj.id;
				searchLock1();
			}
		});
	}
	
	function searchLock1(){
		if(tempAreaId==20){     //20 表示sys_area表中的中国，因数据太大，不允许查看
			alert("不能查看全省！！！");
			return;
		}
		$.get(findDevLockInfoListUtl, {
			'dto.areaId':tempAreaId
		},
		function(data) {
			var repObj = eval("(" + data + ")");
		  	var dataArray = repObj.rows;
		  	if (dataArray != 0 && dataArray.length > 0) {
		  		map.clearOverlays();
		  		var new_point = null;
	  	        for (var i in dataArray) {
			  		if (dataArray[i]) {
			  			new_point = new BMap.Point(dataArray[i].longitude, dataArray[i].latitude)
			  			var marker = new BMap.Marker(new_point);// 创建标注
			     		var contentTitle = dataArray[i].lockName + "（"+dataArray[i].lockCode+"）";
			     		var content = "地址：" + dataArray[i].lockAddres + "<br>经度：" + dataArray[i].longitude + " <br>纬度：" + dataArray[i].latitude;
			     		map.addOverlay(marker); // 将标注添加到地图中
			     		
			     		addClickHandler(contentTitle, content, marker);
			  		}
	  	        }
	  	        if($("#q_lockCode").val() != "" || $("#q_lockName").val() != ""){
	  	        	map.panTo(new_point);//定位到标注位置
	  	        }
		  	}else{
		  		$.messager.show({
					msg : '没有查询到相关门锁信息',
					title : '错误'
				});
		  	}
		});
		}
	
	
	function searchLock(){
		$.get(findDevLockInfoListUtl, {
			'dto.lockCode':$("#q_lockCode").val(),
			'dto.lockName':$("#q_lockName").val()
		},
		function(data) {
			var repObj = eval("(" + data + ")");
		  	var dataArray = repObj.rows;
		  	if (dataArray != 0 && dataArray.length > 0) {
		  		map.clearOverlays();
		  		var new_point = null;
	  	        for (var i in dataArray) {
			  		if (dataArray[i]) {
			  			new_point = new BMap.Point(dataArray[i].longitude, dataArray[i].latitude)
			  			var marker = new BMap.Marker(new_point);// 创建标注
			     		var contentTitle = dataArray[i].lockName + "（"+dataArray[i].lockCode+"）";
			     		var content = "地址：" + dataArray[i].lockAddres + "<br>经度：" + dataArray[i].longitude + " <br>纬度：" + dataArray[i].latitude;
			     		map.addOverlay(marker); // 将标注添加到地图中
			     		
			     		addClickHandler(contentTitle, content, marker);
			  		}
	  	        }
	  	        if($("#q_lockCode").val() != "" || $("#q_lockName").val() != ""){
	  	        	map.panTo(new_point);//定位到标注位置
	  	        }
		  	}else{
		  		$.messager.show({
					msg : '没有查询到相关门锁信息',
					title : '错误'
				});
		  	}
		});
		
//		var data_info = [
//         	[113.32691,23.12643,"华成路29号机柜锁","地址：广东省广州市天河区华成路29号"],
//     		[113.33334,23.12696,"东路11锁","地址：广东省广州市天河区珠江东路11"],
//     		[116.412222,39.912345,"正义路甲5号","地址：北京市东城区正义路甲5号"]
//     	];
//     	
//     	for ( var i = 0; i < data_info.length; i++) {
//     		var marker = new BMap.Marker(new BMap.Point(data_info[i][0], data_info[i][1])); // 创建标注
//     		var contentTitle = data_info[i][2];
//     		var content = data_info[i][3];
//     		map.addOverlay(marker); // 将标注添加到地图中
//     		
//     		addClickHandler(contentTitle, content, marker);
//     	}
	}
	
	var opts = {
 		width : 250, // 信息窗口宽度
 		height : 80, // 信息窗口高度
 		title : "门锁信息", // 信息窗口标题
 		enableMessage : false//设置允许信息窗发送短息
 	};
	
	function addClickHandler(contentTitle, content, marker) {
		//click:点击事件
 		marker.addEventListener("mouseover", function(e) {
 			openInfo(contentTitle, content, e)
 		});
// 		marker.addEventListener("mouseout", function(e) {
// 			map.closeInfoWindow();
// 		});
 	}
 	
 	function openInfo(contentTitle, content, e) {
 		var p = e.target;
 		var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
 		opts.title = contentTitle;
 		var infoWindow = new BMap.InfoWindow(content, opts); // 创建信息窗口对象 
 		map.openInfoWindow(infoWindow, point); //开启信息窗口
 	}
 	
	
	function myFun(result){
		var cityName = result.name;
		map.setCenter(cityName);
	}
	

	function search(){
		var searchName = document.getElementById("q_searchName").value;
		if(searchName != ""){
			local.search(searchName);
		}
		addCustomLayer();
	}
	
	/*var customLayer;
	function addCustomLayer(keyword) {
		if (customLayer) {
			map.removeTileLayer(customLayer);
		}
		customLayer = new BMap.CustomLayer({
			geotableId: 99092
		});
		map.addTileLayer(customLayer);
		//customLayer.addEventListener('hotspotclick',callback);
	}
	
	//单击热点图层
	function callback(e){
		var customPoi = e.customPoi;//poi的默认字段
		var contentPoi=e.content;//poi的自定义字段
		var content = '<p style="width:280px;margin:0;line-height:20px;">地址：' + customPoi.address + '<br/>价格:'+contentPoi.dayprice+'元'+'</p>';
		var searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
			title: customPoi.title, //标题
			width: 290, //宽度
			height: 40, //高度
			panel : "panel", //检索结果面板
			enableAutoPan : true, //自动平移
			enableSendToPhone: true, //是否显示发送到手机按钮
			searchTypes :[
				BMAPLIB_TAB_SEARCH,   //周边检索
				BMAPLIB_TAB_TO_HERE,  //到这里去
				BMAPLIB_TAB_FROM_HERE //从这里出发
			]
		});
		var point = new BMap.Point(customPoi.point.lng, customPoi.point.lat);
		searchInfoWindow.open(point);
	}*/

	