/**
 * @author shizhang.wl
 */

var headers = [{
					"name" : "序号",
					"col" : 40
				}, {
					"name" : "方法信息",
					"col" : 760
				}];

function loadMethodInfo() {
	var setId = document.getElementById("setId").value;
	var request = HTTP.newRequest();
	request.onreadystatechange = function() {
		if(request.readyState == 4) {// If the request is finished
			if(request.status == 200) {
				var methods = eval('('+request.responseText+')');
				var methodTable = document.getElementById("templateTable");
				//Clean the field table
				while(methodTable.hasChildNodes()) {
					methodTable.removeChild(methodTable.firstChild);
				}
				createTableHeader(methodTable, headers);
				for(var i = 0; i < methods.length; i++) {
					var row = createMethodTableRow(methods[i], i);
					methodTable.appendChild(row);
				}
				var div = document.getElementById("templateValueDiv");
				div.style.display = "block";
			} // If it was successful
		}
	};
	request.open("GET", "ajaxFetch.htm?setId=" + setId+"&templateType=2");
	request.send(null);

	//Disable the loadbutton for 5 seconds
	var loadButton = document.getElementById("loadButton");
	loadButton.disabled = true;
	setTimeout(function() {
		loadButton.disabled = false;
		request.abort();
	}, 5 * 1000);
}

function createTableHeader(table, headers) {
	var row = document.createElement("tr");
	row.className = "f14 m5";
	for(var i = 0; i < headers.length; i++) {
		var col = document.createElement("col");
		col.width = headers[i].col;
		table.appendChild(col);
		var th = document.createElement("th");
		th.appendChild(document.createTextNode(headers[i].name));
		row.appendChild(th);
	}
	table.appendChild(row);
}


function createMethodTableRow(method, index) {
	var row = document.createElement("tr");
	row.onmouseout = function() {
		row.style.backgroundColor = '#FFFFFF'
	};
	row.onmouseover = function() {
		row.style.backgroundColor = '#FFF0C5'
	};
	var indexTd = document.createElement("td");
	indexTd.appendChild(document.createTextNode(index));
	row.appendChild(indexTd);
	var methodTableTd = document.createElement("td");
	methodTableTd.appendChild(createMethodTable(method));
	row.appendChild(methodTableTd);
	return row;
}

/**
 * function to create a methodTable
 */
function createMethodTable(method){
	var methodTable = document.createElement("table");
	methodTable.style.borderStyle="hidden";
	methodTable.style.width="758px";
	methodTable.className="tb3";
	var generalInfoRow = document.createElement("tr");
	var generalInfoTd = document.createElement("td");
	generalInfoTd.appendChild(createMethodGeneralInfoTable(method));
	generalInfoRow.appendChild(generalInfoTd);
	methodTable.appendChild(generalInfoRow);
	//Maybe there is paramInfo
	if(Number(method.paramCount)>0){
		var paramInfoRow = document.createElement("tr");
		var paramInfoTd = document.createElement("td");
		paramInfoTd.appendChild(createMethodParamInfoTable(method));
		paramInfoRow.appendChild(paramInfoTd);
		methodTable.appendChild(paramInfoRow);
	}
	return methodTable;
}

/**
 * Const and function to create a MethodGeneralInfoTable
 */
var methodGeneralInfoConst = [{"displayName":"AppName","propertyName":"appName"}
	,{"displayName":"BeanName","propertyName":"beanName"}
	,{"displayName":"方法签名","propertyName":"methodSign"}
	,{"displayName":"方法描述","propertyName":"methodDesc"}];
	
function createMethodGeneralInfoTable(method){
	var methodGeneralInfoTable  = document.createElement("table");
	methodGeneralInfoTable.style.borderStyle="hidden";
	methodGeneralInfoTable.style.width="758px";
	methodGeneralInfoTable.className="tb3";
	for(var i=0; i<methodGeneralInfoConst.length; i++){
		var row = document.createElement("tr");
		var td = document.createElement("td");
		td.appendChild(document.createTextNode(methodGeneralInfoConst[i].displayName));
		td.style.width="120px";
		td.className="break-line";
		var valueTd = document.createElement("td");
		valueTd.className="break-line";
		valueTd.appendChild(document.createTextNode(method[methodGeneralInfoConst[i].propertyName]));
		row.appendChild(td);
		row.appendChild(valueTd);
		methodGeneralInfoTable.appendChild(row);
	}
	return methodGeneralInfoTable;
}

