
var topBtn = document.getElementById("back-to-top");
window.onscroll = function(){
  var top = document.documentElement.scrollTop || document.body.scrollTop;
  var height = window.innerHeight;
  if(top >= height){
    topBtn.style.display = "block";
  }else{
    topBtn.style.display = "none";
  }
}
