/**
 * 
 * @param obj
 * @returns coordinates
 */
function getDOMCoordinates(obj) {
	var x = obj.offsetLeft, y = obj.offsetTop;
	while (obj = obj.offsetParent) {
		x += obj.offsetLeft;
		y += obj.offsetTop;
	}
	var o = new Object();
	o.x = x;
	o.y = y;
	return o;
}