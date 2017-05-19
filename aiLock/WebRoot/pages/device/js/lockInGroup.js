	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'groupId',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
				    {title: '分组名称',field: 'groupName',sortable: true,width: 200},
				    {title: '备注',field: 'note',sortable: true,width: 450}
				]]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.groupName':$("#q_groupName").val(),
			'dto.lockCode':$("#q_lockCode").val(),
			'dto.lockName':$("#q_lockName").val()
		});
	}
	
	function initComboboxDate(){
//		dataToSelect(lockTypeGroup,'q_lockType');
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
		var rows = grid.datagrid('getSelections');//返回所有被选择的行
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
		win = $('#example-window').window({
		    modal:true
		});
		win.window('open');
		form = win.find('form');
		var url = "";
		if (!isNewRecord && editRow != null) {
			url = findGroupLockUtl + '?dto.groupIds=' + editRow.groupId;
			Net.functions.initForm(form,editRow,true);
		}
		
		var groupLockOptions = $.extend({},
				Net.temple.datagrid,
				{
					url: url,
					idField:'lockId',//表明该列是一个唯一列
					pagination:false,
					frozenColumns:[[
		                {field:'ck',checkbox:true}
					]],
					columns:[[
					    {title: '门锁编码',field: 'lockCode',sortable: true,width: 200},
					    {title: '门锁名称',field: 'lockName',sortable: true,width: 200}
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
						text:'删除门锁',
						iconCls:'icon-remove',
						handler:function(){
							deleteLock();
						}
					}],
					onDblClickRow:null
			    });
		
		groupLockGrid =  $('#groupLockGrid').datagrid(groupLockOptions);
		
		if (isNewRecord || editRow == null) {
			groupLockGrid.datagrid('loadData', { total: 0, rows: [] });
			form.form('clear');
			editRow = null;
		}
	}
	
	function deleteLock(){
		var rows = groupLockGrid.datagrid('getChecked');
		for(var i=0; i < rows.length;){
			var index = groupLockGrid.datagrid('getRowIndex', rows[i].lockId);
			groupLockGrid.datagrid('deleteRow', index);
		}
	}
	
	//保存数据
	function submitForm(){
		var lockRows = groupLockGrid.datagrid('getData').rows;
		var f = "";
		var lockIds = "";
		if(lockRows.length == 0){
			$.messager.show({
				msg : '请为该组添加门锁！',
				title : '错误'
			});
		}else{
			for(var i=0; i < lockRows.length; i++){
				lockIds += f + lockRows[i].lockId;
				f = ",";
			}
			
			$("#lockIds").val(lockIds);
			Net.functions.saveRow(win, form, grid,recordSaveUrl);
		}
	};
	
	//删除数据
	function deleteRows(){
		Net.functions.batchRows(grid, recordDeleteUrl, '删除');
	};
	