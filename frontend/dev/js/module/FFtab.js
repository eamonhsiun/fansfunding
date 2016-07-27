;(function(){
  var NAME = "FFtab";

  function FFtab(tabwrap, contentwrap, option){
    this.init(tabwrap, contentwrap, option);
  }
  window[NAME] = FFtab;

  FFtab.prototype.init = function(tabwrap, contentwrap, option){
    this.tabwrap = tabwrap;
    this.contentwrap = contentwrap;
    this.tabs = tabwrap.getElementsByClassName("FFtab");
    this.contents = contentwrap.getElementsByClassName("FFtab-content");
    this.last = 0;
    this.now = 0;
    if(option){
      this.callback = option.callback;
    }
    if(this.tabs.length !== this.contents.length){
      console.error("tab or tabcontent are not enough");
    }
    var length = this.tabs.length > this.contents.length ? this.contents.length : this.tabs.length;
    var _this = this;
    for(var i = 0; i < length; i++){
      this.tabs[i].addEventListener("click", function(num){
        return function(e){
          e.preventDefault();
          _this.goto(num);
        };
      }(i));
      this.tabs[i].classList.remove("FFtab-active");
      this.contents[i].classList.remove("FFtab-content-active");
      this.contents[i].style.display = "none";
    }
    this.tabs[0].classList.add("FFtab-active");
    this.contents[0].classList.add("FFtab-content-active");
    this.contents[0].style.display = "block";
  }
  FFtab.prototype.goto = function(num, callback){
    var length = this.tabs.length;
    if(num >= length || num < 0){
      return;
    }
    this.last = this.now;
    this.now = num;
    this.tabs[this.last].classList.remove("FFtab-active");
    this.contents[this.last].classList.remove("FFtab-content-active");
    this.contents[this.last].style.display = "none";
    this.tabs[this.now].classList.add("FFtab-active");
    this.contents[this.now].classList.add("FFtab-content-active");
    this.contents[this.now].style.display = "block";
    if(this.callback){
      this.callback(num, this.tabs[this.now], this.contents[this.now], this.tabs[this.last], this.contents[this.last]);
    }
    if(callback){
      callback(num, this.tabs[this.now], this.contents[this.now], this.tabs[this.last], this.contents[this.last]);
    }
  }
  FFtab.prototype.next = function(cycle ,callback){
    var length = this.tabs.length;
    if((this.now + 1) >= length){
      if(cycle){
        this.goto(0, callback);
      }
      return;
    }
    this.goto(this.now + 1, callback);
  }
  FFtab.prototype.prev = function(cycle ,callback){
    if((this.now - 1) < 0){
      if(cycle){
        this.goto(this.tabs.length - 1, callback);
      }
      return;
    }
    this.goto(this.now - 1, callback);
  }
}());
