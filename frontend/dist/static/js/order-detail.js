var orderDetailVm = new Vue({
  el: "#order-detail",
  data: {
    status: false,
    orderNo: getQueryString("orderNo"),
    orderStatus: "",
    isSuccess: true,
    categoryId: 1,
    projectId: 0,
    projectName: "",
    feedbackId: 0,
    feedbackTitle: "",
    feedbackDesc: "",
    feedbackImages: [],
    paidTime: 0,
    totalFee: 0,
    tradeNo: "",
    address: {
      province: "",
      city: "",
      district: "",
      address: "",
      phone: "",
      name: "",
      postCode: 0,
    },
    picViewer: {
      status: false,
      src: ""
    }
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
            window.location.href = "project-vue.html?categoryId="+ this.categoryId + "&projectId="+this.projectId;
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
    getOrderDetail: function(){
      var _this = this;
      var orderDetailRequest = ajax({
        method: 'get',
        url: apiUrl + "/user/" + localId + "/orders/" + _this.orderNo + "?token=" + localToken,
      }).then(function (response, xhr) {
        if(!response.result){
          _this.redirect();
          return;
        }
        var data = response.data;
        for(var key in data){
          _this[key] = data[key];
        }
      }).catch(function (response, xhr) {
        if(xhr.status == 404 || xhr.status == 400){
          _this.redirect();
          return;
        }
        alert("服务器连接失败");
      }).always(function (response, xhr) {
      });
    },
    getOrderStatus: function(value){
      switch (value){
      case "TRADE_SUCCESS" :
        return "交易成功";
      default:
        return "查询中";
      }
    },
    viewPic: function(event){
      this.picViewer.src = event.target.src;
      this.picViewer.status = true;
    }
  },
  ready: function(){
    var _this = this;
    if(!this.orderNo){
      this.redirect();
    }
    FFaccount.getAccountStatus(function(status){
      if(status === true){
        _this.status = true;
        _this.getOrderDetail();
      }else{
        _this.status = false;
        _this.redirect("login");
      }
    });
  }
});
