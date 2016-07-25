;(function(){
  var $ = function (i) { return document.querySelector(i); };
  var $$ = function (i) { return document.querySelectorAll(i); };

  function addElementEvent () {
    // 登陆部分
    $("#login-phone").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#login-pwd").focus();
      }
    });
    $("#login-pwd").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        e.target.blur();
        $("#login-btn").click();
      }
    });
    $("#login-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      login();
    });
    $("#login-signup-btn").addEventListener("click", function (e){
      e.preventDefault();
      $(".login-block").classList.add("hide");
      $(".forgetpwd-block").classList.add("hide");
      $(".signup-block").classList.remove("hide");
      window.location.hash = "#signup";
    });
    $("#login-forgetpwd-btn").addEventListener("click", function (e){
      e.preventDefault();
      $(".login-block").classList.add("hide");
      $(".forgetpwd-block").classList.remove("hide");
      $(".signup-block").classList.add("hide");
      window.location.hash = "#forgetpwd";
    });

    //注册部分
    $("#signup-phone").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#signup-icode").focus();
      }
    });
    $("#signup-icode").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#signup-pwd").focus();
      }
    });
    $("#signup-icode-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      $("#signup-icode").classList.remove("error");
      getIcode("signup");
    });
    $("#signup-pwd").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#signup-pwd-2").focus();
      }
    });
    $("#signup-pwd-2").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        e.target.blur();
        $("#signup-btn").click();
      }
    });
    $("#signup-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      signup();
    });
    $("#signup-login-btn").addEventListener("click", function (e){
      e.preventDefault();
      $(".signup-block").classList.add("hide");
      $(".forgetpwd-block").classList.add("hide");
      $(".login-block").classList.remove("hide");
      window.location.hash = "#login";
    });

    //找密码部分
    $("#forgetpwd-phone").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#forgetpwd-pwd").focus();
      }
    });
    $("#forgetpwd-pwd").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#forgetpwd-pwd-2").focus();
      }
    });
    $("#forgetpwd-pwd").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#forgetpwd-pwd-2").focus();
      }
    });
    $("#forgetpwd-pwd-2").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#forgetpwd-icode").focus();
      }
    });
    $("#forgetpwd-icode").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        e.target.blur();
        $("#forgetpwd-btn").click();
      }
    });
    $("#forgetpwd-icode-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      getIcode("forgetpwd");
    });
    $("#forgetpwd-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      forgetpwd();
    });
    $("#forgetpwd-login-btn").addEventListener("click", function (e){
      e.preventDefault();
      $(".forgetpwd-block").classList.add("hide");
      $(".login-block").classList.remove("hide");
      $(".signup-block").classList.add("hide");
      window.location.hash = "#login";
    });
    $("#forgetpwd-signup-btn").addEventListener("click", function (e){
      e.preventDefault();
      $(".forgetpwd-block").classList.add("hide");
      $(".login-block").classList.add("hide");
      $(".signup-block").classList.remove("hide");
      window.location.hash = "#login";
    });
  }

  function login(){
    var loginTel = $("#login-phone").value;
    var loginPwd = $("#login-pwd").value;

    if(!loginTel){
      mdtext.addError($("#login-phone"), "请输入手机号");
      return;
    }else if(!loginPwd){
      mdtext.addError($("#login-pwd"), "请输入密码");
      return;
    }

    $("#login-hint").classList.remove("show");

    var encryptPwd = CryptoJS.MD5(loginPwd).toString().toUpperCase();
    var loginRequest = ajax({
      method: 'post',
      url: apiUrl + '/userbasic/login',
      data: {
        name: loginTel,
        password: encryptPwd
      }
    }).then(function (response, xhr) {
      if(!response.result){
        $("#login-hint").innerHTML = getErrorMsg(response.errCode);
        $("#login-hint").classList.add("show");
        return;
      }

      localStorage.id = response.data.id;
      localStorage.token = response.token;

      redirect();
      console.log("登陆成功");
    }).catch(function (response, xhr) {
      $("#login-hint").innerHTML = "服务器连接失败";
      $("#login-hint").classList.add("show");
    }).always(function (response, xhr) {
      // Do something
    });
  }

  var icodeCounterSignup = 60;
  var icodeCounterForgetpwd = 60;
  var icodeToken = null;
  var icodeTimerSignup = null;
  var icodeTimerForgetpwd = null;

  function getIcode(kind){
    if(!kind){
      return;
    }
    var telPattern = /^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
    var tel = "";
    if(kind == "signup"){
      tel = $("#signup-phone").value;
      if(!tel.match(telPattern)){
        mdtext.addError($("#signup-phone"), "手机号输入错误");
        return;
      }
      $("#signup-hint").classList.remove("show");
    }else if(kind == "forgetpwd"){
      tel = $("#forgetpwd-phone").value;
      if(!tel.match(telPattern)){
        mdtext.addError($("#forgetpwd-phone"), "手机号输入错误");
        return;
      }
      $("#forgetpwd-hint").classList.remove("show");
    }


    var getIcodeRequest = ajax({
      method: 'post',
      url: apiUrl + '/common/newCheckerT',
      data: {
        phone: tel
      }
    }).then(function(kind){
      return function (response, xhr) {
        if(!response.result){
          $("#signup-hint").innerHTML = getErrorMsg(response.errCode);
          $("#signup-hint").classList.add("show");
          return;
        }

        icodeToken = response.token;

//TO-DO
        $("#signup-icode").value = response.data;
        $("#forgetpwd-icode").value = response.data;
      };
    }(kind)).catch(function(kind){
      return function (response, xhr) {
        if(kind == "signup"){
          $("#signup-hint").innerHTML = "验证码获取失败";
          $("#signup-hint").classList.add("show");

          clearInterval(icodeTimerSignup);
          $("#signup-icode-btn").removeAttribute("disabled");
          $("#signup-icode-btn").innerHTML = "发送验证码";

          icodeCounterSignup = 60;
        }else if(kind == "forgetpwd"){
          $("#forgetpwd-hint").innerHTML = "验证码获取失败";
          $("#forgetpwd-hint").classList.add("show");

          clearInterval(icodeTimerForgetpwd);
          $("#forgetpwd-icode-btn").removeAttribute("disabled");
          $("#forgetpwd-icode-btn").innerHTML = "发送验证码";

          icodeCounterForgetpwd = 60;
        }

        icodeToken = null;
      };
    }(kind)).always(function (response, xhr) {

    });

    if(kind == "signup"){
      $("#signup-icode-btn").setAttribute("disabled","disabled");
      $("#signup-icode-btn").innerHTML = "已发送(" + icodeCounterSignup + ")";
      icodeCounterSignup = 60;
      icodeTimerSignup = setInterval(function  () {
        $("#signup-icode-btn").innerHTML = "已发送(" + --icodeCounterSignup + ")";
        if(icodeCounterSignup < 0){
          clearInterval(icodeTimerSignup);
          $("#signup-icode-btn").removeAttribute("disabled");
          $("#signup-icode-btn").innerHTML = "发送验证码";
          icodeCounterSignup = 60;
        }
      }, 1000);
    }else if(kind == "forgetpwd"){
      $("#forgetpwd-icode-btn").setAttribute("disabled","disabled");
      $("#forgetpwd-icode-btn").innerHTML = "已发送(" + icodeCounterForgetpwd + ")";
      icodeCounterForgetpwd = 60;
      icodeTimerForgetpwd = setInterval(function  () {
        $("#forgetpwd-icode-btn").innerHTML = "已发送(" + --icodeCounterForgetpwd + ")";
        if(icodeCounterForgetpwd < 0){
          clearInterval(icodeTimerForgetpwd);
          $("#forgetpwd-icode-btn").removeAttribute("disabled");
          $("#forgetpwd-icode-btn").innerHTML = "发送验证码";
          icodeCounterForgetpwd = 60;
        }
      }, 1000);
    }

  }

  function signup(){
    var signupTel = $("#signup-phone").value;
    var signupIcode = $("#signup-icode").value;

    var signupPwd = $("#signup-pwd").value;
    var signupPwd2 = $("#signup-pwd-2").value;

    if(!signupTel){
      mdtext.addError($("#signup-phone"), "请输入手机号");
      return;
    }else if(!icodeToken){
      mdtext.addError($("#signup-icode"), "请获取验证码");
      return;
    }else if(!signupIcode){
      mdtext.addError($("#signup-icode"), "请输入验证码");
      return;
    }else if(!signupPwd){
      mdtext.addError($("#signup-pwd"), "请输入密码");
      return;
    }else if(signupPwd.length < 6 || signupPwd.length > 16){
      mdtext.addError($("#signup-pwd"), "密码位数错误");
      return;
    }else if(signupPwd2 != signupPwd){
      mdtext.addError($("#signup-pwd-2"), "两次密码不一致");
      return;
    }
    $("#signup-hint").classList.remove("show");

    var encryptPwd = CryptoJS.MD5(signupPwd).toString().toUpperCase();

    console.log(encryptPwd);

    var signupRequest = ajax({
      method: 'post',
      url: apiUrl + '/userbasic/newUser',
      data: {
        password: encryptPwd,
        checker: signupIcode,
        token: icodeToken
      }
    }).then(function (response, xhr) {
      if(!response.result){
        $("#signup-hint").innerHTML = getErrorMsg(response.errCode);
        $("#signup-hint").classList.add("show");
        return;
      }
      console.log('注册成功');

      localStorage.id = response.data.id;
      localStorage.token = response.token;

      redirect();

    }).catch(function (response, xhr) {
      $("#signup-hint").innerHTML = "连接服务器失败";
      $("#signup-hint").classList.add("show");
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function forgetpwd(){
    var forgetpwdTel = $("#forgetpwd-phone").value;
    var forgetpwdIcode = $("#forgetpwd-icode").value;

    var forgetpwdPwd = $("#forgetpwd-pwd").value;
    var forgetpwdPwd2 = $("#forgetpwd-pwd-2").value;

    if(!forgetpwdTel){
      mdtext.addError($("#forgetpwd-phone"), "请输入手机号");
      return;
    }else if(!forgetpwdPwd){
      mdtext.addError($("#forgetpwd-pwd"), "请输入密码");
      return;
    }else if(forgetpwdPwd.length < 6 || forgetpwdPwd.length > 16){
      mdtext.addError($("#forgetpwd-pwd"), "密码位数错误");
      return;
    }else if(forgetpwdPwd2 != forgetpwdPwd){
      mdtext.addError($("#forgetpwd-pwd-2"), "两次密码不一致");
      return;
    }else if(!icodeToken){
      mdtext.addError($("#forgetpwd-icode"), "请获取验证码");
      return;
    }else if(!forgetpwdIcode){
      mdtext.addError($("#forgetpwd-icode"), "请输入验证码");
      return;
    }

    $("#forgetpwd-hint").classList.remove("show");

    var encryptPwd = CryptoJS.MD5(forgetpwdPwd).toString().toUpperCase();

    console.log(encryptPwd);

    var forgetpwdRequest = ajax({
      method: 'post',
      url: apiUrl + '/userbasic/forgetPwd',
      data: {
        password: encryptPwd,
        checker: forgetpwdIcode,
        token: icodeToken
      }
    }).then(function (response, xhr) {
      if(!response.result){
        $("#forgetpwd-hint").innerHTML = getErrorMsg(response.errCode);
        $("#forgetpwd-hint").classList.add("show");
        return;
      }
      console.log('修改密码成功');

      localStorage.id = response.data.id;
      localStorage.token = response.token;
      redirect();
    }).catch(function (response, xhr) {
      $("#forgetpwd-hint").innerHTML = "修改密码失败";
      $("#forgetpwd-hint").classList.add("show");
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function redirect () {
    if(document.referrer != document.URL && document.referrer !== ""){
      window.location.href = document.referrer;
    }else{
      window.location.href = "index.html";
    }
  }
  function checkUserStatus(){
    FFaccount.getAccountStatus(function(status){
      if(status === false){
        redirect();
      }
    });
  }

  //初始行为
  addElementEvent();
  mdtext.init();
  if(window.location.hash.split("#")[1] === "signup"){
    $("#login-signup-btn").click();
  }else if(window.location.hash.split("#")[1] === "forgetpwd"){
    $("#login-forgetpwd-btn").click();
  }
})();


