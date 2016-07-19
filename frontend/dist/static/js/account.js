
var apiUrl = "http://api.immortalfans.com";
// apiUrl = "http://192.168.204.203:8080/fansfunding";
var localId = null;
var localToken = null;
var localUserInfo = null;
function getUserId () {
  return localStorage && localStorage.id ? localStorage.id : null;
}
function getUserToken () {
  return localStorage && localStorage.token ? localStorage.token : null;
}
function getQueryString(arg){
  var reg = new RegExp("(^|&)"+ arg +"=([^&]*)(&|$)");
  var r = window.location.search.substr(1).match(reg);
  if(r!==null)return unescape(r[2]); return null;
}
function getErrorMsg(errCode){
  switch (errCode) {
    case 200:
      return "请求成功";
    case 201:
      return "请求失败";
    case 202:
      return "请求过于频繁";
    case 203:
      return "参数错误";
    case 204:
      return "用户不存在";
    case 205:
      return "用户名已注册";
    case 206:
      return "验证码过期";
    case 207:
      return "验证码错误";
    case 208:
      return "用户名或密码错误";
    case 209:
      return "权限不足";
    default:
      return null;
  }
}
;(function(){
  var $ = function (i) { return document.querySelector(i); };
  var $$ = function (i) { return document.querySelectorAll(i); };

  function getUserInfo () {
    var commonTokenRequest = ajax({
      method: 'get',
      url: apiUrl + "/user/" + localId + "/info?token=" + localToken,
    }).then(function (response, xhr) {
      var res = response.data;
      if(!response.result){
        changeLoginDom(false);
      }
      localUserInfo = res;
      changeLoginDom(true, res);
    }).catch(function (response, xhr) {
      alert("服务器连接失败");
      changeLoginDom(true, {});
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function checkAccountStatus(){
    var userId = getUserId();
    var userToken = getUserToken();
    if(!userId || !userToken){
      changeLoginDom(false);
    }else{
      localId = userId;
      localToken = userToken;
      getUserInfo();
    }
  }

  function changeLoginDom(status, info){
    if(status){
      $("#profile-login").style.display = "block";
      $("#profile-not-login").style.display = "none";
    }else{
      localStorage.removeItem("id");
      localStorage.removeItem("token");
      localId = null;
      localToken = null;
      $("#profile-login").style.display = "none";
      $("#profile-not-login").style.display = "block";
      return;
    }
    $(".profile-avatar img").src = localUserInfo.head;


  }

//res = {
//  accountStatus,
//  username,
//  useravatar,
//  other...
//}

  function logout(){
    var commonTokenRequest = ajax({
      method: 'post',
      url: apiUrl +"/user/" + localId + "/logout",
      data: {
        token: localToken
      }
    }).then(function (response, xhr) {
      if(!response.result){
        alert("登出失败");
      }else{
        console.log("登出成功");
        changeLoginDom(false);
      }
    }).catch(function (response, xhr) {
      alert("服务器连接失败");
      changeLoginDom(false);
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function addElementEvent(){
    $("#logoutbtn").addEventListener("click", function  (e) {
      e.preventDefault();
      logout();
    });
  }

  addElementEvent();
  checkAccountStatus();
})();


