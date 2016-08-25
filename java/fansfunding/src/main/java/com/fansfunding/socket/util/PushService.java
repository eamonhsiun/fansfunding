package com.fansfunding.socket.util;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.socket.dao.NotificationDao;
import com.fansfunding.socket.entity.CommentMessage;
import com.fansfunding.socket.entity.Notification;
import com.fansfunding.socket.entity.SocketResponse;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.response.StatusCode;

@Service
public class PushService {
	@Autowired
	private NotificationDao notificationDao;
	@Autowired
	private UserService userService;
	
	/**
	 * 推送项目评论
	 * @param receiver
	 * @param commenter
	 * @param pointTo
	 * @param content
	 */
	public void pushProjectConmment(int receiver,int commenter,Map<String,Object> pointTo,String content){
		pushComment(1,receiver,commenter,pointTo,content);
	}
	/**
	 * 推送动态评论
	 * @param receiver
	 * @param commenter
	 * @param pointTo
	 * @param content
	 */
	public void pushMommentComment(int receiver,int commenter,Map<String,Object> pointTo,String content){
		pushComment(2,receiver,commenter,pointTo,content);
	}
	/**
	 * 推送评论消息
	 * @param type
	 * @param receiver
	 * @param commenter
	 * @param pointTo
	 * @param content
	 */
	private void pushComment(int type,int receiver,int commenter,Map<String,Object> pointTo,String content){
		CommentMessage msg=CommentMessage.builder()
				.comment(content)
				.pointTo(pointTo)
				.commenter(userService.getUserBasicMap(commenter))
				.type(1)
				.time(new Date())
				.build();
		Notification notification=Notification.builder()
				.content(MessageConverter.objectToJson(new SocketResponse(true,3,StatusCode.SUCCESS,msg)))
				.createBy(String.valueOf(commenter))
				.receiver(receiver)
				.type(3)
				.build();
		if(Dispatcher.comment(receiver, msg)){
			notification.setIsRead("1");
		}
		else{
			notification.setIsRead("0");
		}
		notificationDao.insert(notification);
	}
}
