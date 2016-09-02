Vue.filter("resource" ,function(value) {
  if(!value){
    return "";
  }
  return resourceUrl + value;
});

Vue.filter("commentTime" ,function(value){
  var date;
  if(value instanceof Date){
    date = value;
  }else{
    date = new Date(value);
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
});

Vue.filter("time" ,function(value){
  var date;
  if(value instanceof Date){
    date = value;
  }else{
    date = new Date(value);
  }
  var c = [date.getFullYear(), date.getMonth()+1, date.getDate()];
  var t = [date.getHours(), date.getMinutes()];
  var result = c.join("-") + " " + t.join(":");
  return result;
});


Vue.mixin({
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
})
