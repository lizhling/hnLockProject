Net.temple.datagrid = {
				btnStatus: 0,  //0表示正常页面、10、表述选择弹出页面选择数据
				nowrap: false,
				striped: true,//true:显示行背景
				fit: true,
				url:'',
				sortName:'',
				sortOrder: 'desc',//定义排序顺序
				remoteSort: false,//是否通过远程服务器对数据排序
				rownumbers:true,//true:显示行数
				pagination:true,//true:在数据表格底部显示分页工具栏。
				autoRowHeight:false,
				pageSize:15,
				pageList:[10,15,20,30,50],
				columns:[],
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
				},{
					id:'btndelete',
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						deleteRows();
					}
				}/*,{
					id:'search',
					text:'查看',
					iconCls:'icon-search',
					handler:function(){
						viewRow();
					}
				}*/],
				onDblClickRow: function(rowIndex, rowData){
					grid.datagrid('clearSelections');
					grid.datagrid('checkRow',rowIndex);
					edit();
				},
				onLoadSuccess: function(data){
					//alert(JSON.stringify(data));
					if(window.parent.closeProgress){//判断方法是否存在
						parent.closeProgress();
					}
				}
			};


Net.temple.treegrid = {
				title:'treegrid',
				iconCls:'icon-grid',
				fit: true,
				nowrap: false,
				rownumbers: true,
				animate:true,
				idField:'name',
				treeField:'name',
				
				
				toolbar:[{
					id:'btnadd',
					text:'新增',
					iconCls:'icon-add',
					handler:function(){
					
						addRow();
					}
				},{
					id:'btnedit',
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						editRow();
					}
				},'-',{
					id:'btndelete',
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						deleteRows();
					}
				}]
			};

Net.temple.datebox={
	forWeek:{
			formatter : function(date){
				var y = date.getFullYear();
				return y + '-' + Net.dateHelper.getWeek(date);
			},
			parser : function(s){
			   if (s) {
					var a = s.split('-');
					var y = new Number(a[0]);
					var w = new Number(a[1]);
					var dd = Net.dateHelper.weekToDate(y,w);
					return dd;
				} else {
					return new Date();
				}
			}
	},
	forMonth:{
			formatter : function(date){
				var y = date.getFullYear();
				var m = date.getMonth() + 1;
				var d = date.getDate();
				return y + '-' + (m < 10 ? '0' + m : m) ;
			},
			parser : function(s){
			   if (s) {
					var a = s.split('-');
					var y = new Number(a[0]);
					var m = new Number(a[1]);
					var d = 1;
					var dd = new Date(y, m - 1, d);
					return dd;
				} else {
					return new Date();
				}
			}
	}
}