
var bannerMain = new Swiper ('#banner-main', {
  // Optional parameters
  direction: 'vertical',
  loop: true,
  // Navigation arrows
  // nextButton: '#swiper-button-next',
  // prevButton: '#swiper-button-prev',
  speed: 500,
  autoplay: 3000,
  autoplayDisableOnInteraction: false,
});

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

var defaultProjectCategory = 1;

function getProjects(page){
  plateLoader.init();
  var projectsRequest = ajax({
    method: 'get',
    url: apiUrl +"/project/" + defaultProjectCategory + (page ? "?page=" + page : ""),
  }).then(function (response, xhr) {
    if(!response.result){
      plateLoader.endLoad(false, "获取失败");
    }else{
      var plate = document.getElementById("plate-1");
      var projects = response.data.list;
      plateLoader.endLoad();
      for(var i = 0; i < projects.length; i++){
        addProject(plate, projects[i]);
      }
      var tempPage = response.data.pageNum + 1;
      if(tempPage <= response.data.pages){
        getProjects(tempPage);
      }
    }
  }).catch(function (response, xhr) {
    plateLoader.endLoad(false, "连接服务器失败");
  }).always(function (response, xhr) {
    // Do something
  });
}

function addProject(wrap, data){
  var createTime = data.createTime;
  var targetDeadline = data.targetDeadline;
  var li = '<li class="plate-content-list">' +
'            <a href="project.html?categoryId=' + data.categoryId + '&id='+ data.id +'" class="project-card">' +
'              <div class="project-card-pic">' +
'                <img src="'+ resourceUrl + data.cover+'"alt="">' +
'              </div>' +
'              <div class="project-card-detail">' +
'                <div class="project-card-detail-name">'+data.name+'</div>' +
'                <div class="project-card-detail-hint">' +
'                  '+data.description+
'                </div>' +
'                <div class="project-card-detail-other">' +
'                  <div class="project-card-detail-info project-card-lefttime">' +
'                    剩余时间：<span>'+ getLeftTime(new Date(), data.targetDeadline) +'</span>' +
'                  </div>' +
'                  <div class="project-card-detail-info project-card-target">' +
'                    目标金额：<span>' + data.targetMoney + '</span>' +
'                  </div>' +
'                  <div class="project-card-detail-info project-card-progress">' +
'                    已达成：<span>' + (data.sum/data.targetMoney).toFixed(2) +'</span>%' +
'                  </div>' +
'                </div>' +
'              </div>' +
'              <div class="project-card-progressbar"><span style="width:'+ (data.sum/data.targetMoney) +'%;"></span></div>' +
'              <div class="project-card-initiator">' +
'                <div class="initiator-avatar"><img src="'+ resourceUrl + data.sponsorHead +'" alt=""></div>' +
'                <div class="initiator-info">' +
'                  <div class="initiator-name">发起人：<span>' + data.sponsorNickname +'</span></div>' +
'                  <div class="initiator-intro">发起头衔是什发起头衔是什么好口怕发起头衔是什么好口发起头衔是什么好口怕发起头怕发起头衔是什么好口怕么好口怕</div>' +
'                </div>' +
'              </div>' +
'            </a>' +
'          </li>';
  wrap.innerHTML += li;
}

var plateLoader = new FFloader(document.getElementsByClassName("plate-content")[0]);
getProjects();


