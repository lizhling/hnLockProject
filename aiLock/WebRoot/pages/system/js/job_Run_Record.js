	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'id',//表明该列是一个唯一列
				nowrap:true,
				pageSize:20,
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
			        {title: '任务名称',field: 'jobName',width:180,sortable: true},
			        {title: '服务器IP',field: 'pcServerIp',width:100,sortable: true},
				    {title: '执行开始时间',field: 'runStartDate',width:130,sortable: true},
				    {title: '执行结束时间',field: 'runEndDate',width:130,sortable: true},
				    {title: '用时(秒)',field: 'runTime',width:60,sortable: true},
				    {title: '执行结果',field: 'runResults',width:60,sortable: true,
						formatter : function(value, rec) {
							return dataValueConv(succeedOrFailGroup,value)
						}
					},
					{title: '失败原因',field: 'exceptionInfo',width:300}
				]],
				toolbar:[{
					id:'view',
					text:'查看失败原因',
					iconCls:'icon-search',
					handler:function(){
						viewData();
					}
				}]
		    });
		grid =  $('#datagrid').datagrid(options);
		
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'q_runStartDate':$("#q_runStartDate").datebox('getValue'),
			'q_jobName':$("#q_jobName").val(),
			'q_pcServerIp':$("#q_pcServerIp").val()
		});
	}
	
	function viewData() {
		var rows = grid.datagrid('getSelections');
		if (rows.length == 1) {
			editRow = rows[0];
			if(isUndefined(editRow.exceptionInfo) == ""){
				$.messager.show({
					msg : '没有错误原因！',
					title : '提示'
				});
			}else{
				initEditWindow();
			}
		} else {
			$.messager.show({
				msg : '请选择一项进行查看！',
				title : '错误'
			});
		}
	}
	
	function initEditWindow() {
		win = $('#example-window').window({  
			width:900,
		    height:520,
		    modal:true
		});
		win.css("display", "block");
		form = win.find('form');
		
		if (editRow != null) {
			Net.functions.initForm(form,editRow,true);
		}else{
			form.form('clear');
			editRow = null;
		}
	}

	function closeWindow(){
		win.window('close');
	};

