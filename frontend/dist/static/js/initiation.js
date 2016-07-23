// ;(function(){

  var cropper = null;

  function addElementEvent(){
    $("#step-prev").click(function(event) {
      event.preventDefault();
      progressTab.prev(false);
    });
    $("#step-next").click(function(event) {
      event.preventDefault();
      progressTab.next(false);
    });
    $("#upload-cover-btn").click(function(event) {
      event.preventDefault();
      $("#upload-cover-input").click();
    });
    $("#upload-cover-input").change(function (e) {
      cropCover();
    });
  }
  function cropCover(){
    var coverFile = $("#upload-cover-input")[0].files[0];
    if(coverFile.type.indexOf("image") < 0){
      throw new Error("图片格式错误");
    }
    var coverImg = window.URL.createObjectURL(coverFile);
    if(!cropper){
      cropper = new Cropper($("#upload-cover-img")[0], {
        viewMode: 2,
        aspectRatio: 16 / 9,
      });
    }
    cropper.reset().replace(coverImg);
  }

  var localCategoryId = 1;
  function uploadProject(){

    var projectTitle = $("#progress-title-input").val();
    var projectEndTime = projectTimePicker.getDate().getFullYear() + "-" + (projectTimePicker.getDate().getMonth() + 1) + "-" + projectTimePicker.getDate().getDate();
    var projectContent = editor.getValue();
    var projectMoney = $("#support-money").val();
    cropper.getCroppedCanvas({width:250, height: 250}).toBlob(function (blob){
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
          var uploadProjectRequest = ajax({
            method: 'post',
            url: apiUrl +"/project/" + localCategoryId,
            data: {
              token: localToken,
              name: projectTitle,
              targetDeadline: projectEndTime,
              targetMoney: projectMoney,
              description: projectContent.substr(0,15),
              sponsor: localId,
              cover: response.data[0],
              content: projectContent
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
  var projectTimePicker = rome($("#time-end")[0], {time: false });
  var progressTab = new FFtab($('.initiation-progressbar')[0],$('.initiation-progress')[0],{callback: function(tab1, content1, tab2, content2){
    tab2.classList.add("FFtab-visited");
  }});
  addElementEvent();
// }());
