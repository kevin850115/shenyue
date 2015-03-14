$(function(){
	Date.prototype.format =function(format){
		var o = {
			"M+" : this.getMonth()+1, //month
			"d+" : this.getDate(),    //day
			"h+" : this.getHours(),   //hour
			"m+" : this.getMinutes(), //minute
			"s+" : this.getSeconds(), //second
			"q+" : Math.floor((this.getMonth()+3)/3),  //quarter
			"S" : this.getMilliseconds() //millisecond
		}
		if(/(y+)/.test(format)){
			format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4- RegExp.$1.length));
		}
			
		for(var k in o){
			if(new RegExp("("+ k +")").test(format)){
				format = format.replace(RegExp.$1,RegExp.$1.length==1? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
			}
		}
		return format;
	};
    //定义改变浏览器尺寸时页面自适应的Jk函数////////////////////////////////////////
    //获取页面中需改变元素
    var doc = $(document);
    var side = $("#side");
    var content =$("#content");
    var header = $("#header");
    var TreeHeader = $(".side_list");
    var Tree = $(".side_content .tree_hd");
    var TreeNosarch = $(".side_content .tree_nosarch");
    var main = $("#main");
    var $body = $(window);

    //定义监控界面中布局所用的公共函数
    var Jk = {
	
        flag:true,
        //设置页面浏览器全屏自适应 && 设置tree的高度自适应///////////////////////////			
        setHeight:function(){
            var autoHeight = $body.height()-header.outerHeight();
            var TreeHeight = autoHeight-TreeHeader.outerHeight();
            main.height(autoHeight);
            Tree.height(TreeHeight-40);
            TreeNosarch.height(TreeHeight);
        },
        //隐藏左侧side
        hideSideBar:function(){
            side.animate({
                left:-($("#side").width()-5)
            },300)
            content.animate({
                marginLeft:5
            },300)
		
        },
		
        //显示左侧side
        showSideBar:function(obj){
            side.animate({
                left:0
            },300)
            content.animate({
                marginLeft:side.width()
            },300)
        },
	
        tab:{
            //表格隔行变色
            showRow:(function(){
			
                $(".tab_backrgound tr:even").addClass("even_bg");
		
            })()
		
        }
    }

    Jk.setHeight();
    // 浏览器改变尺寸时调用自适应窗口大小函数
    window.onresize = function(){
        Jk.setHeight();
    }

    var timer = null;
    //为sied_btn添加点击事件,调用隐藏左侧&显示函数 Jk.hiedeSideBar && Jk.		
    $("#side_btn").delegate("span","click",function(event){
		
        if(Jk.flag){
            Jk.hideSideBar();
            $(this).addClass("selected")	
            Jk.flag = false;
        }else{
            Jk.showSideBar();
            $(this).removeClass("selected")	
            Jk.flag = true;
        }
        setTimeout(function() {
            $('.data_list_info').width(parseInt($('.tab_search').width()) - 40).css('overflowX', 'auto')
        }, 300);
        event.stopPropagation()	
    })
    .live("mousedown",function(event){
        var _this = $(this);
        var disX = 0;
        disX = event.pageX-$(this).offset().left;
        doc.live("mousemove",function(event){	
            var minDistance = 270;
            var maxDistance = 410;
            var sideWidth = event.pageX-disX;
            //var maxDistance = $body.width()-minDistance;
				
            if(sideWidth <= minDistance){
                sideWidth = minDistance;
            }else if(sideWidth>=maxDistance){
                sideWidth = maxDistance
            }
            side.width(sideWidth);
            side.find(".side_content").width(sideWidth-5);
            content.css({
                "marginLeft":_this.offset().left+_this.width()
            });
            $.cookie('tree_width', sideWidth);

            clearTimeout(timer);
            timer = setTimeout(function() {
                $('.data_list_info').width(parseInt($('.tab_search').width()) - 40).css('overflowX', 'auto');
                timer = null;
            }, 100);
            event.preventDefault(); 
        }).live("mouseup",function(){
            doc.unbind("mousemove mouseup")
        })
			
        return false;	
		
    });

    //手机版 显示二维码
	$(".mobile_app").hover(function(){
		$(".peanywhere_bg").show();
	},function(){
		$(".peanywhere_bg").hide();
		
	});
    
    //tree tab 切换效果
    $(".side_list a").live("click",function(){
		
        var $oA = $(".side_list a");
        var $oTree = $(".side_content .tree_info"); ///获取操作元素
        var index = $oA.index(this);		   //设置当前点击的索引

        //判断是否产品树和自定义分组进行了切换
        var oldNodeType = $.cookie("TREE_NODE_TYPE");
        var change = false;
        var changeToRole = false;
        if(oldNodeType && oldNodeType == "ROLE" && index != 3) {   
        	change = true;
        } else if(oldNodeType && oldNodeType != "ROLE" && index == 3) {   //进入自定义分组
        	change = true;
        	changeToRole = true;
        }
        
        //若发生点击节点事件，获取树的id，和节点的id
        var selectId = null;
        var treeId = null;
        if(index == 0 && dhtmlxtree != null && dhtmlxtree.getSelectedItemId()) {//全部
        	selectId = dhtmlxtree.getSelectedItemId();
        	treeId = "#dhtmlxtree";
        } else if(index == 1 && mydhtmlxtree != null && mydhtmlxtree.getSelectedItemId()) {//我的产品
        	selectId = mydhtmlxtree.getSelectedItemId();
        	treeId = "#mydhtmlxtree";
        } else if(index == 2 && mycoldhtmlxtree != null && mycoldhtmlxtree.getSelectedItemId() 
        		&& mycoldhtmlxtree.getSelectedItemId() != "c_1") { //我的收藏 
        	selectId = mycoldhtmlxtree.getSelectedItemId();
        	treeId = "#mycoldhtmlxtree";
        } else if(index == 3 && roledhtmlxtree != null && roledhtmlxtree.getSelectedItemId() 
        		&& roledhtmlxtree.getSelectedItemId() != "0") {//自定义分组  
        	selectId = roledhtmlxtree.getSelectedItemId();
        	treeId = "#roledhtmlxtree";
        }
        
        //isToRoleStatus来自role_left.js
        if(changeToRole && selectId && treeId && !isToRoleStatus()) {   //如果不发生跳转，返回
        	return false;
        }

        //切换效果
        $oA.removeClass("selected");		//为所有选中oA元素添加classs
        $(this).addClass("selected");		// 设置当前oA元素的class
        $oTree.hide();
        var curTree = $oTree.eq(index);
        curTree.show();
        if(treeId) setNodeType();   //变更cookie中的node_type
        
        //控制刷新
        if(change && selectId && treeId) {
        	$(treeId).trigger('tree_click',selectId);
        } else if(index == 0 || index == 1) {    //全部 或者 我的产品
        	var nodeType = $.cookie("TREE_NODE_TYPE");
            var nodeId = $.cookie("tree_select_node");
            if(nodeId) {
            	if(nodeType == "ALL") {
            		if(dhtmlxtree != null)  dhtmlxtree.focusItem(nodeId); 
    	        } else if(nodeType == "MY") {
    	        	if(mydhtmlxtree != null) mydhtmlxtree.focusItem(nodeId);
    	        }
            	
            	if(nodeId.indexOf("m") == 0
            			&& window.location.href.indexOf("/monitor/view/monitorItemStatus.htm") != -1) {
            	   if(nodeType == "ALL" && index == 0 && oldNodeType == "MY") {   //全部
            		   $('#dhtmlxtree').trigger('tree_click',nodeId); 
                   } else if(nodeType == "MY" && index == 1) {   //我的产品
                	   $('#mydhtmlxtree').trigger('tree_click',nodeId);
                   }
            	}
            }
        }
    });
	
	//给导航栏目添选中状态
	$("#nav li").click(function(event){
		
		var $oA = $(this).find("a").eq(0);
		var $all_nav_list = $(".nav_list",$("#nav"));
		var $navList = $(this).find(".nav_list");
		var $nav_li_a = $("#nav li>a");
			$nav_li_a.removeClass("selected");
			$oA.addClass("selected")	
			$all_nav_list.hide();
			$navList.show();			
	})	
	
    //input ,textarea 出错时 input高亮显示
    $('input,textarea').live('focus',function(){
        if($(this).hasClass("error_input")){
            $(this).removeClass("error_input")
        }else{
            return;
        }
    })
	
		// 大中小 按钮切换选中状态
	$("#control_btn_nui a").click(function(){
		$("#control_btn_nui a").removeClass("selceted");
		$(this).addClass("selceted")
	})
	//查看区间时间选中切换
	
	$("#control_txt_nui .link").click(function(){
		$("#control_txt_nui .link").removeClass("on")
		$(this).addClass("on")
	})
	
	//添加关键字 @芳亮
	$("#add_key_value").live("click",function(){
		$('<li><input placeholder="关键字名称" name="keyName" class=" txt_input txt_input_min"> <input placeholder="描述" name="keyDesc" class="txt_input txt_input_min"> <select name="keyType"><option value="count">出现次数</option><option value="sum">求和</option><option value="avg">求平均</option></select><span class="del_key"></span></li>').appendTo("#key_search")
	})
	
	$("#key_search ").find(".del_key").live("click",function(){
		$(this).parent().remove();
	})
	
	
	//检测IE7一下的浏览器
	var ieTrue = $.cookie("ie_test");//获取cookie中的值
	var ieAgent =$.browser // 将浏览器的agent保存在变量 ieAgent中
	var ieNumber = parseInt(ieAgent.version, 10); // 获取ie版本
	if(ieAgent.msie && (ieNumber <= 7)&& ieTrue==null){
		ieTest();  // ie7以下版本弹出提示信息
	}
	
	

})

