// ;(function(){
  var $ = function (i) { return document.querySelector(i); };
  var $$ = function (i) { return document.querySelectorAll(i); };

  var apiUrl = "http://192.168.0.113:8088/fansfunding";

  function addElementEvent () {
    // 登陆部分
    $("#login-phone").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        $("#login-pwd").focus();
      }
    });
    $("#login-phone").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#login-pwd").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.target.blur();
        $("#login-btn").click();
      }
    });
    $("#login-pwd").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#login-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      login();
    });
    $("#login-signup-btn").addEventListener("click", function (e){
      e.preventDefault();
      $(".login-block").classList.add("hide");
      $(".signup-block").classList.remove("hide");
      window.location.hash = "#signup";
    });

    //注册部分
    $("#signup-phone").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        $("#signup-icode").focus();
      }
    });
    $("#signup-phone").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#signup-icode").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        $("#signup-pwd").focus();
      }
    });
    $("#signup-icode").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#signup-icode-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      $("#signup-icode").classList.remove("error");
      getIcode();
    });
    $("#signup-pwd").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.target.blur();
        $("#signup-btn").click();
      }
    });
    $("#signup-pwd").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#signup-pwd-2").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#signup-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      signup();
    });
    $("#signup-login-btn").addEventListener("click", function (e){
      e.preventDefault();
      $(".signup-block").classList.add("hide");
      $(".login-block").classList.remove("hide");
      window.location.hash = "#login";
    });
  }

  function login(){
    var loginTel = $("#login-phone").value;
    var loginPwd = $("#login-pwd").value;

    if(!loginTel){
      $("#login-phone").classList.add("error");
      $("#login-hint").innerHTML = "请输入手机号";
      return;
    }else if(!loginPwd){
      $("#login-pwd").classList.add("error");
      $("#login-hint").innerHTML = "请输入密码";
      return;
    }
    var encryptPwd = CryptoJS.MD5(CryptoJS.MD5(loginPwd).toString() + CryptoJS.MD5(localId));
    var loginRequest = ajax({
      method: 'post',
      url: apiUrl + '/user/' + localId + '/login',
      data: {
        name: loginTel,
        password: encryptPwd
      }
    }).then(function (response, xhr) {
      if(response.result == "false"){
        $("#login-phone").classList.add("error");
        $("#login-pwd").classList.add("error");
        $("#login-hint").innerHTML = "用户名或密码错误";
        return;
      }
      localStorage.user.id = response.data.id;
      localStorage.user.taken = response.data.value;
    }).catch(function (response, xhr) {
      $("#login-phone").classList.add("error");
      $("#login-pwd").classList.add("error");
      $("#login-hint").innerHTML = "登录失败";
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function getIcode(){
    var telPattern = /^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
    var signupTel = $("#signup-phone").value;
    if(!signupTel.match(telPattern)){
      $("#signup-phone").classList.add("error");
      $("#signup-hint").innerHTML = "手机号输入错误";
      return;
    }
    var getIcodeRequest = ajax({
      method: 'post',
      url: apiUrl + '/common/newChecker',
      data: {
        phone: signupTel
      }
    }).then(function (response, xhr) {
      if(response.result === false){
        throw new Error();
      }

      icodeId = response.data;

    }).catch(function (response, xhr) {
      $("#signup-icode").classList.add("error");
      $("#signup-hint").innerHTML = "验证码获取失败";

      clearInterval(icodeTimer);
      $("#signup-icode-btn").removeAttribute("disabled");
      $("#signup-icode-btn").innerHTML = "发送验证码";
      icodeCounter = 60;
      icodeId = null;
    }).always(function (response, xhr) {

    });

    $("#signup-icode-btn").setAttribute("disabled","disabled");
    $("#signup-icode-btn").innerHTML = "已发送(" + icodeCounter + ")";
    icodeCounter = 60;
    icodeTimer = setInterval(function  () {
      $("#signup-icode-btn").innerHTML = "已发送(" + --icodeCounter + ")";
      if(icodeCounter < 0){
        clearInterval(icodeTimer);
        $("#signup-icode-btn").removeAttribute("disabled");
        $("#signup-icode-btn").innerHTML = "发送验证码";
        icodeCounter = 60;
      }
    }, 1000);
  }
  var icodeId = null;
  var icodeCounter = 60;
  var icodeTimer = null;

  function signup(){
    var signupTel = $("#signup-phone").value;
    var signupIcode = $("#signup-icode").value;

    var signupPwd = $("#signup-pwd").value;
    var signupPwd2 = $("#signup-pwd-2").value;

    if(!signupTel){
      $("#signup-phone").classList.add("error");
      $("#signup-hint").innerHTML = "请输入手机号";
      return;
    }else if(!icodeId){
      $("#signup-phone").classList.add("error");
      $("#signup-hint").innerHTML = "请获取验证码";
    }else if(!signupIcode){
      $("#signup-icode").classList.add("error");
      $("#signup-hint").innerHTML = "请输入验证码";
      return;
    }else if(!signupPwd){
      $("#signup-pwd").classList.add("error");
      $("#signup-hint").innerHTML = "请输入密码";
      return;
    }else if(signupPwd.length < 6 || signupPwd.length > 16){
      $("#signup-pwd").classList.add("error");
      $("#signup-hint").innerHTML = "密码位数错误";
      return;
    }else if(signupPwd2 != signupPwd){
      $("#signup-pwd-2").classList.add("error");
      $("#signup-hint").innerHTML = "两次密码不一致";
      return;
    }

    // var iv = CryptoJS.enc.Utf8.parse("0102030405060708");
    // var encryptPwd = CryptoJS.AES.encrypt(CryptoJS.MD5(signupPwd).toString(),CryptoJS.MD5(icodeId).toString().substring(3,19),{ iv: iv, mode: CryptoJS.mode.CBC }).toString();

    var encryptPwd = CryptoJS.MD5(signupPwd).toString().toUpperCase();

    console.log(encryptPwd);

    var signupRequest = ajax({
      method: 'post',
      url: apiUrl + '/user/' + icodeId + '/newUser',
      data: {
        checker: signupIcode,
        phone: signupTel,
        password: encryptPwd
      }
    }).then(function (response, xhr) {
      bbb=response;
      if(!response.result){
        clearInterval(icodeTimer);
        $("#signup-icode-btn").removeAttribute("disabled");
        $("#signup-icode-btn").innerHTML = "发送验证码";
        icodeCounter = 60;
        return;
      }
      console.log('注册成功');

      localStorage.id = response.data.id;
      localStorage.token = response.data.value;

    }).catch(function (response, xhr) {
      $("#signup-phone").classList.add("error");
      $("#signup-pwd").classList.add("error");
      $("#signup-hint").innerHTML = "登录失败";
    }).always(function (response, xhr) {
      // Do something
    });
  }


  //初始行为
  addElementEvent();
  if(window.location.hash.split("#")[1] === "signup"){
    $("#login-signup-btn").click();
  }
// })();



var bbb;
