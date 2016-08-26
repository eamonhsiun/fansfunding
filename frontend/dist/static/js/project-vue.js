var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };
var detailLoader = new FFloader($("#project-detail"));
var commentLoader = new FFloader($("#project-comment"));
var momentLoader = new FFloader($("#project-moment"));
var feedbackLoader = new FFloader($("#project-feedback"));
var supporterLoader = new FFloader($("#project-supporter"));
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
var projectVm = new Vue({
  el: "#project",
  data: {
    status: false, //登陆状态
    userInfo: {}, //用户信息
    categoryId: getQueryString("categoryId"), //分类id
    projectId: getQueryString("projectId"), // 项目id
    isFollowed: false, //项目follow
    isSelf: false,
    project: {
      status: false, //项目状态
      connect: false, //项目服务器连接状态
      data: {}, //基本数据
      detail: {} // 详情
    },
    sponsor: {
      status: false,
      connect: false,
      isFollowed: false,
      data: {},
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
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
      list: [] //评论列表
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
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
      list: [],
    },
    feedbacks: {
      status: false,
      connect: false,
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
      list: []
    },
    supporters: {
      status: false,
      connect: false,
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
      list: []
    },
    picViewer: {
      status: false,
      src: ""
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
    },
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
    checkLogin: function(){
      if(!this.status){
        this.redirect("login");
      }
    },
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
    getProject: function(callback){
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
          if(_this.project.data.sponsor == localId){
            _this.isSelf = true;
          }
          callback();
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
      var comments = this.comments;
      var commentsRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/comments" + (page ? "?page=" + page : ""),
      }).then(function (response, xhr) {
        comments.connect = true;
        a = response;
        if(!response.result){
          commentLoader.endLoad(false, "获取评论失败");
          comments.status = false;
        }else{
          var res = response.data;
          comments.list = res.list;
          if(comments.list.length !== 0){
            commentLoader.endLoad();
          }else{
            commentLoader.endLoad(false, "还没有评论，快来评论吧");
          }
          _this.setPagination(comments.pagination, res);
          comments.status = true;
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
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/feedbacks" + (page ? "?page=" + page : ""),
      }).then(function (response, xhr) {
        _this.feedbacks.connect = true;
        if(!response.result){
          feedbackLoader.endLoad(false,"获取回馈失败");
          _this.feedbacks.status = false;
        }else{
          _this.feedbacks.list = response.data.list;
          if(_this.feedbacks.list.length === 0){
            feedbackLoader.endLoad(false,"没有相关回报数据");
          }else{
            feedbackLoader.endLoad();
          }
          _this.setPagination(_this.feedbacks.pagination, response.data);
          _this.feedbacks.status = true;
        }
      }).catch(function (response, xhr) {
        _this.feedbacks.connect = false;
        feedbackLoader.endLoad(false, "服务器连接失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    sendComment: function(){
      this.checkLogin();
      var _this = this;
      if(this.comments.overflow || !this.comments.content){
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
          _this.getComments(0);
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
      this.checkLogin();
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
      $("#comment-textarea").focus();
    },
    getFollowStatus: function(){
      var _this = this;
      var followStatusRequest = ajax({
        method: 'post',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/followers",
        data: {
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
      this.checkLogin();
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
          console.log(response);
          console.log(xhr);
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
    },
    getMoments: function(page){
      var _this = this;
      var momentsRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/moment" + (page ? "?page=" + page : ""),
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
            momentLoader.endLoad(false, "发起人还没有发布动态");
          }
          _this.setPagination(_this.moments.pagination, response.data);
          _this.moments.status = true;
        }
      }).catch(function (response, xhr) {
        momentLoader.endLoad(false, "连接服务器失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    sendMoment: function(){
      this.checkLogin();
      var _this = this;
      if(this.moments.overflow || !this.moments.content){
        return;
      }
      var content = this.moments.content;
      momentLoader.init();
      var momentRequest = ajax({
        method: 'post',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/moment",
        data: {
          token: localToken,
          content: _this.moments.content,
          images: "",
          sponsorId: localId
        }
      }).then(function (response, xhr) {
        if(!response.result){
          momentLoader.endLoad(false, "发布动态失败");
        }else{
          _this.getMoments(0);
          _this.moments.content = "";
          momentLoader.endLoad();
        }
      }).catch(function (response, xhr) {
        momentLoader.endLoad(false, "评论失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    getUserFollowStatus: function(){
      if(localId){
        var _this = this;
        var followStatusRequest = ajax({
          method: 'post',
          url: apiUrl +"/user/" + localId + "/following/" + _this.project.data.sponsor,
          data: {
            token: localToken
          }
        }).then(function (response, xhr) {
          if(response.result){
            _this.sponsor.isFollowed = response.data;
          }
        }).catch(function (response, xhr) {

        }).always(function (response, xhr) {
          // Do something
        });
      }
    },
    followUser: function(){
      this.checkLogin();
      var _this = this;
      if(!this.sponsor.isFollowed){
        var followRequest = ajax({
          method: 'post',
          url: apiUrl +"/user/" + localId + "/follow/" + _this.project.data.sponsor,
          data: {
            token: localToken
          }
        }).then(function (response, xhr) {
          if(response.result){
            _this.sponsor.isFollowed = true;
          }
        }).catch(function (response, xhr) {

        }).always(function (response, xhr) {
          // Do something
        });
      }else{
        var unfollowRequest = ajax({
          method: 'post',
          url: apiUrl +"/user/" + localId + "/unfollow/" + _this.project.data.sponsor,
          data: {
            token: localToken
          }
        }).then(function (response, xhr) {
          if(response.result){
            _this.sponsor.isFollowed = false;
          }
        }).catch(function (response, xhr) {

        }).always(function (response, xhr) {
          // Do something
        });
      }
    },
    addShareBtn: function(){
      window._bd_share_config = {
        common : {
          "bdText": this.project.data.name + " | FAN$",
          "bdPic":resourceUrl + this.project.data.cover,
          "bdMiniList": ["evernotecn","qzone","tsina","tieba","sqq","youdao","mail","weixin"]
        },
        "share":{},
      }
      //以下为js加载部分
      with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?cdnversion='+~(-new Date()/36e5)];
    },
    getSupporters: function(page){
      var _this = this;
      var supportersRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/supporters" + (page ? "?page=" + page : ""),
      }).then(function (response, xhr) {
        _this.supporters.connect = true;
        if(!response.result){
          supporterLoader.endLoad(false, "获取支持者列表失败");
          _this.supporters.status = false;
        }else{
          _this.supporters.list = response.data.list;
          if(_this.supporters.list.length !== 0){
            supporterLoader.endLoad();
          }else{
            supporterLoader.endLoad(false, "项目还没有支持者");
          }
          _this.setPagination(_this.supporters.pagination, response.data);
          _this.supporters.status = true;
        }
      }).catch(function (response, xhr) {
        supporterLoader.endLoad(false, "连接服务器失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    viewPic: function(event){
      this.picViewer.src = event.target.src;
      this.picViewer.status = true;
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
    this.getProject(function(){
      _this.getUserFollowStatus();
      _this.addShareBtn();
    });
    this.getFeedbacks();
  }
});
// $("#intro-btn-share").onmouseover = function(e){
//   e.preventDefault();
// }
