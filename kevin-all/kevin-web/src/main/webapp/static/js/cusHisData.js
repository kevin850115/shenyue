jQuery(document).ready(
		function() {
			//记录生效区间，默认是区间
			var isDateRange = true;
			
			// 时间选择
			jQuery(".set_time .control_input").live('focus', function() {
				WdatePicker({
					skin : 'whyGreen',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					minDate : '1999-03-08 11:30:00',
					maxDate : '2088-03-10 20:59:30',
					onpicked : setTimeChange  //自定义范围变化事件
				})
			});
			
			//自定义范围变化事件
			$(".set_time .control_input").change(function() {
				setTimeChange();
			});
			/**
			 * 自定义范围发生变化:
			 * 1.给出数据类型的默认规则：
			 * 	3小时内--明细
			 * 	1个月内--按小时汇总
			 * 	6个月内--按天汇总
			 * 2.记录生效时间(区间/自定义范围)
			 */
			function setTimeChange() {
				var start = jQuery('.set_time [name=starttime]').val();
				var end = jQuery('.set_time [name=endtime]').val();
				if(start != "" && end != "") {
					var sd = new Date(start);
					var ed = new Date(end);
					var times = ed.getTime() - sd.getTime();
					if(times < 0) return;
					if(times <= 4500 * 60 * 1000) { //三天内
						$("#detail").show();
						controlChange("data_control", $("#detail"));
					} else if (times < 50400 * 60 * 1000) {  //1个月内
						$("#detail").hide();
						controlChange("data_control", $("#hour"));
					} else {  //大于1个月
						$("#detail").hide();
						controlChange("data_control", $("#day"));
					}
					isDateRange = false;   //自定义范围
				}
			}
			
			/**
			 * 改变control类型按钮效果，control类型按钮有2钟：
			 * 1.查询区间
			 * 2.数据类型：明细时，隐藏汇总方式，其他显示汇总方式，默认选中avg
			 */
			function controlChange(clz, a) {
				$("." + clz + " a").removeClass("selected");
				a.addClass("selected");
				//明细类型，没有汇总方式选择
				if(clz == "data_control") {
					if(a.attr("value") == "detail") {
						$("#aggrType").hide();
					} else {
						$("#aggrType option[value='AVG']").attr("selected", true);
						$("#aggrType").show();
					}
				}
			}
			/**
			 * 查询区间点击事件：
			 * 
			 */
			jQuery(".time_control a").live('click', function(e) {
				isDateRange = true;
				//选中效果
				controlChange("time_control", $(this));
				//联动
				timeControl();
				//查询
				doSearch();
			});
			
			/**
			 * 查询区间变化时，联动行为
			 */
			function timeControl() {
				//数据类型默认行为
				var value = parseInt($(".time_control .selected").attr("value"));
				if(value >= -4500) {   //3天内
					$("#detail").show();
					controlChange("data_control", $("#detail"));
				} else if(value == -11520) {   //1周
					$("#detail").hide();
					controlChange("data_control", $("#hour"));
				} else if(value == -50400) {   //1个月
					$("#detail").hide();
					controlChange("data_control", $("#day"));
				}
			}
			//初始化
			timeControl();
			
			/**
			 * 数据类型点击事件
			 */
			jQuery(".data_control a").live('click', function() {
				controlChange("data_control", $(this));
				//触发查询
				doSearch();
			});
			
			/**
			 * 汇总方式变化事件
			 */
			$("#aggrType").live("change", function() {
				doSearch();
			});

			// 业务事件
			jQuery('.btn_search').click(function(e) {
				isDateRange = false; //自定义范围
				doSearch();
			});
			
			function doSearch() {
				if($("#compareType").val()=="specialDay" && $("#specialDay").val()==''){
					$("#specialDay").addClass("error_input");
					$("#error_txt").show('300',function(){
						var setError = setTimeout(function(){
							$("#error_txt").hide('300');
						},2000)
					});
		    		return false;
				}
				var dateRange = jQuery('.time_control .selected').attr('value');
				if(isDateRange) {
					getData({
						dateRange : dateRange
					});
				} else {
					var starttime = jQuery('.set_time [name=starttime]').val();
					var enttime = jQuery('.set_time [name=endtime]').val();
					getData({
						startTime : starttime,
						endTime : enttime,
						dateRange : dateRange
					});
				}
			}
			
			//默认查询
			doSearch();
			
			//getData({
				//dateRange : -180
			//});
			
			
			//对比图曲线
			$("#compareType").change(function(){
				 if(jQuery("#compareType").val()=="specialDay"){
					jQuery("#specialDate").show();
				 }else{
					jQuery("#specialDate").hide();
					doSearch();
				 }
			 });
			 
			// 时间选择
				jQuery(".diytime .control_input").live('focus', function() {
					WdatePicker({
						skin : 'whyGreen',
						dateFmt : 'yyyy-MM-dd',
						minDate : '1999-03-08',
						maxDate : '2088-03-10',
						onpicked : doSearch  //自定义范围变化事件
					})
				});
			
			/**
			 *@param options
			 * startTime
			 * endTime
			 * dateRange
			 */
			function getData(options) {
				/**
				 * ajax请求
				 */
				jQuery.ajax({
					url : ctx + '/monitor/queryData.htm?cmd=getData',
					data : {
						chartId : jQuery.getUrlParam('chartId'),
						refId : jQuery.getUrlParam('refId'),
						refType : jQuery.getUrlParam('refType'),
						chartName : jQuery.getUrlParam('chartName'),
						dateParam : JSON.stringify(options),
						groupby: jQuery.getUrlParam('groupby'),
						type: $("#type").val(),
						appName: $("#app").val(),
						md5: jQuery.getUrlParam('md5'),
						keys: jQuery.getUrlParam('keys'),
						k1: jQuery.getUrlParam('k1'),
						k2: jQuery.getUrlParam('k2'),
						startDate: jQuery.getUrlParam('startDate'),
						beginTime: jQuery.getUrlParam('beginTime'),
						endDate: jQuery.getUrlParam('endDate'),
						startDate2: jQuery.getUrlParam('startDate2'),
						endDate2: jQuery.getUrlParam('endDate2'),
						key: jQuery.getUrlParam('key'),
						sql: jQuery.getUrlParam('sql'),
						valueType: jQuery.getUrlParam('valueType'),
						compareType : $("#compareType").val(),
						wholeDay: jQuery.getUrlParam('wholeDay'),
						specialDay : $("#specialDay").val()
					},
					dataType : 'json',
					success : function(resp) {
						/**
						 * 请求返回后，数据格式为
						 * {success: true, data: {...}}
						 * success标识业务是否执行成功，data是返回的业务数据
						 * 可以使用console.info(resp)结合firebug控制台查看数据结构
						 */
						if (resp.success) {
                            var multiData = resp.data;
                            var multiTitles = resp.names;
                            jQuery.each(multiData,function(idx2,data){
                                var series = [];
                                jQuery.each(data.lines, function(idx, item) {
                                    var mma = ' max:' + item.max + ' min:'
                                    + item.min + ' avg:' + item.avg + ' sum:' + item.sum + ' ' + item.unit;
                                    var nameStr = item.dateTimeStr?'<b>' +item.dateTimeStr+' '+ item.tag + '</b>' + mma :'<b>'+ item.tag + '</b>' + mma;
                                    var chartData = [];

                                    jQuery.each(item.hisData, function(idx, his) {
                                        chartData.push([ his.time,
                                            parseFloat(his.valStr) ]);
                                        });

                                    series.push({
                                        name : nameStr,
                                        data : chartData
                                        });
                                    });
                                var title = multiTitles[idx2];
                                if ( multiData.length > 1 ){
                                    createChart(series, title,$("#compareType").val(), data.userName,"container"+idx2,705,480);
                                }else{
                                    createChart(series, title,$("#compareType").val(), data.userName,"container"+idx2,1005,380);
                                }
                                }); 
						} else {
							alert(resp.info);
						}
					}
				});
			}

			/**
			 * 创建拆线图
			 * @param series 图数据
			 */
			function createChart(series, title,isCompare, userName,container,w,h) {
				Highcharts.setOptions({
					global : {
						useUTC : false
					}
				});

				var chart = new Highcharts.Chart({
					chart : {
						renderTo : container,
						type : 'spline',
						zoomType : 'x',
						spacingRight : 20,
						width: w,
						height: h,
						events: {
							load: function() {
				                if(userName){
				                       var ren = this.renderer,
				                       colors = Highcharts.getOptions().colors;
				                       var attr={fill: '#FFFFFF',rotation:45,'stroke-width': 0,padding: 5, r: 5};// rotation:-45,
				                       var css={color: '#E8E8E8',fontSize: '18px'}
				                       for(var i=0;i<380;i=i+100){
				                          for(var j=0;j<1005;j=j+100){
				                             ren.label(userName, j, i).attr(attr).css(css).add();
				                          }
				                       }
				                }
				             }
						}
					},
					title : {
						text : title || ''
					},
					xAxis : {
						type : 'datetime'

					},
					yAxis : {
						title : {
							text : ' '
						}
//						min : 0
					},

					plotOptions : {
						spline : {
							lineWidth : 1,
							states : {
								hover : {
									lineWidth : 1
								}
							},
							marker : {
								enabled : false,
								states : {
									hover : {
										enabled : true,
										symbol : 'circle',
										radius : 3,
										lineWidth : 1
									}
								}
							},
                         },
                        //series: {
                        //    cursor: 'pointer',
                        //    point: {
                        //        events: {
                        //        click: function() {
                        //           hs.htmlExpand(null, {
                        //                pageOrigin: {
                        //                x: this.pageX,
                        //                y: this.pageY
                        //                },
                        //                headingText: "ok",
                        //                maincontentText: "OK",
                        //                width: 200
                        //                }
                        //                );
                        //            }
                        //        }
                        //    },
                        //    marker: {
                        //        lineWidth: 1
                        //    }
                        //},
                    },
					tooltip : {
						shared : true,
						crosshairs : true,
						formatter : function() {
								var s = '<b>'
									+ Highcharts.dateFormat('%Y-%m-%d %H:%M:%S',
											this.x) + '</b>';
								if(isCompare){
									s = '<b>'+ Highcharts.dateFormat('%H:%M:%S',this.x) + '</b>';
								}
								jQuery.each(this.points, function(i, point) {
									if(isCompare){
										s += '<br/><span style="color:'
											+ point.series.color + '">'
											+ point.series.name.split(" ")[1]
											+ '</span>: ' + point.y;
									}else{
										s += '<br/><span style="color:'
											+ point.series.color + '">'
											+ point.series.name.split(" ")[0]
											+ '</span>: ' + point.y;
									}
								});
								return s;
						}
					},
					series : series
				});
			}
		});
