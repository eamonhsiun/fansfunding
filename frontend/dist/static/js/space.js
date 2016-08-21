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

Vue.component('space-order-list', {
  template: '#space-order-list-template',
  props: ["orders"],
  methods: {
    getTime: function(value){
      var date;
      if(value instanceof Date){
        date = value;
      }else{
        date = new Date(value);
      }
      return date.getFullYear() + "年" + (date.getMonth() + 1) + "月" + date.getDate() + "日 " + date.getHours() + ":" + date.getMinutes();
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
    isFollowed: false,
    projects: {
      sponsor: {
        status: false,
        connect: false,
        list: [],
        count: 0,
        page: 0,
        totalPages: 0
      },
      follow: {
        status: false,
        connect: false,
        list: [],
        count: 0,
        page: 0,
        totalPages: 0
      },
      support: {
        status: false,
        connect: false,
        list: [],
        count: 0,
        page: 0,
        totalPages: 0
      }
    },
    orders: {
      status: false,
      connect: false,
      list: [],
      count: 0,
      page: 0,
      totalPages: 0
    }

  },
  watch: {
    "isSelf": function(){

    }
  },
  methods:{
    redirect: function(target){
      if(target){
        switch (target){
        case "login":
          window.location.href = "login.html";
          return;
        }
      }
      window.location.href = "404.html";
    },
    getSpaceUserInfo: function(callback){
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
        callback();
      }).catch(function (response, xhr) {
        if(xhr.status == 404 || xhr.status == 400){
          _this.redirect();
          return;
        }
        console.log("服务器连接失败");
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
          _this.projects[type].count = response.data.total;
          _this.projects[type].page = response.data.pageNum;
          _this.projects[type].totalPages = response.data.pages;
          _this.projects[type].status = true;
        }
      }).catch(function (response, xhr) {
        _this.projects[type].connect = false;
      }).always(function (response, xhr) {
        // Do something
      });
    },
    getUserFollowStatus: function(){
      var _this = this;
      var followStatusRequest = ajax({
        method: 'post',
        url: apiUrl +"/user/" + localId + "/following/" + _this.spaceId,
        data: {
          token: localToken
        }
      }).then(function (response, xhr) {
        if(response.result){
          _this.isFollowed = response.data;
        }
      }).catch(function (response, xhr) {

      }).always(function (response, xhr) {
        // Do something
      });
    },
    followUser: function(){
      if(this.isSelf){
        return;
      }
      var _this = this;
      if(!this.isFollowed){
        var followRequest = ajax({
          method: 'post',
          url: apiUrl +"/user/" + localId + "/follow/" + _this.spaceId,
          data: {
            token: localToken
          }
        }).then(function (response, xhr) {
          if(response.result){
            _this.isFollowed = true;
          }
        }).catch(function (response, xhr) {

        }).always(function (response, xhr) {
          // Do something
        });
      }else{
        var unfollowRequest = ajax({
          method: 'post',
          url: apiUrl +"/user/" + localId + "/unfollow/" + _this.spaceId,
          data: {
            token: localToken
          }
        }).then(function (response, xhr) {
          if(response.result){
            _this.isFollowed = false;
          }
        }).catch(function (response, xhr) {

        }).always(function (response, xhr) {
          // Do something
        });
      }
    },
    getRecentOrder: function(page){
      var _this = this;
      var projectRequest = ajax({
        method: 'get',
        url: apiUrl +"/user/" + _this.spaceId + "/orders?token=" + localToken,
      }).then(function (response, xhr) {
        _this.orders.connect = true;
        if(!response.result){
          _this.orders.status = false;
        }else{
          _this.orders.list = response.data.list;
          _this.orders.count = response.data.total;
          _this.orders.page = response.data.pageNum;
          _this.orders.totalPages = response.data.pages;
          _this.orders.status = true;
        }
      }).catch(function (response, xhr) {
        _this.orders.connect = false;
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
        _this.redirect("login");
      }
    });
    this.getSpaceUserInfo(function(){
      _this.getRecentProject("sponsor");
      _this.getRecentProject("follow");
      _this.getRecentProject("support");
      _this.getUserFollowStatus();
      _this.getRecentOrder();
    });

  }
})
