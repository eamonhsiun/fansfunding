/**
 * 一个简单的material design 的文本框
 * by IwYvI
 * 2015.11.28
 */

;(function(window, document, undefined) {
	"use strict";
	var NAME = "mdtext";
	var mdtext = {};
	mdtext.init = function  () {
		var inputtext = document.getElementsByClassName("inputtext");
		for(var i = 0; i < inputtext.length; i++){
			var ele = inputtext[i].getElementsByTagName('input')[0];
			if(ele.value && !ele.classList.contains("typing")){
				ele.classList.add("typing");
			}
			inputtext[i].getElementsByTagName('input')[0].addEventListener("input",function (e) {
				var ele = e.target;
				if(ele.value){
					if(!ele.classList.contains("typing")){
						ele.classList.add("typing");
					}
				}else{
					ele.classList.remove("typing");
				}
			});
			inputtext[i].getElementsByTagName('input')[0].addEventListener("blur",function  (e) {
				var ele = e.target;
				if(ele.value && ele.classList.contains("error")){
					ele.classList.remove("error");
				}
			});

		}
	}
	mdtext.addError = function  (ele, msg) {
		ele.classList.add("error");
		if(msg){
			ele.parentNode.getElementsByClassName("hint")[0].innerHTML = msg;
		}
		ele.focus();
	}
	window[NAME] = mdtext;
})(window, document);
