;(function(){
  var NAME = "FFloader";

  //父元素
  function FFloader(ele){
    this.init(ele);
  }

  FFloader.prototype = {
    init: function(ele){
      if(ele){
        this.wrap = ele;
        this.loader = ele.getElementsByClassName("FFloader")[0];
        // this.reload = ele.getElementsByClassName("FFreload")[0];
        this.errormsg = ele.getElementsByClassName("FFerrormsg")[0];
      }
      if(!this.wrap || !this.loader || !this.errormsg){
        throw new Error("wrong arguments");
      }
      this.loader.style.display = "block";
      // this.reload.style.display = "none";
      this.errormsg.style.display = "none";
      this.loaded = false;
    },
    /**
     * 结束加载
     * @param  {bool} err 是否出错
     * @param  {string} msg 错误信息
     * @return {[type]}     [description]
     */
    endLoad: function(err, msg){
      this.loader.style.display = "none";
      this.loaded = true;
      if(err){
        // this.reload.style.display = "block";
        if(msg){
          this.errormsg.innerHTML = msg;
          this.errormsg.style.display = "block";
        }else{
          this.errormsg.style.display = "none";
        }
      }else{
        // this.reload.style.display = "none";
        if(msg){
          this.errormsg.innerHTML = msg;
          this.errormsg.style.display = "block";
        }else{
          this.errormsg.style.display = "none";
        }
      }
    },
    getStatus: function(){
      return this.loaded;
    }
  };
  window[NAME] = FFloader;
}());
