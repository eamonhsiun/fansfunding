var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };
var orderTab = new FFtab($('.kit-progress-tab'),$('.order-progress'),{
  click: false
});

var orderVm = new Vue({
  el: "#order",
  data: {
    status: false,
    userInfo: {},
    projectId: getQueryString("projectId"),
    categoryId: getQueryString("categoryId"),
    feedbackId: getQueryString("feedbackId"),
    orderStatus: getQueryString("orderStatus") ? (getQueryString("orderStatus") === "true" ? true : false) : null,
    orderNo: getQueryString("orderNo"),
    step: 1,
    totalStep: 3,
    error: false,
    project: {
      status: false,
      connect: false,
      data: {},
    },
    feedback: {
      status: false,
      connect: false,
      target: {},
      list: [],
      page: 0,
      totalPages: 0,
    },
    order: {
      money: 0,
    },
    addressId: -1,
    pay: {
      status: false,
      connect: false,
      page: "",
    },
    picViewer: {
      status: false,
      src: ""
    }
  },
  watch: {

  },
  methods: {
    redirect: function(target){
      if(target){
        switch(target){
          case "login":
            window.location.href = "login.html";
            return;
          case "project":
            if(!this.categoryId || !this.projectId){
              window.location.href = "index.html";
              return;
            }
            window.location.href = "project.html?categoryId="+ this.categoryId + "&projectId="+this.projectId;
            return;
          case "order":
            window.location.href = "order-detail.html?orderNo="+ this.orderNo;
            return;
        }
        return;
      }
      if(document.referrer != document.URL && document.referrer !== ""){
        window.location.href = document.referrer;
        return;
      }
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
        }else{
          _this.project.detail = response.data;
          _this.project.status = true;
        }
      }).catch(function (response, xhr) {
        _this.project.connect = false;
      }).always(function (response, xhr) {
        // Do something
      });
    },
    getFeedback: function(page){
      var _this = this;
      var pageArg = "";
      if(page){
        pageArg = "?page=" + page;
      }
      var feedbackRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + this.categoryId + "/" + this.projectId + "/feedbacks" + pageArg,
      }).then(function (response, xhr) {
        _this.feedback.connect = true;
        if(!response.result){
          _this.feedback.status = false;
        }else{
          _this.feedback.list = response.data.list;
          _this.feedback.page = response.data.pageNum;
          _this.feedback.totalPages = response.data.pages;
          if(_this.feedback.list.length === 0){
            _this.redirect();
          }else{
            for(var i = 0; i < _this.feedback.list.length; i++){
              if(_this.feedback.list[i].id == _this.feedbackId){
                _this.feedback.target = _this.feedback.list[i];
                var t = _this.feedback.target;
                _this.feedback.status = true;
                _this.order.money = _this.feedback.list[i].limitation;
                if(t.ceiling > 0 && t.supportTimes >= t.ceiling){
                  alert("支持人已满");
                  _this.redirect("project");
                }
                return;
              }
            }
            if(!_this.feedback.status && _this.feedback.page < _this.feedback.totalPages){
              _this.getFeedback(_this.feedback.page + 1);
            }else{
              _this.redirect();
            }
          }
        }
      }).catch(function (response, xhr) {
        _this.feedback.connect = false;
      }).always(function (response, xhr) {
        // Do something
      });
    },
    submitOrder: function(){
      var _this = this;
      orderTab.next();
      this.step = 2;
      var temp_form = document.createElement("form");
      temp_form .action = apiUrl + "/pay/web";
      temp_form .target = "_blank";
      temp_form .method = "post";
      temp_form .style.display = "none";

      var token = document.createElement("input");
      token.name = "token";
      token.value = localToken;
      temp_form.appendChild(token);

      var feedbackId = document.createElement("input");
      feedbackId.name = "feedbackId";
      feedbackId.value = _this.feedbackId;
      temp_form.appendChild(feedbackId);

      var userId = document.createElement("input");
      userId.name = "userId";
      userId.value = localId;
      temp_form.appendChild(userId);

      var addressId = document.createElement("input");
      addressId.name = "addressId";
      addressId.value = this.addressId;
      temp_form.appendChild(addressId);

      document.body.appendChild(temp_form);
      temp_form.submit();
    },
    cancelOrder: function(){
      orderTab.goto(0);
      this.step = 1;
    },
    finishOrder: function(){
      orderTab.goto(2);
      this.step = 3;
    },
    viewPic: function(event){
      this.picViewer.src = event.target.src;
      this.picViewer.status = true;
    }
  },
  ready: function(){
    if(!this.projectId || !this.categoryId || !this.feedbackId){
      if(this.orderStatus === null || !this.orderNo){
        this.redirect();
        return;
      }
    }
    var _this = this;
    FFaccount.getAccountStatus(function(status){
      if(status === true){
        _this.status = true;
        _this.userInfo = localUserInfo;
      }else{
        _this.status = false;
        _this.redirect("login");
      }
    });
    if(this.orderStatus === true || this.orderStatus === false){
      this.finishOrder();
    }else{
      this.getProject();
      this.getFeedback();
    }
  }
});

