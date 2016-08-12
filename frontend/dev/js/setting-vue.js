var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

Vue.filter("resource" ,function(value) {
  if(!value){
    return "";
  }
  return resourceUrl + value;
});

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
