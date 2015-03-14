function isEmpty(theString)
{
if((theString==null)||(theString.length==0))return true;
else return (false);
}

function isDigit(s)
{
  var patrn=/^[0-9]{1,32}$/;
  var str = Trim(s);
  if (!patrn.exec(str)) return false;
  return true;
}

function isInt(theInt)
{
var flag=true;
if(isEmpty(theInt)) return (false);
else
 {
  for(var i=0;i<theInt.length;i++)
    {
     if(isDigit(theInt.substring(i,i+1))==false)
        {
          flag=false;
          break;
        }

    }
 }
return (flag) ;
}

function fnIsIntNum(strNum)
{
 var strCheckNum = strNum + "";
 if(strCheckNum.length < 1)         // \u7a7a\u5b57\u7b26\u4e32
  return false;
 else if(isNaN(strCheckNum))         // \u4e0d\u662f\u6570\u503c
  return false;
 else if(parseInt(strCheckNum,10) < 0)       // \u4e0d\u662f\u6b63\u6570
  return false;
 else if(parseFloat(strCheckNum) > parseInt(strCheckNum,10)) // \u4e0d\u662f\u6574\u6570
  return false;

 return true;
}

function isEmail(a)
{
 var i=a.length;
 var temp = a.indexOf('@');
 var tempd = a.indexOf('.');
 if (temp > 1) {
  if ((i-temp) > 3) {
   if (tempd!=-1) {
    return true;
   }
  }
}
 return false;
}

function isFullSpace(theValue)
{
var space=" ";
var len=theValue.length;
var flag=true;
for(var i=0;i<len;i++)
  {
   if(theValue.substring(i,i+1)!=" ")
      {
      flag=false;
      break;
      }
  }
return flag;
}

function  isBetween(val,big,small)
{
 if(val>big||val<small)
    return (false);
 else
    return (true);
}

function lTrim(theValue,theWaste)
{
  var length=0;
  var wasLen=0;
  var result="";
  var aStr;
  var flag=true;
  length=theValue.length;
  wasLen=theWaste.length;
  // if(length>2) return (false);
  for(var i=0;i<length;i=i+wasLen)
     {
       if(wasLen+i>length)
          len=length;
       else
          len=wasLen+i;
       aStr=theValue.substring(i,len);
       if(aStr==theWaste&&flag)
          continue;
       flag=false;
       result=result+aStr;
     }
 return (result);
}

function isValidMonth(theValue)
{
 if(isBetween(parseInt(theValue),12,1))
   return true;
 else
   return false;
}
function isValideDay(theValue)
{
 if(isBetween(parseInt(theValue),31,1))
   return true;
 else
   return false;
}

function isMoney(money)
{
	var flag = true;
	var dot;
	var dotbefore;
	var dotafter;
	var len;
	len=money.length;
	dot=money.indexOf(".");
	if(dot == -1)
	{

		if(isInt(money))
		flag = true;
		else flag = false;
	}
	else
	{
		dotbefore = money.substring(0,dot);
		dotafter = money.substring(dot+1,len);
		if(isInt(dotbefore) && isInt(dotafter))
		{
			flag = true;
		}
		else flag=false;
	}

	return (flag);
}
function isDigitPhone(theNumber)
{
var numMask='0123456789-'
if(isEmpty(theNumber))return (false);
else if(numMask.indexOf(theNumber)==-1)return (false);
return (true);
}

function isPhone(thePhone)
{
var flag=true;
if(isEmpty(thePhone)) return (false);
else
 {
  if ( isInt(thePhone) ) { if( thePhone.length != 7 && thePhone.length != 11 && thePhone.length !=12) return false;}
  else {
   for(var i=0;i<thePhone.length;i++)
    {
     if(thePhone.substring(i,i+1)=="-"){if(i!=7 || i ==thePhone.length-1 ){ return false; break;} }
     if(isDigitPhone(thePhone.substring(i,i+1))==false)
        {
          flag=false;
          break;
        }
      }
   }
 }
return (flag) ;
}

function isChinese(strTemp)
{

    var i;
    for(i=0;i<strTemp.length;i++)
    {
        if((strTemp.charCodeAt(i)<0)||(strTemp.charCodeAt(i))>255)
        {
            return false;
        }
    }
    return true;
}

function Trim(sInputString)
{
    var sTmpStr = " ";
    var i = -1;
    while (sTmpStr == " ")
    {
      ++i;
      sTmpStr = sInputString.substr(i,1);
    }
    sInputString = sInputString.substring(i);

    sTmpStr = " ";
    i = sInputString.length;
    while (sTmpStr == " ")
    {
      --i;
      sTmpStr = sInputString.substr(i,1);
    }
    sInputString = sInputString.substring(0,i+1);
    return sInputString;
}
/*
 * function isIpAddress(s) { var
 * pattern=/(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])/.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)/.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)/.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])/;
 * if(!pattern.exec(s)) { alert("主机地址不正确"); return false; } return true; }
 */
