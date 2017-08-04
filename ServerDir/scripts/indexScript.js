(function(window, document, global) {
	window.onload = function() {
		document.getElementsByTagName("body")[0].style.opacity = 1;
		var main_title = document.getElementById("main_title");
		main_title.style.opacity = 1;
		main_title.style.paddingLeft = "32%";
		
		global.goToPVGithub = function() {
			document.getElementsByTagName("body")[0].style.opacity = 0;
			location.href='https://github.com/VirtualIceShard/PolarVision';
		}
	}
}(window, document, this))