String.prototype.trim = function(){	return this.replace(/(^\s*)|(\s*$)/g, "");}  //去掉字符串首尾空格
String.prototype.lengthW = function(){	return this.replace(/[^\x00-\xff]/g,"**").length;}  //求字符穿真实长度汉字２个字节　字符串.lengthw()
String.prototype.isEmail = function(){	return /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(this);} //判断是否email
String.prototype.existChinese = function(){return /^[\x00-\xff]*$/.test(this);}
String.prototype.isPhoneCall = function(){	return /(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/.test(this);} //检查电话号码
String.prototype.isNumber=function(){return /^[0-9]+$/.test(this);} //检查整数
String.prototype.toNumber = function(def){return isNaN(parseInt(this, 10)) ? def : parseInt(this, 10);} // 整数转换
String.prototype.toMoney = function(def){return isNaN(parseFloat(this)) ? def : parseFloat(this);} // 小数转换