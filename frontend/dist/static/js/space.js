var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

var spaceTab = new FFtab($('.FFtabs'),$('.FFtab-contents'));
var momentLoader = new FFloader($("#moment"));

Vue.component('space-project-list', {
  template: '#space-project-list-template',
  props: ["projects","pagination"],
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
  props: ["orders","pagination", "callback"],
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
        pagination: {
          total: 0,
          pageSize: 0,
          pages: 0,
          pageNum: 0,
        },
      },
      follow: {
        status: false,
        connect: false,
        list: [],
        pagination: {
          total: 0,
          pageSize: 0,
          pages: 0,
          pageNum: 0,
        },
      },
      support: {
        status: false,
        connect: false,
        list: [],
        pagination: {
          total: 0,
          pageSize: 0,
          pages: 0,
          pageNum: 0,
        },
      }
    },
    orders: {
      status: false,
      connect: false,
      list: [],
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
    },
    moments: {
      status: false,
      connect: false,
      count: 0,
      MAX_COUNT: 128,
      hint: "剩余128个字",
      overflow: false,
      content: "",
      images: [],
      list: [],
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
    },
    picViewer: {
      status: false,
      src: ""
    }
  },
  watch: {
    'moments.content': function(val){
      this.moments.count = val.length;
      var num = this.moments.MAX_COUNT - this.moments.count;
      if(num >= 0){
        this.moments.overflow = false;
        this.moments.hint = "剩余" + num + "个字";
      }else{
        this.moments.overflow = true;
        this.moments.hint = "超出" + -num + "个字";
      }
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
    setPagination: function(pagination, data){
      pagination.total = data.total;
      pagination.pageNum = data.pageNum;
      pagination.pages = data.pages;
      pagination.pageSize = data. pageSize;
    },
    viewPic: function(event){
      this.picViewer.src = event.target.src;
      this.picViewer.status = true;
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
          _this.setPagination(_this.projects[type].pagination, response.data);
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
    getRecentMoment: function(page){
      var _this = this;
      var momentRequest = ajax({
        method: 'get',
        url: apiUrl +"/user/" + _this.spaceId + "/moment?token=" + localToken + '&rows=12' + (page ? "&page=" + page : ""),
      }).then(function (response, xhr) {
        _this.moments.connect = true;
        if(!response.result){
          momentLoader.endLoad(false, "获取动态失败");
          _this.moments.status = false;
        }else{
          _this.moments.list = response.data.list;
          if(_this.moments.list.length !== 0){
            momentLoader.endLoad();
          }else{
            momentLoader.endLoad(false, "没有相关动态");
          }
          _this.setPagination(_this.moments.pagination, response.data);
          _this.moments.status = true;
        }
      }).catch(function (response, xhr) {
        _this.moments.connect = false;
        momentLoader.endLoad(false, "连接服务器失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    getRecentOrder: function(page){
      var _this = this;
      var projectRequest = ajax({
        method: 'get',
        url: apiUrl +"/user/" + _this.spaceId + "/orders?token=" + localToken + '&rows=12' + (page ? "&page=" + page : ""),
      }).then(function (response, xhr) {
        _this.orders.connect = true;
        if(!response.result){
          _this.orders.status = false;
        }else{
          _this.orders.list = response.data.list;
          _this.setPagination(_this.orders.pagination, response.data);
          _this.orders.status = true;
        }
      }).catch(function (response, xhr) {
        _this.orders.connect = false;
      }).always(function (response, xhr) {
        // Do something
      });
    },
    sendMoment: function(){
      var _this = this;
      if(this.moments.overflow || !this.moments.content){
        return;
      }
      var content = this.moments.content;
      momentLoader.init();
      var momentRequest = ajax({
        method: 'post',
        url: apiUrl +"/user/" + localId + "/moment",
        data: {
          token: localToken,
          content: _this.moments.content,
          images: "",
        }
      }).then(function (response, xhr) {
        if(!response.result){
          momentLoader.endLoad(false, "发布动态失败");
        }else{
          _this.getRecentMoment(0);
          _this.moments.content = "";
          momentLoader.endLoad();
        }
      }).catch(function (response, xhr) {
        momentLoader.endLoad(false, "评论失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    viewPic: function(event){
      this.picViewer.src = event.target.src;
      this.picViewer.status = true;
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
      _this.getRecentMoment();
    });

  }
})
