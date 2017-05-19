	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				queryParams: {
					'dto.lockCode':$("#q_lockCode").val()
				},
				idField:'lockCode',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
				    {title: '记录类型',field: 'recordTpye',sortable: true,width: 82},
				    {title: '所属区域',field: 'areaName',sortable: true,width: 60},
				    {title: '门锁编码',field: 'lockCode',sortable: true,width: 130},
				    {title: '门锁名称',field: 'lockName',sortable: true,width: 170},
				    {title: '钥匙编码',field: 'keyCode',sortable: true,width: 120,
				    	formatter : function(value, rec) {
				    		if(value == "0000000000000007"){
				    			return "";
				    		}else{
				    			return value;
				    		}
				    	}
				    },
				    {title: '钥匙类型',field: 'keyTypeName',sortable: true,width: 60},
				    {title: '开锁人员',field: 'unlockPerName',width: 60},
				    {title: '联系电话',field: 'unlockPerPhone',width: 82},
				    {title: '所属组织',field: 'orgName',width: 100},
				    {title: '开锁时间',field: 'unlockTime',width: 125},
//				    	formatter : function(value, rec) {
//				    		if(value != undefined){
//				    			return value.substring(0,16);
//				    		}
//				    	}
					{title: '上传时间',field: 'uploadTime',width: 125},
					{title: '备注',field: 'note',width: 325}
				]],
				toolbar:null
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search(queryType) {
		var unEndTime = $("#q_unEndTime").datebox('getValue');
		if(isNotUndefined(unEndTime)){
			unEndTime = unEndTime + " 23:59:59";
		}
		grid.datagrid('unselectAll');
		
		var queryParams = {
			'dto.lockCode':$("#q_lockCode").val(),
			'dto.lockName':$("#q_lockName").val(),
			'dto.lockType':$("#q_lockType").combobox('getValue'),
			'dto.recordCode':$("#q_recordCode").combobox('getValue'),
			'dto.unlockPerId':$("#q_unlockPerId").combobox('getValue'),
			'dto.unStartTime':$("#q_unStartTime").datebox('getValue'),
			'dto.orgId' : $("#q_orgId").combotree('getValue'),
			'dto.areaId':$('#q_areaId').combotree('getValue'),
			'dto.unEndTime':unEndTime,
			'dto.queryType':queryType
		}
		if(queryType != 1){
			grid.datagrid('load',queryParams);
		}else{
			Net.functions.excelExport(recordQueryUrl, queryParams);
		}
	}
	
	function initComboboxDate(){
		jsonToSelect(findPersonnelInfoOptionsUtl + "?dto.status=1","",'q_unlockPerId');

		dataToSelect(lockTypeGroup,'q_lockType');
		$('#q_lockType').combobox({
			onChange:function(newValue, oldValue){
				if(newValue == "1"){
					dataToSelect(passiveRecordTpyeGroup,'q_recordCode');
				}else{
					dataToSelect(activeRecordTpyeGroup,'q_recordCode');
				}
			}
		});
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
	