
function viewLockRecords(title, url){
	parent.centerTabs.tabs('add', {
		title : title,
		closable : true,
		content : '<iframe src="' + url +'" frameborder="0" style="width:100%;height:100%;"></iframe>'
	});
}

function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

var getDataGrid = function(data){
	var columns = new Array();
	var colData = null;
	var line_1 = new Array();
	var line_2 = new Array();
	var line_3 = new Array();
	var line_4 = new Array();
	var line_5 = new Array();
	
	var repObj = eval("(" + data + ")");
  	var dataArray = repObj.rows;
  	if (dataArray != 0 && dataArray.length > 0) {
		//动态生成表头开始
		$.each(dataArray,function(){
			colData = new Object();
			colData.field = this.field;
			colData.title = this.title;
			colData.width = this.width;
			colData.rowspan = this.rowspan;
			colData.colspan = this.colspan;
			colData.align = 'center';
			if(this.sortable == 1){
				colData.sortable = true;
			}
			
			switch (this.lineLevel) {
				case 1: line_1.push(colData); break;
				case 2: line_2.push(colData); break;
				case 3: line_3.push(colData); break;
				case 4: line_4.push(colData); break;
				case 5: line_5.push(colData); break;
				default: break;
			}
			
		});
		for(var i = 1;i < 6;i++){
			if(eval("line_"+ i).length > 0){
				columns.push(eval("line_"+ i));
			}
		}
	};
	return columns;
}