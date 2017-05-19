	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'lockCode',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
				    {title: '记录类型',field: 'recordTpye',sortable: true,width: 120,
				    	formatter : function(value, rec) {
				    		return dataValueConv(recordTpyeGroup,value)
				    	}
				    },
				    {title: '门锁编码',field: 'lockCode',sortable: true,width: 120},
				    {title: '门锁名称',field: 'lockName',sortable: true,width: 120},
				    {title: '钥匙编码',field: 'keyCode',sortable: true,width: 120},
				    {title: '钥匙名称',field: 'keyName',sortable: true,width: 120},
				    {title: '开锁人员',field: 'perName',width: 60},
				    {title: '开锁时间',field: 'unlockTime',width: 125},
					{title: '上锁时间',field: 'closeLockTime',width: 125},
					{title: '记录上传时间',field: 'uploadTime',width: 125}
				]],
				toolbar:null
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.lockCode':$("#q_lockCode").val(),
			'dto.lockName':$("#q_lockName").val(),
			'dto.recordTpye':$("#q_recordTpye").combobox('getValue'),
			'dto.perId':$("#q_perId").combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(recordTpyeGroup,'q_recordTpye');
		jsonToSelect(findPersonnelInfoOptionsUtl + "?dto.status=1","",'q_perId');
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
			width:540,
		    height:330,
		    modal:true
		});
		win.css("display", "block");
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
	