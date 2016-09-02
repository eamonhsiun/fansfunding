var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

(function(){
  function getComment(){
    var token = getToken();
    var request = ajax({
      method: 'post',
      url: 'api.immortalfans.com',
      data: {
        user: 'john'
      }
    }).then(function (response, xhr) {

    }).catch(function (response, xhr) {
      // Do something
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function addComment(){
    var token = getToken();
    var comment = $("#feed-textarea").value;
    if(!comment){
      alert("未输入评论内容");
    }
    var request = ajax({
      method: 'post',
      url: 'api.immortalfans.com',
      data: {
        user: 'john'
      }
    }).then(function (response, xhr) {

    }).catch(function (response, xhr) {
      // Do something
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function createComment(comments){
    var i = 0;
    var ul = $("#project-feed").getElementsByTagName("ul")[0];
    for(i; i < comments.length; i++){
      var comment = comments[i];
      var li = document.createElement("li");
      li.classList.add("project-feed-card");
      li.innerHTML = '<div class="feed-card-wrap"><div class="feed-card-left"><div class="feed-card-avatar"><img src="' + comment.avatar + '" alt=""></div></div><div class="feed-card-right"><div class="feed-card-info"><div class="feed-card-info-username">'+ comment.username + '</div><div class="feed-card-info-type">' + comment.type + '</div></div><div class="feed-card-detail"><p>'+ comment.content + '</p></div></div></div>';
      ul.appendChild(li);
    }
  }

  function addElementEvent(){
    $("#feed-submit").addEventListener("click", function (e) {
      addComment();
    });
  }

})();
