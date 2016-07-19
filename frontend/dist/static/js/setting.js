;(function(){
  var $ = function (i) { return document.querySelector(i); };
  var $$ = function (i) { return document.querySelectorAll(i); };

  var uploadData= {
    dirty: false,
    img: null,
    nickname: ""
  };

  function addElementEvent (){
    $("#upload-avatar-btn").addEventListener("click", function  (e) {
      e.preventDefault();
      $("#upload-avatar-input").click();
    });
    $("#upload-avatar-input").addEventListener("change", function  (e) {
      uploadAvatar();
    });
  }
  // var date = new FormData($("from[name='form1']")[0]);

  function uploadAvatar (){
    var avatarFile = $("#upload-avatar-input").files[0];
    if(avatarFile.type.indexOf("image") < 0){
      alert("图片格式错误");
      return;
    }
    var avatarImg = window.URL.createObjectURL(avatarFile);
    $("#new-avatar").src= avatarImg;

    avatarForm = new FormData();
    avatarForm.append("file", avatarFile);
    avatarForm.append("token", localToken);

    uploadData.dirty = true;

    var uploadAvatarRequest = ajax({
      method: 'post',
      url: apiUrl + '/user/' + localId + '/head',
      headers: {
        'content-type': null
      },
      data: avatarForm
    }).then(function (response, xhr) {
      if(!response.result){
        console.log(response.errCode);
        return;
      }
      console.log('修改头像成功');

    }).catch(function (response, xhr) {
      alert('test');
    }).always(function (response, xhr) {
      // Do something
    });
  }
a = uploadNickname;
  function uploadNickname(){
    var newNickname = $("#upload-nickname-input").value;

    if(newNickname !== localUserInfo.nickname){
      uploadData.dirty = true;
    }
    var uploadNicknameRequest = ajax({
      method: 'post',
      url: apiUrl + '/user/' + localId + '/nickname',
      data: {
        nickname: newNickname,
        token: localToken
      }
    }).then(function (response, xhr) {
      if(!response.result){
        return;
      }
      console.log('修改昵称成功');

    }).catch(function (response, xhr) {
      alert('test');
    }).always(function (response, xhr) {
      // Do something
    });
  }


  // function redirect () {
  //   if(localId && localToken){
  //     return;
  //   }else{
  //     window.location.href = "login.html?ref=setting.html";
  //   }
  // }

  // redirect();

  addElementEvent();
})();

