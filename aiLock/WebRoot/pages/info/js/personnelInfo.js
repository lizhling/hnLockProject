	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'perId',//表明该列是一个唯一列
				columns:[[
				    {title: '编号',field: 'perId',checkbox: true},
				    {title: '姓名',field: 'perName',sortable: true,width: 100},
					{title: '账号',field: 'perAccounts',sortable: true,width: 100},
					{title: '手机号码',field: 'phoneNo',width: 100},
					{title: '所属组织',field: 'orgName',width: 150},
					{title: '智能钥匙人员ID',field: 'smartKeyPerId',width: 100},
					{title: '创建/修改时间',field: 'cuTime',width: 125},
					{title: '人员状态',field: 'status',sortable: true,width: 100,
						formatter : function(value, rec) {
							return dataValueConv(userStatusGroup,value)
						}
					}
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
					id:'butreset', text:'重置密码', iconCls:'icon-edit',
					handler:function(){
						resetPerPassword();
					}
				},{
					id:'unlock', text:'配置授权', iconCls:'icon-add',
					handler:function(){
						jumpAuthorization();
					}
				},{
					id:'search', text:'导入', iconCls:'icon-up',
					handler:function(){
						showImportWin();
					}
				},{
					id:'export', text:'导出', iconCls:'icon-down',
					handler:function(){
						exportExcel();
					}
				},{
					id:'search', text:'下载导入模版', iconCls:'icon-down',
					handler:function(){
						mouldDownLoad();
					}
				}]
		    });
		grid =  $('#datagrid').datagrid(options);
	}
	
	function search() {
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.perAccounts':$("#q_perAccounts").val(),
			'dto.perName':$("#q_perName").val(),
			'dto.status':$("#q_status").combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(userStatusGroup,'q_status');
		dataToSelect(userStatusGroup,'status');
		dataToSelect(dataImportType,'importType');
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
	
	function jumpAuthorization(){
		var rows = grid.datagrid('getSelections');//返回所有被选择的行
		if (rows.length > 0) {
			var perIds = "", perNames = "";
			var f = "";
			for(var i=0; i < rows.length; i++){
				if(rows[i].status == 1){
					perIds += f + rows[i].perId;
					perNames += f + rows[i].perName;
					f = ",";
				}
			}
			viewLockRecords("门锁授权管理",  basePath + '/pages/device/lockKeyAuthorize.jsp?perIds='+perIds+'&perNames='+perNames);
		}else{
			$.messager.show({
				msg : '请先选择要授权的人员！',
				title : '错误'
			});
		}
	}
	
	function addKeyInfo(){
		if($('#status').combobox('getValue') != 1){
        	$.messager.alert('错误', "状态为正常的人员才能分配钥匙！", 'error');
        	return;
        }
		
		$.messager.progress({title:'处理中'});
		form.form('submit', {
			url: recordSaveUrl,
			type: 'POST',
			onSubmit: function(){
				var isValid = $(this).form('validate');
		        if (!isValid){
					$.messager.progress('close');
				}else{
					$('#save_button').attr("disabled", false);
				}
				return isValid;
	        },
			success: function(data){
				$.messager.progress('close');
				var repObj = eval("(" + data + ")");
				var resultCode = repObj.resultCode;
			  	if(resultCode == 0){
			        if(grid){
						grid.datagrid('unselectAll');
						grid.datagrid('reload');
					}
			        viewLockRecords("钥匙资料管理",  basePath + '/pages/device/keyInfo.jsp?perId='+
			        		repObj.resultMessage+'&perOrgId='+$('#orgId').combotree('getValue'));
					
			        win.window('close');
				}else{
					$.messager.alert('错误', repObj.resultMessage, 'error');
					$('#save_button').attr("disabled", false);
				}
			}
		});
	}
	
	function resetPerPassword(){
		var rows = grid.datagrid('getSelections');//返回所有被选择的行
		if (rows.length == 1) {
			$.messager.confirm('重置密码确认', '你确认要重置该人员的APP登录密码?', function(r){
	        	if(r){
	            	$.get(resetPerPasswordUtl, {
	            		'dto.perId': rows[0].perId
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
	
	function showImportWin(){
		importWin = $('#import-window').window({
		    modal:true
		});
		importWin.window('open');
		importFrom = importWin.find('form');
	}
	
	//导入数据
	function importGroup(){
		Net.functions.saveRow(importWin, importFrom, grid,importFileUrl);
	};
	
	function closeImportWin(){
		importWin.window('close');
	};
	
	//模板文件下载
	var mouldDownLoad = function(){
		$("#downloadIframe").attr("src", mouldDownLoadUrl + "?downLoadFileName=personnelInfoMould.xls");
	}
	
	var tree;
	var treePanel;
	var tempOrgId="";
	var tempPerIds;
	$(function() {
		treePanel = $('#treePanel').panel({
			tools : [ {
				iconCls : 'icon-reload',
				handler : function() {
					tree.tree('reload');
				}
			}, {
				iconCls : 'icon-redo',
				handler : function() {
					var node = tree.tree('getSelected');
					if (node) {
						tree.tree('expandAll', node.target);
					} else {
						tree.tree('expandAll');
					}
				}
			}, {
				iconCls : 'icon-undo',
				handler : function() {
					var node = tree.tree('getSelected');
					if (node) {
						tree.tree('collapseAll', node.target);
					} else {
						tree.tree('collapseAll');
					}
				}
			} ]
		});
		
		tree = $('#orgTree').tree({
			url : menuUrl,
			//url : 'layout/tree_data.json',
			animate : true,
			checkbox : true,
			cascadeCheck : false,
			checkbox: true,
			lines: true,
			plain: false,
//			onDblClick : function(node) {
//				var str = JSON.stringify(node);
//				var jsonobj=eval('('+str+')');
//				grid.datagrid('unselectAll');
//				grid.datagrid('load',{
//					'dto.orgId' :jsonobj.id
//				});
//			},
			onCheck : function(node, checked){
				var str = JSON.stringify(node);
				var jsonobj=eval('('+str+')');
				var nodes = $('#orgTree').tree('getChecked');
				var s = '';
                for (var i = 0; i < nodes.length; i++) {
                    if (s != '') 
                        s += ',';
                    s += nodes[i].id;
                }
                grid.datagrid('unselectAll');
				grid.datagrid('load',{
					'dto.orgIds' :s
				});
					tempOrgId=s;
			}
		});
	});
	$(function() {
        $("#treePanel").slimScroll({
        	alwaysVisible: true,
        	railColor: '#222',
        	color: '#ffcc00',
            height: '370px;'
        });
    });
	
	function getChildren(children){
		return false;
	}
	
	//导出人员相关信息
	function exportExcel(){
		getSelectionsIds();
		if(tempOrgId==""&&tempPerIds==""){
			alert("请选择需要导出的人员或区域！！！！！");
			return false;
		}
		var queryParams = {
			'dto.perAccounts':$("#q_perAccounts").val(),
			'dto.perName':$("#q_perName").val(),
			'dto.status':$("#q_status").combobox('getValue'),
			'dto.orgIds':tempOrgId!=null?tempOrgId:"",
			'dto.perIds':tempPerIds!=null?tempPerIds:"",
		}
		Net.functions.excelExport(exportQueryUrl, queryParams);
	}
	
	//获取选择的人员
	function getSelectionsIds(){
		var rows = grid.datagrid('getSelections');
		var id = grid.datagrid('options').idField;
		
		var ids = [];
		for (var i = 0; i < rows.length; i ++){
			ids = ids.concat([rows[i][id]]);
		}
		tempPerIds=ids;
	};
	
	