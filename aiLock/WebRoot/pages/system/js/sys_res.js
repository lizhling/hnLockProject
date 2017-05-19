	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'resId',//表明该列是一个唯一列
				pageSize:10,
				columns:[[
				    {title: '编号',field: 'resId',checkbox: true,width: 50},
					{title: '资源名称',field: 'resName',sortable: true,width: 150},
					{title: '上级资源',field: 'parentResName',sortable: true,width: 150},
					{title: '资源类型',field: 'resType',width: 80,
						formatter : function(value, rec) {
							return dataValueConv(resTypeGroup,value)
						}
					},
					{title: '资源URL',field: 'resUrl',width: 250},
					{title: '资源TAG',field: 'resTag',width: 80},
					{title: '资源状态',field: 'status',width: 80,
						formatter : function(value, rec) {
							return dataValueConv(enDiStatusGroup,value)
						}
					},
					{title: '排序',field: 'resOrder',width: 50,sortable: true}
				]]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.resName':$("#q_resName").val(),
			'dto.resType':$("#q_resType").combobox('getValue'),
			'dto.status' : $("#q_status").combobox('getValue'),
			'dto.resParentId' : $("#q_resParentId").combotree('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(resTypeGroup,'resType');
		dataToSelect(enDiStatusGroup,'status');
		
		dataToSelect(resTypeGroup,'q_resType');
		dataToSelect(enDiStatusGroup,'q_status');
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
		if($("#resParentId").combotree('getValue') == $("#resId").val()){
			$.messager.show({
				msg : '上级资源不能为本资源，请重新选择！',
				title : '错误'
			});
			return false;
		}
		Net.functions.saveRow(win, form, grid,recordSaveUrl);
	};
	
	//删除数据
	function deleteRows(){
		Net.functions.batchRows(grid, recordDeleteUrl, '删除');
	};