function isip(s)
{
    // var
	// patrn=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    var patrn=/^[0-9.]{1,20}$/;
    if(!patrn.exec(s))
    {return false}
    return true
}

function isPhoneNumber(s)
{
    var patrn=/^([0-9]|[-]){1,20}$/;
    if (!patrn.exec(s))
    {
        return false;
    }
    return true;
}

function samePassword(t,s)
{
    if(t != s)
    {
        return false;
    }
    return true;
}

function ismobile(s)
{
    var patrn=/^(13)[0-9]{9}$/;
    if (!patrn.exec(s))
    {
       return false;
    }
    return true;
}

function IsDateValid(str)
{
    if (str == "")
    return true;
    var tempDateSeg = str.split(",");
    for(var i=0;i<tempDateSeg.length;i++)
    {
        var dateSplit = tempDateSeg[i].split("-");
        if(dateSplit.length == 2)
        {
          var temp1 = Trim(dateSplit[0]);
          var temp2 = Trim(dateSplit[1]);
          if(fnIsIntNum(temp1))
          {
            if(parseInt(temp1,10)<=0 || parseInt(temp1,10)>31)
              return false;
          }
          else
             return false;
          if(temp2 != "")
          {
            if(fnIsIntNum(temp2))
            {
              if(parseInt(temp2,10)<=0 || parseInt(temp2,10)>31)
                return false;
            }
            else
              return false;
          }
        }
        else if(dateSplit.length == 1)
        {
          var temp3 = Trim(dateSplit[0]);
          if(fnIsIntNum(temp3))
          {
             if(parseInt(temp3,10)<=0 || parseInt(temp3,10)>31)
                return false;
          }
          else
            return false;
        }
        else
          return false;
    }
    return true;
}

function CheckDate(dates,datee)
 {
     arrDates = dates.split("-");
     arrDatee = datee.split("-");
     if(arrDates.length != 3 ||arrDatee.length != 3)
     {
      	return false;
     }
     if(fnIsIntNum(arrDates[0]) && fnIsIntNum(arrDates[1]) && fnIsIntNum(arrDates[2]) &&fnIsIntNum(arrDatee[0]) && fnIsIntNum(arrDatee[1]) && fnIsIntNum(arrDatee[2]))   // \u662f\u6574\u6570
     {
        var yy = parseInt(arrDates[0],10);
        var mm = parseInt(arrDates[1],10);
        var dd = parseInt(arrDates[2],10);
      	if(parseInt(arrDatee[0],10) > yy)
      	{
          return true;
      	}
      	else if(parseInt(arrDatee[0],10) == yy)
       {
           if(parseInt(arrDatee[1],10) > mm)
           {
               return true;
           }
           else if(parseInt(arrDatee[1],10) == mm)
           {
               if(parseInt(arrDatee[2],10) >= dd)
               {
               	   return true;
               }
           }
       }
       return false;
       }
 }

 /**
	 * 函数名称：Istime 函数功能：判断是否输入一个时间hh:mm:ss 输入参数：shijian 返回值：是 true 否 false
	 */
function Istime(time)
{

  arrHHs = time.split(":");

  var hh = parseInt( arrHHs[0],10);
  var mm = parseInt( arrHHs[1], 10);
  var ss = parseInt( arrHHs[2],10);
  if(hh>23 ||hh<0||mm<0||mm>59||ss<0||ss>59)
  {return false;}
return true;
}
/**
 * 函数名称：Checktime 函数功能：判断结束时间是否大于起始时间 输入参数：起始时间dates和结束时间datee hh:mm:ss 返回值：是
 * true 否 false
 */
 function Checktime(dates,datee)
 {
     arrDates = dates.split(":");
     arrDatee = datee.split(":");
     if(arrDates.length != 3 ||arrDatee.length != 3)
     {
       return false;
     }
     if(fnIsIntNum(arrDates[0]) && fnIsIntNum(arrDates[1]) && fnIsIntNum(arrDates[2]) &&fnIsIntNum(arrDatee[0]) && fnIsIntNum(arrDatee[1]) && fnIsIntNum(arrDatee[2]))   // 是整数
     {
        var yy = parseInt(arrDates[0],10);
        var mm = parseInt(arrDates[1],10);
        var dd = parseInt(arrDates[2],10);
       if(parseInt(arrDatee[0],10) > yy)
       {
          return true;
       }
       else if(parseInt(arrDatee[0],10) == yy)
       {
           if(parseInt(arrDatee[1],10) > mm)
           {
               return true;
           }
           else if(parseInt(arrDatee[1],10) == mm)
           {
               if(parseInt(arrDatee[2],10) > dd)
               {
                   return true;
               }
           }
       }
       return false;
       }
 }
/**
 * 函数名称：Istimecomp 函数功能：判断输入时间早晚 输入参数：两个时间 返回值：是 true 否 false
 */
