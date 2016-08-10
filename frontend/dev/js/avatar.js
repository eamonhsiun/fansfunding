;(function(){
  var $ = function (i) { return document.querySelector(i); };
  var $$ = function (i) { return document.querySelectorAll(i); };

  var cropper = null;

  function addElementEvent (){
    $("#upload-avatar-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      $("#upload-avatar-input").click();
    });
    $("#upload-avatar-input").addEventListener("change", function  (e) {
      cropAvatar();
    });
    $("#upload-avatar-submit").addEventListener("click", function  (e) {
      e.preventDefault();
      uploadAvatar();
    });
  }
  // var date = new FormData($("from[name='form1']")[0]);

  function cropAvatar(){
    var avatarFile = $("#upload-avatar-input").files[0];
    if(avatarFile.type.indexOf("image") < 0){
      $("#upload-avatar-alert").innerHTML = "图片格式错误";
      $("#upload-avatar-alert").style.display = "block";
      return;
    }
    $("#upload-avatar-alert").style.display = "none";
    var avatarImg = window.URL.createObjectURL(avatarFile);
    if(!cropper){
      $(".cropper-wrap").style.display = "block";
      cropper = new Cropper($("#new-avatar"), {
        viewMode: 3,
        aspectRatio: 1 / 1,
        dragMode: 'move',
        zoomable: false,
        preview: ".avatar-preview"
      });
    }
    // $("#new-avatar").src= avatarImg;
    cropper.reset().replace(avatarImg);
  }

  function uploadAvatar (){
    if(!cropper){
      $("#upload-avatar-alert").innerHTML = "请先上传图片";
      $("#upload-avatar-alert").style.display = "block";
      return;
    }
    $("#upload-avatar-alert").style.display = "none";
    cropper.getCroppedCanvas({width:250, height: 250}).toBlob(function (blob){
      avatarForm = new FormData();
      avatarForm.append("file", blob);
      // avatarForm.append("token", localToken);
      var uploadAvatarRequest = ajax({
        method: 'post',
        url: apiUrl + '/userbasic/' + localId + '/head',
        headers: {
          'content-type': null
        },
        data: avatarForm,
      }).then(function (response, xhr) {
        if(!response.result){
          $("#upload-avatar-alert").innerHTML = errCode;
          $("#upload-avatar-alert").style.display = "block";
          return;
        }
        $("#upload-avatar-alert").innerHTML = "图片上传成功";
        $("#upload-avatar-alert").style.display = "block";
        window.location.reload();
      }).catch(function (response, xhr) {
        $("#upload-avatar-alert").innerHTML = "服务器连接失败";
        $("#upload-avatar-alert").style.display = "block";
      }).always(function (response, xhr) {

      });
    });
  }
  addElementEvent();
  FFaccount.getAccountStatus(function(status){
    if(status === true){
      $("#avatar-preview-large img").src= resourceUrl + localUserInfo.head;
      $("#avatar-preview-middle img").src= resourceUrl + localUserInfo.head;
      $("#avatar-preview-small img").src= resourceUrl + localUserInfo.head;
    }
  });
}());
