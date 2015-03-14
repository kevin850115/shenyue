/*
 * @summary 获取客户端当前日期和时间。
 * @return 客户端当前日期和时间字符串，格式为"yyyy-MM-dd HH:mm:ss"。
 */
function getDateTime()
{
	var now = new Date();
	var year = now.getFullYear().toString();
	var month = (now.getMonth() + 1).toString();
	if (month.length < 2) {
		month = '0' + month;
	}
	var day = now.getDate().toString();
	if (day.length < 2) {
		day = '0' + day;
	}
	var hour = now.getHours().toString();
	if (hour.length < 2) {
		hour = '0' + hour;
	}
	var minute = now.getMinutes().toString();
	if (minute.length < 2) {
		minute = '0' + minute;
	}
	var second = now.getSeconds().toString();
	if (second.length < 2) {
		second = '0' + second;
	}
	return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;	
}


function createSpan(id, value,cssName)
{
	var span = document.createElement("SPAN");
	span.id = id;
	span.innerHTML = value;
	span.className = cssName;
	return span;
}
function delRow(tableid)
{
 	var tbl = $(tableid);
	var rowindex = tbl.rows.length;
	while( --rowindex != 0 )
	{  
	 	 if( tbl.rows[rowindex].cells[5].innerText == "" ){
		 	 tbl.rows[rowindex].style.display="none";
		}
	}
}

function splitstring(str)
{

	if(str.indexOf("-")>-1||str.indexOf("4+")>-1)
	{
		var reg = /([02]\-[13])|(4\+)/g;	
	 	return str.match(reg);;
	}
	else if(str.indexOf("+")>-1&&str.indexOf("4+")<0)
	{		
		var reg = /[上下]\+[单双]/g;	
	 	return str.match(reg);
	}
	else
	{
	 	var reg = /./g;	
	 	return str.match(reg);
	}
	return "";
}
function TableResult()
{
	 var tbl = $('tbl_winend');
	 var rows = tbl.rows.length;	 
 	 var i=0;
 	 var vote="";
 	 for(i=1;i<rows;i++)
	 {
	 	if(tbl.rows[i].style.display=="")
	 	{
	 		
	 		var k=0;
	 		var temp = "";
	 		for(k=0;k<tbl.rows[i].cells[5].childNodes.length;k++)
			{					
				if(tbl.rows[i].cells[5].childNodes[k].innerText!="")
				{
					if(temp!="")
						temp=temp+",";
					temp=temp+tbl.rows[i].cells[5].childNodes[k].innerText;
				}		
			}
			if(vote!="")
		 		vote=vote+"/";
			vote=vote+"["+temp+"]";	 		
	 	}
	 	else
	 	{
	 		if(vote!="")
	 			vote=vote+"/";
	 		vote=vote+"[]";
	 	}
	 }	 
	 return vote;
}

function delScocerRow(tableid)
{
 	var tbl = $(tableid);
	var rowindex = tbl.rows.length;
	while( --rowindex != 0 )
	{  
	 	 if( tbl.rows[rowindex].cells[4].innerText == "" ){
		 	 tbl.rows[rowindex].style.display="none";
		}
	}
}

function amountTotal(obj)
{
   var tbl = $('tbl_buy');
   var rows = tbl.rows.length;
   var total=0;
   var nnum = countShowRows('tbl_winend');
   total=countMoney(nnum);
   $('acount').innerText = total+".00";
}

var source;	  
function loadXmlData(filename) 
{
	source = new ActiveXObject('Microsoft.XMLDOM');
	source.async = false;
	source.load(filename);
} 
//
function getnodevalue(nodename)
{	
	var nodes = source.getElementsByTagName("item");	
	for(var i=0;i<nodes.length;i++)	
	{
		if(nodes[i].getAttribute("id")==nodename)
				return nodes[i].text;	
	}
	return "";
}

function submitdata()
{
	if($('acount').innerHTML=="0"||$('acount').innerHTML=="")
	{
		alert("您还没有购买彩票");
		return false;
	}
	else
	{
		var re = /(\<BR\>)|(\<br\>)/g;
		var bettings = $('contentDiv').innerHTML.replace(re,'&');
		var m = $('acount').innerHTML;
		var rule = "单式:"+$('multiple').value+":"+ m;
		$('txtVote').value = rule
		$('txtResult').value = bettings +"；"+m;		
		var truthBeTold = window.confirm("共计："+m+"元，您确认下注吗？");
		if (truthBeTold) 
		{						
			return true;
		}
		return false;		
	}
}