//tooltip工具
var tool = {
    positionTooltip : function(e){
        var tPosX = e.pageX - 10;
        var tPosY = e.pageY;
        var distances = screen.width -240;
        var disY = $(document).height();
					
        $(".tips-top").css({
            left:18,
            bottom:-8
        }).addClass("tips-top-bottom");
        $('div.tooltip').css({
            bottom:(disY-e.pageY+20), 
            left:tPosX-15
         });
        $('div.tooltip').show();
    },
    showTooltip:function(event){
        var  $that = $(this);
        var thisTxt = $that.attr("content");
        $('<div class="tooltip"><div class="tips-text">'+ thisTxt +'</div><div class="tips-top diamond"></div></div>').appendTo('body');
        tool.positionTooltip(event);
    },
    showTooltip2:function(event,obj){// 通知关闭状态专用
   	var mtype = $(obj).parent().parent().parent().parent().attr("type");
   	var reasonInfo = $(obj).attr("reasonInfo");
   	var itemId = $(obj).parent().parent().parent().attr("itemId");
    	var mid = 0;
    	var url = "";
    	var idata = "";
    	if(mtype=="host"){
    		mid = $(obj).parent().parent().parent().attr("hostId");
    		gid = $(obj).parent().parent().parent().attr("appgroupid");
    		url = "/monitor/notice/close/HostClose.json";
    		idata = "cmd=getHostClose&itemId="+itemId+"&hostId="+mid+"&appgroupId="+gid;
    	}else{
    		mid = $(obj).parent().parent().parent().attr("hostgroupId");
    		url = "/monitor/notice/close/GroupClose.json";
    		idata = "cmd=getGroupClose&itemId="+itemId+"&hostgroupId="+mid; 
    	}
		jQuery.ajax( {
			type : "POST",
			url : contextPath + url,
			data : idata,
			success : function(data) {
				
				if (data.success) {
					var html = data.info+"<br>";
					html = html+"开始时间: "+data.data.beginTimeStr+"<br>结束时间: "+data.data.endTimeStr+"<br>";
					html = html+"操作人: "+(data.data.modifier==undefined?"API":data.data.modifier)+"["+(data.data.nickName==undefined?"API":data.data.nickName)+"]"+"<br>";;
					html = html+"上次关闭原因: "+(reasonInfo==undefined || reasonInfo==""?data.data.reason:reasonInfo);
					 $('<div class="tooltip" style="display:none;"><div class="tips-text">'+ html +'</div><div class="tips-top diamond"></div></div>').appendTo('body');
				}else{
					alert(data.info);
				}
				tool.positionTooltip(event);
			}
		})
		
		
	},
    hideTooltip: function(){
        $('div.tooltip').remove();
    }
};	
//$('.current_alarm').hover(tool.showTooltip , tool.hideTooltip).mousemove(tool.positionTooltip);


