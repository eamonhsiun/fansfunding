
var apiUrl = "http://api.immortalfans.com:8080";
var resourceUrl = "http://resources.immortalfans.com:8080/";
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
      return "其他错误";
  }
}
;(function(){
  var $ = function (i) { return document.querySelector(i); };
  var $$ = function (i) { return document.querySelectorAll(i); };

  var NAME = "FFaccount";

  var FFaccount = {
    loginHook: []
  };

  window[NAME] = FFaccount;

  FFaccount.getUserInfo = function () {
    var _this = this;
    var userInfoRequest = ajax({
      method: 'get',
      url: apiUrl + "/user/" + localId + "/info?token=" + localToken,
    }).then(function (response, xhr) {
      var res = response.data;
      if(!response.result){
        _this.changeLoginDom(false);
        return;
      }
      _this.changeLoginDom(true, res);
    }).catch(function (response, xhr) {
      _this.changeLoginDom(true);
    }).always(function (response, xhr) {
    });
  }

  FFaccount.checkAccountStatus = function(){
    var userId = getUserId();
    var userToken = getUserToken();
    if(!userId || !userToken){
      this.changeLoginDom(false);
    }else{
      localId = userId;
      localToken = userToken;
      this.getUserInfo();
    }
  }

  FFaccount.logout = function(){
    var _this = this;
    var commonTokenRequest = ajax({
      method: 'post',
      url: apiUrl +"/user/" + localId + "/logout",
      data: {
        token: localToken
      }
    }).then(function (response, xhr) {
      if(!response.result){
        _this.changeLoginDom(false);
      }else{
        _this.changeLoginDom(false);
      }
      for(var i = 0; i < _this.loginHook.length; i++){
        _this.loginHook[i](false);
      }
    }).catch(function (response, xhr) {
      _this.changeLoginDom(false);
    }).always(function (response, xhr) {
      // Do something
    });
  }

  FFaccount.getAccountStatus = function(callback){
    if(!localId || !localToken){
      callback(false);
      return;
    }else if(localUserInfo){
      if(localUserInfo.status){
        callback(localUserInfo.status);
      }else{
        callback(false);
      }
      return;
    }
    this.loginHook.push(callback);
  }

  FFaccount.changeLoginDom = function(status, info){
    if(status){
      if(info){
        info.status = true;
        localUserInfo = info;
        $("#profile-login").style.display = "block";
        $("#profile-not-login").style.display = "none";
        $(".profile-avatar img").src = resourceUrl + localUserInfo.head;
      }else{
        status = false;
      }
    }else{
      localUserInfo = {status: false};
      localStorage.removeItem("id");
      localStorage.removeItem("token");
      localId = null;
      localToken = null;
      $("#profile-login").style.display = "none";
      $("#profile-not-login").style.display = "block";
      return;
    }
    for(var i = 0; i < this.loginHook.length; i++){
      this.loginHook[i](status);
    }
  }

  function addElementEvent(){
    $("#logoutbtn").addEventListener("click", function  (e) {
      e.preventDefault();
      FFaccount.logout();
    });
  }

  addElementEvent();
  FFaccount.checkAccountStatus();
})();


