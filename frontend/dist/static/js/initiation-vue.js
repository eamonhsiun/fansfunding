var localCategoryId = 1;

var initiationVm = new Vue({
  el: "#initiation",
  data: {
    status: false,
    userInfo: {},
    step: 1,
    totalStep: 4,
    error: false,
    errormsg: [],
    projectId: 0,
    categoryId: localCategoryId,
    project: {
      title: "",
      endTime: null,
      coverImg: null,
      money: "",
      intro: "",
      content: "",
    },
    feedbacks: {
      list: [{
        limitation: "",
        description: "",
        images:[],
        ceiling: -1,
        title: "",
        uploadCount: 0,
        uploadUrl: [],
      }],
    },
    request: {
      progress: 0,
      status: false,
    },
    response: {
      raw: {},
      connect: false,
      result: false,
    }
  },
  watch: {

  },
  methods: {
    redirect: function(target){
      if(!target){
        window.location.href = "login.html";
        return;
      }else{
        window.location.href = "project-vue.html?categoryId="+ this.categoryId +"&id=" + this.projectId;
      }
    },
    readBlobAsDataURL: function(blob, callback) {
      var a = new FileReader();
      a.onload = function(e) {callback(e.target.result);};
      a.readAsDataURL(blob);
    },
    dataURLtoBlob: function(dataurl) {
      var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
          bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
      while(n--){
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new Blob([u8arr], {type:mime});
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
          preview: "#upload-cover-preview",
          dragMode: 'move',
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
      this.error = false;
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
        break;
      case 2:
        if(!this.project.content){
          this.errormsg.push("未输入项目详情");
        }
        break;
      case 3:
        var feedbacks = this.feedbacks.list;
        if(feedbacks.length === 0){
          this.errormsg.push("未添加回报项");
        }else{
          for(var i = 0; i < feedbacks.length; i++){
            if(!feedbacks[i].limitation || !feedbacks[i].description){
              this.errormsg.push("第" + (i+1) + "个回报项内容没有写完");
            }
          }
        }
        break;
      }
      if(this.errormsg.length !== 0){
        this.error = true;
        progressTab.goto(page-1);
        return false;
      }
      return true;
    },
    prevStep: function(){
      if(this.step > 1){
        progressTab.goto(this.step - 2);
      }
    },
    nextStep: function(){
      if(this.step < this.totalStep){
        if(this.validateProgress(this.step)){
          progressTab.goto(this.step);
        }
      }
    },
    initializeProject: function(){
      this.request.progress = 0;
      for(var i = 1; i < this.totalStep; i++){
        if(!this.validateProgress(i)){
          return;
        }
      }
      var _this = this;
      this.request.progress = 10;
      cropper.getCroppedCanvas({width:700, height: 450}).toBlob(function (blob){
        var imageForm = new FormData();
        imageForm.append("files", blob);
        var imageRequest = ajax({
          method: 'post',
          url: apiUrl +"/project/" + _this.categoryId + "/images",
          headers: {
            'content-type': null
          },
          data: imageForm
        }).then(function (response, xhr) {
          if(!response.result){
            console.log(getErrorMsg(response.errCode));
            return;
          }else{
            _this.request.progress = 20;
            var initializeProjectRequest = ajax({
              method: 'post',
              url: apiUrl +"/project/" + _this.categoryId,
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
              _this.response.connect = true;
              _this.response.raw = response;
              if(!response.result){
                _this.response.result = false;
                console.log(getErrorMsg(response.errCode));
                return;
              }else{
                _this.request.progress = 40;
                _this.response.result = true;
                _this.projectId = response.data;
                _this.uploadFeedback();
              }
            }).catch(function (response, xhr) {
              _this.response.connect = false;
              _this.response.result = false;
              alert("发起项目连接服务器失败");
            }).always(function (response, xhr) {
              // Do something
            });
          }
        }).catch(function (response, xhr) {
          _this.response.connect = false;
        }).always(function (response, xhr) {
          // Do something
        });
      });
    },
    uploadFeedbackImage: function(index, callback){
      var _this = this;
      var feedbacks = this.feedbacks.list;
      var feedback = feedbacks[index];
      var images = feedback.images;
      if(images.length === 0){
        callback("");
        return;
      }
      for(var i = 0; i < images.length; i++){
        var imageForm = new FormData();
        imageForm.append("files", _this.dataURLtoBlob(images[i]));
        var feedbackImageRequest = ajax({
          method: 'post',
          url: apiUrl +"/project/" + _this.categoryId + "/" + _this.projectId + "/feedback/images",
          headers: {
            'content-type': null
          },
          data: imageForm
        }).then(function (response, xhr) {
          if(!response.result){
            console.log(getErrorMsg(response.errCode));
            return;
          }else{
            feedback.uploadCount--;
            feedback.uploadUrl.push(response.data[0]);
            if(feedback.uploadCount <= 0){
              callback(feedback.uploadUrl.join(","));
            }
            console.log("回馈" + i + " 图片上传成功");
          }
        }).catch(function (response, xhr) {

        }).always(function (response, xhr) {
          // Do something
        });
      }
    },
    uploadFeedback: function(index){
      var _this = this;
      var i = index || 0;
      var feedbacks = this.feedbacks.list;
      var amount = feedbacks.length;
      if(i >= amount){
        console.log("回馈上传完成");
        alert("项目发起完成");
        redirect(true);
        return false;
      }else{
        this.request.progress += (60 / _this.feedbacks.list.length);
        this.feedbacks.list[i].uploadUrl = [];
        this.uploadFeedbackImage( i ,function(url){
          console.log("回馈" + i + "开始上传");
          var data = {
            token: localToken,
            title: _this.feedbacks.list[i].title,
            ceiling: _this.feedbacks.list[i].ceiling,
            description: _this.feedbacks.list[i].description,
            limitation: _this.feedbacks.list[i].limitation,
          }
          if(url){
            data.images = url;
          }
          var feedbackRequest = ajax({
            method: 'post',
            url: apiUrl +"/project/" + _this.categoryId + "/" + _this.projectId + "/feedbacks",
            data: data
          }).then(function (response, xhr) {
            if(!response.result){
              console.log(getErrorMsg(response.errCode));
              return;
            }else{
              console.log("回馈" + i + "上传成功");
              _this.uploadFeedback(i + 1);
            }
          }).catch(function (response, xhr) {
            alert("上传回馈" + i + "失败");
          }).always(function (response, xhr) {
            // Do something
          });
        });
      }
    },
    addFeedback: function(){
      var feedback = {
        limitation: "",
        description: "",
        images: [],
        uploadCount: 0,
        uploadUrl: [],
      }
      this.feedbacks.list.push(feedback);
    },
    removeFeedback: function(index){
      if(this.feedbacks.list.length<=1){
        alert("必须保留一个回报项");
        return;
      }
      if(confirm("确定要删除此回报项吗？")){
        this.feedbacks.list.splice(index, 1);
      }
    },
    loadFeedbackImage: function(index, event){
      var _this = this;
      var file = event.target.files[0];
      if(file.type.indexOf("image") < 0){
        alert("图片格式错误");
        return;
      }
      this.readBlobAsDataURL(file, function(dataurl){
        _this.feedbacks.list[index].images.push(dataurl);
        _this.feedbacks.list[index].uploadCount = _this.feedbacks.list[index].images.length;
        _this.feedbacks.list[index].uploadUrl = [];
      })
    },
    deleteFeedbackImage: function(index, imgIndex){
      this.feedbacks.list[index].images.splice(imgIndex, 1);
      this.feedbacks.list[index].uploadCount = this.feedbacks.list[index].images.length;
    },
  },
  ready: function(){
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
  }
})



var projectTimePicker = rome($("#time-end")[0], {
  time: false,
  min: new Date()
});
projectTimePicker.on('data', function(value) {
  initiationVm.project.endTime = value;
});
var progressTab = new FFtab($('.kit-progress-tab')[0],$('.initiation-progress')[0],{
  callback: function(index, tab1, content1, tab2, content2){
    tab2.classList.add("FFtab-visited");
    initiationVm.step = index + 1;
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
    url: apiUrl +"/project/" + initiationVm.categoryId + "/images",
    params: null,
    fileKey: 'files',
    connectionCount: 3,
    leaveConfirm: 'Uploading is in progress, are you sure to leave this page?'
  },
  toolbarFloat: true,
  toolbarFloatOffset: 70,
  pasteImage: true
  //optional options
});
editor.on('valuechanged', function(e, src){
  initiationVm.project.content = this.getValue();
});
var cropper = null;
window.onbeforeunload = function(e){
  if(!confirm("确认要离开吗")){
    return false;
  }
  return true;
}
