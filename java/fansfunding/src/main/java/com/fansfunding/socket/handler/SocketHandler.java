package com.fansfunding.socket.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fansfunding.socket.util.MessageConverter;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;


@Component
public class SocketHandler implements WebSocketHandler {
	@Autowired
	private UserService userService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	Map<String,Object> attrs=session.getAttributes();
    	if(!userService.isExist((Integer)attrs.get("userId"))){
            session.sendMessage(
            		new TextMessage(
            				MessageConverter.objectToJson(
            						new Status(false,StatusCode.USER_NULL,"用户不存在",null))));
    		session.close();
    	}
    	else{
            session.sendMessage(new TextMessage("Server:connected OK!"));
    	}
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        TextMessage returnMessage = new TextMessage(wsm.getPayload()
				+ " received at server");
		wss.sendMessage(returnMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        if(wss.isOpen()){
            wss.close();
        }
       System.out.println("websocket connection closed......");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        System.out.println("websocket connection closed......");
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}