function submitdatab()
{
	if($('acount').innerHTML=="0"||$('acount').innerHTML=="")
	{
		alert("您还没有购买彩票");
		return false;
	}
	else
	{
		$('txtVote').value = $("dvote").innerHTML;
		$('txtResult').value = TableResult()+"；"+$('acount').innerHTML;		
		var truthBeTold = window.confirm("共计："+$('acount').innerHTML+"元，您确认下注吗。");
		if (truthBeTold) 
		{						
			return true;
		}
		return false;		
	}
}

function OpenselWindow(url)
{
	location.href = url;
}


function checkUrl(pageName)
{
	if( location.href.indexOf(pageName) > -1 )
	{
		location = "/index.htm";
	}	
}
function getfilename(str)  //从链接地址里提取出本文档名
{
	return str.match(/.+\/(.+)$/)[1];
}

function trimString(string)
{
	var length1, i, j;
	var string1 = "";
	length1 = string.length;
	for (i = 0 ; i < length1 ; i++)	//trim left
	{  
		if(string.charAt(i) != " ")
		{
			//trimed left
			for (j = i ; j < length1 ; j++)
				string1 = string1 + string.charAt(j);
				break;
		}
	}
	length1 = string1.length;
	string = string1;
	string1 = "";
	for (i = length1 - 1 ; i >= 0 ; i--)
	{  
	//trim right
		if(string.charAt(i) != " ")
		{
		//trimed right
			for (j = 0 ; j <= i ; j++)
				string1 = string1 + string.charAt(j);
				break;
		}
	}
	string = string1;
	return(string)
}

//公用方法

//String.prototype.trim = function () {
//	var reExtraSpace = /^\s+(.*?)\s+$/;
//	return this.replace(reExtraSpace, "$1");
//};

