			//All methods to be saved
			var allMethods = {};
			//function to load method info of an App
			function loadMethodInfoByAppName() {
				//Delete all old children of inner table
				var innerTable = document.getElementById("innerTable");
				while(innerTable.hasChildNodes()) {
					innerTable.removeChild(innerTable.firstChild);
				}
				//Create inner table header
				var headers = [{
					"name" : "BeanName",
					"col" : 150
				}, {
					"name" : "方法签名",
					"col" : 200
				}, {
					"name" : "方法描述",
					"col" : 200
				}, {
					"name" : "操作",
					"col" : 50
				}];
				createTableHeader(headers, innerTable);
				var appName = document.getElementById("appName").value;
				var request = HTTP.newRequest();
				request.onreadystatechange = function() {
					if(request.readyState == 4) {// If the request is finished
						if(request.status == 200) {
							var methodsOfAnApp = eval(request.responseText);
							for(var i = 0; i < methodsOfAnApp.length; i++) {
								var row = createInnerTableRow(methodsOfAnApp[i], i, exists(appName, methodsOfAnApp[i].beanName, methodsOfAnApp[i].methodSign));
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
							//Use closure to capture the methods fetched through appName lookup
							var okButton = document.getElementById("okButton");
							okButton.onclick = function() {
								var methodsSelected;
								if( appName in allMethods) {
									methodsSelected = allMethods[appName];
								} else {
									methodsSelected = {};
									allMethods[appName] = methodsSelected;
								}
								var checkBoxes = document.getElementsByName("c");
								for(var i = 0; i < checkBoxes.length; i++) {
									if(checkBoxes[i].checked) {
										var methodsOfABean;
										if(methodsSelected[methodsOfAnApp[i].beanName]) {
											methodsOfABean = methodsSelected[methodsOfAnApp[i].beanName];
										} else {
											methodsOfABean = {};
											methodsSelected[methodsOfAnApp[i].beanName] = methodsOfABean;
										}
										var method;
										if(methodsOfABean[methodsOfAnApp[i].methodSign]) {
											method = methodsOfABean[methodsOfAnApp[i].methodSign];
										} else {
											method = {};
											methodsOfABean[methodsOfAnApp[i].methodSign] = method;
										}
										method.methodDesc = methodsOfAnApp[i].methodDesc;
										method.paramCount = methodsOfAnApp[i].paramCount;
										method.paramDesc = methodsOfAnApp[i].paramDesc;
										method.paramType = methodsOfAnApp[i].paramType;
									}
								}
								refreshMethods();
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
				request.open("GET", "ajaxFetch.htm?appName="+appName+"&setType=2");
				request.send(null);

				var loadButton = document.getElementById("loadButton");
				loadButton.disabled=true;
				setTimeout(function()
				{
					loadButton.disabled=false;
					request.abort();
				},5*1000);
			}

			//Create table header
			function createTableHeader(headers, table) {
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

			function createInnerTableRow(method, i, exists) {
				var row = document.createElement("tr");
				row.className="break-line";
				var paramReg = /param*/;
				for(var prop in method) {
					//Ajax的弹出框中不显示方法参数的信息
					if(!paramReg.test(prop)){
						var td = document.createElement("td");
						td.appendChild(document.createTextNode(method[prop]));
						row.appendChild(td);
					}
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

			function refreshMethods() {
				var table = document.getElementById("table");
				while(table.hasChildNodes()) {
					table.removeChild(table.firstChild);
				}
				var headers = [{
					"name" : "序号",
					"col" : 50
				}, {
					"name" : "AppName",
					"col" : 100
				}, {
					"name" : "BeanName",
					"col" : 200
				}, {
					"name" : "方法签名",
					"col" : 250
				}, {
					"name" : "方法描述",
					"col" : 250
				}, {
					"name" : "操作",
					"col" : 50
				}];
				var header = createTableHeader(headers, table);
				var methodsArray = [];
				for(var appName in allMethods) {
					var methodsSelectedOfAnApp = allMethods[appName];
					for(var beanName in methodsSelectedOfAnApp) {
						var methodsOfABean = methodsSelectedOfAnApp[beanName];
						for(var methodSign in methodsOfABean) {
							var method = {};
							method.appName = appName;
							method.beanName = beanName;
							method.methodSign = methodSign;
							method.methodDesc = methodsOfABean[methodSign].methodDesc;
							methodsArray.push(method);
						}
					}
				}
				methodsArray.sort(function(a, b) {
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
							if(a.methodSign > b.methodSign)
								return 1;
							else if(a.methodSign < b.methodSign)
								return -1;
							return0;
						}
					}
				});
				for(var i = 0; i < methodsArray.length; i++) {
					var row = createRow(methodsArray[i], i);
					table.appendChild(row);
				}
			}

			function createRow(method, i) {
				var row = document.createElement("tr");
				row.className="break-line";
				var indexTd = document.createElement("td");
				indexTd.appendChild(document.createTextNode(i));
				row.appendChild(indexTd);
				var appName = method.appName;
				var beanName = method.beanName;
				var methodSign = method.methodSign;
				var methodDesc = method.methodDesc;
				var parts = ["appName", "beanName", "methodSign", "methodDesc"];
				for(var i = 0; i < parts.length; i++) {
					var td = document.createElement("td");
					td.appendChild(document.createTextNode(method[parts[i]]));
					row.appendChild(td);
				}
				var deleteButton = document.createElement("img");
				deleteButton.src = "/static/images/delete.gif";
				deleteButton.onclick = function() {
					if(exists(appName, beanName, methodSign)) {
						delete allMethods[appName][beanName][methodSign];
						refreshMethods();
					}
				};
				var buttonTd = document.createElement("td");
				buttonTd.appendChild(deleteButton);
				row.appendChild(buttonTd);
				return row;
			}

			//check whether a method exists in allMethods
			function exists(appName, beanName, methodSign) {
				if(allMethods[appName]) {
					if(allMethods[appName][beanName]) {
						if(allMethods[appName][beanName][methodSign])
							return true;
					}
				}
				return false;
			}
			
			//Count the method number
			function methodCount()
			{
				var count = 0;
				for(var appName in allMethods)
				{
					for(var beanName in allMethods[appName])
					{
						for(var methodSign in allMethods[appName][beanName])
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