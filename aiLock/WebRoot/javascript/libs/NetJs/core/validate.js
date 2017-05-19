$.extend($.fn.validatebox.defaults.rules,{
	minLength : {
		validator : function(value, param) {
			value = Trim(value);
			return value.length >= param[0];
		},
		message : '最少输入 {0} 个字符.'
	},
	maxLength : {
		validator : function(value, param) {
			value = Trim(value);
			return value.length < param[0];
		},
		message : '最多输入{0}个字符.'
	},
	textLength : {
		validator : function(value, param) {
			/**
			 * 去掉前后的空格 chenxh修改的
			 */
			value = Trim(value);
			return (value.length >= param[0] && value.length <= param[1]);
		},
		message : '请输入{0}-{1}个字符.'
	},
	numberType : {
		validator : function(value, param) {
			value = Trim(value);
			if (!IsNumber(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '必须输入 {0}-{1}位数字.'
	},
	intNumberType : {
		validator : function(value, param) {
			value = Trim(value);
			if (!spaceNumber(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '必须输入 {0}-{1}位数字.'
	},
	checkIntNumberType : {
		validator : function(value, param) {
			value = Trim(value);
			if (!IntNumber(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '必须输入 {0}-{1}位整数.'
	},
	checkCharType : {
		validator : function(value, param) {
			value = Trim(value);
			if (!Checkchar(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '最多输入 {0}-{1}位内容,不能特殊字符.'
	},
	checkCharTypes : {
		validator : function(value, param) {
			value = Trim(value);
			if (!Checkchars(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '最多输入 {0}-{1}位内容,不能特殊字符.'
	},
	BigEngType : {
		validator : function(value, param) {
			if (BigEng(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '最多输入 {0}-{1}位内容,只能为大写字母.'
	},
	numOrEngType : {
		validator : function(value, param) {
			value = Trim(value);
			if (NumOrEng(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '最多输入 {0}-{1}位内容,帐号只能由数字或英文组成.'
	},
	numOrEngTypeCheck : {
		validator : function(value, param) {
			value = Trim(value);
			if (NumOrEng(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '最多输入 {0}-{1}位内容,只能输入数字或英文.'
	},
	thisEmail : {
		validator : function(value, param) {
			value = Trim(value);
			if (!ThisEmail(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '请输入正确的邮箱地址.'
	},
	/*
	 * checkStringType:{ validator: function(value, param){
	 * value = Trim(value); if (!CheckString(value)) { return
	 * (value.length >= param[0] && value.length <= param[1]); }
	 * return false; }, message: '必须输入 {0}-{1}位内容,不能输入特殊字符.' },
	 */
	mobelType : {
		validator : function(value, param) {
			value = Trim(value);
			return IsMobel(value);
		},
		message : '请输入正确的手机号码'
	},
	numberDate : {
		validator : function(value, param) {
			value = Trim(value);
			if (!IsNumber(value)) {
				return value.length == 6
						&& Number(value % 100) <= 12;
			}
			return false;
		},
		message : '输入必须为年份月份字符串,例：201204'
	},
	dateType : {
		validator : function(value, param) {
			value = Trim(value);
			return IsValidDate(value);
		},
		message : '请输入格式为：yyyy-MM-dd 日期字符,例：2014-01-01'

	},
	emailType : {
		validator : function(value, param) {
			value = Trim(value);
			return IsEmailType(value);
		},
		message : '请输入电子邮箱地址！'
	},
	// 电子邮箱地址 校验。
	isEmail : {
		validator : function(value, param) {
			value = Trim(value);
			return IsEmail(value);
		},
		message : "请输入电子邮箱地址！"
	},
	isIdCard : {
		validator : function(value, param) {
			value = Trim(value);
			if ($('#' + param[2]).combobox('getText') == '身份证') {
				if (IsIdCard(value)) {
					return (value.length == param[0] || value.length == param[1]);
				}
				return false;
			} else {
				return true;
			}
		},
		message : '请输入正确的身份证号码！(必须输入 {0}或{1}位的证件号码)'
	},
	NumberFloatCheck : {
		validator : function(value, param) {
			value = Trim(value);
			if (NumberFloatCheck(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '请输入的数最大长度为{1}，若为小数时小数点前最多为5位，小数点后最多2位'
	},
	NumberFloatCheckToLongitude : {
		validator : function(value, param) {
			value = Trim(value);
			if (NumberFloatCheckToLongitude(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '经度或纬度输入不正确'
	},
	CannotChineseCharacter : {
		validator : function(value, param) {
			value = Trim(value);
			if (CheckHZString(value)) {
				return (value.length >= param[0] && value.length <= param[1]);
			}
			return false;
		},
		message : '编码不能输入汉字!'
	},
	HexadecimalCheck : {
		validator : function(value, param) {
			if (HexadecimalCheck(value)) {
				return value.length == param[0];
			}
			return false;
		},
		message : '只能输入{0}位长度的16进制字符（0至9 和 A至F）!'
	},
	ValueOneToFourCheck : {
		validator : function(value, param) {
			if (ValueOneToFourCheck(value)) {
				return value.length == param[0];
			}
			return false;
		},
		message : '只能输入{0}位长度的1至4数字!'
	},
	ValueBlueName : {
		validator : function(value, param) {
			if (ValueBlueName(value)) {
				//return (value.length >= param[0] && value.length <= param[1]);
				return value.length == param[0];
			}
			return false;
		},
		message : '请输入正确的{0}位长度蓝牙名称!'
	}
	
});

// datebox extend
/*$.extend($.fn.datebox.defaults, {
	formatter : function(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);
	},
	parser : function(s) {
		if (s) {
			var a = s.split('-');
			var y = new Number(a[0]);
			var m = new Number(a[1]);
			var d = new Number(a[2]);
			var dd = new Date(y, m - 1, d);
			return dd;
		} else {
			return new Date();
		}
	}
});*/

$.extend($.fn.datebox.defaults, {
	formatter : function(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);
	},
	parser : function(s) {
		if (s) {
			var i = s.split(' ');
			var a = i[0].split('-');
			var y = new Number(a[0]);
			var m = new Number(a[1]);
			var d = new Number(a[2]);
			var dd = new Date(y, m - 1, d);
			return dd;
		} else {
			return new Date();
		}
	}
});

$.extend($.fn.combobox.defaults, {//将从起始位开始匹配改为任意位匹配
	filter : function(q,row){
		var pts = $(this).combobox("options");
	    return row[pts.textField].indexOf(q)>-1;
	}
//,formatter:function(row){
//	    var pts=$(this).combobox("options");
//	    return row[pts.textField];
//	},loader:function(_7c9,_7ca,_7cb){
//	    var pts=$(this).combobox("options");
//	    if(!pts.url){
//	    return false;
//	    }
//	}
});

$.extend($.fn.form.defaults, {
	onLoadSuccess : function(data) {
		if ('session' == '${error}') {
			// 子页
			if (window.location.href.indexOf("action") == -1) {
				var top_href = top.location.href;
				top.location = top_href;
			}
			// 父页
			else {
				var sef_href = window.location.href;
				window.location = sef_href;
			}
		}
	}
});

// 去掉前后的空格
// 如果为空，返回空字符串
this.Trim = function(str) {
	if (str == null)
		return "";
	return str.replace(/(^\s*)|(\s*$)/g, "");
};

// 验证是否为空
this.IsNull = function(value) {
	return (CheckNull(value) == null);
};

// 验证是否为数字
this.IsNumber = function(value) {
	return (isNaN(Trim(value)));
};

/**
 * 验证是否为整数,连小数点都不行
 */
this.IntNumber = function(value) {
	return (Trim(value).match(/^[-+]?\d*$/) == null);
};

/**
 * 验证字符串只能是数字或者数字加空格
 */
this.spaceNumber = function(value) {
	var str = Trim(value);
	var pattern = /[^0-9 ]/g;
	return pattern.test(str);
}

/**
 * 验证字符串只能是16进制字符
 */
this.HexadecimalCheck = function(value) {
	var str = Trim(value);
	var pattern = /^[0-9A-F]*$/;
	return pattern.test(str);
}

/**
 * 验证字符串只能是1、2、3、4字符
 */
this.ValueOneToFourCheck = function(value) {
	var str = Trim(value);
	var pattern = /^[1-4]*$/;
	return pattern.test(str);
}

/**
 * 验证蓝牙密码
 */
this.ValueBlueName = function(value) {
	var str = Trim(value);
	var pattern = /^CT-[A-F0-9]*$/;
	return pattern.test(str);
}

/**
 * 验证字符串只能是汉字,数字,空格,英文
 */
this.Checkchar = function(text) {
	var str = Trim(text);
	// var pattern = /^[a-zA-z0-9 \u4E00-\u9FA5]*$/;
	var pattern = /^[a-zA-z0-9 \u4E00-\u9FA5 \-]*$/;
	return !pattern.test(str);
};

/**
 * 验证字符串只能是汉字,数字,空格,英文,括号
 */
this.Checkchars = function(text) {
	var str = Trim(text);
	var pattern = /^[a-zA-z0-9 \u4E00-\u9FA5\()\（）\-]*$/;
	return !pattern.test(str);
};

/**
 * 只能是英文或数字
 */
this.NumOrEng = function(value) {
	var str = Trim(value);
	var pattern = /^([a-zA-Z0-9_-])*$/;
	return pattern.test(str);
};

/**
 * 只能是大写字母
 */
this.BigEng = function(value) {
	var pattern = /^([A-Z])*$/;
	return pattern.test(value);
};

/**
 * 验证邮箱地址的正确性
 */
this.ThisEmail = function(value) {
	var str = Trim(value);
	var pattern = /^([a-zA-Z0-9]+[_-|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_-|\_|\.]?)*[a-zA-Z0-9]+\.(net|com|cn|org|cc|tv|[0-9]{1,3})(\S*)$/;
	return !pattern.test(str);
}

/**
 * 验证输入的内容不得包含以下字符、不能输入汉字，和一些非法字符
 */
this.CheckHZString = function(text) {
	var str = Trim(text);
	var pattern = /^[\u4E00-\u9FA5]*[，]*[、]*[\]*[……]*[（]*[）]*[“]*[”]*[？]*[！]*[。]*$/;
	return !pattern.test(str);
};

// 验证手机号码
this.IsMobel = function(value) {
	var str = Trim(value);
	var regex = /^[1][0-9]{10}$/;
	return regex.test(str);
};

// 验证浮点数小数位
this.FloatCheck = function(num) {
	var re = /^\d+\.?\d*$/;
	return re.exec(num) != null;
};

// 小数点后保留两位数字
this.NumberFloatCheck = function(num) {
	var re = /^[0-9]+([.]\d{1,2})?$/;
	return re.exec(num) != null;
};

// 小数点后保留四位小数
this.NumberFloatCheck4 = function(num) {
	var re = /^[0-9]+([.]\d{1,4})?$/;
	return re.exec(num) != null;
}

// 只能输入正、负数、小数的正则表达式(小数点前3位后8位)
this.NumberFloatCheckToLongitude = function(num) {
	var re = /^(([-]?[0-9]\d{0,2}))$|^([-]?(0|[1-9]\d{0,2}))\.(\d{1,14})$/;
	return re.exec(num) != null;
};

// 检查字符串是否为null或者空字符串，返回字符串或者Null
// text为待检查字符串
this.CheckNull = function(text) {
	if (text == null)
		return null;
	else if (text != null) {
		text = Trim(text);
		if (text == "")
			return null;
		else
			return text;
	}
};

// 检查字符串是否为yyyy-MM-dd格式时间
this.IsValidDate = function(strDate) {
	if (strDate == null) {
		return false;
	}
	strDate = Trim(strDate);
	var reg = /^(\d{4})(-)(\d{2})(-)(\d{2})$/;
	if (!reg.test(strDate)) {
		return false;
	}
	var ss = strDate.split("-");
	var year = parseInt(ss[0], 10);
	var month = parseInt(ss[1], 10);
	var date = parseInt(ss[2], 10);

	if (isNaN(month) || month < 1 || month > 12) {
		return false;
	}

	var daysOfMonth = new Date(year, month, 0).getDate();
	if (isNaN(date) || date < 1 || date > daysOfMonth) {
		return false;
	}
	return true;
};

// 检查是否为正确的邮箱格式
this.IsEmailType = function(value) {
	var str = Trim(value);
	var isemail = /^([a-zA-Z0-9]+[-_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[-_|\_|\.]?)*[a-zA-Z0-9]+\.(net|com|cn|org|cc|tv)(\S*)$/;
	return isemail.test(str);
};

// 检查字符串是否是邮箱地址。
this.IsEmail = function(str) {
	str = Trim(str);
	if (str == null || str == "") {
		return false;
	}
	var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.([a-zA-Z0-9_-]{2,3}){1,2})$/;
	return reg.test(str);
};

// 检查是否是正确的身份证号码(不判断地区跟出生日期)
this.IsIdCard = function(str) {
	str = Trim(str);
	if (str == null || str == "") {
		return false;
	}
	// var isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
	// if(str.length<=15){
	// return isIDCard1.test(str);
	// }
	// if(str.length>15 && str.length<=18){
	// return /(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(str)
	// }
	var isIDCard1 = /^\d{15}$|^\d{17}([0-9]|X|x)$/;
	return isIDCard1.test(str);

}
