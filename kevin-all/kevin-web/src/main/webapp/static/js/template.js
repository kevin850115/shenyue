/**
 * @author shizhang.wl
 */
function clearAjaxDivContent(){
	var ajaxDiv = document.getElementById("ajaxDiv");
	while(ajaxDiv.hasChildNodes()){
		ajaxDiv.removeChild(ajaxDiv.lastChild);
	}
}

function createAjaxDivContent(prompt){
	clearAjaxDivContent();
	var promptTxt = document.createTextNode(prompt);
	var br = document.createElement("br");
	var input = document.createElement("input");
	input.type="text";
	input.name="ajaxInput";
	input.id="ajaxInput";
	input.setAttribute("data_valueType","0");
	input.size=40;
	var ajaxDiv = document.getElementById("ajaxDiv");
	ajaxDiv.appendChild(promptTxt);
	ajaxDiv.appendChild(br);
	ajaxDiv.appendChild(input);
	return input;
}

function createAjaxDivContentWithCheckbox(prompt){
	clearAjaxDivContent();
	var promptTxt = document.createTextNode(prompt);
	var br = document.createElement("br");
	var input = document.createElement("input");
	input.type="text";
	input.name="ajaxInput";
	input.id="ajaxInput";
	input.setAttribute("data_valueType","0");
	input.size=30;
	var checkbox = document.createElement("input");
	checkbox.type="checkbox";
	checkbox.name="ajaxCheckbox";
	checkbox.id="ajaxCheckbox";
	checkbox.setAttribute("data_input","ajaxInput");
	checkbox.onclick=function() {
		checkNull(this);
	}
	var span = document.createElement("span");
	span.innerHTML="是否null";
	var ajaxDiv = document.getElementById("ajaxDiv");
	ajaxDiv.appendChild(promptTxt);
	ajaxDiv.appendChild(br);
	ajaxDiv.appendChild(input);
	ajaxDiv.appendChild(checkbox);
	ajaxDiv.appendChild(span);
	return input;
}

function updateTemplateName(templateId){
	var templateNameElement = document.getElementById("templateName");
	var ajaxInput = createAjaxDivContent("请输入新的模板名称",templateNameElement.textContent);
	
	ajaxInput.value = templateNameElement.textContent;
	var commitButton = document.getElementById("commit");
	commitButton.onclick = function() {
		var newTemplateName = ajaxInput.value;
		if(newTemplateName == null || newTemplateName == "") {
			alert("模板名称不能为空");
			return;
		}
		var request = HTTP.newRequest();
		var timeoutValue = setTimeout(function() {
			//Clear the normal listener
			request.abort();
			unalpha();
			alert("操作超时");
		}, 5 * 1000);
		request.onreadystatechange = function() {
			if(request.readyState == 4) {// If the request is finished
				if(request.status == 200) {
					clearTimeout(timeoutValue);
					var responseObj = eval('('+request.responseText+')');
					var result = responseObj["result"];
					if(result == "OK"){
						document.getElementById("templateName").innerHTML = newTemplateName;
						unalpha();
						alert("操作成功");
					}else{
						unalpha();
						alert(responseObj["errorMsg"]);
					}
				} // If it was successful
			}
		};
		request.open("POST", "ajaxUpdateTemplateName.htm");
		request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		request.send("templateId="+templateId+"&newTemplateName="+encodeURIComponent(encodeURIComponent(newTemplateName)));
		showWaiting();
	};
	showContent("300px","100px");
}


function isAllowNull(type){
	var fundamentalType = new Array("boolean","byte","short","char","int","long","float","double");
	for(i=0;i<fundamentalType.length;i++){
		if(trim(type) == fundamentalType[i])
			return false;
	}
	return true;
}

function checkNull(element){
	var id = element.getAttribute("data_input");
	var  input = document.getElementById(id);
	if(element.checked){
		//输入框灰掉。并且填入NULL
		input.value = "NULL";
		input.readOnly=true;
		input.setAttribute("data_valueType","1");
	}else{
		input.value = "";
		input.readOnly=false;
		input.setAttribute("data_valueType","0");
	}
}

function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
