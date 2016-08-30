var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

var spaceTab = new FFtab($('.FFtabs'),$('.FFtab-contents'));

Vue.component('space-project-list', {
  template: '#space-project-list-template',
  props: ["type", "spaceId", "num"],
  data: function(){
    return {
      list: [],
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
    }
  },
  events: {
    'space-project-get': function(type){
      if(type == this.type){
        this.getRecentProject();
      }
    },
    'space-project-get-all': function(){
      this.getRecentProject();
      return true;
    }
  },
  methods: {
    getRecentProject: function(page){
      var _this = this;
      var projectRequest = ajax({
        method: 'get',
        url: apiUrl +"/user/" + _this.spaceId + "/projects?rows=3&token=" + localToken + "&type=" + _this.type + (page ? "&page=" + page : ""),
      }).then(function (response, xhr) {
        if(!response.result){
          _this.$broadcast('ffloader-failure', "项目获取错误");
        }else{
          _this.list = response.data.list;
          if(_this.list.length === 0){
            _this.$broadcast('ffloader-failure', "没有相关项目");
          }else{
            _this.$broadcast('ffloader-success');
          }
          _this.setPagination(_this.pagination, response.data);
          _this.num = _this.pagination.total;
        }
      }).catch(function (response, xhr) {
        _this.$broadcast('ffloader-error');
      }).always(function (response, xhr) {
        // Do something
      });
    },
  }
});

Vue.component('space-order-list', {
  template: '#space-order-list-template',
  // props: ["orders","pagination", "callback"],
  data: function(){
    return {
      list: [],
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
    }
  },
  events: {
    'space-order-get': function(){
      this.getRecentOrder();
    },
    'space-order-get-all': function(){
      this.getRecentOrder();
      return true;
    },
  },
  methods: {
    getOrderStatus: function(value){
      switch (value){
      case "TRADE_SUCCESS" :
        return "交易成功";
      default:
        return "查询中";
      }
    },
    getRecentOrder: function(page){
      var _this = this;
      var orderRequest = ajax({
        method: 'get',
        url: apiUrl +"/user/" + localId + "/orders?token=" + localToken + '&rows=12' + (page ? "&page=" + page : ""),
      }).then(function (response, xhr) {
        if(!response.result){
          _this.$broadcast('ffloader-failure', "订单获取错误");
        }else{
          _this.list = response.data.list;
          if(_this.list.length === 0){
            _this.$broadcast('ffloader-failure', "没有相关订单");
          }else{
            _this.$broadcast('ffloader-success');
          }
          _this.setPagination(_this.pagination, response.data);
        }
      }).catch(function (response, xhr) {
        _this.$broadcast('ffloader-error');
      }).always(function (response, xhr) {
        // Do something
      });
    },
  },
});

var spaceVm = new Vue({
  el: "#space",
  data: {
    status: false,
    isSelf: false,
    userInfo: {},
    spaceId: getQueryString("userid"),
    spaceUserInfo: {},
    projectNum: {
      sponsor: 0,
      follow: 0,
      support: 0,
    },
    isFollowed: false,
    picViewer: {
      status: false,
      src: ""
    }
  },
  watch: {
  },
  events: {
    'moment-img-show': function(src){
      this.picViewer.src = src;
      this.picViewer.status = true;
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
        url: apiUrl + "/user/" + localId + "/info?viewId=" + _this.spaceId + "&token=" + localToken,
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
    getSpaceUserProjectNum: function(){

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
      this.$refs.recent.momentFilter();
    },
    getFollowingMoment: function(){
      if(this.isSelf){
        this.$refs.follower.momentFilter();
      }
    },
    getAllProjects: function(){
      this.$broadcast("space-project-get-all");
    },
    getAllOrders: function(){
      this.$broadcast("space-order-get-all");
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
      _this.getAllProjects();
      _this.getUserFollowStatus();
    });

  }
})