String.prototype.trim = function(sp)
{
	if( sp ){
		var res = stringFormat("/(^\%S\*)|(\%S\*$)/g",sp);
		return this.replace(res,'');
	}
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

String.prototype.trimDot = function()
{
	return this.replace(/(^\,*)|(\,*$)/g,'');
}

function genRandomNum(digitalLower,digitalUpper,digitalNum){
  var randomNum = "";
  var i = 0; // 用来控制循环
  var temp = 0;
  //设置循环条件
  while (i < digitalNum) {
      var str = "";
      temp = Math.round(Math.random()*(digitalUpper-digitalLower))+digitalLower;
      if (temp < 10) {
              str += "0";
         }
      //将产生的随机数转化为字符串类型
      str += temp;
      //设置一组数字中不能有重复的号码,并且不能出现"00"类型的无效字符
      if (randomNum.indexOf(str) < 0) {
          //将产生的每一组数字之间用空格符号隔开
          str = str + " ";
          randomNum += str;
          i++;	
      }
  }
  //截取两端的空格，排序后返回空格分隔开的号码
  return randomNum.replace(/(^\s*)|(\s*$)/g,"").split(" ").sort().toString().replace(/,/g," ");
}

function stringFormat()
{
    if (arguments.length < 2){
		return "";
    }
    var strFormat = arguments[0].toString();
    for (var i = 1; i < arguments.length; i++){
        strFormat = strFormat.replace("%S",arguments[i]);
    }
    return strFormat;
}

function $()
{
	var elements = new Array();
	for (var i = 0; i < arguments.length; i++)
	{
		var element = arguments[i];
		if (typeof element == 'string'){
			element = document.getElementById(element);
		}
		if (arguments.length == 1){
			return element;
		}
		elements.push(element);
	}
	return elements;
}

function getCombination(n,r)
{
	if(n<r){ 
		return 0;
	}
	if(n==r){
		return 1;
	}
	if(n>r) 
	{
		var aNum=1;
		var bNum=1;
		var cNum=1;
		for(i=1;i<=n;i++){
			aNum*=i;
		}
		for(i=1;i<=r;i++){
			bNum*=i;
		}
		for(i=1;i<=n-r;i++){
			cNum*=i;
		}
		return aNum/(bNum*cNum);
	}
}

function qureyString(key)
{
	var str = location.search.split('?')[1];
	if( !str )
		return '';
	if( key )	
	{
		var qStr = str.split('&');
		for( var i=0;i< qStr.length;i++ )
		{
			var qs = qStr[i].split('=');
			if( key == qs[0] )
				return qs[1];
		}
	}
	else
	{
		return str.split('=')[1];
	}
}

function detectBrowser()
{
	//browser detection
	var strUserAgent = navigator.userAgent.toLowerCase();
	var isIE = strUserAgent.indexOf("msie") > -1;
	return isIE;
}

function inputFilter(bDot)
{	
	var iKeyCode, objInput;
	var reValidChars = /[0-9.]/;
	var strKey;
	var event = window.event || arguments.callee.caller.arguments[0];
	var inDot = (bDot)?true:false;
	var isIE = detectBrowser();
	if (isIE)
	{
		iKeyCode = event.keyCode;
		objInput = event.srcElement;
	}
	else
	{
		iKeyCode = event.which;
		objInput = event.target;
	}
	strKey = String.fromCharCode(iKeyCode);
	if (reValidChars.test(strKey))
	{
		if(iKeyCode==46)
		{
			if( !inDot )
			{
				if (isIE)
				{
					event.keyCode= 0;
				}
				else if(event.which!=0 && event.which!=8)
				{
					if( event.preventDefault )
					{
						event.preventDefault();
						event.stopPropagation();
					}
				}
			}			
			if(objInput.value.indexOf('.')!=-1)
			{
				if (isIE)
				{
					event.keyCode= 0;
				}
				else
				{
					if(event.which!=0 && event.which!=8)
					{
						if( event.preventDefault )
						{
							event.preventDefault();
							event.stopPropagation();
						}
					}
				}
			}
		}
	}
	else
	{
		if (isIE)
		{
			event.keyCode= 0;
		}
		else if(event.which!=0 && event.which!=8)
		{
			if( event.preventDefault )
			{
				event.preventDefault();
				event.stopPropagation();
			}
		}
	}
}

function regKeyPressEvent(elementId,keyPressHandler)
{
    var dom = document;
    var ele = document.getElementById(elementId);
    var mfListen = dom.addEventListener ? true:false;
    if( ele )
    {
        mfListen ? ele.addEventListener("keypress",keyPressHandler,true):ele.onkeypress = keyPressHandler;
    }    
}

function regKeyUpEvent(elementId,keyUpHandler)
{
    var dom = document;
    var ele = document.getElementById(elementId);
    var mfListen = dom.addEventListener ? true:false;
    if( ele )
    {
        mfListen ? ele.addEventListener("keyup",keyUpHandler,true):ele.onkeyup = keyUpHandler;
    }    
}

function clearSelected()
{
	if( qureyString('sid') !='30' )
	{
		var tblwin = $('tbl_win');	
		for(var i=1;i<tblwin.rows.length;i++)
		{
			for(var k=1;k<tblwin.rows[i].cells.length;k++)
			{
				var img = tblwin.rows[i].cells[k].childNodes[0];
				if( !img )
					continue;
				var	filepath = img.src;	
				if(filepath.indexOf("x.gif")>-1 || filepath.indexOf("z.gif")>-1 )		
				{
					var re = /(x.gif)|(z.gif)/g;
					img.src = filepath.replace(re,'.gif');				
				}
			}
		}
	}
	else
	{
		clearSelected_dlt();
	}
}
function realClearSelected(name,count)
{
   for(var i =0;i<count;i++)
   {
     $(name+i).className ="qz2";
   }
}
function clearSelected_dlt()
{
	var tblwin = $('tbl_front');
	for(var i=1;i<tblwin.rows.length;i++)
	{
		for(var k=0;k<tblwin.rows[i].cells.length;k++)
		{
			var img = tblwin.rows[i].cells[k].childNodes[0];
			if( !img )
				continue;
			var	filepath = img.src;	
			if(filepath.indexOf("x.gif")>-1 || filepath.indexOf("z.gif")>-1 )		
			{
				var re =  /(x.gif)|(z.gif)/g;
				img.src = filepath.replace(re,'.gif');				
			}
		}
	}	
	tblwin = $('tbl_back');
	for(var i=1;i<tblwin.rows.length;i++)
	{
		for(var k=0;k<tblwin.rows[i].cells.length;k++)
		{
			var img = tblwin.rows[i].cells[k].childNodes[0];
			if( !img || !img.tagName || img.tagName.toLowerCase() != 'img' )
				continue;
			var	filepath = img.src;	
			if(filepath.indexOf("x.gif")>-1|| filepath.indexOf("z.gif")>-1  )		
			{
				var re =  /(x.gif)|(z.gif)/g;
				img.src = filepath.replace(re,'.gif');				
			}
		}
	}
}
