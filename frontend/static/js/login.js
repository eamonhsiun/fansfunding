// ;(function(){

  function addElementEvent () {
    // 登陆部分
    $("#login-phone").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#login-pwd").focus();
      }
    });
    $("#login-phone").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#login-pwd").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
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
    $("#signup-phone").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#signup-icode").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#signup-pwd").focus();
      }
    });
    $("#signup-icode").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
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
    $("#signup-pwd").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#signup-pwd-2").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        e.target.blur();
        $("#signup-btn").click();
      }
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
    $("#forgetpwd-phone").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#forgetpwd-pwd").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        $("#forgetpwd-pwd-2").focus();
      }
    });
    $("#forgetpwd-pwd").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#forgetpwd-pwd").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        $("#forgetpwd-icode").focus();
      }
    });
    $("#forgetpwd-pwd-2").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#forgetpwd-icode").addEventListener("keydown", function (e) {
      if (e.keyCode == 13) {
        e.preventDefault();
        e.target.blur();
        $("#forgetpwd-btn").click();
      }
    });
    $("#forgetpwd-icode").addEventListener("blur", function (e) {
      e.target.classList.remove("error");
    });
    $("#forgetpwd-icode-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      $("#forgetpwd-icode").classList.remove("error");
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
    var encryptPwd = CryptoJS.MD5(loginPwd).toString().toUpperCase();
    var loginRequest = ajax({
      method: 'post',
      url: apiUrl + '/user/login',
      data: {
        name: loginTel,
        password: encryptPwd
      }
    }).then(function (response, xhr) {
      if(!response.result){
        $("#login-phone").classList.add("error");
        $("#login-pwd").classList.add("error");
        $("#login-hint").innerHTML = "用户名或密码错误";
        return;
      }
      localStorage.id = response.data.id;
      localStorage.taken = response.token;
      console.log("登陆成功");
    }).catch(function (response, xhr) {
      $("#login-phone").classList.add("error");
      $("#login-pwd").classList.add("error");
      $("#login-hint").innerHTML = "服务器连接失败";
    }).always(function (response, xhr) {
      // Do something
    });
  }

  var icodeCounterSignup = 60;
  var icodeCounterForgetPwd = 60;
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
        $("#signup-phone").classList.add("error");
        $("#signup-hint").innerHTML = "手机号输入错误";
        return;
      }
    }else if(kind == "forgetpwd"){
      tel = $("#forgetpwd-phone").value;
      if(!tel.match(telPattern)){
        $("#forgetpwd-phone").classList.add("error");
        $("#forgetpwd-hint").innerHTML = "手机号输入错误";
        return;
      }
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
          throw new Error();
        }

        icodeToken = response.token;

        $("#signup-icode").value = response.data;
        $("#forgetpwd-icode").value = response.data;
      };
    }(kind)).catch(function(kind){
      return function (response, xhr) {
        if(kind == "signup"){
          $("#signup-icode").classList.add("error");
          $("#signup-hint").innerHTML = "验证码获取失败";

          clearInterval(icodeTimerSignup);
          $("#signup-icode-btn").removeAttribute("disabled");
          $("#signup-icode-btn").innerHTML = "发送验证码";

          icodeCounterSignup = 60;
        }else if(kind == "forgetpwd"){
          $("#forgetpwd-icode").classList.add("error");
          $("#forgetpwd-hint").innerHTML = "验证码获取失败";

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
      icodeCounter = 60;
      icodeTimerSignup = setInterval(function  () {
        $("#signup-icode-btn").innerHTML = "已发送(" + --icodeCounterSignup + ")";
        if(icodeCounter < 0){
          clearInterval(icodeTimerSignup);
          $("#signup-icode-btn").removeAttribute("disabled");
          $("#signup-icode-btn").innerHTML = "发送验证码";
          icodeCounterSignup = 60;
        }
      }, 1000);
    }else if(kind == "forgetpwd"){
      $("#forgetpwd-icode-btn").setAttribute("disabled","disabled");
      $("#forgetpwd-icode-btn").innerHTML = "已发送(" + icodeCounterForgetpwd + ")";
      icodeCounter = 60;
      icodeTimerforgetpwd = setInterval(function  () {
        $("#forgetpwd-icode-btn").innerHTML = "已发送(" + --icodeCounterForgetpwd + ")";
        if(icodeCounter < 0){
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
      $("#signup-phone").classList.add("error");
      $("#signup-hint").innerHTML = "请输入手机号";
      return;
    }else if(!icodeToken){
      $("#signup-phone").classList.add("error");
      $("#signup-hint").innerHTML = "请获取验证码";
      return;
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

    var encryptPwd = CryptoJS.MD5(signupPwd).toString().toUpperCase();

    console.log(encryptPwd);

    var signupRequest = ajax({
      method: 'post',
      url: apiUrl + '/user/newUser',
      data: {
        password: encryptPwd,
        checker: signupIcode,
        token: icodeToken
      }
    }).then(function (response, xhr) {
      if(!response.result){
        clearInterval(icodeTimerSignup);
        $("#signup-icode-btn").removeAttribute("disabled");
        $("#signup-icode-btn").innerHTML = "发送验证码";
        icodeCounterSignup = 60;
        throw new Error();
      }
      console.log('注册成功');

      localStorage.id = response.data.id;
      localStorage.token = response.token;

    }).catch(function (response, xhr) {
      $("#signup-phone").classList.add("error");
      $("#signup-pwd").classList.add("error");
      $("#signup-hint").innerHTML = "注册失败";
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
      $("#forgetpwd-phone").classList.add("error");
      $("#forgetpwd-hint").innerHTML = "请输入手机号";
      return;
    }else if(!forgetpwdPwd){
      $("#forgetpwd-pwd").classList.add("error");
      $("#forgetpwd-hint").innerHTML = "请输入密码";
      return;
    }else if(forgetpwdPwd.length < 6 || forgetpwdPwd.length > 16){
      $("#forgetpwd-pwd").classList.add("error");
      $("#forgetpwd-hint").innerHTML = "密码位数错误";
      return;
    }else if(forgetpwdPwd2 != forgetpwdPwd){
      $("#forgetpwd-pwd-2").classList.add("error");
      $("#forgetpwd-hint").innerHTML = "两次密码不一致";
      return;
    }else if(!icodeToken){
      $("#forgetpwd-phone").classList.add("error");
      $("#forgetpwd-hint").innerHTML = "请获取验证码";
      return;
    }else if(!forgetpwdIcode){
      $("#forgetpwd-icode").classList.add("error");
      $("#forgetpwd-hint").innerHTML = "请输入验证码";
      return;
    }

    var encryptPwd = CryptoJS.MD5(forgetpwdPwd).toString().toUpperCase();

    console.log(encryptPwd);

    var forgetpwdRequest = ajax({
      method: 'post',
      url: apiUrl + '/user/forgetPwd',
      data: {
        password: encryptPwd,
        checker: forgetpwdIcode,
        token: icodeToken
      }
    }).then(function (response, xhr) {
      if(!response.result){
        clearInterval(icodeTimerForgetpwd);
        $("#forgetpwd-icode-btn").removeAttribute("disabled");
        $("#forgetpwd-icode-btn").innerHTML = "发送验证码";
        icodeCounterForgetpwd = 60;
        throw new Error();
      }
      console.log('修改密码成功');

      localStorage.id = response.data.id;
      localStorage.token = response.token;

    }).catch(function (response, xhr) {
      $("#forgetpwd-phone").classList.add("error");
      $("#forgetpwd-pwd").classList.add("error");
      $("#forgetpwd-hint").innerHTML = "修改密码失败";
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function redirect () {
    var ref = getQueryString("ref");
    if(ref){
      window.location.href = ref;
    }else{
      window.location.href = "http://localhost:8000/index.html";
    }
  }

  //初始行为
  addElementEvent();
  if(window.location.hash.split("#")[1] === "signup"){
    $("#login-signup-btn").click();
  }else if(window.location.hash.split("#")[1] === "forgetpwd"){
    $("#login-forgetpwd-btn").click();
  }
// })();


