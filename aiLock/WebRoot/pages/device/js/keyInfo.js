	var perCorrect = false;
	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'keyId',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
				    {title: '钥匙编码',field: 'keyCode',sortable: true,width: 150},
				    {title: '钥匙名称',field: 'keyName',sortable: true,width: 150},
				    {title: '类型',field: 'keyType',sortable: true,width: 80,
				    	formatter : function(value, rec) {
				    		return dataValueConv(keyTypeGroup,value)
				    	}
				    },
				    {title: '持有人',field: 'perName',sortable: true,width: 80},
					{title: '所属组织',field: 'orgName',width: 100},
					{title: '状态',field: 'status',sortable: true,width: 80,
						formatter : function(value, rec) {
							return dataValueConv(keyStatusGroup,value)
						}
					},
					{title: '备注',field: 'note',sortable: true,width: 280}
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
					id:'rPhone', text:'钥匙解绑', iconCls:'icon-edit',
					handler:function(){
						keyNnbundling();
					}
				}]
		    });
		grid =  $('#datagrid').datagrid(options);
		
		$('#perId').combobox({
			onChange: function(newValue, oldValue){
				perCorrect = false;
			},
			onSelect: function(record){
				perCorrect = true;
			}
		});
		
		$('#orginfo_left','q_orgId').combotree({   
    	url: 'orgInfoUrl',   
    	required: true  
		}); 
	}
	
	function search() {
		alert($("#q_orgId").combotree('getValue'));
		grid.datagrid('unselectAll');
		grid.datagrid('load',{
			'dto.keyCode':$("#q_keyCode").val(),
			'dto.keyName':$("#q_keyName").val(),
			'dto.keyType':$("#q_keyType").combobox('getValue'),
			'dto.status':$("#q_status").combobox('getValue'),
			'dto.orgId' : $("#q_orgId").combotree('getValue'),
			'dto.perId':$("#q_perId").combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		jsonToSelect(findKeyGroupOptionsUtl,"",'groupId');
		jsonToSelect(findPersonnelInfoOptionsUtl,"",'perId,q_perId');
		
		dataToSelect(keyTypeGroup,'q_keyType');
		dataToSelect(keyStatusGroup,'q_status');
		dataToSelect(keyTypeGroup,'keyType');
		dataToSelect(keyStatusGroup,'status');
		
		var perId = getQueryString("perId");
		var perOrgId = getQueryString("perOrgId");
		if(isNotUndefined(perId) && isNotUndefined(perOrgId)){
			add();
			setTimeout(initializePer(perId,perOrgId),500);
		}
	}
	
	function initializePer(perId, perOrgId){
		$("#perId").combobox('setValue', perId);
		$("#orgId").combotree('setValue', perOrgId);
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
		$('#keyType').combo({
			onChange:function(newValue, oldValue){
				if(newValue == 1){
					$("#keyCodeTd").html("钥匙编码：");
					$("#keyNameTd").html("钥匙名称：");
					$('#keyCode').validatebox({
						 validType:'HexadecimalCheck[8]'
					});
					$('#groupId').combo({disabled:false});
					$('#lockingTime').numberbox({disabled:false,value:"24"});
					
					//$('#blueName').removeAttr("disabled").val("CT");
				}else if(newValue == 2 || newValue == 3 || newValue == 4){
					$('#keyCode').validatebox({
						 validType:'HexadecimalCheck[16]'
					});
					$('#groupId').combo({disabled:true,value:""});
					$('#lockingTime').numberbox({disabled:true,value:""});
					//$('#blueName').attr("disabled","disabled").val('');
				}
				
				if(newValue == 2){
					$("#keyCodeTd").html("门卡卡号：");
					$("#keyNameTd").html("门卡名称：");
				}else if(newValue == 3){
					$("#keyCodeTd").html("身份证ID：");
					$("#keyNameTd").html("身份证名称：");
				}else if(newValue == 4){
					$("#keyCodeTd").html("手机ID：");
					$("#keyNameTd").html("手机名称：");
				}
			}
		})
		
		win = $('#example-window').window({  
		    modal:true
		});
		win.window('open');
		form = win.find('form');
		
		if (!isNewRecord && editRow != null) {
			Net.functions.initForm(form,editRow,true);
			perCorrect = true;
		}else{
			form.form('clear');
			editRow = null;
		}
	}
	
	//保存数据
	function submitForm(){
		if(!perCorrect){
			$.messager.show({
				msg : '该持有人员不存在，请重新选择！',
				title : '错误'
			});
			return;
		}
		Net.functions.saveRow(win, form, grid,recordSaveUrl);
	};
	
	//删除数据
	function deleteRows(){
		Net.functions.batchRows(grid, recordDeleteUrl, '删除');
	};
	
	function keyNnbundling(){
		var rows = grid.datagrid('getSelections');//返回所有被选择的行
		if (rows.length == 1) {
			if(rows[0].keyType != 1){
				$.messager.show({
					msg : '该钥匙不是智能钥匙，不支持解绑功能！',
					title : '错误'
				});
				return;
			}
			$.messager.confirm('钥匙解绑确认', '你确认对该钥匙和绑定手机进行解绑?', function(r){
	        	if(r){
	            	$.get(keyNnbundlingUtl, {
	            		'dto.keyId': rows[0].keyId
	         		},
	         		function(data) {
	         			var repObj = eval("(" + data + ")");
	         			var resultCode = repObj.resultCode;
	        			if(resultCode == 0){
	        				$.messager.show({
	        					title : '提示', msg : '钥匙解绑成功！'
	        				});
	        			}else{
	        				$.messager.alert('错误', repObj.resultMessage, 'error');
	        			}
	         		});
	        	}
			});
		}else{
			$.messager.show({
				msg : '请选择一项进行解绑！',
				title : '错误'
			});
		}
	}
	
	var tree;
	var treePanel;
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
					'dto.orgids' :s
				});
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
	