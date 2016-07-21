;(function(){
  var NAME = "FFtab";

  function FFtab(tabwrap, contentwrap){
    this.init(tabwrap, contentwrap);
  }

  window[NAME] = FFtab;

  FFtab.prototype.init = function(tabwrap, contentwrap){
    this.tabwrap = tabwrap;
    this.contentwrap = contentwrap;
    this.tabs = tabwrap.getElementsByClassName("FFtab");
    this.contents = contentwrap.getElementsByClassName("FFtab-content");
    if(this.tabs.length !== this.contents.length){
      throw new Error("tab or tabcontent are not enough");
    }
    var length = this.tabs.length;
    var _this = this;
    for(var i = 0; i < length; i++){
      this.tabs[i].addEventListener("click", function(num){
        return function(e){
          e.preventDefault();
          for(var j = 0; j < length; j++){
            _this.tabs[j].classList.remove("FFtab-active");
            _this.contents[j].classList.remove("FFtab-content-active");
            _this.contents[j].style.display = "none";
          }
          _this.tabs[num].classList.add("FFtab-active");
          _this.contents[num].classList.add("FFtab-content-active");
          _this.contents[num].style.display = "block";
        };
      }(i));
    }
    this.tabs[0].classList.add("FFtab-active");
    this.contents[0].classList.add("FFtab-content-active");
    this.contents[0].style.display = "block";
  }
}());
