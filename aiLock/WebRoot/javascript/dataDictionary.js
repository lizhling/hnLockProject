var isOrNoGroup = [['1','是'],['0','否']];//是与否
var areOrisNoGroup = [['1','有'],['0','无']];//是与否
var enDiStatusGroup = [['1','启用'],['0','禁用']];//菜单/资源等状态
var resTypeGroup = [['0','网页'],['1','按钮'],['2','图片']];//资源类型
var noticeStatusGroup = [['1','发布'],['2','关闭']];//公告状态
var userStatusGroup = [['1','正常'],['2','禁用'],['3','已删除']];//平台用户状态
var dataImportType = [['1','普通'],['2','数据库']];//平台用户状态
var keyStatusGroup = [['0','未激活'],['1','使用中'],['2','禁用中'],
                      ['3','故障'],['4','挂失'],['5','已删除']];//钥匙状态
var keyTypeGroup = [['1','智能钥匙'],['2','门卡'],['3','身份证'],['4','手机']];//钥匙类型
var lockStatusGroup = [['0','未激活'],['1','使用中'],['2','禁用中'],
                      ['3','故障'],['4','挂失'],['5','已删除']];//门锁状态
var lockTypeGroup = [['1','无源锁'],['2','有源锁']];//门锁类型
var statusCodeGroup = [['01','有效'],['00','无效']];//开锁授权状态
var permissionsTypeGroup = [['2','通用菜单'],['1','人员菜单'],['0','管理人员菜单']];//app菜单权限类型

var hoursGroup = [['23','23点'],['00','0点'],['08','8点'],['09','9点'],['10','10点'],['11','11点'],['12','12点'],
                  ['13','13点'],['14','14点'],['15','15点'],['16','16点'],['17','17点'],['18','18点'],['20','20点'],['21','21点'],
                  ['22','22点'],['01','1点'],['02','2点'],['03','3点'],['04','4点'],['05','5点'],['06','6点'],['07','7点']];


var passiveRecordTpyeGroup = [['33','智能钥匙开门'],['3A','开门无权限'],['3B','挂失钥匙开门'],['34','远程授权开门'],['36','失败-用户码错误'],['37','失败-未到有效期'],
							  ['38','失败-超过有效期'],['39','失败-锁密码错误']];//无源记录类型


var activeRecordTpyeGroup = [
['06','远程开门'],
['30','APP蓝牙开门'],
['00','用户进门'],
['19','出门按钮开门']];//有源记录类型

/*['01','遥控开锁'],
['11','用户出门'],
['08','非法卡试图进门'],
['16','非法卡试图出门'],
['10','限制状态试图进门'],
['05','非法入侵'],
['15','超过时效的卡出门失败'],
['20','删除母卡删卡'],
['25','门未关好报警'],
['29','心跳包推送'],
['02','火警联动触发自动开锁'],
['07','禁行时段用户进门失败'],
['12','门外自助查询权限'],
['17','限制状态试图出门'],
['03','密码开锁'],
['13','门内自助查询权限'],
['18','卡+密码开门'],
['04','机械钥匙技术性开锁'],
['09','超过时效的卡进门失败'],
['14','禁行时段用户出门失败']*/

//用于门锁列表区分显示颜色
var alarmTypeGroup = [['05','1'],//非法入侵
                      ['02','2'],//火警联动触发自动开锁
                      ['04','3'],//机械钥匙技术性开锁
                      ['25','3'],
                      ['58','3']];//门未关好报警

var alarmLevelGroup = [['1','紧急告警'],['2','重要告警'],['3','一般告警']];

var versionOsGroup = [['1','IOS'],['2','Android']];//发布平台
var updateTypeGroup = [['1','强制更新'],['2','可选择更新']];//版本更新类型

var dataToSelect = function(dataGroup,selectId){
	var dataItems = [];
	if(selectId.indexOf("q_") > -1){
	    dataItems.push('[{"value":"","text":"--请选择--"},');
	}else{
		dataItems.push('[');
	}
    var f = "";
	for(var i=0;i<dataGroup.length;i++){
		var keyValue = dataGroup[i];
		dataItems.push(f,'{"value":"',keyValue[0],'","text":"',keyValue[1],'"}');
		f = ","
	}
    dataItems.push("]");
    $('#'+selectId).combobox('loadData',eval(dataItems.join('')));
    $('#'+selectId).combobox('setValue','');
};

var dataValueConv = function(dataGroup,dataValue){
	for(var i=0;i<dataGroup.length;i++){
		var keyValue = dataGroup[i];
		if(dataValue == keyValue[0]){
			return keyValue[1];
		}
	}
};

var jsonToSelect = function(url,addOptionText,selectId){
	var dataItems = [];
	
	if(isNotUndefined(addOptionText)){
	    dataItems.push('[{"value":"","text":"',addOptionText,'"},');
	}else{
		dataItems.push('[');
	}
	
	$.get(url, {
	},
	function(data) {
		var repObj = eval("(" + data + ")");
	  	var dataArray = repObj.rows;
	  	if (dataArray != 0 && dataArray.length > 0) {
	  		var f = "";
	  		for(var i=0;i<dataArray.length;i++){
	  			dataItems.push(f,'{"value":"',dataArray[i][0],'","text":"',dataArray[i][1],'"}');
  				f = ",";
	  		}
	  	}
	  	dataItems.push("]");
	  	var selId = selectId.split(",");
	  	for(var i=0;i<selId.length;i++){
	  		$('#'+selId[i]).combobox('loadData',eval(dataItems.join('')));
	  	}
	});
}
