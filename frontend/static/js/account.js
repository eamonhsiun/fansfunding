
var apiUrl = "http://api.immortalfans.com";
var localId = null;
var localToken = null;
var localUserInfo = null;
function getUserId () {
  return localStorage && localStorage.id ? localStorage.id : null;
}
function getUserToken () {
  return localStorage && localStorage.token ? localStorage.token : null;
}
var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

// ;(function(){

  // function getCommonToken () {
  //   var commonTokenRequest = ajax({
  //     method: 'post',
  //     url: apiUrl + "/common/newToken",
  //     data: {
  //       phone: 13006128628
  //     }
  //   }).then(function (response, xhr) {
  //     if(response.result){
  //       localId = response.data.id;
  //       localToken = response.data.value;
  //       changeLoginDom({ info: {accountStatus : false}});
  //       return;
  //     }
  //   }).catch(function (response, xhr) {
  //     alert("服务器连接失败");
  //   }).always(function (response, xhr) {
  //     // Do something
  //   });
  // }
  function getUserInfo () {
    var commonTokenRequest = ajax({
      method: 'post',
      url: apiUrl + "/user/" + localId + "/info",
      data: {
        token: localToken
      }
    }).then(function (response, xhr) {
      var res = response.data;
      if(!response.result){
        changeLoginDom(false);
      }
      localUserInfo = res;
      changeLoginDom(true, res);
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
      $("#profile-login").style.display = "none";
      $("#profile-not-login").style.display = "block";
      return;
    }


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
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function addElementEvent(){

  }

  checkAccountStatus();
// })();