/**
 * Headers and function to create a MethodParamInfoTable
 */
var methodParamTableHeaders = [{"name":"参数顺序","col":"60"}
	,{"name":"参数类型","col":"120"}
	,{"name":"参数描述","col":"280"}
	,{"name":"参数值","col":"240"}
	,{"name":"null","col":"60"}];

function createMethodParamInfoTable(method){
	var methodParamInfoTable = document.createElement("table");
	methodParamInfoTable.style.borderStyle="hidden";
	methodParamInfoTable.style.width="758px";
	methodParamInfoTable.className="tb3";
	createTableHeader(methodParamInfoTable,methodParamTableHeaders);
	var paramCount = Number(method.paramCount);
	for(var i=0; i<paramCount; i++){
		var row = document.createElement("tr");
		var indexTd = document.createElement("td");
		indexTd.appendChild(document.createTextNode((i+1)+""));
		row.appendChild(indexTd);
		var typeTd = document.createElement("td");
		typeTd.className="break-line";
		typeTd.appendChild(document.createTextNode(method.paramType[i]));
		row.appendChild(typeTd);
		var descTd = document.createElement("td");
		descTd.className="break-line";
		descTd.appendChild(document.createTextNode(method.paramDesc[i]));
		row.appendChild(descTd);
		var valueTd = document.createElement("td");
		var valueInput = document.createElement("input");
		valueInput.type = "text";
		var inputId = method.id+"_"+i;
		valueInput.id = inputId;
		valueInput.style.width="290px";
		valueInput.setAttribute("data_type",method.paramType[i]);
		//Warning message
		var span = document.createElement("span");
		valueTd.appendChild(valueInput);
		valueTd.appendChild(span);
		row.appendChild(valueTd);
		//null
		var nullTd = document.createElement("td");
		if(isAllowNull(method.paramType[i])){
			var nullInput = document.createElement("input");
			nullInput.type = "checkbox";
			nullInput.id = "null_" + inputId;
			nullInput.setAttribute("data_input",inputId);
			nullInput.onclick= function(){
				checkNull(this);
			}
			nullTd.appendChild(nullInput);
		}
		row.appendChild(nullTd);
		methodParamInfoTable.appendChild(row);
	}
	return methodParamInfoTable;
}

function generateMethodValue() {
	var methodValueArray = [];
	var tempObject = {};
	var templateTable = document.getElementById("templateTable");
	var methodValueInputs = templateTable.getElementsByTagName("input");
	var valid = true;
	var tempNull = {};
	for(var i = 0; i < methodValueInputs.length; i++) {
		if(methodValueInputs[i].type == "text"){
			var paramValue = methodValueInputs[i].value;
			var type = methodValueInputs[i].getAttribute("data_type");
			var span = methodValueInputs[i].nextSibling;
			if(type.indexOf("String")<0 &&(paramValue == null || paramValue == "")) {
				span.style.color = "red";
				span.innerHTML = "非String参数值不能为空";
				valid = false;
			}
			else
			{
				span.style.color = "red";
				span.innerHTML = "";
			}
			var paramInputId = methodValueInputs[i].id;
			var ids = paramInputId.split("_");
			var methodId = ids[0];
			var paramId = parseInt(ids[1]);
			if(!tempObject[methodId]){
				tempObject[methodId] = [];
			}
			tempObject[methodId].push(paramValue);
			if(!tempNull[methodId]){
				tempNull[methodId] = [];
			}
			var nullElement = document.getElementById("null_" +paramInputId);
			if(nullElement!=null && nullElement.checked){
				tempNull[methodId].push(paramId+1);
			}
		}
	}
	if(valid)
	{
		for(var prop in tempObject){
			var methodParamObject = {};
			methodParamObject["methodId"] = prop;
			methodParamObject["methodParamValue"] = tempObject[prop].join(",");
			if(!tempNull[prop]){
				tempNull[prop] = [];
			}
			methodParamObject["nullValueIndex"] = tempNull[prop].join(",");
			methodValueArray.push(methodParamObject);
		}
		return methodValueArray;
	}
}

