	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'userId',//表明该列是一个唯一列
				columns:[[
				    {title: '编号',field: 'userId',checkbox: true,width: 50},
				    {title: '用户姓名',field: 'name',sortable: true,width: 150},
					{title: '账号',field: 'userName',sortable: true,width: 150},
					{title: '手机号码',field: 'phoneNo',width: 150},
					{title: '所属组织',field: 'orgName',width: 100},
					{title: '用户状态',field: 'status',sortable: true,width: 150,
						formatter : function(value, rec) {
							return dataValueConv(userStatusGroup,value)
						}
					},
					{title: '角色Id',field: 'roleId',width: 100,hidden:true}
					
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
					id:'btsearch',text:'角色配置',iconCls:'icon-edit',
					handler:function(){
						roleDistribution();
					}
				},{
					id:'butreset', text:'重置密码', iconCls:'icon-edit',
					handler:function(){
						resetPassword();
					}
				}]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.name':$("#q_name").val(),
			'dto.status':$("#q_status").combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(userStatusGroup,'q_status');
		dataToSelect(userStatusGroup,'status');
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
	
	function roleDistribution(){
		var rows = grid.datagrid('getSelections');
		if (rows.length == 1) {
			editRow = rows[0];
			roleGridWindow();
		} else {
			$.messager.show({
				msg : '请选择一项进行修改！',
				title : '错误'
			});
		}
	}
	
	function roleGridWindow() {
		roleGridWin = $('#roleGrid-window').window({
		    modal:true
		});
		
		var roleOptions = $.extend({},
			Net.temple.datagrid,
			{
				url: findSysRoleUrl,
				pagination:false,
				idField:'roleId',//表明该列是一个唯一列
				queryParams:{
					'dto.status': '1'
				},
				columns:[[
				    {title: '编号',field: 'roleId',sortable: true,checkbox: true,width: 50},
					{title: '角色名称',field: 'roleName',sortable: true,width: 150},
					{title: '是否启用',field: 'status',width: 100,
						formatter : function(value, rec) {
							return dataValueConv(isOrNoGroup,value)
						}
					},
					{title: '备注',field: 'note',sortable: true,width: 160}
				]],
				toolbar:[{
					id:'btsearch',text:'确定',iconCls:'icon-ok',
					handler:function(){
						saveSysUserInRole();
					}
				}],
				onLoadSuccess:function(data){//数据载入成功时事件
					var roleIds = editRow.roleId;
					var roleIdArray = roleIds.split(',');
					for(var i = 0; i < roleIdArray.length; i++){
						roleGrid.datagrid('selectRecord', roleIdArray[i]);//选中idField行
					}
					
				}
			});
		
		roleGridWin.window('open');//显示
		
		roleGrid =  $('#roleDatagrid').datagrid(roleOptions);
		roleGrid.datagrid('unselectAll');//取消全选
	}
	
	
	function saveSysUserInRole(){
		var roleRows = roleGrid.datagrid('getSelections');
	    var roleIds = "";
	    var j = "";
		for (var i = 0; i < roleRows.length; i ++) {
			roleIds += j + [roleRows[i].roleId];
			j = ",";
		}
		
		$.post(saveSysUserInRoleUrl,{
			'dto.userId':editRow.userId,
			'dto.roleIds':roleIds
		},function(data) {
			var repObj = eval("(" + data + ")");
			var resultCode = repObj.resultCode;
		  	if(resultCode == 0){
		        $.messager.show({
					title : '提示',
					msg : '角色分配成功！'
				});
		        
		        grid.datagrid('unselectAll');//取消全选
				grid.datagrid('reload');//重新加载数据
		        closeRoleResWin();
			}else{
				$.messager.alert('错误', repObj.resultMessage, 'error');
			}
		});
	}
	
	function closeRoleResWin(){
		roleGridWin.window('close');
	};
	
	function resetPassword(){
		var rows = grid.datagrid('getSelections');//返回所有被选择的行
		if (rows.length == 1) {
			$.messager.confirm('重置密码确认', '你确认要重置用户的登录密码?', function(r){
	        	if(r){
	            	$.get(resetPasswordUtl, {
	            		'dto.userId': rows[0].userId
	         		},
	         		function(data) {
	         			var repObj = eval("(" + data + ")");
	         			var resultCode = repObj.resultCode;
	        			if(resultCode == 0){
	        				$.messager.show({
	        					title : '提示', msg : '密码重置成功！'
	        				});
	        			}else{
	        				$.messager.alert('错误', repObj.resultMessage, 'error');
	        			}
	         		});
	        	}
			});
		}else{
			$.messager.show({
				msg : '请选择一项进行修改！',
				title : '错误'
			});
		}
	}
	