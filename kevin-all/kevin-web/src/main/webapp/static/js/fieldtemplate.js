/**
 * @author shizhang.wl
 */
function loadFieldInfo() {
	var setId = document.getElementById("setId").value;
	var request = HTTP.newRequest();
	request.onreadystatechange = function() {
		if(request.readyState == 4) {// If the request is finished
			if(request.status == 200) {
				var fields = eval('('+request.responseText+')');
				var fieldTable = document.getElementById("templateTable");
				//Clean the field table
				while(fieldTable.hasChildNodes()) {
					fieldTable.removeChild(fieldTable.firstChild);
				}
				var headers = [{
					"name" : "序号",
					"col" : 40
				}, {
					"name" : "应用名",
					"col" : 120
				}, {
					"name" : "BeanName",
					"col" : 120
				}, {
					"name" : "开关名称",
					"col" : 140
				}, {
					"name" : "开关描述",
					"col" : 140
				}, {
					"name" : "开关类型",
					"col" : 100
				}, {
					"name" : "开关值",
					"col" : 140
				}, {
					"name" : "null",
					"col" : 60
				}
				];
				createFieldTableHeader(fieldTable, headers);
				for(var i = 0; i < fields.length; i++) {
					var row = createFieldTableRow(fields[i], i);
					fieldTable.appendChild(row);
				}
				var div = document.getElementById("templateValueDiv");
				div.style.display = "block";
			} // If it was successful
		}
	};
	request.open("GET", "ajaxFetch.htm?setId=" + setId+"&templateType=1");
	request.send(null);

	//Disable the loadbutton for 5 seconds
	var loadButton = document.getElementById("loadButton");
	loadButton.disabled = true;
	setTimeout(function() {
		loadButton.disabled = false;
		request.onreadystatechange = function() {
		};
	}, 5 * 1000);
}

