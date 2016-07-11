var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

function addElementEvent () {
  $("#project-detail-btn").addEventListener("click", function(){
    tabs = this.parentNode.getElementsByClassName("project-wrap-tab");
    var i;
    for(i = 0; i < tabs.length; i++){
      tabs[i].classList.remove("tab-active");
    }
    this.classList.add("tab-active");
    contents = document.getElementsByClassName("project-content");
    for(i = 0; i < contents.length; i++){
      contents[i].classList.add("hide");
    }
    $('#project-detail').classList.remove("hide");
  });
  $("#project-feed-btn").addEventListener("click", function(){
    tabs = this.parentNode.getElementsByClassName("project-wrap-tab");
    for(var i = 0; i < tabs.length; i++){
      tabs[i].classList.remove("tab-active");
    }
    this.classList.add("tab-active");
    contents = document.getElementsByClassName("project-content");
    for(i = 0; i < contents.length; i++){
      contents[i].classList.add("hide");
    }
    $('#project-feed').classList.remove("hide");
  });

  $("#subscribe-now").addEventListener("click", function  (e) {
    if($('.tab-active').id != "project-detail-btn"){
      $('#project-detail-btn').click();
    }
  })
}
addElementEvent();
