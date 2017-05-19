
var isNotUndefined = function(value) {
	return !isUndefined(value);
};

var isUndefined = function(value) {
	if(value == undefined || value == "" || value == "undefined" ){
		return true;
	}
	return false;
};

var undefinedShift = function(value) {
	if(isUndefined(value)){
		return "";
	}
	return value;
};

var getQueryString = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

var removeDuplicateArray = function(o){
	var obj={},ret=new Array(),i=0;
	for(var a in o){
		obj[o[a]]=o[a];
	}
	for(ret[i++] in obj);
	return ret;
}
