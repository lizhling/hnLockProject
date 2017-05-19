	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'contentId',//表明该列是一个唯一列
				columns:[[
				    {title: '编号',field: 'contentId',checkbox: true,width: 50},
					{title: '内容标题',field: 'title',sortable: true,width: 200},
					{title: '内容类型',field: 'contentType',sortable: true,width: 80,
						formatter : function(value, rec) {
							return dataValueConv(contentTypeGroup,value)
						}
					},
					{title: '内容状态',field: 'status',width: 80,
						formatter : function(value, rec) {
							return dataValueConv(resEnabledGroup,value)
						}
					},
					{title: 'ios支持最低版本',field: 'iosVersion',width: 110},
					{title: 'Android支持最低版本',field: 'androidVersion',width: 130},
					/*{title: '是否置顶',field: 'isPuttop',width: 100,
						formatter : function(value, rec) {
							return dataValueConv(isOrNoGroup,value)
						}
					},*/
					{title: '创建人',field: 'ceataUserName',width: 80},
					{title: '创建时间',field: 'ceataTime',width: 80},
					{title: '修改人',field: 'updateUserName',width: 80},
					{title: '修改时间',field: 'updateTime',width: 80}
				]],
				toolbar:[{
					id:'btnadd',
					text:'新增',
					iconCls:'icon-add',
					handler:function(){
						add();
					}
				},{
					id:'btnedit',
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						edit();
					}
				}]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.title':$("#q_title").val(),
			'dto.isPuttop' : $("#q_subtitle").val(),
			'dto.status':$("#q_status").combobox('getValue'),
			'dto.contentType' : $("#q_contentType").combotree('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(resEnabledGroup,'status');
		dataToSelect(resEnabledGroup,'q_status');
		
		//dataToSelect(isOrNoGroup,'isPuttop');
		//dataToSelect(isOrNoGroup,'q_isPuttop');
		
		dataToSelect(contentTypeGroup,'contentType');
		dataToSelect(contentTypeGroup,'q_contentType');
		
		jsonToSelect(findVersionCodeToOsUrl + "?dto.versionOs=0","",'iosVersion');
		jsonToSelect(findVersionCodeToOsUrl + "?dto.versionOs=1","",'androidVersion');
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
			width:630,
		    height:440,
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
	//function deleteRows(){
	//	Net.functions.batchRows(grid, recordDeleteUrl, '删除');
	//};
