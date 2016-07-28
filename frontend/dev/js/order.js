var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

Vue.filter("resource" ,function(value) {
  if(!value){
    return "";
  }
  return resourceUrl + value;
});

var orderVm = new Vue({
  el: "#order",
  data: {
    status: false,
    userInfo: {},
    projectId: getQueryString("id"),
    categoryId: getQueryString("categoryId"),
    feedbackId: getQueryString("feedbackId"),
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
    address: {
      status: false,
      connect: false,
      list: []
    },
    pay: {
      status: false,
      connect: false,
    }
  },
  watch: {

  },
  methods: {
    redirect: function(){

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
  },
  ready: function(){
    if(!this.projectId || !this.categoryId || !this.feedbackId){
      this.redirect();
    }
    var _this = this;
    FFaccount.getAccountStatus(function(status){
      if(status === true){
        _this.status = true;
        _this.userInfo = localUserInfo;
      }else{
        _this.status = false;
        _this.redirect();
      }
    });
    this.getProject();
  }
});


var orderTab = new FFtab($('.kit-progress-tab'),$('.order-progress'),{
  callback: function(index, tab1, content1, tab2, content2){
    tab2.classList.add("FFtab-visited");
    initiationVm.step = index + 1;
  },
  click: false
});

