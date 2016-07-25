var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };
var detailLoader = new FFloader($("#project-detail"));
var commentLoader = new FFloader($("#project-comment"));
var repayLoader = new FFloader($("#project-repay"));
var projectTab = new FFtab($('.project-tabs'),$('.project-contents'));

//-------------filter---------------
Vue.filter("resource" ,function(value) {
  if(!value){
    return "";
  }
  return resourceUrl + value;
});
Vue.filter("commentTime" ,function(value){
  var date;
  if(value instanceof Date){
    date = value;
  }else{
    date = new Date(value);
  }
  var nowDate = new Date();
  var intervalDate = nowDate.getTime() - date.getTime();
  var day = Math.floor(intervalDate/(24*3600*1000));
  if(day > 0){
    return (date.getMonth() + 1) + "月" + date.getDate() + "日";
  }else{
    var hour = Math.floor((intervalDate%(24*3600*1000))/(3600*1000));
    if(hour > 0){
      return hour + "小时前";
    }else{
      var minutes = Math.floor(((intervalDate%(24*3600*1000))%(3600*1000))/(60*1000));
      if(minutes < 1){
        return "刚刚";
      }else{
        return minutes + "分钟前";
      }
    }
  }
});

//-----------vue对象---------------
var vm = new Vue({
  el: "#project",
  data: {
    status: false, //登陆状态
    userInfo: {}, //用户信息
    categoryId: getQueryString("categoryId"), //分类id
    projectId: getQueryString("id"), // 项目id
    isFollowed: false,
    project: {
      status: false, //项目状态
      connect: false, //项目服务器连接状态
      data: {}, //基本数据
      detail: {} // 详情
    },
    comments: {
      status:false, //评论状态
      connect: false, //评论服务器连接
      count: 0, //字数统计
      MAX_COUNT: 128, //最大字数
      hint: "剩余128个字", //提示
      overflow: false, //字数是否超出
      content: "", //本人评论内容
      reply: {
        status: false, //是否回复
        pointTo: 0, //回复对象
        pointToNickname: "", //回复对象昵称
        placeholder: ""
      },
      page: 0,
      totalPages: 0,
      list: [] //评论列表
    },
    feedbacks: {
      status: false,
      connect: false,
      page: 0,
      totalPages: 0,
      list: []
    },
    followers: {
      status: false,
      connect: false,
      page: 0,
      totalPages: 0,
      count: 0,
      list: []
    }
  },
  watch: {
    'comments.content': function(val){
      this.comments.count = val.length;
      var num = this.comments.MAX_COUNT - this.comments.count;
      if(num >= 0){
        this.comments.overflow = false;
        this.comments.hint = "剩余" + num + "个字";
      }else{
        this.comments.overflow = true;
        this.comments.hint = "超出" + -num + "个字";
      }
    }
  },
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
    redirect: function(){
      window.location.href = "404.html";
    },
    getProject: function(){
      var _this = this;
      var projectRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId,
      }).then(function (response, xhr) {
        _this.project.connect = true;
        if(!response.result){
          _this.project.status = false;
          _this.redirect();
        }else{
          _this.project.data = response.data;
          _this.project.status = true;
        }
      }).catch(function (response, xhr) {
        _this.project.connect = false;
      }).always(function (response, xhr) {
        // Do something
      });
      var projectDetailRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/detail",
      }).then(function (response, xhr) {
        _this.project.connect = true;
        if(!response.result){
          _this.project.status = false;
          detailLoader.endLoad(false, "获取信息失败");
        }else{
          _this.project.detail = response.data;
          detailLoader.endLoad();
          _this.project.status = true;
        }
      }).catch(function (response, xhr) {
        _this.project.connect = false;
        detailLoader.endLoad(false, "服务器连接失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    getComments: function(page){
      var _this = this;
      var commentsRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/comments",
      }).then(function (response, xhr) {
        _this.comments.connect = true;
        a = response;
        if(!response.result){
          commentLoader.endLoad(false, "获取评论失败");
          _this.comments.status = false;
        }else{
          _this.comments.list = response.data.list;
          if(_this.comments.list.length !== 0){
            commentLoader.endLoad();
          }else{
            commentLoader.endLoad(false, "还没有评论，快来评论吧");
          }
          _this.comments.status = true;
        }
      }).catch(function (response, xhr) {
        commentLoader.endLoad(false, "连接服务器失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    getFeedbacks: function(page){
      var _this = this;
      var feedbacksRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/feedbacks",
      }).then(function (response, xhr) {
        _this.feedbacks.connect = true;
        if(!response.result){
          repayLoader.endLoad(false,"获取回馈失败");
          _this.feedbacks.status = false;
        }else{
          _this.feedbacks.list = response.data.list;
          if(_this.feedbacks.length === 0){
            repayLoader.endLoad(false,"没有相关回报数据");
          }else{
            repayLoader.endLoad();
          }
          _this.feedbacks.status = true;
        }
      }).catch(function (response, xhr) {
        _this.feedbacks.connect = false;
        repayLoader.endLoad(false, "服务器连接失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    sendComment: function(){
      var _this = this;
      if(this.comments.overflow){
        return;
      }
      var content = this.comments.content;
      commentLoader.init();
      var commentRequest = ajax({
        method: 'post',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/comments",
        data: {
          token: localToken,
          userId: localId,
          content: content,
          pointTo: this.comments.reply.pointTo,
          categoryId: this.categoryId,
          projectId: this.projectId
        }
      }).then(function (response, xhr) {
        if(!response.result){
          commentLoader.endLoad(false, "评论失败");
        }else{
          _this.comments.list.unshift({
            commenterHead: _this.userInfo.head,
            pointTo: _this.comments.reply.pointTo,
            pointToNickname: _this.comments.reply.pointToNickname,
            commenterName: _this.userInfo.name,
            commenterId: localId,
            commenterNickname: _this.userInfo.nickname,
            projectId: _this.projectId,
            commentTime: (new Date()).getTime(),
            content: _this.comments.content,
          });
          _this.comments.content = "";
          _this.replyComment();
          commentLoader.endLoad();
        }
      }).catch(function (response, xhr) {
        commentLoader.endLoad(false, "评论失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    replyComment: function(id, nickname){
      if(!id || !nickname || id == this.comments.reply.pointTo){
        this.comments.reply.status = false;
        this.comments.reply.pointTo = 0;
        this.comments.reply.pointToNickname = "";
        this.comments.reply.placeholder = "";
      }else{
        this.comments.reply.status = true;
        this.comments.reply.pointTo = id;
        this.comments.reply.pointToNickname = nickname;
        this.comments.reply.placeholder = "回复" + nickname + "：";
      }
    },
    getFollowStatus: function(){
      var _this = this;
      var followStatusRequest = ajax({
        method: 'post',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/followers",
        data: {
          categoryId: this.categoryId,
          projectId: this.projectId,
          userId: localId
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
    followProject: function(){
      var _this = this;
      if(!this.isFollowed){
        var followRequest = ajax({
          method: 'post',
          url: apiUrl +"/user/" + localId + "/follow/" + this.categoryId + "/" + this.projectId,
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
          url: apiUrl +"/user/" + localId + "/unfollow/" + this.categoryId + "/" + this.projectId,
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
    }
  },
  ready: function() {
    var _this = this;
    if(!this.categoryId || !this.projectId){
      this.redirect();
    }
    FFaccount.getAccountStatus(function(status){
      if(status === true){
        _this.status = true;
        _this.userInfo = localUserInfo;
        _this.getFollowStatus();
      }else{
        _this.status = false;
      }
    });
    this.getProject();
    this.getFeedbacks();
  }
});
