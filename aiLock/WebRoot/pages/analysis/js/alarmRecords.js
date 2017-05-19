	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				queryParams: {
					'dto.recordId':$("#q_recordId").val(),
					'dto.lockCode':$("#q_lockCode").val()
				},
				idField:'recordId',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
				    {title: '所属区域',field: 'areaName',sortable: true,width: 60},
				    {title: '告警级别',field: 'alarmLevel',sortable: true,width: 60,
				    	styler: function(value,row,index){
							if (value == '紧急告警'){
								return 'background-color:rgb(255,51,51);';
							}else if(value == '重要告警'){
								return 'background-color:rgb(204,0,153);';
							}else if(value == '一般告警'){
								return 'background-color:rgb(255,255,102);';
							}
						}
				    },
				    {title: '门锁编码',field: 'lockCode',sortable: true,width: 135},
				    {title: '门锁名称',field: 'lockName',sortable: true,width: 170},
				    /*{title: '钥匙编码',field: 'keyCode',sortable: true,width: 120},
				    {title: '钥匙类型',field: 'keyType',sortable: true,width: 60,
				    	formatter : function(value, rec) {
				    		return dataValueConv(keyTypeGroup,value)
				    	}
				    },*/
				    {title: '告警信息',field: 'recordTpye',sortable: true,width: 135},
				    {title: '告警时间',field: 'unlockTime',width: 105,
				    	formatter : function(value, rec) {
				    		if(value != undefined){
				    			return value.substring(0,16);
				    		}
				    	}
				    },
					{title: '告警上传时间',field: 'uploadTime',width: 105,
				    	formatter : function(value, rec) {
				    		if(value != undefined){
				    			return value.substring(0,16);
				    		}
				    	}
				    },
					{title: '所属组织',field: 'orgName',width: 100},
				    {title: '确认人',field: 'userName',width: 50},
				    {title: '确认时间',field: 'confirmTime',width: 105,
				    	formatter : function(value, rec) {
				    		if(value != undefined){
				    			return value.substring(0,16);
				    		}
				    	}
				    },
					{title: '备注',field: 'note',width: 180}
				    
				]],
				toolbar:[{
					id:'btnAlarm', text:'告警确认', iconCls:'icon-ok',
					handler:function(){
						showAlarmConfirm();
					}
				}]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search(queryType) {
		grid.datagrid('unselectAll');
		
		var queryParams = {
			'dto.lockCode':$("#q_lockCode").val(),
			'dto.lockName':$("#q_lockName").val(),
			'dto.orgId' : $("#q_orgId").combotree('getValue'),
			'dto.areaId':$('#q_areaId').combotree('getValue'),
			'dto.alarmLevel':$("#q_alarmLevel").combobox('getValue'),
			'dto.queryType':queryType
		};
		if(queryType != 1){
			grid.datagrid('load',queryParams);
		}else{
			Net.functions.excelExport(recordQueryUrl, queryParams);
		}
	}
	
	function initComboboxDate(){
		dataToSelect(alarmLevelGroup,'q_alarmLevel');
	}
	
	function closeWindow(){
		win.window('close');
	};

	var rowsConfirm;
	function showAlarmConfirm() {
		isNewRecord = false;
		rowsConfirm = grid.datagrid('getSelections');//返回所有被选择的行
		if (rowsConfirm.length > 0) {
			initEditWindow();
		} else {
			$.messager.show({
				msg : '请选择一项进行修改！',
				title : '错误'
			});
		}
	}
	
	function initEditWindow() {
		$('#alarmConfirmWin').window({
		    modal:true
		});
		$('#alarmConfirmWin').window('open');
	}
	
	//保存数据
	function alarmConfirm(){
		var ids = [];
		for (var i = 0; i < rowsConfirm.length; i ++){
			ids = ids.concat([rowsConfirm[i].recordId]);
		}
		ids.join(',');
		
		$.get(alarmConfirmUtl, {
			'dto.recordIds':ids+"",
    		'dto.note': $('#note').val()
 		},
 		function(data) {
 			var repObj = eval("(" + data + ")");
 			var resultCode = repObj.resultCode;
			if(resultCode == 0){
				$.messager.show({
					title : '提示', msg : '告警处理确认成功！'
				});
				grid.datagrid('unselectAll'); 
				grid.datagrid('reload');
				$('#alarmConfirmWin').window('close');
			}else{
				$.messager.alert('错误', repObj.resultMessage, 'error');
			}
 		});
	};
	