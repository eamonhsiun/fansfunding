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
      alert("图片格式错误");
      return;
    }
    var avatarImg = window.URL.createObjectURL(avatarFile);
    if(!cropper){
      cropper = new Cropper($("#new-avatar"), {
        viewMode: 3,
        aspectRatio: 1 / 1,
        dragMode: 'move',
        zoomable: false
      });
    }
    // $("#new-avatar").src= avatarImg;
    cropper.reset().replace(avatarImg);
  }

  function uploadAvatar (){
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
        data: avatarForm
      }).then(function (response, xhr) {
        if(!response.result){
          console.log(response.errCode);
          return;
        }
        alert('修改头像成功');
        window.location.reload();
      }).catch(function (response, xhr) {
        alert('连接服务器失败');
      }).always(function (response, xhr) {
        // Do something
      });
    });
  }
  addElementEvent();
  FFaccount.getAccountStatus(function(status){
    if(status === true){
      $("#new-avatar").src= resourceUrl + localUserInfo.head;
    }
  });
}());
