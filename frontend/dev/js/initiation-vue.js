var projectTimePicker = rome($("#time-end")[0], {
  time: false,
  min: new Date()
});
projectTimePicker.on('data', function(value) {
  initiationVm.project.endTime = value;
});
var progressTab = new FFtab($('.initiation-progressbar')[0],$('.initiation-progress')[0],{
  callback: function(index, tab1, content1, tab2, content2){
    tab2.classList.add("FFtab-visited");
}});
var editor = new Simditor({
  textarea: $('#editor'),
  toolbar: [
    'title',
    'bold',
    'italic',
    'underline',
    'fontScale',
    'color',
    '|',
    'ol',
    'ul',
    'table',
    'hr',
    '|',
    'link',
    'image',
    'alignment',
  ],
  upload: {
    url: '',
    params: null,
    fileKey: 'upload_file',
    connectionCount: 3,
    leaveConfirm: 'Uploading is in progress, are you sure to leave this page?'
  }
  //optional options
});
editor.on('valuechanged', function(e, src){
  initiationVm.project.content = this.getValue();
});
var cropper = null;


var localCategoryId = 1;

var initiationVm = new Vue({
  el: "#initiation",
  data: {
    status: false,
    userInfo: {},
    step: 1,
    totalStep: 4,
    errormsg: [],
    project: {
      title: "",
      endTime: null,
      coverImg: null,
      money: "",
      intro: "",
      content: "",
    }
  },
  watch: {

  },
  methods: {
    redirect: function(){
      window.location.href = "login.html";
    },
    selectCover: function(){
      $("#upload-cover-input").click();
    },
    cropCover: function(){
      var coverFile = $("#upload-cover-input")[0].files[0];
      if(coverFile.type.indexOf("image") < 0){
        throw new Error("图片格式错误");
      }
      var coverImg = window.URL.createObjectURL(coverFile);
      if(!cropper){
        cropper = new Cropper($("#upload-cover-img")[0], {
          viewMode: 2,
          aspectRatio: 16 / 9,
          preview: "#upload-cover-preview"
        });
        $("#upload-cover-img")[0].addEventListener('crop', function (e) {
          var data = cropper.getData();
        });
      }
      cropper.reset().replace(coverImg);
      this.project.coverImg = coverImg;
    },
    validateProgress: function(page){
      this.errormsg.length = 0;
      switch(page){
      case 1:
        if(!this.project.title){
          this.errormsg.push("未输入标题");
        }
        if(!this.project.endTime){
          this.errormsg.push("未选择截止日期");
        }
        if(!this.project.money){
          this.errormsg.push("未输入筹款金额");
        }
        if(!this.project.intro){
          this.errormsg.push("未输入项目简介");
        }
        if(!this.project.coverImg){
          this.errormsg.push("未选择封面图片");
        }
        if(this.errormsg.length !== 0){
          progressTab.goto(0);
          return false;
        }
        break;
      case 2:
        if(!this.project.content){
          this.errormsg.push("未输入项目详情");
        }
        if(this.errormsg.length !== 0){
          progressTab.goto(1);
          return false;
        }
        break;
      }
      return true;
    },
    initializeProject: function(){
      for(var i = 1; i < this.totalStep; i++){
        if(!this.validateProgress(i)){
          return;
        }
      }
      var _this = this;
      cropper.getCroppedCanvas({width:700, height: 450}).toBlob(function (blob){
        var imageForm = new FormData();
        imageForm.append("files", blob);
        var imageRequest = ajax({
          method: 'post',
          url: apiUrl +"/project/" + localCategoryId + "/images",
          headers: {
            'content-type': null
          },
          data: imageForm
        }).then(function (response, xhr) {
          if(!response.result){
            console.log(response.errCode);
            return;
          }else{
            console.log(response);
            var initializeProjectRequest = ajax({
              method: 'post',
              url: apiUrl +"/project/" + localCategoryId,
              data: {
                token: localToken,
                name: _this.project.title,
                targetDeadline: _this.project.endTime,
                targetMoney: _this.project.money,
                description: _this.project.intro,
                sponsor: localId,
                cover: response.data[0],
                content: _this.project.content
              }
            }).then(function (response, xhr) {
              if(!response.result){
                alert("项目发起失败")
              }else{
                alert("项目发起成功")
              }
            }).catch(function (response, xhr) {
              alert("发起项目连接服务器失败");
            }).always(function (response, xhr) {
              // Do something
            });
          }
        }).catch(function (response, xhr) {
          alert('连接服务器失败');
        }).always(function (response, xhr) {
          // Do something
        });
      });
    }
  },
  ready: function(){
    var _this = this;
    FFaccount.getAccountStatus(function(status){
      if(status === true){
        _this.status = true;
        _this.userInfo = localUserInfo;
      }else{
        _this.status = false;
        this.redirect();
      }
    });
  }
})
