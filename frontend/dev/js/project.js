var localCategoryId = null;
var localProjectId = null;
;(function(){
  var $ = function (i) { return document.querySelector(i); };
  var $$ = function (i) { return document.querySelectorAll(i); };

  function addElementEvent () {
    $("#comment-submit").addEventListener("click", function(e){
      sendComment();
    });
    $("#project-comment-btn").addEventListener("click", function(e){
      getComments(localCategoryId, localProjectId);
    });
    $("#comment-textarea").addEventListener("input", function(e){
      var num = 128 - getCharactorNum(e.target.value);
      if(num >= 0){
        $("#comment-hint").innerHTML = "剩余" + num + "个字";
        return;
      }
      $("#comment-hint").innerHTML = "超出" + -num + "个字";
    });
  }

  function getProject(categoryId, projectId){
    var projectsRequest = ajax({
      method: 'get',
      url: apiUrl +"/project/" + categoryId + "/" + projectId,
    }).then(function (response, xhr) {
      if(!response.result){
        redirect();
        return;
      }else{
        fillProject(response.data);
      }
    }).catch(function (response, xhr) {
      alert("连接服务器失败");
    });
  }

  function getProjectDetail(categoryId, projectId){
    detailLoader.init();
    var projectsRequest = ajax({
      method: 'get',
      url: apiUrl +"/project/" + categoryId + "/" + projectId + "/detail",
    }).then(function (response, xhr) {
      if(!response.result){
        detailLoader.endLoad(false, "获取信息失败");
      }else{
        detailLoader.endLoad();
        fillProjectDetail(response.data);
      }
    }).catch(function (response, xhr) {
      detailLoader.endLoad(false, "服务器连接失败");
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function fillProject(data){
    $('.project-title').innerHTML = encodeHTML(data.name);
    $('.intro-show').getElementsByTagName('img')[0].src = resourceUrl + data.cover;
    $('#target-money').getElementsByTagName('span')[0].innerHTML = encodeHTML(data.targetMoney);
    $('#parcent').getElementsByTagName('span')[0].innerHTML = encodeHTML((data.sum/data.targetMoney).toFixed(2));
    $('#lefttime').getElementsByTagName('span')[0].innerHTML = getLeftTime(new Date(), data.targetDeadline);
    $('.initiator-name').getElementsByTagName('span')[0].innerHTML = encodeHTML(data.sponsorNickname);
    $('.initiator-avatar').getElementsByTagName('img')[0].src= resourceUrl + data.sponsorHead;
  }

  function fillProjectDetail(data){
    $('#project-detail-wrap').innerHTML = data.content;
  }

  function getFeedbacks(categoryId, projectId, page){
    var fedbacksRequest = ajax({
      method: 'get',
      url: apiUrl +"/project/" + categoryId + "/" + projectId + "/feedbacks",
    }).then(function (response, xhr) {
      if(!response.result){
        repayLoader.endLoad(false,"获取回馈失败");
      }else{
        var repaywrap = $('.project-repay ul');
        var feedback = response.data.list;
        for(var i = 0; i < feedback.length; i++){
          fillFeedbacks(repaywrap, feedback[i]);
        }
        if(feedback.length === 0){
          repayLoader.endLoad(false,"没有相关回报数据");
        }else{
          repayLoader.endLoad();
        }
      }
    }).catch(function (response, xhr) {
      repayLoader.endLoad(false, "服务器连接失败");
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function fillFeedbacks(wrap, data){
    var li = document.createElement("li");
    li.classList.add("repay-list");
    var feedback_1 =
'            <div class="repay-money">支持<span>' + encodeHTML(data.limitation) + '</span>元</div>' +
// '            <div class="repay-num">已支持<span>200</span>人</div>' +
'            <div class="repay-detail">' +
'              <div class="repay-detail-content">' +
        encodeHTML(data.description) +
'              </div>' +
'              <div class="repay-detail-pics">' ;
    var feedback_2 = "";
    for(var i = 0; i < data.images.length; i++){
      feedback_2 += '                <div class="repay-detail-pic">' +
'                  <img src="'+ resourceUrl + data.images[i] + '" alt="">' +
'                </div>';
    }
    var feedback_3 = '              </div>' +
'            </div>' +
'            <div class="repay-btn">前往支持</div>';
    li.innerHTML = feedback_1 + feedback_2 + feedback_3;
    wrap.appendChild(li);
  }

  function getComments(categoryId, projectId, page){
    commentLoader.init();
    $('#project-comment ul').innerHTML = "";
    var commentsRequest = ajax({
      method: 'get',
      url: apiUrl +"/project/" + categoryId + "/" + projectId + "/comments",
    }).then(function (response, xhr) {
      if(!response.result){
        commentLoader.endLoad(false, "获取评论失败");
      }else{
        var commentwrap = $('#project-comment ul');
        var comment = response.data.list;
        for(var i = 0; i < comment.length; i++){
          fillComments(commentwrap, comment[i]);
        }
        if(comment.length !== 0){
          commentLoader.endLoad();
        }else{
          commentLoader.endLoad(false, "还没有评论，快来评论吧");
        }
      }
    }).catch(function (response, xhr) {
      commentLoader.endLoad(false, "连接服务器失败");
    }).always(function (response, xhr) {
      // Do something
    });
  }

  function fillComments(wrap, data, order){
    var li = document.createElement("li");
    li.classList.add("comment-wrap");
    var comment =
'                <div class="comment-userinfo">' +
'                  <div class="comment-avatar">' +
'                    <img src="'+ resourceUrl + data.commenterHead +'" alt="">' +
'                  </div>' +
'                  <div class="comment-userinfo-name">' + encodeHTML(data.commenterNickname) + '</div>' +
'                  <div class="comment-unerinfo-intro">口发起头衔是什么好口怕发怕么好口怕</div>' +
'                </div>' +
'                <div class="comment-content">' +
'                  <div class="comment-inner">' +
(data.pointTo === 0 ? "" : ("回复" + data.pointToNickname + "："))  + encodeHTML(data.content) +
'                  </div>' +
'                  <div class="comment-more">' +
'                    <div class="comment-time">' + getDateString(data.commentTime) + '</div>' +
'                    <div class="comment-btns">' +
'                      <div class="comment-btn comment-like">123</div>' +
'                      <div class="comment-btn comment-reply">回复</div>' +
'                    </div>' +
'                  </div>' +
'                </div>';
    li.innerHTML = comment;
    if(order === true){
      wrap.insertBefore(li, wrap.childNodes[0]);
    }else{
      wrap.appendChild(li);
    }
    li.getElementsByClassName("comment-reply")[0].addEventListener("click", function(id, nickname){
      return function(e){
        e.preventDefault();
        var textarea = $("#comment-textarea");
        textarea.setAttribute("placeholder", "回复" + nickname + "：");
        textarea.setAttribute("comment-reply-to", id);
        textarea.setAttribute("comment-reply-nickname", nickname);
        textarea.focus();
      }
    }(data.commenterId, data.commenterNickname));
  }

  function sendComment(){
    var comment = $('#comment-textarea').value;
    if(getCharactorNum(comment) > 128) {
      return;
    }
    var pointTo = $('#comment-textarea').getAttribute("comment-reply-to") || 0;
    var pointToNickname = $("#comment-textarea").getAttribute("comment-reply-nickname") || "";
    if(getCharactorNum(comment)<=0){
      return;
    }
    commentLoader.init();
    var commentRequest = ajax({
      method: 'post',
      url: apiUrl +"/project/" + localCategoryId + "/" + localProjectId + "/comments",
      data: {
        token: localToken,
        userId: localId,
        content: comment,
        pointTo: pointTo,
        categoryId: localCategoryId,
        projectId: localProjectId
      }
    }).then(function (response, xhr) {
      if(!response.result){
        commentLoader.endLoad(false, "评论失败");
      }else{
        var commentwrap = $('#project-comment ul');
        var data = {
          commenterHead: localUserInfo.head,
          pointTo: pointTo,
          pointToNickname: pointToNickname,
          commenterName: localUserInfo.name,
          commenterId: localId,
          commenterNickname: localUserInfo.nickname,
          projectId: localProjectId,
          commentTime: (new Date()).getTime(),
          content: comment,
        }
        fillComments(commentwrap, data, true);
        $('#comment-textarea').value = "";
        $("#comment-hint").innerHTML = "剩余128个字";
        $('#comment-textarea').removeAttribute("comment-reply-to");
        $("#comment-textarea").removeAttribute("comment-reply-nickname");
        $("#comment-textarea").removeAttribute("placeholder");
        commentLoader.endLoad();
      }
    }).catch(function (response, xhr) {
      commentLoader.endLoad(false, "评论失败");
    }).always(function (response, xhr) {
      // Do something
    });
  }

/**
 * 获取时间信息
 * @param  {int} time 时间戳
 * @return {stirng}      时间描述词
 */
  function getDateString(time){
    var date;
    if(time instanceof Date){
      date = time;
    }else{
      date = new Date(time);
    }
    var nowDate = new Date();
    var intervalDate = nowDate.getTime() - date.getTime();
    var day = Math.floor(intervalDate/(24*3600*1000));
    if(day > 0){
      return (date.getMonth() + 1) + "月" + date.getDate() + "日";
    }else{
      var hour = Math.floor((intervalDate%(24*3600*1000))/(3600*1000));
      if(hour > 0){
        return hour + "小时前";
      }else{
        var minutes = Math.floor(((intervalDate%(24*3600*1000))%(3600*1000))/(60*1000));
        if(minutes < 1){
          return "刚刚";
        }else{
          return minutes + "分钟前";
        }
      }
    }
  }

/**
 * 获取时间差
 * @param  {int} startTime 开始时间戳
 * @param  {int} endTime   结束时间戳
 * @return {string}           时间差
 */
  function getLeftTime(startTime, endTime){
    var d1;
    if(startTime instanceof Date){
      d1 = startTime;
    }else{
      d1 = new Date(startTime);
    }
    var d2 = new Date(endTime);
    var d3 = d2.getTime() - d1.getTime();
    if(d3 <= 0 ){
      return "已结束";
    }
    var day = Math.floor(d3/(24*3600*1000));
    var hour = Math.floor((d3%(24*3600*1000))/(3600*1000));
    return (day === 0 ? "" : day + "天") + hour + "小时";
  }

/**
 * 改变登陆状态缩影响的dom
 * @return {[type]} [description]
 */
  function checkUserStatus(){
    FFaccount.getAccountStatus(function(status){
      if(status === true){
        $('.comment-login').classList.add("hide");
        $('.comment-text').classList.add("show");
        $('#comment-user-avatar img').src = resourceUrl + localUserInfo.head;
        $('#comment-user-nickname').innerHTML = localUserInfo.nickname;
      }
    });
  }

/**
 * 获取文本长度
 * @param  {string} string 文本
 * @return {int}        长度
 */
  function getCharactorNum(string){
    return string.length;
  }

  function encodeHTML(str) {
    if(typeof(str) !== "string") return str;
    var s = "";
    if (str.length === 0) return "";
    s = str.replace(/&/g, "&gt;");
    s = s.replace(/</g, "&lt;");
    s = s.replace(/>/g, "&gt;");
    s = s.replace(/ /g, "&nbsp;");
    s = s.replace(/\'/g, "&#39;");
    s = s.replace(/\"/g, "&quot;");
    s = s.replace(/\n/g, "<br>");
    return s;
  }

  function redirect () {
    window.location.href = "404.html";
  }

  var detailLoader = new FFloader($("#project-detail"));
  var commentLoader = new FFloader($("#project-comment"));
  var repayLoader = new FFloader($("#project-repay"));

  function init(){
    var categoryId = getQueryString("categoryId");
    var projectId = getQueryString("id");
    if(!categoryId || !projectId){
      redirect();
      return;
    }
    localCategoryId = categoryId;
    localProjectId = projectId;
    checkUserStatus();
    getProject(categoryId, projectId);
    getProjectDetail(categoryId, projectId)
    getFeedbacks(categoryId, projectId);
  }
  init();
  addElementEvent();
  var projectTab = new FFtab($('.project-tabs'),$('.project-contents'));
})();
