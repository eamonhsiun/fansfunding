var plateLoader = new FFloader(document.getElementsByClassName("plate-content")[0]);

Vue.filter("resource" ,function(value) {
  if(!value){
    return "";
  }
  return resourceUrl + value;
});

Vue.component('project-list', {
  template: '#project-list-template',
  props: ["projects"],
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
  }
});

var projectListVm = new Vue({
  el: "#project-list",
  data: {
    status: false, //登陆状态
    categoryId: getQueryString("categoryId") || 1,
    projects: {
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
    setPagination: function(pagination, data){
      pagination.total = data.total;
      pagination.pageNum = data.pageNum;
      pagination.pages = data.pages;
      pagination.pageSize = data. pageSize;
    },
    getProjects: function(page){
      var _this = this;
      // plateLoader.init();
      var projectsRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + _this.categoryId + "?rows=12" + (page ? "&page=" + page : ""),
      }).then(function (response, xhr) {
        _this.projects.connect = true;
        if(!response.result){
          plateLoader.endLoad(false, "没有相关项目");
        }else{
          _this.projects.status = true;
          _this.projects.list = response.data.list;
          _this.setPagination(_this.projects.pagination, response.data);
          plateLoader.endLoad();
          window.scrollTo(0, 0);
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