//公共错误提示插件
jQuery.alertError = function(msg,timeout,type) {
    if(!timeout) {
        timeout = 3000;
    }
    var $alert = $(".alert_error");
    if(type == undefined || type == 0){
    	$alert.addClass("error_style");
    	$alert.find(".close").addClass("close_x");
    }
    else if(type == 1){
    	$alert.addClass("tips_style");
    	$alert.find(".close").addClass("close_ok");
    }
    $alert.find("p").html(msg).parent().animate({
        top:5
    },200);
    $alert.find('.close').live("click",function(){
        $alert.animate({
            top:-100
        },200)
    })
    setTimeout(function(){
        $alert.animate({
            top:-100
        },200)
    },timeout)
};

//处理html 大于/小于符号转码
jQuery.HTMLDecode = function(text)   
{   
    var temp = document.createElement("span");   
    temp.innerHTML = text;   
    var output = temp.innerText || temp.textContent;   
    temp = null;   
    return output;   
};

//将tab 数据转化成jason
jQuery.TableToJson = function(id) {
    var txt = "[";
    var table = document.getElementById(id);
    var row = table.getElementsByTagName("tr");
    var col = row[0].getElementsByTagName("th");
    for (var j = 1; j < row.length; j++) {
    var r = "{";
    for (var i = 0; i < col.length-1; i++) {
    var tds = row[j].getElementsByTagName("td");
    if(tds[i].getAttribute("data")){
    r += "\"" + col[i].getAttribute("title") + "\"\:\"" + tds[i].getAttribute("data") + "\",";
    }else{
    r += "\"" + col[i].getAttribute("title") + "\"\:\"" + $.HTMLDecode(tds[i].innerHTML) + "\",";
    }
    }
    r = r.substring(0, r.length-1)
    r += "},";
    txt += r;
    }
    txt = txt.substring(0, txt.length - 1);
    txt += "]";	
    return txt; 
}; 

