/**
 * The class "Tip" is used for
 * giving user some help
 * for example:
 * var user_tip = new Tip("div_id");
 * And the user_tip must be a global variable 
 * @param id:the div id which you want to name
 * @returns
 */
function Tip(id) {
	this.tipId = id;
	//the tip content
	this.content = "";
	//the first div which contains the content
	this.div = null;
	//the second div which shows the shadow effect
	this.nextDiv = null;
	//the tip default width
	this.width = 150;
	//the tip default height
	this.height = 80;
	this.divPadding = 5;
}
Tip.prototype.ensureDivExist = function(){
	if (!this.div || !this.nextDiv) {
		this.drawTipDiv();
	}
};
Tip.prototype.show = function() {
	this.ensureDivExist();
	this.div.style.display = "";
	this.nextDiv.style.display = "";
	this.div.focus();
};
Tip.prototype.hide = function() {
	this.ensureDivExist();
	this.div.style.display = "none";
	this.nextDiv.style.display = "none";
};
Tip.prototype.setContent = function(htmlStr){
	this.ensureDivExist();
	this.content = htmlStr;
	this.div.innerHTML = htmlStr;
};
Tip.prototype.setPosition = function(left,top){
	this.ensureDivExist();
	this.div.style.left = left + "px";
	this.div.style.top = top + "px";
	
	this.nextDiv.style.left = Number(left) + 3 + "px";
	this.nextDiv.style.top = Number(top) + 3 + "px";
};
Tip.prototype.setSize = function(width,height){
	this.ensureDivExist();
	this.div.style.width = width + "px";
	this.div.style.height = height + "px";
	this.nextDiv.style.width = width + "px";
	this.nextDiv.style.height = height + "px";
};
Tip.prototype.drawTipDiv = function() {
	var oDiv = document.createElement("div");
	this.div = oDiv;
	document.body.appendChild(oDiv);
	oDiv.id = this.tipId;
	oDiv.tabIndex = 0xff;
	oDiv.style.position = "absolute";
	oDiv.style.zIndex = 0xffff;
	oDiv.style.width = this.width + "px";
	oDiv.style.height = this.height + "px";
	oDiv.style.padding = this.divPadding + "px";
	//yellow
	oDiv.style.backgroundColor = "#ffffe1";
	oDiv.style.borderStyle = "inset";
	oDiv.style.borderWidth = "1px";
	var o = this;
	oDiv.onblur = function() {
		o.hide();
	};
	oDiv.innerHTML = this.content;

	var oNextDiv = document.createElement("div");
	this.nextDiv = oNextDiv;
	document.body.appendChild(oNextDiv);
	oNextDiv.id = this.tipId + "2";
	oNextDiv.style.position = "absolute";
	oNextDiv.style.zIndex = 0xfff0;
	oNextDiv.style.width = this.width + "px";
	oNextDiv.style.height = this.height + "px";
	oNextDiv.style.padding = this.divPadding + "px";
	oNextDiv.style.backgroundColor = "gray";
	oNextDiv.style.borderStyle = "solid";
	oNextDiv.style.borderWidth = "1px";
	
	//oDiv.focus();
};