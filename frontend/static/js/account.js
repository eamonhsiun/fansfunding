function getCookie(cookieName){
  var arrstr = document.cookie.split("; ");
  for(var i = 0;i < arrstr.length;i ++){
    var temp = arrstr[i].split("=");
    if(temp[0] == cookieName) {
      return unescape(temp[1]);
    }
  }
  return null;
}

function getToken(){
  return getCookie("token");
}

;(function(){
  var $ = function (i) { return document.querySelector(i); };
  var $$ = function (i) { return document.querySelectorAll(i); };
  function getLoginStatus (){
    var token = getToken();
    var request = ajax({
      method: 'post',
      url: 'api.immortalfans.com',
      data: {
        user: 'john'
      }
    }).then(function (response, xhr) {

    }).catch(function (response, xhr) {
      // Do something
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function changeDom(data){
    if(data.loginStatus){
      $("#profile-login").style.display = "block";
      $("#profile-not-login").style.display = "none";
    }else{
      $("#profile-login").style.display = "none";
      $("#profile-not-login").style.display = "block";
    }
  }
})();


