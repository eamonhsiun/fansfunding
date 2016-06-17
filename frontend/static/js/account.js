
var apiUrl = "http://192.168.0.113:8088/fansfunding";
var localId = null;
var localToken = null;
var localUserInfo = null;
function getUserId () {
  return localStorage && localStorage.id ? localStorage.id : null;
}
function getUserToken () {
  return localStorage && localStorage.token ? localStorage.token : null;
}

// ;(function(){
  var $ = function (i) { return document.querySelector(i); };
  var $$ = function (i) { return document.querySelectorAll(i); };

  function getCommonToken () {
    var commonTokenRequest = ajax({
      method: 'post',
      url: apiUrl + "/common/newToken",
      data: {
        phone: 13006128628
      }
    }).then(function (response, xhr) {
      aaa = response;
      if(response.result){
        localId = response.data.id;
        localToken = response.data.value;
        changeDom({ info: {accountStatus : false}});
        return;
      }
    }).catch(function (response, xhr) {
      alert("服务器连接失败");
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function getUserInfo () {
    var commonTokenRequest = ajax({
      method: 'get',
      url: apiUrl + "/user/" + localId + "/" + localToken + "/"
    }).then(function (response, xhr) {
      bbb = response;
      var res = response.data;
      if(!response.result){
        res.accountStatus = false;
      }
      localUserInfo = res;
      changeDom(res);
    }).catch(function (response, xhr) {
      alert("服务器连接失败");
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function checkAccountStatus(){
    var userId = getUserId();
    var userToken = getUserToken();
    if(!userId || !userToken){
      getCommonToken();
    }else{
      localId = userId;
      localToken = userToken;
      // getUserInfo();
    }
  }

  function changeDom(info){
    if(info.accountStatus){
      $("#profile-login").style.display = "block";
      $("#profile-not-login").style.display = "none";
    }else{
      $("#profile-login").style.display = "none";
      $("#profile-not-login").style.display = "block";
      return;
    }
  }

  function logout(){
    var commonTokenRequest = ajax({
      method: 'get',
      url: apiUrl + "/user/" + localToken + "/logout",
      data: {
        id: localId
      }
    }).then(function (response, xhr) {
      bbb = response;
      var res = response.data;
      if(response.result){
        res.accountStatus = true;
        localUserInfo = res;
        localStorage.removeItem("id");
        localStorage.removeItem("token");
        getCommonToken();
        changeDom(res);
      }
    }).catch(function (response, xhr) {
      alert("服务器连接失败");
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function addElementEvent(){

  }

  checkAccountStatus();
// })();


