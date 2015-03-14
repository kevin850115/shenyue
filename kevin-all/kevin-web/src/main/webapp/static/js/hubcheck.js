/**
 * @author shizhang.wl
 */
function singleCheck() {
	var app = document.getElementById("app").value;
	var ipAndPort = document.getElementById("ip").value;
	var request = HTTP.newRequest();
	var timeoutValue = setTimeout(function() {
		request.abort();
		unalpha();
		alert("操作超时");
	}, 10 * 1000);
	request.onreadystatechange = function() {
		if(request.readyState == 4) {// If the request is finished
			if(request.status == 200) {
				clearTimeout(timeoutValue);
				unalpha();
				var singleResult = eval('(' + request.responseText + ')');
				createResultTable(singleResult, true/*single*/);
			} // If it was successful
		}
	};
	request.open("GET", "singleCheckAjax.htm?app=" + app + "&ipAndPort=" + ipAndPort);
	request.send(null);
	showWaiting();
}

function checkAll() {
	var request = HTTP.newRequest();
	var timeoutValue = setTimeout(function() {
		request.abort();
		unalpha();
		alert("操作超时");
	}, 10 * 1000);
	request.onreadystatechange = function() {
		if(request.readyState == 4) {// If the request is finished
			if(request.status == 200) {
				clearTimeout(timeoutValue);
				unalpha();
				var totalResult = eval('(' + request.responseText + ')');
				createResultTable(totalResult, false/*total*/);
			} // If it was successful
		}
	};
	request.open("GET", "checkAllAjax.htm");
	request.send(null);
	showWaiting();
}

function showWaiting() {
	var container = document.getElementById("container");
	var content = document.getElementById("content");
	var waiting = document.getElementById("waiting");
	waiting.style.display = "block";
	container.style.display = "block";
	content.style.display = "none";
}

function showContent() {
	var container = document.getElementById("container");
	var content = document.getElementById("content");
	var waiting = document.getElementById("waiting");
	content.style.display = "block";
	waiting.style.display = "none";
	container.style.display = "block";
}

function unalpha() {
	var container = document.getElementById("container");
	container.style.display = "none";
}

function showDetail(detailId) {
	var detailContent = document.getElementById(detailId).value;
	var detailDiv = document.getElementById("detailContent");
	while(detailDiv.hasChildNodes()) {
		detailDiv.removeChild(detailDiv.firstChild);
	}
	detailDiv.innerHTML = detailContent;
	showContent();
}

function createResultTable(result, single) {
	var resultDiv = document.getElementById("resultDiv");
	//delete all children content of result-div
	while(resultDiv.hasChildNodes()) {
		resultDiv.removeChild(resultDiv.firstChild);
	}
	var resultTable = document.createElement("table");
	resultTable.className = "w800 tb3 tlf";
	resultTable.style.tableLayout = "fixed";
	resultTable.appendChild(createHeader());
	for(var i = 0; i < 4; i++) {
		var col = document.createElement("col");
		col.width = "200px";
		resultTable.appendChild(col);
	}
	if(single) {
		resultTable.appendChild(createOneResultRow(result));
	} else {
		for(var i = 0; i < result.length; i++) {
			resultTable.appendChild(createOneResultRow(result[i]));
		}
	}
	resultDiv.appendChild(resultTable);
}

function createHeader() {
	var headerRow = document.createElement("tr");
	headerRow.className = "f14 m5";
	var headerNames = ["App", "Ip", "Status", "Operation"];
	for(var i = 0; i < headerNames.length; i++) {
		var th = document.createElement("th");
		th.appendChild(document.createTextNode(headerNames[i]));
		headerRow.appendChild(th);
	}
	return headerRow;
}

function createOneResultRow(singleResult) {
	var row = document.createElement("tr");
	var appValue = singleResult["app"];
	var ipValue = singleResult["ip"];
	var statusValue = singleResult["status"];
	var detailValue = singleResult["detail"];
	var detailId = appValue + "_" + ipValue;
	var propArray = ["app", "ip", "status", "detail"];
	for(var i = 0; i < propArray.length; i++) {
		var td = document.createElement("td");
		if("detail" == propArray[i]) {
			var detailButton = document.createElement("button");
			detailButton.innerHTML = "详细";
			detailButton.type = "button";
			detailButton.onclick = function() {
				showDetail(detailId);
			};
			var dataElement = document.createElement("input");
			dataElement.type = "hidden";
			dataElement.id = detailId;
			dataElement.value = detailValue
			td.appendChild(detailButton);
			td.appendChild(dataElement);
		} else {
			td.appendChild(document.createTextNode(singleResult[propArray[i]]));
		}
		row.appendChild(td);
	}
	return row;
}