//添加图标的连接插入到页面中
jQuery.fn.expandLink = function(data){
    var $tab = $(this);
    $(this).find(".link").toggle(function(event,data){
        var data = "<div><h2 class='sub_title'>fsadfsadfsa</h2><br/>fsdafsafsdafsdafs</div>";
        var string = '<tr class="data_list"><td colspan="9">'+data+'</td></tr>'
        $(string).insertAfter($(this).parent().parent());
        $tab.find(".sub_title").live("click",function(){
            _openModalWindow({
                contextPath:contextPath,
                url:"",
                title:"百度",
                width:1024,
                height:480,
                max:false,
                callback_function:null
            })
        });
        event.stopPropagation();			
    },function(){
        $tab.find(".data_list").remove();
    });
}

jQuery.getUrlParam = function getParameter(paramName) {
    var searchString = window.location.search.substring(1),
    i, val, params = searchString.split("&");

    for (i=0;i<params.length;i++) {
        val = params[i].split("=");
        if (val[0] == paramName) {
            return unescape(val[1]);
        }
    }
    return null;
}

jQuery.setUrlParam = function setParameter(oldurl,paramname,pvalue) {
   oldurl=oldurl.replace(new RegExp("#","g"),"");
   var reg=new RegExp('(\\?|&)'+paramname+'=([^&]*)(&|$)','gi');var pst=oldurl.match(reg);    
   if((pst==undefined)   ||   (pst==null)){ return oldurl+((oldurl.indexOf('?')==-1)?'?':'&')+paramname+'='+pvalue; }    
   var   t=pst[0];var retxt=t.substring(0,t.indexOf('=')+1)+pvalue;     
   if(t.charAt(t.length-1)=='&')retxt+='&';      
   return   oldurl.replace(reg,retxt);   
}

