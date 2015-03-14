String.prototype.trim = function(){	return this.replace(/(^\s*)|(\s*$)/g, "");}  //ȥ���ַ�����β�ո�
String.prototype.lengthW = function(){	return this.replace(/[^\x00-\xff]/g,"**").length;}  //���ַ�����ʵ���Ⱥ��֣����ֽڡ��ַ���.lengthw()
String.prototype.isEmail = function(){	return /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(this);} //�ж��Ƿ�email
String.prototype.existChinese = function(){return /^[\x00-\xff]*$/.test(this);}
String.prototype.isPhoneCall = function(){	return /(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/.test(this);} //���绰����
String.prototype.isNumber=function(){return /^[0-9]+$/.test(this);} //�������
String.prototype.toNumber = function(def){return isNaN(parseInt(this, 10)) ? def : parseInt(this, 10);} // ����ת��
String.prototype.toMoney = function(def){return isNaN(parseFloat(this)) ? def : parseFloat(this);} // С��ת��