function IStimecomp(sn,sd)
{
    arr1 = sn.split(" ");
    arr2 = sd.split(" ");
  if(!CheckDate(arr1[0],arr2[0]))
  {alert("结束日期必须晚于开始日期");
   return false;}

  if(arr1[0] == arr2[0])
  {
  if(!Checktime(arr1[1],arr2[1]))
  {alert("结束时间必须晚于开始时间");
   return false;}
  }
return true;

}
/**
 * 函数名称：checkyearandmonth 函数功能：判断输入时间是否是正确年和月 输入参数：时间 返回值：是 true 否 false
 */
function checkyearandmonth(date)
{
    date = Trim(date);
    var patrn=/^[0-9]{6}$/;
    if (!patrn.exec(date))
    {
        alert("输入日期位数错误");
        return false;
    }
    Month = ""+date.substring(4);
    month = parseInt(Month,10);
    if(month<1||month>12)
    {
    	alert("请输入正确的年月");
        return false;
    }
    return true;
}
function isNineDigit(s)
{
  var patrn=/^[0-9]{1,9}$/;
  var str = Trim(s);
  if (!patrn.exec(str)) return false;
  return true;
}
/**
 * 函数名称：checktime 函数功能：判断输入时间是否是hh：mm 输入参数：一个 返回值：是 true 否 false
 */
function checktime(str)
{

    date = Trim(str);
    var timpSplit = date.split(":");
    if(timpSplit.length!=2)
    {alert("时间段格式不正确");return false;}
    if(fnIsIntNum(timpSplit[0]) && fnIsIntNum(timpSplit[1]))
       {
       	  if(parseInt(Trim(timpSplit[0]),10)<0 || parseInt(Trim(timpSplit[0]),10)>=24)
          {
             alert("\u6bcf\u5929\u7684\u65f6\u95f4\u6bb5\u4e2d\u5c0f\u65f6\u4e0d\u5408\u6cd5")
             return false;
          }
         if(parseInt(Trim(timpSplit[1]),10)<0 || parseInt(Trim(timpSplit[1]),10)>=60)
         {
             alert("\u6bcf\u5929\u7684\u65f6\u95f4\u6bb5\u4e2d\u5206\u949f\u4e0d\u5408\u6cd5")
             return false;
         }
         return true;
       }

       else
       {
         alert("\u6bcf\u5929\u7684\u65f6\u95f4\u6bb5\u8f93\u5165\u4e0d\u5408\u6cd5");
         return false;
       }

   return false;
}

function CheckDescLength(object,labelNum)
{
	var str = object.value;
	var label=document.getElementById(labelNum);
	if (str.length==0||str.length>100) {
		label.innerHTML='*不能为空,1-100个字符';
		object.focus();
		return false;
	}
	label.innerHTML='';
	return true;
}

function CheckPathLength(object,labelNum)
{
	var str = object.value;
	var patern=/\S+/;
	var label=document.getElementById(labelNum);
	if (!str.match(patern)||str.length>500) {
		label.innerHTML='*不能为空,1-500个字符';
		object.focus();
		return false;
	}
	label.innerHTML='';
	return true;
}

function CheckNum(object,labelNum)
{
	var str = object.value;
	var patern=/^[0-9]{1,3}$/;
	var label=document.getElementById(labelNum);
	if (!str.match(patern)||str.length>3) {
		label.innerHTML='*数字格式有误，0-999';
		object.focus();
		return false;
	}
	label.innerHTML='';
	return true;
}   

function CheckRoleName(object,labelStr)
{
	var str = object.value;
	var patern=/^ROLE_\S+/;
	var label=document.getElementById(labelStr);
	if (!str.match(patern)||str.length>50) {
		label.innerHTML='*字符串格式有误,以ROLE_开头，6个字符以上';
		object.focus();
		return false;
	}
	label.innerHTML='';
	return true;
}

function CheckPath(object,labelPath)
{
	var str = object.value;
	var patern=/^\/[a-zA-Z0-9]*\*$/;
	var label=document.getElementById(labelPath);
	if (!str.match(patern)) {
		label.innerHTML='*路径格式有误';
		object.focus();
		return false;
	}
	label.innerHTML='';
	return true;
}

function CheckNotNull(object,labelName)
{
	var str = object.value;
	var patern=/\S+/;
	var label=document.getElementById(labelName);
	if (!str.match(patern)||str.length>50) {
		label.innerHTML='*不能为空,1-50个字符';
		object.focus();
		return false;
	}
	label.innerHTML='';
	return true;
}

function CheckNotTooLong(object,labelName)
{
	var str = object.value;
	var label=document.getElementById(labelName);
	if (str.length>250) {
		label.innerHTML='不能超过250个字符';
		object.focus();
		return false;
	}
	label.innerHTML='';
	return true;
}

function CheckMobile(object,labelMobile)
{
	var str = object.value;
	var patrn=/^1[3458]{1}[0-9]{9}$/;
	var label=document.getElementById(labelMobile);
	if (!str.match(patern)&&str.length>0)
	{
		label.innerHTML='*手机号码不符合要求';
		object.focus();
		return false;
	}
	label.innerHTML='';
	return true;
}
