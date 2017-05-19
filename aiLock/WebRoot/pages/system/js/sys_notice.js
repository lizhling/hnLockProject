	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'noticeId',//表明该列是一个唯一列
				columns:[[
				    {title: '编号',field: 'noticeId',checkbox: true,width: 10},
					{title: '公告标题',field: 'title',sortable: true,width: 500},
					{title: '是否置顶',field: 'isPuttop',width: 100,
						formatter : function(value, rec) {
							return dataValueConv(isOrNoGroup,value)
						}
					},
					{title: '发布人',field: 'userName',width: 80},
					{title: '发布时间',field: 'releaseTime',width: 150},
				]]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.title':$("#q_title").val(),
			'dto.isPuttop' : $("#q_subtitle").val(),
			'dto.status':$("#q_status").combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(noticeStatusGroup,'status');
		dataToSelect(noticeStatusGroup,'q_status');
		
		dataToSelect(isOrNoGroup,'isPuttop');
		dataToSelect(isOrNoGroup,'q_isPuttop');
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
