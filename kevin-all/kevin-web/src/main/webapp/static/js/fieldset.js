			//All fields to be saved
			var allFields = {};
			//function to load field info of an App
			function loadFieldInfoByAppName() {
				//Delete all old children of inner table
				var innerTable = document.getElementById("innerTable");
				while(innerTable.hasChildNodes()) {
					innerTable.removeChild(innerTable.firstChild);
				}
				//Create inner table header
				var headers = ["BeanName", "开关名称", "开关描述", "开关类型", "操作"];
				innerTable.appendChild(createTableHeader(headers));
				var appName = document.getElementById("appName").value;
				var request = HTTP.newRequest();
				request.onreadystatechange = function() {
					if(request.readyState == 4) {// If the request is finished
						if(request.status == 200) {
							var fieldsOfAnApp = eval(request.responseText);
							for(var i = 0; i < fieldsOfAnApp.length; i++) {
								var row = createInnerTableRow(fieldsOfAnApp[i], i, exists(appName, fieldsOfAnApp[i].beanName, fieldsOfAnApp[i].fieldName));
								innerTable.appendChild(row);
							}
							var allCheckBox = document.getElementById("allCheckBox");
							allCheckBox.onclick = function() {
								var checkBoxes = document.getElementsByName("c");
								for(var i = 0; i < checkBoxes.length; i++) {
									if(!checkBoxes[i].disabled)
										checkBoxes[i].checked = allCheckBox.checked;
								}
							};
							//Use closure to capture the fields fetched through appName lookup
							var okButton = document.getElementById("okButton");
							okButton.onclick = function() {
								var fieldsSelected;
								if( appName in allFields) {
									fieldsSelected = allFields[appName];
								} else {
									fieldsSelected = {};
									allFields[appName] = fieldsSelected;
								}
								var checkBoxes = document.getElementsByName("c");
								for(var i = 0; i < checkBoxes.length; i++) {
									if(checkBoxes[i].checked) {
										var fieldsOfABean;
										if(fieldsSelected[fieldsOfAnApp[i].beanName]) {
											fieldsOfABean = fieldsSelected[fieldsOfAnApp[i].beanName];
										} else {
											fieldsOfABean = {};
											fieldsSelected[fieldsOfAnApp[i].beanName] = fieldsOfABean;
										}
										var field;
										if(fieldsOfABean[fieldsOfAnApp[i].fieldName]) {
											field = fieldsOfABean[fieldsOfAnApp[i].fieldName];
										} else {
											field = {};
											fieldsOfABean[fieldsOfAnApp[i].fieldName] = field;
										}
										field.fieldDesc = fieldsOfAnApp[i].fieldDesc;
										field.type = fieldsOfAnApp[i].type;
									}
								}
								refreshFields();
								unalpha();
							};
							var cancelButton = document.getElementById("cancelButton");
							cancelButton.onclick = function() {
								unalpha();
							}
							alpha();
						} // If it was successful
					}
				};
				request.open("GET", "ajaxFetch.htm?appName="+appName+"&setType=1");
				request.send(null);

				var loadButton = document.getElementById("loadButton");
				loadButton.disabled=true;
				setTimeout(function()
				{
					loadButton.disabled=false;
					request.onreadystatechange = function(){};
				},5*1000);
			}

			//Create table header
			function createTableHeader(headers) {
				var row = document.createElement("tr");
				for(var i = 0; i < headers.length; i++) {
					var th = document.createElement("th");
					th.appendChild(document.createTextNode(headers[i]));
					row.appendChild(th);
				}
				return row;
			}

			function createInnerTableRow(field, i, exists) {
				var row = document.createElement("tr");
				for(var prop in field) {
					var td = document.createElement("td");
					td.appendChild(document.createTextNode(field[prop]));
					row.appendChild(td);
				}
				var checkBoxTd = document.createElement("td");
				var checkBox = document.createElement("input");
				checkBox.type = "checkbox";
				checkBox.name = "c";
				checkBox.id = "c" + i;
				checkBox.checked = exists;
				if(exists)
					checkBox.disabled = true;
				checkBoxTd.appendChild(checkBox);
				row.appendChild(checkBoxTd);
				return row;
			}

			function refreshFields() {
				var table = document.getElementById("table");
				while(table.hasChildNodes()) {
					table.removeChild(table.firstChild);
				}
				var headers = ["序号", "App", "BeanName", "开关名称", "开关描述", "开关类型", "操作"];
				var header = createTableHeader(headers);
				table.appendChild(header);
				var fieldsArray = [];
				for(var appName in allFields) {
					var fieldsSelectedOfAnApp = allFields[appName];
					for(var beanName in fieldsSelectedOfAnApp) {
						var fieldsOfABean = fieldsSelectedOfAnApp[beanName];
						for(var fieldName in fieldsOfABean) {
							var field = {};
							field.appName = appName;
							field.beanName = beanName;
							field.fieldName = fieldName;
							field.fieldDesc = fieldsOfABean[fieldName].fieldDesc;
							field.type = fieldsOfABean[fieldName].type;
							fieldsArray.push(field);
						}
					}
				}
				fieldsArray.sort(function(a, b) {
					if(a.appName > b.appName)
						return 1;
					else if(a.appName < b.appName)
						return -1;
					else {
						if(a.beanName > b.beanName)
							return 1;
						else if(a.beanName < b.beanName)
							return -1;
						else {
							if(a.fieldName > b.fieldName)
								return 1;
							else if(a.fieldName < b.fieldName)
								return -1;
							return0;
						}
					}
				});
				for(var i = 0; i < fieldsArray.length; i++) {
					var row = createRow(fieldsArray[i], i);
					table.appendChild(row);
				}
			}

			function createRow(field, i) {
				var row = document.createElement("tr");
				var indexTd = document.createElement("td");
				indexTd.appendChild(document.createTextNode(i));
				row.appendChild(indexTd);
				var appName = field.appName;
				var beanName = field.beanName;
				var fieldName = field.fieldName;
				var parts = ["appName", "beanName", "fieldName", "fieldDesc", "type"];
				for(var i = 0; i < parts.length; i++) {
					var td = document.createElement("td");
					td.appendChild(document.createTextNode(field[parts[i]]));
					row.appendChild(td);
				}
				var deleteButton = document.createElement("img");
				deleteButton.src = "/static/images/delete.gif";
				deleteButton.onclick = function() {
					if(exists(appName, beanName, fieldName)) {
						delete allFields[appName][beanName][fieldName];
						refreshFields();
					}
				};
				var buttonTd = document.createElement("td");
				buttonTd.appendChild(deleteButton);
				row.appendChild(buttonTd);
				return row;
			}

			//check whether a field exists in allFields
			function exists(appName, beanName, fieldName) {
				if(allFields[appName]) {
					if(allFields[appName][beanName]) {
						if(allFields[appName][beanName][fieldName])
							return true;
					}
				}
				return false;
			}
			
			//Count the field number
			function fieldCount()
			{
				var count = 0;
				for(var appName in allFields)
				{
					for(var beanName in allFields[appName])
					{
						for(var fieldName in allFields[appName][beanName])
							count++;
					}
				}
				return count;
			}
			function alpha() {
				var container = document.getElementById("container");
				container.style.display = "block";
			}

			function unalpha() {
				document.getElementById("allCheckBox").checked = false;
				var container = document.getElementById("container");
				container.style.display = "none";
			}