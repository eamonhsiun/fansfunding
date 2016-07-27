var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

var spaceTab = new FFtab($('.FFtabs'),$('.FFtab-contents'));

Vue.component('space-project-list', {
  template: '#space-project-list-template',
  props: ["projects"],
  methods: {
    getLeftTime: function(startTime, endTime){
      var d1;
      if(startTime instanceof Date){
        d1 = startTime;
      }else{
        d1 = new Date(startTime);
      }
      var d2 = new Date(endTime);
      var d3 = d2.getTime() - d1.getTime();
      if(d3 <= 0 ){
        return "已结束";
      }
      var day = Math.floor(d3/(24*3600*1000));
      var hour = Math.floor((d3%(24*3600*1000))/(3600*1000));
      return (day === 0 ? "" : day + "天") + hour + "小时";
    },
  }
});

Vue.filter("resource" ,function(value) {
  if(!value){
    return "";
  }
  return resourceUrl + value;
});

var spaceVm = new Vue({
  el: "#space",
  data: {
    status: false,
    isSelf: false,
    userInfo: {},
    spaceId: getQueryString("userid"),
    spaceUserInfo: {},
    projects: {
      sponsor: {
        status: false,
        connect: false,
        list: [],
        page: 0,
        totalPages: 0
      },
      follow: {
        status: false,
        connect: false,
        list: [],
        page: 0,
        totalPages: 0
      },
      support: {
        status: false,
        connect: false,
        list: [],
        page: 0,
        totalPages: 0
      }
    }

  },
  watch: {
    "isSelf": function(){

    }
  },
  methods:{
    redirect: function(){
      window.location.href = "404.html";
    },
    getSpaceUserInfo: function(){
      var _this = this;
      var spaceUserInfoRequest = ajax({
        method: 'get',
        url: apiUrl + "/user/" + _this.spaceId + "/info?token=" + localToken,
      }).then(function (response, xhr) {
        if(!response.result){
          _this.redirect();
          return;
        }
        _this.spaceUserInfo = response.data;
        if(_this.spaceId === localId){
          _this.isSelf = true;
        }
      }).catch(function (response, xhr) {
        console.log("加载失败");
      }).always(function (response, xhr) {
      });
    },
    getRecentProject: function(type, page){
      if(!type){
        throw new Error("没有指定项目类型");
      }
      var _this = this;
      var projectRequest = ajax({
        method: 'get',
        url: apiUrl +"/user/" + _this.spaceId + "/projects?token=" + localToken + "&type=" + type,
      }).then(function (response, xhr) {
        _this.projects[type].connect = true;
        if(!response.result){
          _this.projects[type].status = false;
        }else{
          _this.projects[type].list = response.data.list;
          _this.projects[type].status = true;
        }
      }).catch(function (response, xhr) {
        _this.projects[type].connect = false;
      }).always(function (response, xhr) {
        // Do something
      });
    }
  },
  ready: function(){
    var _this = this;
    if(!this.spaceId){
      this.spaceId = localId;
    }
    FFaccount.getAccountStatus(function(status){
      if(status === true){
        _this.status = true;
        _this.userInfo = localUserInfo;
      }else{
        _this.status = false;
      }
    });
    this.getSpaceUserInfo();
    this.getRecentProject("sponsor");
    this.getRecentProject("follow");
    this.getRecentProject("support");
  }
})
