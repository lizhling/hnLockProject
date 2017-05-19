	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'areaId',//表明该列是一个唯一列
				columns:[[
				    {title: '编号',field: 'areaId',checkbox: true},
					{title: '区域名称',field: 'areaName',sortable: true,width: 150},
					{title: '上级区域',field: 'parentName',sortable: true,width: 150},
					{title: '显示顺序',field: 'areaOrder',sortable: true,width: 60},
					{title: '备注',field: 'note',width: 350}
				]],
				toolbar:[{
					id:'btnadd', text:'新增', iconCls:'icon-add',
					handler:function(){
						add();
					}
				},{
					id:'btnedit', text:'修改', iconCls:'icon-edit',
					handler:function(){
						edit();
					}
				},{
					id:'btndelete', text:'删除', iconCls:'icon-remove',
					handler:function(){
						deleteRows();
					}
				},{
					id:'btnadd', text:'导入', iconCls:'icon-up',
					handler:function(){
						showImportWin();
					}
				},{
					id:'btnadd', text:'导出', iconCls:'icon-down',
					handler:function(){
						exportData();
					}
				}]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.areaName': $("#q_areaName").val(),
			'dto.parentId': $("#q_parentId").combotree('getValue')
		});
	}
	
	function initComboboxDate(){
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
	
	var isCanSave = false;
	function initEditWindow() {
		win = $('#example-window').window({  
		    modal:true
		});
		win.window('open');
		form = win.find('form');
		$('#imageFile').attr("src","");
		
		isCanSave = false;
		$('#parentId').combo({disabled:false});
		
		if (!isNewRecord && editRow != null) {
			Net.functions.initForm(form,editRow,true);
			if($("#parentId").combotree('getText') == $("#parentId").combotree('getValue')){
				if(isNotUndefined(editRow.parentId)){
					isCanSave = true;
				}
				$('#parentId').combo({disabled:true});
				$("#parentId").combo('setText', editRow.parentName);
			}
		}else{
			form.form('clear');
			editRow = null;
		}
	}
	
	//保存数据
	function submitForm(){
		if($("#areaName").val() == $("#parentId").combotree('getText')){
			$.messager.alert('错误', "区域名称不能和上级区域名称同名", 'error');
			return;
		}else if(isCanSave){
			$.messager.show({
				msg : '您没有权限修改该数据！',
				title : '错误'
			});
		}else{
			Net.functions.saveRow(win, form, grid,recordSaveUrl);
		}
	};
	
	//删除数据
	function deleteRows(){
		Net.functions.batchRows(grid, recordDeleteUrl, '删除');
	};
	
	
	function showImportWin(){
		importWin = $('#import-window').window({
		    modal:true
		});
		importWin.window('open');
		importFrom = importWin.find('form');
	}
	
	function closeImportWin(){
		importWin.window('close');
	};
	
	//导出数据
	function exportData(){
		Net.functions.excelExport(exportAreaInfo, null);
	};
	
	//导入数据
	function importData(){
		Net.functions.saveRow(importWin, importFrom, grid,importAreaInfo);
	};
