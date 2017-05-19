	//初始化数据列表
	function initDataGrid() {
		var options = $.extend({},
			Net.temple.datagrid,
			{
				url: recordQueryUrl,
				idField:'lockId',//表明该列是一个唯一列
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				nowrap:true,
				columns:[[
				    {title: '联机',field: 'onlineStauts',sortable: true,width: 33,align:'left',
				    	formatter : function(value, rec) {
//				    		alert(value);
				    		if(value == '1'){
				    			return "<img style='padding-top:0px' src='"+basePath+"css/images/zx.png'></img>";
				    		}else if(value == '0'){
				    			return "<img style='padding-top:0px' src='"+basePath+"css/images/lx.png'></img>";
				    		}
				    	}
				    },
				    {title: '门锁编码',field: 'lockCode',sortable: true,width: 130},
				    {title: '门锁名称',field: 'lockName',sortable: true,width: 170},
				    {title: '蓝牙名称',field: 'lockInBlueCode',sortable: true,width: 80},
				    {title: '门锁蓝牙MAC',field: 'blueMac',sortable: true,width: 80},
//				    {title: '蓝牙KEY',field: 'privateKey',sortable: true,width:80},
				    {title: '类型',field: 'lockType',sortable: true,width: 50,
				    	formatter : function(value, rec) {
				    		return dataValueConv(lockTypeGroup,value)
				    	}
				    },
				    {title: '可配卡',field: 'wheCanMatchCard',sortable: true,width: 45,
				    	formatter : function(value, rec) {
				    		return dataValueConv(isOrNoGroup,value)
				    	}
				    },
//				    {title: '蓝牙',field: 'lockInBlueCode',width: 35,
//				    	formatter : function(value, rec) {
//				    		if(isUndefined(value)){
//				    			return "无";
//				    		}else{
//				    			return "有";
//				    		}
//				    	}
//				    },
				    {title: '所在区域',field: 'areaName',width: 70},
					{title: '所属组织',field: 'orgName',width: 100},
					//{title: '管理员',field: 'managerName',width: 60},
					{title: '所在地址',field: 'lockAddres',width: 240},
					{title: '状态',field: 'status',sortable: true,width: 45,
						formatter : function(value, rec) {
							return dataValueConv(lockStatusGroup,value)
						}
					},
					{title: '备注',field: 'note',sortable: true,width: 230}
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
					id:'unlock', text:'远程开锁', iconCls:'icon-unlock',
					handler:function(){
						remoteUnlock();
					}
				},{
					id:'lockState', text:'获取门锁状态', iconCls:'icon-search',
					handler:function(){
						getLockState();
					}
				}/*,{
					id:'setTime', text:'设置时间', iconCls:'icon-search',
					handler:function(){
						setLockTime();
					}
				}*/,{
					id:'lockState', text:'查看门锁记录', iconCls:'icon-search',
					handler:function(){
						var rows = grid.datagrid('getSelections');//返回所有被选择的行
						if (rows.length == 1) {
							editRow = rows[0];
							viewLockRecords("开锁记录",  basePath + '/pages/analysis/unlockRecords.jsp?lockCode='+editRow.lockCode);
						} else {
							$.messager.show({
								msg : '请选择一把门锁查看！',
								title : '错误'
							});
						}
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
					id:'search', text:'导入模版下载', iconCls:'icon-down',
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
			'dto.lockCode':$("#q_lockCode").val(),
			'dto.lockName':$("#q_lockName").val(),
			'dto.lockType':$("#q_lockType").combobox('getValue'),
			'dto.status':$("#q_status").combobox('getValue'),
			'dto.orgId' : $("#q_orgId").combotree('getValue'),
			'dto.areaId':$('#q_areaId').combotree('getValue'),
			'dto.lockInBlueCode':$('#q_BtName').val(),
			'dto.isVicePassive':$("#q_isVicePassive").combobox('getValue'),
			'dto.isBlueConfig':$("#q_isBlueConfig").combobox('getValue')
		});
	}
	
	function initComboboxDate(){
		dataToSelect(lockTypeGroup,'q_lockType');
		dataToSelect(lockStatusGroup,'q_status');
		dataToSelect(lockStatusGroup,'status');
		
		$('#q_isBlueConfig').combobox({
			onSelect:function(newValue, oldValue){
				if(newValue != ""){
					$('#q_lockType').combobox("setValue",'2');
				}
			}
		});
		
		$('#q_isVicePassive').combobox({
			onSelect:function(newValue, oldValue){
				if(newValue != ""){
					$('#q_lockType').combobox("setValue",'2');
				}
			}
		});	
		
		$('#lockType').combobox({
			onChange:function(newValue, oldValue){
				if(newValue == "1"){
					$('#lockInModuleCode').validatebox({required:false});
					$('#lockInModuleCode').attr("disabled","disabled").val('');
					$('#lockDeviceNo').validatebox({required:false});
					$('#lockDeviceNo').attr("disabled","disabled").val('');
					$('#ipAddress').attr("disabled","disabled").val('');
					$('#lockInBlueCode').attr("disabled","disabled").val('');
					$('#wheCanMatchCard').combo({readonly:false}).combobox("setValue",'0');
					$('#vicePassiveLockCode').attr("disabled","disabled").val('');
					
					$('#lockCode').validatebox({validType:'HexadecimalCheck[8]', required:true});
					$('#lockCode').removeAttr("readonly");
				}else{
					$('#lockInModuleCode').removeAttr("disabled");
					$('#lockInModuleCode').validatebox({required:true});
					$('#lockDeviceNo').removeAttr("disabled");
					$('#lockDeviceNo').validatebox({required:true});
					$('#ipAddress').removeAttr("disabled");
					$('#wheCanMatchCard').combo({readonly:false}).combobox("setValue",'0');
					$('#vicePassiveLockCode').removeAttr("disabled");
					
					$('#lockCode').validatebox({validType:null, required:false}).val('');
					$("#lockCode").attr("readonly","readonly");
				}
			}
		});
		
		dataToSelect(areOrisNoGroup,'q_isVicePassive');
		dataToSelect(areOrisNoGroup,'q_isBlueConfig');
		dataToSelect(lockTypeGroup,'lockType');
		dataToSelect(isOrNoGroup,'wheCanMatchCard');
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
			$('#lockInModuleCode').val($('#lockInModuleCode').val().substr(12, 12));
		}else{
			form.form('clear');
			editRow = null;
		}
	}
	
	//保存数据
	function submitForm(){
		if($('#lockType').combobox('getValue') == 2){
			var lockInModuleCode = $('#lockInModuleCode').val();
			var lockCode = lockInModuleCode + $('#lockDeviceNo').val();
			$('#lockCode').val(lockCode);
		}
		Net.functions.saveRow(win, form, grid,recordSaveUrl);
	};
	
	//删除数据
	function deleteRows(){
		Net.functions.batchRows(grid, recordDeleteUrl, '删除');
	};
	
	var lockId = "";
	function remoteUnlock(){
		lockId = checkLock();
		if(lockId != ""){
			$('#unLockWin').window({
			    modal:true
			});
			$('#unLockWin').window('open');
		}
	}
	
	function confirmUnlock(){
		//$('#unlock').attr("disabled", true);
		if($('#unLockPassw').val() != "123"){
			$.messager.show({
				title : '错误', msg : '远程开锁密码错误！'
			});
			return;
		}
		$('#unLockWin').window('close');
		$('#unLockPassw').val("");
		
		$.get(remoteUnlockUtl, {
			'dto.lockId':lockId
		},
		function(data) {
			var repObj = eval("(" + data + ")");
			var resultCode = repObj.resultCode;
			if(resultCode == 0){
				$.messager.show({
					title : '提示', msg : '开锁成功！'
				});
			}else{
				$.messager.alert('错误', repObj.resultMessage, 'error');
			}
			//setTimeout("$('#unlock').attr('disabled', false);",7000);
		});
	}
	

	function setLockTime(){
		var lockId = checkLock();
		if(lockId != ""){
			$.get(setLockTimeUtl, {
				'dto.lockId':lockId
			},
			function(data) {
				var repObj = eval("(" + data + ")");
				var resultCode = repObj.resultCode;
				if(resultCode == 0){
					$.messager.show({
						title : '提示', msg : '时间设置成功！'
					});
				}else{
					$.messager.alert('错误', repObj.resultMessage, 'error');
				}
			});
		}
	}
	
	function getLockState(){
		var lockId = checkLock();
		if(lockId != ""){
			$.get(getLockStateUtl, {
				'dto.lockId':lockId
			},
			function(data) {
				var repObj = eval("(" + data + ")");
				var resultCode = repObj.resultCode;
				if(resultCode == 0){
					$.messager.alert('门锁状态信息',repObj.resultMessage);
				}else{
					$.messager.alert('错误', repObj.resultMessage, 'error');
				}
			});
		}
	}

	function checkLock(){
		var rows = grid.datagrid('getSelections');//返回所有被选择的行
		if (rows.length == 1) {
			var lockType = rows[0].lockType;
			if(lockType != 2){
				$.messager.show({
					msg : '该锁不是有源门锁，不支持远程操作！',
					title : '错误'
				});
				return "";
			}
			return rows[0].lockId;
		} else {
			$.messager.show({
				msg : '请选择需要远程操作的门锁！',
				title : '错误'
			});
		}
		return "";
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
		$("#downloadIframe").attr("src", mouldDownLoadUrl + "?downLoadFileName=lockInfoMould.xls");
	}
	

		
	var tree;
	var treePanel;
	var tempAreaId;
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
		tree = $('#areaTree').tree({   
	   		 url: menuUrl,   
	    	 animate : true,
			 checkbox : true,
			 lines: true,
			 plain: false,
//			 cascadeCheck : false,
//	    	 onDblClick : function(node) {
//				var str = JSON.stringify(node);
//				var jsonobj=eval('('+str+')');
//				grid.datagrid('unselectAll');
//				grid.datagrid('load',{
//					'dto.areaId':jsonobj.id
//				});
//				tempAreaId = jsonobj.id;
//				//获取锁信息数据
//				getAreaLockInfo();
//			},
			onCheck : function(node, checked){
				var str = JSON.stringify(node);
				var jsonobj=eval('('+str+')');
				var nodes = $('#areaTree').tree('getChecked');
				var s = '';
                for (var i = 0; i < nodes.length; i++) {
                    if (s != '') 
                        s += ',';
                    s += nodes[i].id;
                }
                grid.datagrid('unselectAll');
				grid.datagrid('load',{
					'dto.areaIds' :s
				});
				tempAreaId = s;
				getAreaLockInfo();
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

    function popWin_func() { 
		$("#popWin").slideDown("slow"); $("#reshow").hide(); 
	}
	
	$(function () { 
		//定时弹出锁信息对话框
//             setTimeout(function () {
//                 popWin_func();
//             }, 2000)
             $("#close").click(function () {
                 $("#popWin").slideUp("slow"); $("#reshow").show();
             })
             $("#reshow").mouseover(function () {
                 popWin_func(); //jihua.cnblogs.com
              })
	}); 
	
	//获取区域内锁信息列表
	function getAreaLockInfo(){
		$.get(getDevLockCountUtl, {
			'dto.areaIds':tempAreaId
		},
		function(data) {
			　　//jQuery 方式
			    var dataset = $.parseJSON(data);
			    var retCode = JSON.stringify(dataset.resultCode);
			    var Value = dataset.resultMessage;
				var jsonobj=eval('('+Value+')');
				$("#onlineLockNum").text(jsonobj.onlineLock);
				$("#unlineLockNum").text(jsonobj.unlineLock);
				$("#btLockNum").text(jsonobj.btLock);
				popWin_func();
		});
	}
	
	
	//导出门锁信息
	function exportExcel(){
		var queryParams = {
			'dto.lockCode':$("#q_lockCode").val(),
			'dto.lockName':$("#q_lockName").val(),
			'dto.lockType':$("#q_lockType").combobox('getValue'),
			'dto.status':$("#q_status").combobox('getValue'),
			'dto.orgId' : $("#q_orgId").combotree('getValue'),
			'dto.areaId':$('#q_areaId').combotree('getValue'),
			'dto.areaIds':tempAreaId!=null?tempAreaId:"",
			'dto.isVicePassive':$("#q_isVicePassive").combobox('getValue'),
			'dto.isBlueConfig':$("#q_isBlueConfig").combobox('getValue')
		}
		Net.functions.excelExport(exportQueryUrl, queryParams);
	}
	