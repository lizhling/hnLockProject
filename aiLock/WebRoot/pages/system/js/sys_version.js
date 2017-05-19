	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'versionId',
				nowrap:true,
				columns:[[
				    {title: '编号',field: 'versionId',checkbox: true,width: 10},
					{title: '版本号',field: 'versionCode',sortable: true,width: 70},
					{title: '版本名称',field: 'versionName',sortable: true,width: 70},
					{title: '版本平台',field: 'versionOs',width: 70,
						formatter : function(value, rec) {
							return dataValueConv(versionOsGroup,value)
						}
					},
					{title: '版本信息',field: 'versionInfo',width: 230},
					{title: '更新类型',field: 'updateType',sortable: true,width: 75,
						formatter : function(value, rec) {
							return dataValueConv(updateTypeGroup,value)
						}
					},
					{title: '更新地址',field: 'donwloadUrl',width: 250},
					{title: '创建时间',field: 'createTime',width: 130}
				]]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.versionCode': $("#q_versionCode").val(),
			'dto.versionName': $("#q_versionName").val(),
			'dto.versionOs' : $("#q_versionOs").combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(updateTypeGroup,'updateType');
		dataToSelect(versionOsGroup,'versionOs');
		dataToSelect(versionOsGroup,'q_versionOs');
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
		win = $('#example-window').window({  
		    modal:true
		});
		win.window('open');
		form = win.find('form');
		
		if (!isNewRecord && editRow != null) {
			Net.functions.initForm(form,editRow,true);
		}else{
			form.form('clear');
			editRow = null;
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
