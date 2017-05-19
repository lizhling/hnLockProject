	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'authorizeId',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
				    {title: '授权批次码',field: 'authorizeCode',sortable: true,width: 140},
				    {title: '门锁编码',field: 'lockCode',sortable: true,width: 130},
				    {title: '门锁名称',field: 'lockName',sortable: true,width: 140},
				    {title: '开锁人员',field: 'unlockPerName',sortable: true,width: 60},
				    {title: '启用时间',field: 'startTime',sortable: true,width: 125/*,
				    	formatter : function(value, rec) {
				    		if(isNotUndefined(value)){
				    			return value.substr(0, 13) + "点";
				    		}
				    	}*/
				    },
				    {title: '类型',field: 'lockType',sortable: true,width: 50,
				    	formatter : function(value, rec) {
				    		return dataValueConv(lockTypeGroup,value)
				    	}
				    },
				    {title: '截至时间',field: 'endTime',sortable: true,width: 125},
//				    {title: '开锁次数',field: 'unlockNumber',sortable: true,width: 80},
				    {title: '蓝牙开锁',field: 'blueUnlock',sortable: true,width: 60,
				    	formatter : function(value, rec) {
				    		return dataValueConv(enDiStatusGroup,value)
				    	}
				    },
				    {title: '创建/修改人',field: 'cuUserName',sortable: true,width: 75},
				    {title: '创建/修改时间',field: 'cuTime',sortable: true,width: 125},
					{title: '状态',field: 'statusCode',sortable: true,width: 40,
						formatter : function(value, rec) {
							return dataValueConv(statusCodeGroup,value)
						}
					}
				]]
		    });
		grid =  $('#datagrid').datagrid(options);
	}

	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.lockCode':$("#q_lockCode").val(),
			'dto.lockName':$("#q_lockName").val(),
			'dto.statusCode':$("#q_statusCode").combobox('getValue'),
			'dto.unlockPerId':$('#q_unlockPerId').combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(statusCodeGroup,'q_statusCode');
		dataToSelect(statusCodeGroup,'statusCode');
		dataToSelect(enDiStatusGroup,'blueUnlock');
		dataToSelect(isOrNoGroup,'scopeUnlock');
		dataToSelect(lockTypeGroup,'lockType');
		
		$.get(findPersonnelInfoOptionsUtl, {
		},
		function(data) {
			var dataItems = [];
			dataItems.push('[');
			
			var repObj = eval("(" + data + ")");
			var dataArray = repObj.rows;
			if (dataArray != 0 && dataArray.length > 0) {
		  		var f = "";
		  		for(var i=0;i<dataArray.length;i++){
		  			dataItems.push(f,'{"value":"',dataArray[i][0],'","text":"',dataArray[i][1],'"}');
	  				f = ",";
		  		}
		  	}
		  	dataItems.push("]");
		  	personnelInfoOpJson = eval(dataItems.join(''));
		  	
		  	$('#q_unlockPerId').combobox({
				valueField:'value',
	            textField:'text',
			    data:personnelInfoOpJson
			});
		  	
		  	$('#perList').datalist({
		  		lines:true,
		  		textField: "text",
		  		valueField: "value",
		  		singleSelect: false,
		  		data:personnelInfoOpJson
		  	});
		});
		
		
		$.get(findNormalLockInfoListUtl, {
		},
		function(data) {
			var repObj = eval("(" + data + ")");
		  	lockInfoOpJson = repObj.rows;
		  	
			$('#lockList').datalist({
		  		lines:true,
				textField: "lockName",
				valueField: "lockId",
	            singleSelect: false,
	            data: lockInfoOpJson
			});
		});
			 
