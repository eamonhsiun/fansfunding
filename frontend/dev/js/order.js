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
    order: {
      money: 0,
    },
    address: {
      status: false,
      connect: false,
      selected: 0,
      default: 0,
      list: [],
      maskHide: false,
      newAddress: {
        name: "",
        province: "",
        city: "",
        district: "",
        address: "",
        postCode: "",
        phone: "",
        addressId: "",
        isDefault: "",
      }
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
                _this.feedback.status = true;
                _this.order.money = _this.feedback.list[i].limitation;
                break;
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
    getUserAddress: function(){
      var _this = this;
      var userAddressRequest = ajax({
        method: 'get',
        url: apiUrl + "/user/" + localId + "/shopping_address?token=" + localToken,
      }).then(function (response, xhr) {
        _this.address.connect = true;
        if(!response.result){
          _this.address.status = false;
          return;
        }
        _this.address.status = true;
        _this.address.list = response.data;
        for(var i = 0; i < _this.address.list.length; i++){
          if(_this.address.list[i].isDefault){
            _this.address.default = i;
            _this.address.selected = i;
          }
        }
      }).catch(function (response, xhr) {
        _this.address.connect = false;
        console.log("地址加载失败");
      }).always(function (response, xhr) {
      });
    },
    addUserAddress: function(){
      var _this = this;
      var addr = this.address.newAddress;
      if(!addr.name || !addr.province || !addr.city || !addr.district || !addr.address || !addr.postCode || !addr.phone){
        return;
      }
      var addAddressRequest = ajax({
        method: 'post',
        url: apiUrl + "/user/" + localId + "/shopping_address",
        data: {
          token: localToken,
          name: addr.name,
          province: addr.province,
          city: addr.city,
          district: addr.district,
          address: addr.address,
          phone: addr.phone,
          postCode: addr.postCode
        }
      }).then(function (response, xhr) {
        if(!response.result){
          console.log("地址添加失败");
          return;
        }
        addr.addressId = response.data;
        addr.isDefault = 0;
        _this.address.list.push({token: localToken,name: addr.name,province: addr.province,city: addr.city,district: addr.district,address: addr.address,phone: addr.phone,postCode: addr.postCode,addressId: addr.addressId,isDefault: addr.isDefault});
        for(var key in addr){
          addr[key] = "";
        }
      }).catch(function (response, xhr) {
        _this.address.connect = false;
        console.log("地址添加失败");
      }).always(function (response, xhr) {
      });
    },
    removeAddressMask: function(){
      this.address.maskHide = true;
    },
    setDefaultUserAddress: function(index){
      var _this = this;
      if(index == this.address.default){
        return;
      }
      var setDefaultAddressRequest = ajax({
        method: 'post',
        url: apiUrl + "/user/" + localId + "/shopping_address/default",
        data: {
          token: localToken,
          addressId: _this.address.list[index].addressId
        }
      }).then(function (response, xhr) {
        if(!response.result){
          console.log("默认地址设置失败");
          return;
        }
        _this.address.default = index;
      }).catch(function (response, xhr) {
        console.log("默认地址连接错误");
      }).always(function (response, xhr) {
      });
    },
    selectUserAddress: function(index){
      this.address.selected = index;
    }
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
    this.getFeedback();
    this.getUserAddress();
  }
});


var orderTab = new FFtab($('.kit-progress-tab'),$('.order-progress'),{
  callback: function(index, tab1, content1, tab2, content2){
    tab2.classList.add("FFtab-visited");
    initiationVm.step = index + 1;
  },
  click: false
});

