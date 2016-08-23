Vue.component('address-selector', {
  template: '#address-selector-template',
  /**
   * [props description]
   * type: "selector","editor"
   * selectedId: sync 选择的序号
   */
  props: ["type", "selectedid"],
  data: function(){
    return {
      status: false,
      connect: false,
      selected: -1,
      list: [],
      maskHide: false,
      editor: false,
      newAddress: {
        name: "",
        province: "",
        city: "",
        district: "",
        address: "",
        postCode: "",
        phone: "",
        addressId: "",
        isDefault: false,
        alter: {
          status: false,
          lastNum: -1,
          backup: {}
        },
        alert: {
          show: false,
          text: "修改成功"
        }
      }
    }
  },
  watch: {
    "selected": function(value){
      this.selectedid = this.list[value].addressId;
    }
  },
  methods: {
    activeAlert: function(target, text){
      if(text){
        this[target].alert.show = true;
        this[target].alert.text = text;
      }else{
        this[target].alert.show = false;
      }
    },
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
            if(_this.type != "editor" ){
              _this.selected = i;
            }
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
      if(this.validateAddress(addr)){
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
          if(_this.list.length === 0){
            addr.isDefault = true;
          }else{
            addr.isDefault = false;
          }
          _this.list.push({name: addr.name,province: addr.province,city: addr.city,district: addr.district,address: addr.address,phone: addr.phone,postCode: addr.postCode,addressId: addr.addressId,isDefault: addr.isDefault});
          _this.addAddressMask();
        }).catch(function (response, xhr) {
          _this.connect = false;
          console.log("地址添加失败");
        }).always(function (response, xhr) {
        });
      }
    },
    cancelAddUserAddress: function(){
      var alter = this.newAddress.alter;
      if(alter.status){
        this.list.splice(alter.lastNum, 0, alter.backup);
      }
      this.addAddressMask();
    },
    removeAddressMask: function(){
      this.maskHide = true;
      window.scrollTo(0, document.getElementById("address-add").offsetTop);
    },
    addAddressMask: function(){
      this.maskHide = false;
      this.newAddress = {
        name: "",
        province: "",
        city: "",
        district: "",
        address: "",
        postCode: "",
        phone: "",
        addressId: "",
        isDefault: "",
        alter: {
          status: false,
          last: -1,
          backup: {}
        },
        alert: {
          show: false,
          text: "修改成功"
        }
      };
    },
    setDefaultUserAddress: function(index){
      var _this = this;
      if(this.list[index].isDefault){
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
        for(var i = 0; i < _this.list.length; i++){
          _this.list[i].isDefault = false;
        }
        _this.list[index].isDefault = true;
      }).catch(function (response, xhr) {
        console.log("默认地址连接错误");
      }).always(function (response, xhr) {
      });
    },
    selectUserAddress: function(index){
      if(this.type != "editor" ){
        this.selected = index;
      }
    },
    alterUserAddress: function(index){
      var alter = this.newAddress
      if(alter.alter.status){
        this.list.splice(alter.alter.lastNum, 0, alter.alter.backup);
        if(index >= alter.alter.lastNum){
          index += 1;
        }
      }

      var temp = this.list[index];
      this.list.splice(index, 1);
      alter.alter.lastNum = index;
      alter.alter.status = true;
      alter.alter.backup = temp;
      for(var key in alter){
        if(temp[key]){
          alter[key] = temp[key];
        }
      }
      this.removeAddressMask();
    },
    submitAlterUserAddress: function(){
      var _this = this;
      var addr = this.newAddress;
      if(this.newAddress.alter.status && this.validateAddress(addr)){
        if(!addr.name || !addr.province || !addr.city || !addr.district || !addr.address || !addr.postCode || !addr.phone){
          return;
        }
        var submitAlterUserAddressRequest = ajax({
          method: 'post',
          url: apiUrl + "/user/" + localId + "/shopping_address/" + addr.addressId,
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
            console.log("地址修改失败");
            return;
          }
          _this.list.splice(addr.alter.lastNum, 0, {name: addr.name,province: addr.province,city: addr.city,district: addr.district,address: addr.address,phone: addr.phone,postCode: addr.postCode,addressId: addr.addressId,isDefault: addr.isDefault});
          _this.addAddressMask();
        }).catch(function (response, xhr) {
          console.log("地址修改失败");
        }).always(function (response, xhr) {
        });
      }
    },
    deleteUserAddress: function(index){
      if(confirm("确认删除这个地址吗？")){
        var _this = this;
        var deleteUserAddressRequest = ajax({
          method: 'post',
          url: apiUrl + "/user/" + localId + "/shopping_address/" + _this.list[index].addressId + "/delete",
          data: {
            token: localToken
          }
        }).then(function (response, xhr) {
          if(!response.result){
            console.log("地址删除失败");
            return;
          }
          if(_this.list[index].isDefault && _this.list.length>0){
            _this.list[0].isDefault = true;
          }
          _this.list.splice(index, 1);
          if(_this.selected == index){
            _this.selected = 0;
          }
        }).catch(function (response, xhr) {
          console.log("地址删除失败");
        }).always(function (response, xhr) {
        });
      }
    },
    validateAddress: function(data){
      var alert = this.newAddress.alert;
      if(!data.name || !data.province || !data.city || !data.district || !data.address || !data.postCode || !data.phone){
        this.activeAlert("newAddress", "信息填写不全")
        return false;
      }
      this.activeAlert("newAddress");
      return true;
    }
  },
  ready: function(){
    if(!this.type){
      this.type = "selector";
    }
    if(this.type == "editor"){
      this.editor = true;
    }
    if(this.type == "selector-editor"){
      this.editor =true;
    }
    this.getUserAddress();
  }
});
