
var bannerMain = new Swiper ('#banner-main', {
  // loop: true,
  // nextButton: '#swiper-button-next',
  // prevButton: '#swiper-button-prev',
  speed: 500,
  // autoplay: 3000,
  // autoplayDisableOnInteraction: false,
});

var defaultProjectCategory = 1;

var plateLoader = new FFloader(document.getElementsByClassName("plate-content")[0]);

Vue.filter("resource" ,function(value) {
  if(!value){
    return "";
  }
  return resourceUrl + value;
});

var indexVm = new Vue({
  el: "#index",
  data: {
    status: false, //登陆状态
    hotProjects: {
      status: false,
      connect: false,
      list: [],
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
    }
  },
  methods: {
    getLeftTime: function(startTime, endTime){
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
    },
    getProjects: function(page){
      var _this = this;
      plateLoader.init();
      var projectsRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + defaultProjectCategory + "?rows=9" + (page ? "&page=" + page : ""),
      }).then(function (response, xhr) {
        _this.hotProjects.connect = true;
        if(!response.result){
          plateLoader.endLoad(false, "获取失败");
        }else{
          _this.hotProjects.status = true;
          _this.hotProjects.list = response.data.list;
          plateLoader.endLoad();
        }
      }).catch(function (response, xhr) {
        plateLoader.endLoad(false, "连接服务器失败");
      }).always(function (response, xhr) {
        // Do something
      });
    }
  },
  ready: function(){
    this.getProjects();
  }
});