function createFieldTableHeader(table, headers) {
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

function createFieldTableRow(field, index) {
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
	for(var prop in field) {
		if("id" != prop) {
			var td = document.createElement("td");
			td.className = "break-line";
			td.appendChild(document.createTextNode(field[prop]));
			row.appendChild(td);
		}
	}
	var fieldValueTd = document.createElement("td");
	//input
	var fieldValueInput = document.createElement("input");
	fieldValueInput.type = "text";
	fieldValueInput.id = field.id;
	fieldValueInput.setAttribute("data_type",field["type"]);
	fieldValueInput.style.width="130px";
	//Warning message
	var span = document.createElement("span");
	fieldValueTd.appendChild(fieldValueInput);
	fieldValueTd.appendChild(span);
	row.appendChild(fieldValueTd);
	//null
	var nullTd = document.createElement("td");
	if(isAllowNull(field["type"])){
		var nullInput = document.createElement("input");
		nullInput.type = "checkbox";
		nullInput.id = "null_" + field.id;
		nullInput.setAttribute("data_input",field.id);
		nullInput.onclick= function(){
			checkNull(this);
		}
		nullTd.appendChild(nullInput);
	}
	row.appendChild(nullTd);
	return row;
}

function generateFieldValue() {
	var fieldValueArray = [];
	var fieldTable = document.getElementById("templateTable");
	var fieldValues = fieldTable.getElementsByTagName("input");
	var valid = true;
	for(var i = 0; i < fieldValues.length; i++) {
		if(fieldValues[i].type == "text"){
			var fieldValue = fieldValues[i].value;
			var span = fieldValues[i].nextSibling;
			var type = fieldValues[i].getAttribute("data_type");
			if(type.indexOf("String")<0 && (fieldValue == null || fieldValue == "")) {
				span.style.color = "red";
				span.innerHTML = "非String开关不能为空";
				valid = false;
			}
			else
			{
				span.style.color = "red";
				span.innerHTML = "";
			}
			var fieldValueObj = {};
			fieldValueObj["fieldId"] = fieldValues[i].id;
			fieldValueObj["fieldValue"] = fieldValue;
			var valueType = 0;
			var nullElement = document.getElementById("null_" +fieldValues[i].id );
			if(nullElement!=null && nullElement.checked){
				valueType = 1;
			}
			fieldValueObj["valueType"] = valueType;
			fieldValueArray.push(fieldValueObj);
		}
	}
	if(valid)
	{
		return fieldValueArray;
	}
}

function changeTemplateValue(tdId) {
	var td = document.getElementById(tdId);
	var type = document.getElementById("type_" + tdId).getAttribute("data_type");
	var newTemplateValueInput;
	if(isAllowNull(type)){
		newTemplateValueInput= createAjaxDivContentWithCheckbox("请输入开关值");
	}else{
		newTemplateValueInput = createAjaxDivContent("请输入开关值");
	}
	var commitButton = document.getElementById("commit");
	if(td.id.indexOf("_") != -1) {
		//Create a new TemplateValue
		var templateId = td.id.substring(0, td.id.indexOf("_"));
		var fieldId = td.id.substring(td.id.indexOf("_") + 1);
		commitButton.onclick = function() {
			var newValue = newTemplateValueInput.value;
			if(type.indexOf("String")<0 && (newValue == null || newValue == "")) {
				alert("非String开关值不能为空");
				return;
			}
			var valueType = newTemplateValueInput.getAttribute("data_valueType");
			var request = HTTP.newRequest();
			var timeOutValue = setTimeout(function() {
				//Clear the normal listener
				request.onreadystatechange = function() {
				};
				unalpha();
				alert("操作超时");
			}, 5 * 1000);
			request.onreadystatechange = function() {
				if(request.readyState == 4) {// If the request is finished
					if(request.status == 200) {
						clearTimeout(timeOutValue);
						var templateValueObj = eval('('+request.responseText+')');
						var valueType = templateValueObj["valueType"];
						if(valueType!=null && valueType==1){
							td.style.color="#AAAAAA";
							td.innerHTML="NULL";
						}else{
							td.style.color="";
							td.innerHTML = templateValueObj["newValue"];
						}
						unalpha();
						alert("操作成功");
					} // If it was successful
				}
			};
			request.open("POST", "ajaxCreateNewTemplateValue.htm");
			request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			request.send("templateId=" + templateId + "&fieldId=" + fieldId + "&newValue=" + encodeURIComponent(encodeURIComponent(newValue))
					+ "&valueType=" + valueType);
			showWaiting();
		};
	} else {
		//Change a TemplateValue
		newTemplateValueInput.value = td.getAttribute("data_value");
		//check null
		var checkbox = document.getElementById("ajaxCheckbox");
		var currValueType = td.getAttribute("data_valueType");
		if(currValueType == "1" && checkbox != null){
			checkbox.checked=true;
			checkNull(checkbox);
		}
		commitButton.onclick = function() {
			var newValue = newTemplateValueInput.value;
			if(type.indexOf("String")<0 && (newValue == null || newValue == "")) {
				alert("非String开关值不能为空");
				return;
			}
			var valueType = newTemplateValueInput.getAttribute("data_valueType");
			var request = HTTP.newRequest();
			var timeOutValue = setTimeout(function() {
				//Clear the normal listener
				request.onreadystatechange = function() {
				};
				unalpha();
				alert("操作超时");
			}, 5 * 1000);
			request.onreadystatechange = function() {
				if(request.readyState == 4) {// If the request is finished
					if(request.status == 200) {
						clearTimeout(timeOutValue);
						var templateValueObj = eval('('+request.responseText+')');
						var valueType = templateValueObj["valueType"];
						if(valueType!=null && valueType==1){
							td.style.color="#AAAAAA";
							td.innerHTML="NULL";
							td.setAttribute("data_value","NULL");
							td.setAttribute("data_valueType","1");
						}else{
							td.style.color="";
							td.innerHTML = templateValueObj["newValue"];
							td.setAttribute("data_value",templateValueObj["newValue"]);
							td.setAttribute("data_valueType","0");
						}
						unalpha();
						alert("操作成功");
					} // If it was successful
				}
			};
			request.open("POST", "ajaxUpdateTemplateValue.htm");
			request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			request.send("templateValueId=" + td.id + "&newValue=" + encodeURIComponent(encodeURIComponent(newValue))
					+ "&valueType=" + valueType);
			showWaiting();
		};
	}
	showContent("300px","100px");
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

