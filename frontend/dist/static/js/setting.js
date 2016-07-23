;(function(){
  var $ = function (i) { return document.querySelector(i); };
  var $$ = function (i) { return document.querySelectorAll(i); };

  var uploadData= {
    dirty: false,
    img: null,
    nickname: ""
  };

  function addElementEvent (){
    $("#setting-profile-submit-btn").addEventListener("click", function(e){
      e.preventDefault();
      uploadNickname();
    });
  }

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
      alert('修改昵称成功');

    }).catch(function (response, xhr) {
      alert('test');
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function redirect () {
    window.location.href = "login.html";
  }

/**
 * 改变登陆状态缩影响的dom
 * @return {[type]} [description]
 */
  function changeUserStatus(){
    FFaccount.getAccountStatus(function(status){
      if(status === true){
        $('#new-avatar').src = resourceUrl + localUserInfo.head;
        $('#upload-nickname-input').value = localUserInfo.nickname;
      }else{
        redirect();
      }
    });
  }

  addElementEvent();
  changeUserStatus();
})();

