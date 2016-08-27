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
