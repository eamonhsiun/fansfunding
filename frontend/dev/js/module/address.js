var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

Vue.component('address-selector', {
  data: {
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
  template: '#address-selector',
  props: ["address"],
  methods: {
    getUserAddress: function(){
      var _this = this;
      var userAddressRequest = ajax({
        method: 'get',
        url: apiUrl + "/user/" + localId + "/shopping_address?token=" + localToken,
      }).then(function (response, xhr) {
        _this.connect = true;
        if(!response.result){
          _this.status = false;
          return;
        }
        _this.status = true;
        _this.list = response.data;
        for(var i = 0; i < _this.list.length; i++){
          if(_this.list[i].isDefault){
            _this.default = i;
            _this.selected = i;
          }
        }
      }).catch(function (response, xhr) {
        _this.connect = false;
        console.log("地址加载失败");
      }).always(function (response, xhr) {
      });
    },
    addUserAddress: function(){
      var _this = this;
      var addr = this.newAddress;
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
        _this.list.push({token: localToken,name: addr.name,province: addr.province,city: addr.city,district: addr.district,address: addr.address,phone: addr.phone,postCode: addr.postCode,addressId: addr.addressId,isDefault: addr.isDefault});
        for(var key in addr){
          addr[key] = "";
        }
      }).catch(function (response, xhr) {
        _this.connect = false;
        console.log("地址添加失败");
      }).always(function (response, xhr) {
      });
    },
    removeAddressMask: function(){
      this.maskHide = true;
    },
    setDefaultUserAddress: function(index){
      var _this = this;
      if(index == this.default){
        return;
      }
      var setDefaultAddressRequest = ajax({
        method: 'post',
        url: apiUrl + "/user/" + localId + "/shopping_address/default",
        data: {
          token: localToken,
          addressId: _this.list[index].addressId
        }
      }).then(function (response, xhr) {
        if(!response.result){
          console.log("默认地址设置失败");
          return;
        }
        _this.default = index;
      }).catch(function (response, xhr) {
        console.log("默认地址连接错误");
      }).always(function (response, xhr) {
      });
    },
    selectUserAddress: function(index){
      this.selected = index;
    },
  }
});