//	  	$('#lockList').datalist({
//	  		url: findNormalLockInfoListUtl,
//	  		lines:true,
//			textField: "lockName",
//			valueField: "lockId",
//            singleSelect: false,
//		});
		
		$('#existingLocks').datalist({
			lines:true,
			textField: "lockName",
			valueField: "lockId",
			singleSelect: false
		});
		
		$('#existingPers').datalist({
			lines:true,
			singleSelect: false
		});
		
		var perIds = getQueryString("perIds");
		var perNames = getQueryString("perNames");
		if(isNotUndefined(perIds) && isNotUndefined(perNames)){
			add();
			var perId = perIds.split(",");
			var perName = perNames.split(",");
			var existingPer = [], f = "";
			existingPer.push('[');
			for(var k=0; k < perId.length; k++){
				existingPer.push(f,'{"value":"',perId[k],'","text":"',perName[k],'"}');
  				f = ",";
			}
			existingPer.push(']');
			$('#existingPers').datalist({
				url: "",
				lines:true,
				data: eval(existingPer.join('')),
				textField: "text",
				valueField: "value",
				singleSelect: false
			});
		}
	}
	
	function closeWindow(){
		win.window('close');
	};

	function add() {
		isNewRecord = true;
		findAuthorizeInfoUrl = "";
		initEditWindow();
	}

	function edit() {
		isNewRecord = false;
		var rows = grid.datagrid('getSelections');//返回所有被选择的行
		if (rows.length == 1) {
			editRow = rows[0];
			
			$('#existingLocks').datalist({
				lines:true,
				url: findAuthorizeLockListUrl + "?authorizeCode=" + editRow.authorizeCode,
				textField: "lockName",
				valueField: "lockId",
				singleSelect: false
			});
			
			$('#existingPers').datalist({
				lines:true,
				url: findAuthorizePerListUrl + "?authorizeCode=" + editRow.authorizeCode,
				textField: "text",
				valueField: "value",
				singleSelect: false
			});
			
			initEditWindow();
			
			var startTime = editRow.startTime;
			var index = startTime.indexOf(' ');
			$('#startDate').datebox('setValue', startTime.substring(0, index));
			$('#startHours').combobox('setValue',startTime.substring(index + 1, index + 3));
			
			var endTime = editRow.endTime;
			index = endTime.indexOf(' ');
			$('#endDate').datebox('setValue', endTime.substring(0, index));
			$('#endHours').combobox('setValue',endTime.substring(index + 1, index + 3));
		} else {
			$.messager.show({
				msg : '请选择一项进行修改！',
				title : '错误'
			});
		}
	}
	
	function initEditWindow() {
		win = $('#example-window').window({
		    modal:true
		});
		win.window('open');
		form = win.find('form');
		
		if (!isNewRecord && editRow != null) {
			Net.functions.initForm(form,editRow,true);
		}else{
			$('#existingLocks').datalist('loadData', { total: 0, rows: [] });
			$('#existingPers').datalist('loadData', { total: 0, rows: [] });
			form.form('clear');
			editRow = null;
		}
	}
	
	function addLocks(){
		var rows = $('#lockList').datalist('getSelections');
		insertRowLock(rows);
		$('#lockList').datalist('clearSelections');
	}
	
	function delLocks(){
		var rows = $('#existingLocks').datalist('getSelections');
		for(var i=0;i < rows.length;i++){
			var index = $('#existingLocks').datalist('getRowIndex', rows[i]);
			$('#existingLocks').datalist('deleteRow',index);
		}
	}
	
	function addPers(){
		var perInfos = $('#perList').datalist('getSelections');
		var rows = $('#existingPers').datalist('getData').rows;
		if(rows.length <= 0){
			$('#existingPers').datalist({
				url: "",
				lines:true,
				data: perInfos,
				textField: "text",
				valueField: "value",
				singleSelect: false
			});
		}else{
			var insertPer = true;
			for(var i=0; i < perInfos.length; i++){
				insertPer = true;
				if(rows.length > 0){
					for(var j=0; j < rows.length; j++){
						if(perInfos[i].value == rows[j].value){
							insertPer = false;
							break;
						}
					}
				}
				if(insertPer == true){
					$('#existingPers').datalist('insertRow', {
						row : perInfos[i]
					});
				}
			}
		}
		$('#perList').datalist('clearSelections');
	}
	
	function delPers(){
		var rows = $('#existingPers').datalist('getSelections');
		for(var i=0;i < rows.length;i++){
			var index = $('#existingPers').datalist('getRowIndex', rows[i]);
			$('#existingPers').datalist('deleteRow',index);
		}
	}
	
	
	//保存数据
	function submitForm(){
		$('#startTime').val($('#startDate').datebox('getValue') + ' 00:00:00');
		$('#endTime').val($('#endDate').datebox('getValue') + ' 23:59:59');
		
		var lockRows = $('#existingLocks').datalist('getData').rows;
		var f = "";
		var authorizeLockIds = "";

		for(var i=0; i < lockRows.length; i++){
			authorizeLockIds += f + lockRows[i].lockId;
			f = ",";
		}
		
		f = "";
		var perRows = $('#existingPers').datalist('getData').rows;
		var authorizePerIds = "";
		for(var i=0; i < perRows.length; i++){
			authorizePerIds += f + perRows[i].value;
			f = ",";
		}
		
		$("#authorizeLockIds").val(authorizeLockIds);
		$("#authorizePerIds").val(authorizePerIds);
		
		Net.functions.saveRow(win, form, grid,recordSaveUrl);
	};
	
	//删除数据
	function deleteRows(){
		Net.functions.batchRows(grid, recordDeleteUrl, '删除');
	};
	
	