function changeParamValue(paramValueTdId){
	//copy oldvalue to new position
	var currentParamValue = document.getElementById(paramValueTdId).getAttribute("data_value");
	var type = document.getElementById("type_" + paramValueTdId).getAttribute("data_type");
	var currentValueType = document.getElementById(paramValueTdId).getAttribute("data_valueType");
	var newValueInput;
	if(isAllowNull(type)){
		newValueInput= createAjaxDivContentWithCheckbox("请输入参数值");
		var checkbox = document.getElementById("ajaxCheckbox");
		if(currentValueType == "1" && checkbox != null){
			checkbox.checked=true;
			checkNull(checkbox);
		}else{
			newValueInput.value = currentParamValue;
		}
	}else{
		newValueInput = createAjaxDivContent("请输入参数值");
		newValueInput.value = currentParamValue;
	}
	//parse templatevalueid and paramindex
	var methodTemplateValueId = paramValueTdId.substring(0,paramValueTdId.indexOf("_"));
	var paramIndex = paramValueTdId.substring(paramValueTdId.indexOf("_")+1);
	//register commit button listener
	var commitButton = document.getElementById("commit");
	commitButton.onclick = function() {
			var newValue = newValueInput.value;
			var valueType = newValueInput.getAttribute("data_valueType");
			if(type.indexOf("String")<0 && (newValue == null || newValue == "")) {
				alert("非String参数值不能为空");
				return;
			}
			//prepare the new whole paramValue
			var newParamValueArray = [];
			//nullParams
			var nullValueArray = [];
			var paramTdArray = document.getElementsByClassName(methodTemplateValueId);
			for(var i=0 ;i< paramTdArray.length; i++){
				if(i == (Number(paramIndex)-1)){
					newParamValueArray.push(newValue);
					if(valueType == "1"){
						nullValueArray.push(i + 1);
					}
				}else{
					newParamValueArray.push(paramTdArray[i].getAttribute("data_value"));
					if(paramTdArray[i].getAttribute("data_valueType")=="1"){
						nullValueArray.push(i + 1);
					}
				}
			}
			var request = HTTP.newRequest();
			var timeOutValue = setTimeout(function() {
				request.abort();
				unalpha();
				alert("操作超时");
			}, 5 * 1000);
			request.onreadystatechange = function() {
				if(request.readyState == 4) {// If the request is finished
					if(request.status == 200) {
						clearTimeout(timeOutValue);
						var responseObj = eval('('+request.responseText+')');
						var array = responseObj["newParamValueString"].split(",");
						var nullArray = responseObj["nullValueIndex"].split(",");
						for(var i=0;i<paramTdArray.length;i++){
							var isNull = false;
							for(var j=0;j<nullArray.length;j++){
								if(Number(nullArray[j]) == (i+1)){
									isNull = true;
									break;
								}
							}
							if(isNull){
								paramTdArray[i].style.color="#AAAAAA";
								paramTdArray[i].innerHTML="NULL";
								paramTdArray[i].setAttribute("data_value","NULL");
								paramTdArray[i].setAttribute("data_valueType","1");
							}else{
								paramTdArray[i].style.color="";
								paramTdArray[i].innerHTML = array[i];
								paramTdArray[i].setAttribute("data_value",array[i]);
								paramTdArray[i].setAttribute("data_valueType","0");
							}
						}
						unalpha();
						alert("操作成功");
					} // If it was successful
				}
			};
			request.open("POST", "ajaxUpdateMethodParamValue.htm");
			request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			request.send("methodTemplateValueId="+methodTemplateValueId+"&newParamValue="
				+encodeURIComponent(encodeURIComponent(newParamValueArray.join(",")))
				+"&nullValueIndex="+nullValueArray.join(","));
			showWaiting();
		};
	showContent("300px","100px");
}

