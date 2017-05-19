	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'basicDataId',//表明该列是一个唯一列
				columns:[[
				    {title: '编号',field: 'basicDataId',checkbox: true,width: 50},
					{title: '类型名称',field: 'typeName',sortable: true,width: 150},
					{title: '类型编码',field: 'typeCode',sortable: true,width: 150},
					{title: '类型标识',field: 'typeTag',sortable: true,width: 150},
					{title: '上级类型',field: 'parentName',width: 150},
					{title: '备注',field: 'memo',width: 150}
				]]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.typeName':$("#q_typeName").val(),
			'dto.typeCode':$("#q_typeCode").val(),
			'dto.typeTag':$("#q_typeTag").val(),
			'dto.parentName':$("#q_parentName").val()
		});
	}
	
	function initComboboxDate(){
		dataToSelect(resTypeGroup,'resType');
		dataToSelect(resTypeGroup,'q_resType');
	}
	
	function closeWindow(){
		win.window('close');
	};

	function add() {
		isNewRecord = true;
		initEditWindow();
	}

	function edit() {
		isNewRecord = false;
		var rows = grid.datagrid('getSelections');
		if (rows.length == 1) {
			editRow = rows[0];
			initEditWindow();
		} else {
			$.messager.show({
				msg : '请选择一项进行修改！',
				title : '错误'
			});
		}
	}
	
	function initEditWindow() {
		$("#levelTABID").html("");
		$("#sonDiv").height(110);
		$("#sonDiv").css("overflow-y","hidden");
		rowCount = 0;
		
		win = $('#example-window').window({  
		    modal:true
		});
		win.window('open');
		form = win.find('form');
		
		if (!isNewRecord && editRow != null) {
			Net.functions.initForm(form,editRow,true);
			findSonSbdList();//加载子基础数据类型
			$("#typeTag").attr("readonly","readonly");
		}else{
			form.form('clear');
			editRow = null;
			$("#typeTag").removeAttr("readonly");
		}
	}
	
	//保存数据
	function submitForm(){
		Net.functions.saveRow(win, form, grid,recordSaveUrl);
	};
	
	//删除数据
	function deleteRows(){
		Net.functions.batchRows(grid, recordDeleteUrl, '删除');
	};
	
	
	//表格添加行
	var rowCount = 0;
	function addOnclick(){
		highlyDynamic();

		rowCount++;
	    
	    var items = [];
	    items.push('<tr class="tr_'+rowCount+'" valign="top">');
		items.push('<td width="10%" align="right"><input type="hidden" id="sonBasicDataId'+rowCount+'" name="sbd['+rowCount+'].basicDataId">类型名称：</td>');
		items.push('<td width="14%"><input size="12" id="sonTypeName'+rowCount+'" name="sbd['+rowCount+'].typeName"/></td>');
		items.push('<td width="8%" align="right">编码：</td><td width="14%"><input size="10" id="sonTypeCode'+rowCount+'" name="sbd['+rowCount+'].typeCode" /></td>');
		items.push('<td width="8%" align="right">备注：</td><td width="15%"><input size="12" id="sonMemo'+rowCount+'" name="sbd['+rowCount+'].memo" /></td>');
		items.push('<td width="9%">&nbsp;&nbsp;&nbsp;<img onclick="addOnclick()" title="新增子类型" src="'+basePath+'javascript/jquery-easyui-1.4.2/themes/icons/edit_add.png"></img>');
		items.push('&nbsp;&nbsp;&nbsp;<img onclick=delObj(this,"") title="删除子类型" src="'+basePath+'javascript/jquery-easyui-1.4.2/themes/icons/edit_remove.png"></img></td>');
		items.push('</tr>');
		
		$("#levelTABID").append(items.join(''));
	}
	 
	//删除子类型数据
	function delObj(obj,basicDataId){
		if (!isNewRecord && editRow != null && basicDataId != '') {
			$.get(findSysBasicDataListUrl, {
				'dto.parentId' : basicDataId
			},
			function(data) {
				var repObj = eval("(" + data + ")");
			  	var dataArray = repObj.rows;
			  	if (dataArray != 0  && dataArray.length > 0) {
			  		$.messager.alert('错误', "该类型有子类型，不能被删除！", 'error');
			  	}else{
			  		delRow(obj);
			  	}
		  	});
		}else{
			delRow(obj);
		}
	}
	
	//表格删除行
	function delRow(obj){
		var table = document.getElementById("levelTABID");
		var i = obj.parentNode.parentNode.rowIndex;
		table.deleteRow(i);
		rowCount--;
		
		if(rowCount > 3 && rowCount < 6){
			win.window('resize',{
				height: 330 + (rowCount - 4) * 25
			})
			$("#sonDiv").height(110 + (rowCount - 4) * 25);
		}
		
		if(rowCount > 6){
			$("#sonDiv").css("overflow-y","scroll");
		}else if(rowCount <= 6){
			$("#sonDiv").css("overflow-y","hidden");
		}
	}
	
	function highlyDynamic(){
		if(rowCount >= 3 && rowCount < 6){
			win.window('resize',{
				height: 330 + (rowCount - 3) * 25
			})
			$("#sonDiv").height(110 + (rowCount - 3) * 25);
		}
		
		if(rowCount > 5){
			$("#sonDiv").css("overflow-y","scroll");
		}else if(rowCount <= 5){
			$("#sonDiv").css("overflow-y","hidden");
		}
	}

	
    //关闭
	function closeWindow(){
		win.window('close');
	};
	
	function findSonSbdList(){
		$.get(findSysBasicDataListUrl, {
			'dto.parentId' : editRow.basicDataId
		},
		function(data) {
			var items = [];
			var repObj = eval("(" + data + ")");
		  	var dataArray = repObj.rows;
		  	
  	        for (var x in dataArray) {
		  		if (dataArray[x]) {
		  			highlyDynamic();
		  			
		  			rowCount++;

		  			items.push('<tr class="tr_'+rowCount+'">');
		  			items.push('<td width="10%" align="right"><input type="hidden" id="sonBasicDataId'+rowCount+'" name="sbd['+rowCount+'].basicDataId" value="'+dataArray[x].basicDataId+'">类型名称：</td>');
					items.push('<td width="14%"><input size="12" id="sonTypeName'+rowCount+'" name="sbd['+rowCount+'].typeName" value="'+dataArray[x].typeName+'"/></td>');
					items.push('<td width="8%" align="right">编码：</td><td width="14%"><input size="10" id="sonTypeCode'+rowCount+'" name="sbd['+rowCount+'].typeCode" value="'+undefinedShift(dataArray[x].typeCode)+'"/></td>');
					items.push('<td width="8%" align="right">备注：</td><td width="15%"><input size="12" id="sonMemo'+rowCount+'" name="sbd['+rowCount+'].memo" value="'+undefinedShift(dataArray[x].memo)+'"/></td>');
					items.push('<td width="9%">&nbsp;&nbsp;&nbsp;<img onclick="addOnclick()" title="新增子类型" src="'+basePath+'javascript/jquery-easyui-1.4.2/themes/icons/edit_add.png"></img>');
					items.push('&nbsp;&nbsp;&nbsp;<img onclick=delObj(this,'+dataArray[x].basicDataId+') title="删除子类型" src="'+basePath+'javascript/jquery-easyui-1.4.2/themes/icons/edit_remove.png"></img></td>');
					items.push('</tr>');
					
					$("#levelTABID").append(items.join(''));
					items = [];
		  		}
  	        }
		});
	}
