	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'menuId',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				pageSize:10,
				columns:[[
				    {title: '菜单ID',field: 'menuId',sortable: true,width: 50},
					{title: '菜单名称',field: 'menuName',sortable: true,width: 150},
					{title: '菜单类型',field: 'permissionsType',width: 100,
						formatter : function(value, rec) {
							return dataValueConv(permissionsTypeGroup,value)
						}
					},
					{title: '排序',field: 'menuOrder',width: 80,sortable: true},
					{title: '菜单状态',field: 'status',width: 80,
						formatter : function(value, rec) {
							return dataValueConv(enDiStatusGroup,value)
						}
					}
				]]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.menuName':$("#q_menuName").val(),
			'dto.permissionsType':$("#q_permissionsType").combobox('getValue'),
			'dto.status' : $("#q_status").combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(permissionsTypeGroup,'permissionsType');
		dataToSelect(enDiStatusGroup,'status');
		
		dataToSelect(permissionsTypeGroup,'q_permissionsType');
		dataToSelect(enDiStatusGroup,'q_status');
		
		jsonToSelect(findAppMenuPermissionsOptionsUrl,"",'parentId');
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