function insertParamValue(className){
	//get the methodTemplateId and methodId
	var methodTemplateId = className.substring(0,className.indexOf("_"));
	var methodId = className.substring(className.indexOf("_")+1);
	var tdNodeList = document.getElementsByClassName(className);
	var methodParamTable = null;
	var parent = tdNodeList[0].parentNode;
	while(parent!=null && parent.nodeName!="TABLE"){
		parent = parent.parentNode;
	}
	methodParamTable = parent;
	//deep copy
	var cloneMethodParamTable = methodParamTable.cloneNode(true);
	var cloneTableRows = cloneMethodParamTable.getElementsByTagName("tr");
	//remove the "edit" column name
	cloneTableRows[0].removeChild(cloneTableRows[0].lastElementChild);
	//remove the "edit" button
	cloneTableRows[1].removeChild(cloneTableRows[1].lastElementChild);
	cloneMethodParamTable.style.width="760px";
	//change td to input
	var cloneTdNodeList = cloneMethodParamTable.getElementsByClassName(className);
	for(var i=0;i<cloneTdNodeList.length; i++){
		var inputId = className+"_"+i;
		cloneTdNodeList[i].innerHTML="<input id=\""+inputId+"\" name=\""+inputId+"\" type=\"text\" size=\"37\"/>";
	}
	clearAjaxDivContent();
	var ajaxDiv = document.getElementById("ajaxDiv");
	ajaxDiv.appendChild(cloneMethodParamTable);
	//register commit button listener
	var commitButton = document.getElementById("commit");
	commitButton.onclick = function() {
		var inputArray = cloneMethodParamTable.getElementsByTagName("input");
		var newParamValueArray = [];
		for(var i=0;i<inputArray.length; i++){
			var newParamValue = inputArray[i].value;
			if(newParamValue == null || newParamValue == "") {
				alert("方法参数值不能为空");
				return;
			}
			newParamValueArray.push(newParamValue);
		}
		var request = HTTP.newRequest();
		var timeOutValue = setTimeout(function() {
			request.abort();
			unalpha();
			alert("操作超时");
		}, 5 * 1000);
		request.onreadystatechange = function() {
			if(request.readyState == 4) {// If the request is finished
				if(request.status == 200) {
					clearTimeout(timeOutValue);
					var responseObj = eval('('+request.responseText+')');
					var methodTemplateValueId = responseObj["methodTemplateValueId"];
					var array = responseObj["newParamValueString"].split(",");
					var originValueTdNodeList = methodParamTable.getElementsByClassName(className);
					var tempArray = [];
					for(var i=0;i<originValueTdNodeList.length;i++){
						tempArray.push(originValueTdNodeList[i]);
					}
					for(var i=0;i<tempArray.length;i++){
						tempArray[i].className = methodTemplateValueId;
						tempArray[i].id = methodTemplateValueId+"_"+(i+1);
						tempArray[i].innerHTML = array[i];
						//delete the obsolete edit button
						if(tempArray[i].nextElementSibling){
							tempArray[i].parentNode.removeChild(tempArray[i].nextElementSibling);
						}
						//register new edit button and its listener
						tempArray[i].parentNode.appendChild(createNormalEditComponent(methodTemplateValueId+"_"+(i+1)));
					}
					unalpha();
					alert("操作成功");
				} // If it was successful
			}
		};
		request.open("POST", "ajaxInsertMethodParamValue.htm");
		request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		request.send("methodTemplateId="+methodTemplateId+"&methodId="+methodId
			+"&newParamValue="+encodeURIComponent(encodeURIComponent(newParamValueArray.join(","))));
		showWaiting();
	};
	showContent("760px", window.getComputedStyle(cloneMethodParamTable, null).height);
}

function createNormalEditComponent(tdId){
	var td = document.createElement("td");
	td.class="break-line";
	var a = document.createElement("a");
	a.href = "#";
	a.onclick = function(){
		changeParamValue(tdId);
	};
	a.innerHTML = "<img src=\"/static/images/edit.png\" title=\"编辑\"></img>"
	td.appendChild(a);
	return td;
}

function showContent(width, height)
{
	var container = document.getElementById("container");
	var content = document.getElementById("content");
	content.style.width=width;
	content.style.height=height;
	var waiting = document.getElementById("waiting");
	content.style.display = "block";
	waiting.style.display = "none";
	container.style.display = "block";
}

function showWaiting()
{
	var container = document.getElementById("container");
	var content = document.getElementById("content");
	var waiting = document.getElementById("waiting");
	content.style.display = "none";
	waiting.style.display = "block";
	container.style.display = "block";
}

function unalpha()
{
	var container = document.getElementById("container");
	container.style.display = "none";
}
