
Net.functions = {
	formStatus : false, 
	loadFormSuccess : function (fn, data){
		fn(data);
	},
	batchRows : function (grid,url,action){
		var rows = grid.datagrid('getSelections');
		var id = grid.datagrid('options').idField;
		if (rows.length>0){
			$.messager.confirm('请确认', '确定' + action + rows.length + '条记录?', function(r){
				if (r){
					var ids = [];
					for (var i = 0; i < rows.length; i ++){
						ids = ids.concat([rows[i][id]]);
					}
					$.ajax({
						url: url+'?'+id+'s='+ids.join(','),
						type: 'POST',
						error: function(XMLHttpRequest, textStatus, errorThrown){
							$.messager.alert('错误','服务器错误','error');
						},
						success: function(data){
							var repObj = eval("(" + data + ")");
							var resultCode = repObj.resultCode;
						  	if(resultCode == 0){
						        $.messager.show({
									title : '提示',
									msg : '成功' + action + '！'
								});
						        if(grid){
									grid.datagrid('unselectAll'); 
									grid.datagrid('reload');
								}
							}else{
								$.messager.alert('错误', repObj.resultMessage, 'error');
								$('#save_button').attr("disabled", false);
							}
						}
					});
				}
			});
		} else {
			$.messager.alert('提示','请选择要删除的记录！','info');
		}
	},
	batchDealRows: function (grid,url,action){
		var rows = grid.datagrid('getSelections');
		var id = grid.datagrid('options').idField;
		if (rows.length>0) {
			var ids = [];
			for (var i = 0; i < rows.length; i ++){
				ids = ids.concat([rows[i][id]]);
			}
			$.ajax({
				url: url+'?'+id+'s='+ids.join(','),
				type: 'POST',
				error: function(XMLHttpRequest, textStatus, errorThrown){
					$.messager.alert('错误','服务器错误','error');
				},
				success: function(data){
					var repObj = eval("(" + data + ")");
					var resultCode = repObj.resultCode;
				  	if(resultCode == 0){
				        $.messager.show({
							title : '提示',
							msg : action
						});
				        if(grid){
							grid.datagrid('unselectAll'); 
							grid.datagrid('reload');
						}
					}else{
						$.messager.alert('错误', repObj.resultMessage, 'error');
						$('#save_button').attr("disabled", false);
					}
				}
			});
		} else {
			$.messager.alert('提示','最少选择一条记录!','info');
		}
	},
	batchRowsAction : function (grid,url,action,type,memo,other)
	{	
		var rows = grid.datagrid('getSelections');
		var id = grid.datagrid('options').idField;
		if (rows.length>0)
		{
			$.messager.confirm('请确认', '确定'+action+rows.length+'条记录?', 
					function(r)
					{
						if (r)
						{
							var ids = [];
							for (var i = 0; i < rows.length; i ++) 
							{
								ids = ids.concat([rows[i][id]]);
							}
							$.ajax(
							{
								url:url+'&memo='+memo+'&other='+other,
								//url: url+'?'+id+'s='+ids+'&type='+type+'&other='+other+'&memo='+memo,
								type: 'POST',
								error: function(XMLHttpRequest, textStatus, errorThrown)
								{
									$.messager.alert('错误','服务器错误','error');
								},
								success: function(data)
								{
									var result = new OperateResult();
									result.init(data);
									if(result.getDataCount() > 0)
									{
										$.messager.alert('提示','成功' + action + result.getDataCount() + '条记录','info');
										grid.datagrid('unselectAll'); 
										grid.datagrid('reload');
									}
									else //session过期，返回登录页面
									{
										if ('error' == result.getError())
										{
											$.messager.alert('错误', action + result.getMsg(), 'error');
										}
										/*if('session'==result.getError())
										{
											var top_href = top.location.href;
											top.location = top_href;
										}*/
									}
								}
							});
						}
					});
		} 
		else 
		{
			$.messager.alert('提示','最少选择一条记录!','info');
		}
	},
	batchAuditFinance : function (grid,url,action)
	{
		var rows = grid.datagrid('getSelections');
		var id = grid.datagrid('options').idField;
		if (rows.length>0)
		{
			$.messager.confirm('请确认', '确定'+action+rows.length+'条记录?', 
					function(r)
					{
						if (r)
						{
							var ids = [];
							for (var i = 0; i < rows.length; i ++) 
							{
								ids = ids.concat([rows[i][id]]);
							}
							$.ajax(
							{
								url: url+'&'+id+'s='+ids,
								type: 'POST',
								error: function(XMLHttpRequest, textStatus, errorThrown)
								{
									$.messager.alert('错误','服务器错误','error');
								},
								success: function(data)
								{
									var result = new OperateResult();
									result.init(data);
									if(result.getDataCount() > 0)
									{
										$.messager.alert('提示','成功' + action + result.getDataCount() + '条记录','info');
										grid.datagrid('unselectAll'); 
										grid.datagrid('reload');
									}
									else //session过期，返回登录页面
									{
										if ('error' == result.getError())
										{
											$.messager.alert('错误', action + result.getMsg(), 'error');
										}
										/*if('session'==result.getError())
										{
											var top_href = top.location.href;
											top.location = top_href;
										}*/
									}
								}
							});
						}
					});
		} 
		else 
		{
			$.messager.alert('提示','最少选择一条记录!','info');
		}
	},
	
	addRow: function (win,form,url)
	{
		this.initForm(form, false);
		win.window('open');
		form.form('clear');
		form.url = url;
	},
	saveRow: function (win,form,grid,url){
		$.messager.progress({title:'处理中'});
		form.form('submit', {
			url:url,
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
			        $.messager.show({
						title : '提示',
						msg : '保存成功！'
					});
			        if(grid){
						grid.datagrid('unselectAll'); 
						grid.datagrid('reload');
					}
			        win.window('close');
				}else{
					$.messager.alert('错误', repObj.resultMessage, 'error');
					$('#save_button').attr("disabled", false);
				}
			}
		});
	},
	submitSave: function(form,url){
		this.initForm(form, false);
		form.form('submit',{
				url:url,
				type: 'POST',
				error: function(XMLHttpRequest, textStatus, errorThrown)
				{
					$.messager.alert('错误','服务器错误','error');
					$('#save_button').attr("disabled", false);
				},
				onSubmit: function()
				{  
				
					if ($(this).form('validate'))
					{  
						var json = new FormToJson();
						$('#formMap').attr({value: json.getJson()});
						$('#save_button').attr("disabled", true);
						return true;
					}
					$('#save_button').attr("disabled", false);
					return false;
				},
				success: function(data)
				{	 	
					var result = new OperateResult();
					result.init(data);
					if (result.isSuccess()) {
						alert('操作成功!');
						window.opener.query();
						window.close();
					} else {
						$.messager.alert('错误', result.getMsg(), 'error');
						$('#save_button').attr("disabled", false);
					}
				}
	    });
	},
	editRow: function (grid,win,loadUrl,updateUrl)
	{
		form = win.find('form');
		form.form('clear');
		var id = grid.datagrid('options').idField;
		var rows = grid.datagrid('getSelections');
		if (rows.length == 1)
		{
			this.initForm(form, false);
			win.window('open');
			
			form.form('load', loadUrl+'?'+id+'='+rows[0][id]+'&time='+new Date().getTime());
			form.url = updateUrl+'?'+id+'='+rows[0][id];
			return true;
		}
		else 
		{
			$.messager.alert('提示','请选择一条记录!','info');
			return false;
		}
	},
	viewRow: function (grid, win, loadUrl)
	{
		form = win.find('form');
		form.form('clear');
		var id = grid.datagrid('options').idField;
		var rows = grid.datagrid('getSelections');
		if (rows.length == 1)
		{
			this.initForm(form, true);
			win.window('open');
			form.form('load', loadUrl+'?'+id+'='+rows[0][id]+'&time='+new Date().getTime());
			form.url = '';
			return true;
		}
		else 
		{
			$.messager.alert('提示','请选择一条记录!','info');
			return false;
		}
	},
	
	clickViewRow: function (grid, win, loadUrl)
	{
		form = win.find('form');
		form.form('clear');
			this.initForm(form, true);
			win.window('open');
			form.form('load', loadUrl+'&time='+new Date().getTime());
			form.url = '';
			return true;
	},
	
	initForm: function (form,editRow,disable)
	{
		var className = "";
		var id = "";
		var object = $('form input, form select, form textarea, form img');
		for (var i = 0; i < object.length; i++){
			try{
				id = object[i].id;
				className = object[i].className;
				
				var value = "";
				for(key in editRow){
					if(key == id){
						value = editRow[key];
						break;
					}
				}
	
				if (className != null && className != ''){
					if(className.indexOf("easyui-textbox") >= 0){
						$('#'+id).textbox('setValue',value);
					}else if(className.indexOf("easyui-validatebox") >= 0){
						$('#'+id).val(value);
					}else if (className.indexOf("easyui-combobox") >= 0){
						$('#'+id).combobox('setValue',value);
					}else if (className.indexOf("easyui-combotree") >= 0){
						if(object[i].tagName == "SELECT"){
							var valueArray = value.split(", ");
							$('#'+id).combotree('setValues',valueArray);
						}else{
							$('#'+id).combotree('setValue',value);
						}
					}else if (className.indexOf("easyui-searchbox") >= 0){
						$('#'+id).searchbox('setValue',value);
					}else if (className.indexOf("easyui-numberbox") >= 0){
						$('#'+id).numberbox('setValue',value);
					}else if (className.indexOf("easyui-datebox") >= 0 || className.indexOf("easyui-datetimebox") >= 0){
						$('#'+id).datebox('setValue',value);
					}
				} else if(object[i].tagName == "IMG") {
					if(value != ""){
						$('#'+id).attr("src", basePath + value + "?time=" + new Date().getTime());//basePath：项目头路径,须在jsp页面中有定义
						
						var separator = value.lastIndexOf("/");
						if(value.lastIndexOf("\\") != -1){
							separator = value.lastIndexOf("\\");
						}
						
						var fileName = value.substring(separator,value.length);
						var filePath = value.substring(0, separator + 1);
						//alert(value);
						
						$('#'+id+"h").attr("src", basePath + filePath + "h" + fileName + "?time=" + new Date().getTime());
						$('#'+id+"xh").attr("src", basePath + filePath + "xh" + fileName + "?time=" + new Date().getTime());
					}
				} else {
					$('#'+id).val(value);
				}

			}catch (e) {
				$.messager.show({msg : "function.js 339-364 行报错：<br>" + e.message, title : '错误'});
			}
		}
		form.form('validate');
		return;
	},
	excelExport: function(url, queryParams){
//		window.open(url+"?q_queryType=1",'toDownload','');

		if ($('#exportWindow').length == 0) {
			$('body').append($('<div/>').attr({id : "exportWindow"}));
			$('#exportWindow').append($('<form method="post"></form>'));
		}
		var exportForm = $('#exportWindow').find('form');
		exportForm.form('submit',{
			url : url,
			queryParams: queryParams,
			type: 'POST',
			onSubmit : function() {
				return true;
			},
			success : function(data) {
			}
		});
	},
	addHiddenInput: function (form)
	{
		if ($('#formMap').length == 0)
		{
			form.append("<input type=\"hidden\"  name=\"formMap\" id=\"formMap\"/>");
		}
	},
	addTreeRow:function(grid,win,url){
			var row = grid.treegrid("getSelected");
			if(row){
				win.window('open');
				form = win.find('form');
				form.form('clear');
				form.url = url+'?parentId='+row.id;
			}
			else{
				$.messager.alert('提示','请选择一条记录!','info');
			}
			
	},
	saveTreeRow:function(form,grid){
		form.form('submit', {
			url:form.url,
			onSubmit: function(){
				   return $(this).form('validate');
	        },
			success:function(data){
				if ('${success}'){
					$.messager.alert('提示','操作成功!','info');
					grid.treegrid('reload');
					win.window('close');
				} else {
					$.messager.alert('错误', '${msg}', 'error');
				}
			}
		});
	},
	editTreeRow:function(grid,win,loadUrl,updateUrl){
		form = win.find('form');
		
		var row = grid.treegrid("getSelected");
		if(row){
			win.window('open');
			form.form('load', loadUrl+'?id='+row.id);
			form.url = updateUrl+'?id='+row.id;
		} else {
			$.messager.alert('提示','请选择一条记录!','info');
		}
		
	},
	deleteTreeRows : function(grid,url){
	
		var row = grid.treegrid("getSelected");
		if(row){
			$.messager.confirm('请确认', '确定删除该记录?', function(r){
				if (r){
					for (var i = 0; i < rows.length; i++) {
						$.ajax({
						    url: url+'?id='+row.id,
						    type: 'GET',
						    error: function(){
						       $.messager.alert('错误','服务器错误','error');
						    },
						    success: function(data){
								$.messager.alert('提示','成功删除${count}条记录','info');
									grid.treegrid('reload');
								 
						    }
						});
					}
				}
			});
		} else {
			$.messager.alert('提示','最少选择一条记录!','info');
		}
	}
	
}

/*$.extend($.fn.layout.methods, {  
    remove: function(jq, region){  
        return jq.each(function(){  
            var panel = $(this).layout("panel",region);
            if(panel){
            	panel.panel("destroy");
                var panels = $.data(this, 'layout').panels;
                panels[region] = $('>div[region=' + region + ']', $(this));
                $.data(this, 'layout').panels = panels;
                $(this).layout("resize");
            }
        });  
    },
    add:function(jq, params){
        return jq.each(function(){  
            var container =$(this);
            var panel = $('>div[region=' + params.region + ']', container);
            if(!panel.length){
                var pp = $('<div/>').attr("region",params.region).addClass('layout-body').appendTo(container);
                var cls = 'layout-panel layout-panel-' + params.region + ' layout-split-' + params.region;
                pp.panel($.extend({},params.options,{
                    cls : cls
                }));
                var panels = $.data(this, 'layout').panels;
                panels[params.region] = pp;
                $.data(this, 'layout').panels = panels;
                $(this).layout("resize");
            }
        });
    }
});*/


