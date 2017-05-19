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
				    {title: '授权批次码',field: 'authorizeCode',sortable: true,width: 150},
				    {title: '门锁编码',field: 'lockCode',sortable: true,width: 150},
				    {title: '门锁名称',field: 'lockName',sortable: true,width: 120},
				    {title: '开锁人员',field: 'unlockPerName',sortable: true,width: 80},
				    {title: '开锁次数',field: 'unlockNumber',sortable: true,width: 80},
				    {title: '创建/修改人',field: 'cuUserName',sortable: true,width: 100},
				    {title: '创建/修改时间',field: 'cuTime',sortable: true,width: 130},
					{title: '状态',field: 'statusCode',sortable: true,width: 60,
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
			'dto.lockType':$("#q_lockType").combobox('getValue'),
			'dto.status':$("#q_status").combobox('getValue'),
			'dto.orgId':$("#q_orgId").combobox('getValue'),
			'dto.basicDataId':$('#q_basicDataId').combotree('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(hoursGroup,'startHours');
		dataToSelect(hoursGroup,'endHours');
		dataToSelect(statusCodeGroup,'q_statusCode');
		dataToSelect(statusCodeGroup,'statusCode');
		
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
		});
		
	}
	
	function closeWindow(){
		win.window('close');
	};

	function add() {
		isNewRecord = true;
		findAuthorizeInfoUtl = "";
		initEditWindow();
	}

	function edit() {
		isNewRecord = false;
		var rows = grid.datagrid('getSelections');//返回所有被选择的行
		if (rows.length == 1) {
			editRow = rows[0];
			findAuthorizeInfoUtl = recordQueryUrl + '?dto.authorizeCode=' + editRow.authorizeCode;
			
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
	
	var delLock = false;
	function initEditWindow() {
		win = $('#example-window').window({
			width:850,
		    height:455,
		    modal:true
		});
		win.css("display", "block");
		form = win.find('form');
		var lockOptions = $.extend({},
				Net.temple.datagrid,
				{
					url: findAuthorizeInfoUtl,
					idField:'lockId',//表明该列是一个唯一列
					pagination:false,
					showGroup:true,
					singleSelect:true,
					columns:[[
						{title: '操作',field: 'lockId',sortable: true,width: 30,align : 'center',
							formatter : function(value, rowData, rowIndex) {
								return '<img onclick="deleteLock('+value+')" title="删除该门锁授权" src="'+basePath+'javascript/jquery-easyui-1.3.5/themes/icons/edit_remove.png"></img>';
							}
						},
					    {title: '门锁编码',field: 'lockCode',sortable: true,width: 200},
					    {title: '门锁名称',field: 'lockName',sortable: true,width: 240},
					    {title: '开锁人员',field: 'unlockPerId',sortable: true,width: 100,
					    	formatter: function(value, row, index) {
                                return row.unlockPerName;
                            },
					    	editor:{
					    		type:'combobox',
					    		options:{
	                                valueField:'value',
	                                textField:'text',
	                                data:personnelInfoOpJson,
	                                required:true
	                            }
					    	}
					    }
					]],
					toolbar:[{
						id:'btnadd',
						text:'添加门锁',
						iconCls:'icon-add',
						handler:function(){
							showLockInfoWin();
						}
					},{
						id:'btnadd',
						text:'从组添加',
						iconCls:'icon-add',
						handler:function(){
							showGroupWin();
						}
					},{
						id:'updateUn',
						text:'统一设置开锁人员',
						iconCls:'icon-edit',
						handler:function(){
							showPersonnelWin();
						}
					}],
					onClickCell:function(index, field, value){
			    		if(field == 'unlockPerId'){
			    			if (editIndex != index){
			    				if (endEditing()){
			    					editIndex = index;
			    					lockGrid.datagrid('beginEdit', index);
			    				}
			    			}else{
			    				editIndex = index;
			    				endEditing();
			    			}
			    		}else{
//			    			if(field == 'lockId' && delLock == true){
//			    				delLock = false
//			    				lockGrid.datagrid('deleteRow', index);
//			    			}
			    			endEditing();
			    		}
			    	}
			    });
		lockGrid =  $('#lockGrid').datagrid(lockOptions);
		
		if (!isNewRecord && editRow != null) {
			Net.functions.initForm(form,editRow,true);
		}else{
			lockGrid.datagrid('loadData', { total: 0, rows: [] });
			form.form('clear');
			editRow = null;
		}
	}
	
	var editIndex = undefined;
	function endEditing(){
		if (editIndex == undefined){return true;}
		if (lockGrid.datagrid('validateRow', editIndex)){
			var ed = lockGrid.datagrid('getEditor', {index:editIndex, field:'unlockPerId'});
			var unlockPerName = $(ed.target).combobox('getText');
			lockGrid.datagrid('getRows')[editIndex]['unlockPerName'] = unlockPerName;
			lockGrid.datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	
	function deleteLock(value){
//		delLock = true;
		var rowIndex = lockGrid.datagrid('getRowIndex', value);
		lockGrid.datagrid('deleteRow', rowIndex);
	}
	
	//保存数据
	function submitForm(){
		$('#startTime').val($('#startDate').datebox('getValue') + ' ' + $('#startHours').combobox('getValue') + ':00:00');
		$('#endTime').val($('#endDate').datebox('getValue') + ' ' + $('#endHours').combobox('getValue') + ':59:59');
		
		if (editIndex != undefined){
			endEditing();
		}
		
		var lockRows = lockGrid.datagrid('getData').rows;
		var f = "";
		var lockAuthorizeInfos = "";
		for(var i=0; i < lockRows.length; i++){
			lockAuthorizeInfos += f +lockRows[i].authorizeId + ',' + lockRows[i].lockId + ',' + lockRows[i].unlockPerId;
			f = ";";
		}
		
		$("#lockAuthorizeInfos").val(lockAuthorizeInfos);
		
		Net.functions.saveRow(win, form, grid,recordSaveUrl);
//		var inserted = lockGrid.datagrid('getChanges', "inserted");
//		var updateNone = lockGrid.datagrid('getChanges', "updated");
//		var deleteNone = lockGrid.datagrid('getChanges', "deleted");
//		for(var i=0; i < updateNone.length; i++){
//			alert(JSON.stringify(updateNone[i]));
//			lockGrid.datagrid('appendRow',updateNone[i]);
//			alert(updateNone[i].lockName);
//		}
	};

	function updateKey(rowIndex){
		var rowData = lockGrid.datagrid('getRows')[rowIndex];
		rowData.keyCode = '121212';
		rowData.keyName = '的速度';
		$('#lockGrid').datagrid('updateRow', {
			index: rowIndex,
			row : rowData
		});
	}
	
	
	//删除数据
	function deleteRows(){
		Net.functions.batchRows(grid, recordDeleteUrl, '删除');
	};
	