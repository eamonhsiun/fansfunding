var plateLoader = new FFloader(document.getElementsByClassName("plate-content")[0]);

var projectListVm = new Vue({
  el: "#project-list",
  data: {
    status: false, //登陆状态
    categoryId: getQueryString("categoryId") || 1,
    searchCondition: getQueryString("search"),
    title: "浏览项目",
    categorys: {
      status: false,
      connect: false,
      list: [],
    },
    projects: {
      status: false,
      connect: false,
      list: [],
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: getQueryString("page") || 1,
      },
    },
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
    redirect: function(target){
      if(target){
        switch(target){

        }
        return;
      }
      window.location.href = "404.html";
    },
    getCategorys: function(){
      var _this = this;
      var categorysRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/categorys",
      }).then(function (response, xhr) {
        var data = response;
        _this.categorys.connect = true;
        if(!data.result || data.data.length === 0){
          // plateLoader.endLoad(false, "没有相关项目");
        }else{
          _this.categorys.status = true;
          _this.categorys.list = data.data;

        }
      }).catch(function (response, xhr) {
        console.log("获取项目分类失败")
      }).always(function (response, xhr) {
        // Do something
      });
    },
    getProjects: function(page){
      var _this = this;
      // plateLoader.init();
      var projectsRequest = ajax({
        method: 'get',
        url: apiUrl +"/project/" + _this.categoryId + "?rows=12" + (page ? "&page=" + page : ""),
      }).then(function (response, xhr) {
        _this.addProject(_this.projects, response);
      }).catch(function (response, xhr) {
        plateLoader.endLoad(false, "连接服务器失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    searchProject: function(page){
      var _this = this;
      // plateLoader.init();
      var projectsRequest = ajax({
        method: 'get',
        url: apiUrl +"/search/project" + "?keyword=" + _this.searchCondition + "&rows=12" + (page ? "&page=" + page : ""),
      }).then(function (response, xhr) {
        _this.addProject(_this.projects, response);
      }).catch(function (response, xhr) {
        plateLoader.endLoad(false, "连接服务器失败");
      }).always(function (response, xhr) {
        // Do something
      });
    },
    addProject: function(wrap, data){
      wrap.connect = true;
      if(!data.result || data.data.list.length === 0){
        plateLoader.endLoad(false, "没有相关项目");
        wrap.list = [];
        this.setPagination(wrap.pagination, data.data);
      }else{
        wrap.status = true;
        wrap.list = data.data.list;
        this.setPagination(wrap.pagination, data.data);
        plateLoader.endLoad();
      }
    },
    changeCategory: function(id){
      this.categoryId = id;
      this.getProjects();
    }
  },
  ready: function(){
    if(this.searchCondition){
      this.title = "“" + this.searchCondition + "”的搜索结果："
      this.searchProject(this.projects.pagination.pageNum);
    }else{
      this.getCategorys();
      this.getProjects(this.projects.pagination.pageNum);
    }
  }
});
