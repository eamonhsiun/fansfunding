// ;(function(){
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

    var avatarForm = new FormData();
    avatarForm.append("avatar", avatarFile);

    uploadData.dirty = true;
  }

  function uploadNikcname(){
    var newNikcname = $("#upload-nickname-input").value;

  }

  addElementEvent();
// })();

