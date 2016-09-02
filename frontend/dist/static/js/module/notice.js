var websocketUrl = "ws://api.immortalfans.com:8080/websocket?userId=" + localId + "&token=" + localToken;

var socket = new WebSocket(websocketUrl);


// 监听Socket的关闭
socket.onclose = function(event) {
  console.log('Client notified socket has closed',event);
};

socket.onmessage = function(event) {
  console.log(event);
};

socket.send( JSON.stringify({"receiver": 10000053, "content":"你好"}))
