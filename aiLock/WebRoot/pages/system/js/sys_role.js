	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'roleId',//表明该列是一个唯一列
				columns:[[
				    {title: '编号',field: 'roleId',checkbox: true,width: 50},
					{title: '角色名称',field: 'roleName',sortable: true,width: 150},
					{title: '是否启用',field: 'status',width: 100,
						formatter : function(value, rec) {
							return dataValueConv(enDiStatusGroup,value)
						}
					},
					{title: '描述',field: 'note',width: 250}
				]],
				toolbar:[{
					id:'btnadd',text:'新增',iconCls:'icon-add',
					handler:function(){
						add();
					}
				},{
					id:'btnedit',text:'修改',iconCls:'icon-edit',
					handler:function(){
						edit();
					}
				},{
					id:'btndelete',text:'删除',iconCls:'icon-remove',
					handler:function(){
						deleteRows();
					}
				},{
					id:'btsearch',text:'资源配置',iconCls:'icon-edit',
					handler:function(){
						resDistribution();
					}
				}]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.roleName': $("#q_roleName").val(),
			'dto.status' : $("#q_status").combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(enDiStatusGroup,'status');
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
		Net.functions.saveRow(win, form, grid,recordSaveUrl);
	};
	
	//删除数据
	function deleteRows(){
		Net.functions.batchRows(grid, recordDeleteUrl, '删除');
	};
	
	
	function resDistribution(){
		var rows = grid.datagrid('getSelections');
		if (rows.length == 1) {
			editRow = rows[0];
			$.get(findSysResByRoleUrl, {
				'dto.roleId':editRow.roleId
			},
			function(data) {
				var repObj = eval("(" + data + ")");
			  	var dataArray = repObj.rows;
			  	
			  	var t= $('#resTree').tree('getChecked');
				for(var a = 0; a < t.length; a++){
					$('#resTree').tree('uncheck',t[a].target);
				}
				
			  	if (dataArray != 0 && dataArray.length > 0) {
		  	        for (var x in dataArray) {
				  		if (dataArray[x]) {
				  			var targe = $('#resTree').tree('find',dataArray[x].id.resId);
				  			//alert($('#'+targe.domId));
							//该节点存在 且 没有子节点  才勾中该节点
							if(targe != null && $('#resTree').tree('getChildren',targe.target).length == 0){
								$('#resTree').tree('check',targe.target);
							}
				  		}
		  	        }
			  	}
			});
			
			roleResWindow();
		} else {
			$.messager.show({
				msg : '请选择一项进行修改！',
				title : '错误'
			});
		}
	}
	
	function roleResWindow() {
		roleResWin = $('#roleRes-window').window({
			width:335,
		    height:405,
		    modal:true
		});
		roleResWin.window('open');
		roleResForm = roleResWin.find('form');
	}
	
	var parentIds = [];
	function getParentId(nodeId){
		parentIds.push(nodeId);
		
		var nodeObj = $('#resTree').tree('find',nodeId);
		var parentId = nodeObj.parentId;
		
		if(parentId != 1 && parentId != undefined){
			getParentId(parentId);
		}
	}
	
	function saveSysRoleUseRes(){
		parentIds = [];
		var checkedRes = $("#resTree").tree('getChecked');
	    var resIds = "";
		for (var i = 0; i < checkedRes.length; i ++) {
			if(checkedRes[i].id != 1){
				getParentId(checkedRes[i].id);//保存节点ID并递归保存父节点ID
			}
		}
		
		parentIds = removeDuplicateArray(parentIds);//去除重复节点
		
		if(parentIds.length > 0){
			parentIds.push(1);//加上根节点
		}
		resIds = parentIds.join(',');
		
		$.post(saveSysRoleUseResUrl,{
			'dto.roleId':editRow.roleId,
			'dto.resIds':resIds
		},function(data) {
			var repObj = eval("(" + data + ")");
			var resultCode = repObj.resultCode;
		  	if(resultCode == 0){
		        $.messager.show({
					title : '提示',
					msg : '资源分配成功！'
				});
		        closeRoleResWin();
			}else{
				$.messager.alert('错误', repObj.resultMessage, 'error');
				$('#save_button').attr("disabled", false);
			}
		});
	}
	
	function closeRoleResWin(){
		roleResWin.window('close');
	};