jQuery.deSerialize = function(fuck){
    var url = location.href;
    //    var qs = url.substring(url.indexOf('?') + 1).split('&');
    var qs = fuck.split('&');
    for(var i = 0, result = {};
        i < qs.length; i++){
        qs[i] = qs[i].split('=');
        result[qs[i][0]] = decodeURI(qs[i][1]);
    }
    return result;
}

/**
 * 全局翻页js，放在此处尽管丑陋，总比每个页面都复制一份强吧。
 */
function changePage(page,pageSize){
    var params = jQuery.deSerialize(window.location.search.substring(1));
    params.page = page;
    params.pageSize = pageSize;
    location.href = "?" + jQuery.param(params);
}

function setNodeType() {
    var index = $('.side_list a').index($('.side_list .selected'));
    var NODE_TYPE = 'TREE_NODE_TYPE';
    if(index == 1){
        $.cookie(NODE_TYPE,"MY");
    }
    else if(index == 0){
        $.cookie(NODE_TYPE,"ALL");
    }
    else if(index == 2){
        $.cookie(NODE_TYPE,"COL");
    } else {
    	 $.cookie(NODE_TYPE,"ROLE");
    }
}

jQuery.ue = function(select, event, callback) {
    var target = jQuery(select);
    if(target) {
        target.unbind(event);
        target.bind(event, callback);
    }
}

//留个丑陋的全局变量在这里，用于判断左侧菜单点击事件，具体怎么回事可以在工程中搜索该变量。
var EXPAND_ON_M = false;



// 判断ie浏览器
function ieTest(){
			
   //设置提示内容	
	var html="<div class='ie_test' id='ie_test'><p>您当前使用的浏览器Alimonitor不能完美的支持! 推荐使用<span>Chrome、</span><span>Firefox、</span>";
		html+="<span>IE8+</span>的浏览器进行预览！效果会更佳!!!</p><span id='ie_close'></span></div>";
		
		$("body").append("<div id='ie_mask'></div>")
		$("body").append(html);
		
		// 设置遮罩动画效果
		$("#ie_mask").animate({
				opacity:0.6
			},300,function(){
			$("#ie_test").animate({
				top:'40%'
			})	

	})	
	
	$.cookie("ie_test","ie7Test");
	// 	关闭内容
	$("body").click(function(e){
		if(e.target.id=="ie_mask" ||e.target.id=="ie_close"){
			$("#ie_mask").animate({
					opacity:0.4
				},600)
				
				$("#ie_test").animate({
						top:"85%",
						opacity:0
					},600,function(){
						//从dom中删除添加节点
						$("#ie_test").remove();
						$("#ie_mask").remove();	
				})	
		}

	})
}

/**
 * 获取radio选中的值
 */
function getRadioCheckedValue(name){
	var obj = document.getElementsByName(name);
	if(obj!=null){
        var i;
        for(i=0;i<obj.length;i++){
            if(obj[i].checked){
                return obj[i].value;            
            }
        }
    }
    return null;
}
	
/**
 * 选中radio标签
 */
function setRadioChecked(name,value){
	var obj = document.getElementsByName(name);
	if(obj!=null){
        var i;
        for(i=0;i<obj.length;i++){
        	if(obj[i].value==value){
        		obj[i].checked = true;
        	}
        }
    }
}

Array.prototype.has = function(n) {
    var arr = this;
    for(var i = 0; i < arr.length; i++) {
        if(arr[i] === n) return true;
    }
    return false;
}
