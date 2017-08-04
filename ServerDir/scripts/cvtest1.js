
(function(window, document, global) {
	var cv = document.getElementById("cv1");
	var canvas = cv.getContext("2d");
	var clearScreen = function() {
		canvas.clearRect(cv.width, cv.heigth);
	}
	global.genRandomSquare = function() {
		clearScreen();
		canvas.fillStyle = "#000000";
		canvas.fillRect(Math.random()*450, Math.random()*225, 50, 50);
	}
}(window, document, this));