var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

var settingVm = new Vue({
  el: "#setting",
  data: {
    status: false,
    userInfo: {},
    profile: {
      connect: false,
      status: false,
      dirty: false,
      alert: {
        show: false,
        text: "保存成功"
      }
    },
    security: {
      connect: false,
      status: false,
      oldPwd: "",
      newPwd: "",
      confirmPwd: "",
      alert: {
        show: false,
        text: "修改成功"
      }
    }
  },
  watch: {

  },
  methods: {
    redirect: function(){
      window.location.href = "login.html";
    },
    activeAlert: function(target, text){
      if(text){
        this[target].alert.show = true;
        this[target].alert.text = text;
      }else{
        this[target].alert.show = false;
      }
    },
    uploadProfile: function(){
      var _this = this;
      var emailPattern = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
      if(!this.userInfo.nickname){
        this.activeAlert("profile", "昵称不能为空");
        return;
      }
      if(!this.userInfo.email.match(emailPattern)){
        this.activeAlert("profile", "邮箱格式错误");
        return;
      }
      this.activeAlert("profile");

      var uploadProfileRequest = ajax({
        method: 'post',
        url: apiUrl + '/user/' + localId + '/info',
        data: {
          token: localToken,
          nickname: _this.userInfo.nickname,
          email: _this.userInfo.email,
          sex: _this.userInfo.realInfo.sex,
          intro: _this.userInfo.intro
        }
      }).then(function (response, xhr) {
        if(!response.result){
          _this.activeAlert("profile", "修改失败");
          return;
        }
        _this.activeAlert("profile", "修改成功");

      }).catch(function (response, xhr) {
        _this.activeAlert("profile", "服务器连接失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    changePassword: function(){
      var _this = this;
      // if(!this.security.oldPwd){
      //   this.activeAlert("security", "旧密码不能为空");
      //   return;
      // }
      if(!this.security.newPwd){
        this.activeAlert("security", "新密码不能为空");
        return;
      }
      if(this.security.newPwd.length < 6 || this.security.newPwd.length > 16){
        this.activeAlert("security", "密码位数错误");
        return;
      }
      if(this.security.confirmPwd !== this.security.newPwd){
        this.activeAlert("security", "两次密码不一致");
        return;
      }
      this.activeAlert("security");

      var encryptPwd = CryptoJS.MD5(this.security.newPwd).toString().toUpperCase();

      var changePasswordRequest = ajax({
        method: 'post',
        url: apiUrl + '/user/' + localId + '/newPwd',
        data: {
          token: localToken,
          password: encryptPwd
        }
      }).then(function (response, xhr) {
        if(!response.result){
          _this.activeAlert("security", getErrorMsg(response.errCode));
          return;
        }
        _this.activeAlert("security", "修改成功");

      }).catch(function (response, xhr) {
        _this.activeAlert("security", "服务器连接失败");
      }).always(function (response, xhr) {
        // Do something
      });
    }
  },
  ready: function(){
    var _this = this;
    FFaccount.getAccountStatus(function(status){
      if(status){
        _this.userInfo = localUserInfo;
        _this.status = true;
      }else{
        _this.status = false;
        _this.redirect();
      }
    })
  },
});

var orderTab = new FFtab($('.kit-tabs'),$('.setting-wrap'));
