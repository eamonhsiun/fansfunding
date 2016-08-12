var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };
var orderTab = new FFtab($('.kit-progress-tab'),$('.order-progress'),{
  click: false
});

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
      page: "",
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
            window.location.href = "project-vue.html?categoryId="+ this.categoryId + "&id="+this.projectId;
            return;
          case "order":

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
                _this.feedback.status = true;
                _this.order.money = _this.feedback.list[i].limitation;
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
      addressId.value = _this.address.list[_this.address.selected].addressId;
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
    if(this.orderNo){
      this.finishOrder();
    }else{
      this.getProject();
      this.getFeedback();
      this.getUserAddress();
    }
  }
});

