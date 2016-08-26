package com.fansfunding.socket.util;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.socket.dao.NotificationDao;
import com.fansfunding.socket.entity.CommentMessage;
import com.fansfunding.socket.entity.Notification;
import com.fansfunding.socket.entity.NotificationMessage;
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
				.type(type)
				.time(new Date())
				.build();
		Notification notification=Notification.builder()
				.content(MessageConverter.objectToJson(new SocketResponse(true,2,StatusCode.SUCCESS,msg)))
				.createBy(String.valueOf(commenter))
				.receiver(receiver)
				.type(2)
				.build();
		if(Dispatcher.comment(receiver, msg)){
			notification.setIsRead("1");
		}
		else{
			notification.setIsRead("0");
		}
		notificationDao.insert(notification);
	}
	
	/**
	 * 推送点赞消息
	 * @param receiver
	 * @param causer
	 * @param reference
	 */
	public void pushLike(int receiver,int causer,Map<String,Object> reference){
		this.pushNotification(1, receiver, causer, reference);
	}
	/**
	 * 推送支持消息
	 * @param receiver
	 * @param causer
	 * @param reference
	 */
	public void pushSupport(int receiver,int causer,Map<String,Object> reference){
		this.pushNotification(2, receiver, causer, reference);
	}
	/**
	 * 推送转发消息
	 * @param receiver
	 * @param causer
	 * @param reference
	 */
	public void pushRepost(int receiver,int causer,Map<String,Object> reference){
		this.pushNotification(3, receiver, causer, reference);
	}
	/**
	 * 推送项目关注消息
	 * @param receiver
	 * @param causer
	 * @param reference
	 */
	public void pushProjectFollow(int receiver,int causer,Map<String,Object> reference){
		this.pushNotification(4, receiver, causer, reference);
	}
	/**
	 * 推送用户关注消息
	 * @param receiver
	 * @param causer
	 * @param reference
	 */
	public void pushUserFollow(int receiver,int causer,Map<String,Object> reference){
		this.pushNotification(5, receiver, causer, reference);
	}
	/**
	 * 推送项目动态消息
	 * @param receiver
	 * @param causer
	 * @param reference
	 */
	public void pushProjetcMoment(int receiver,int causer,Map<String,Object> reference){
		this.pushNotification(6, receiver, causer, reference);
	}
	
	/**
	 * 推送系统通知
	 * @param type
	 * @param receiver
	 * @param causer
	 * @param reference
	 */
	private void pushNotification(int type,int receiver,int causer,Map<String,Object> reference){
		NotificationMessage msg=NotificationMessage.builder()
				.reference(reference)
				.causer(userService.getUserBasicMap(causer))
				.type(type)
				.time(new Date())
				.build();
		Notification notification=Notification.builder()
				.content(MessageConverter.objectToJson(new SocketResponse(true,3,StatusCode.SUCCESS,msg)))
				.createBy(String.valueOf(causer))
				.receiver(receiver)
				.type(3)
				.build();
		if(Dispatcher.notice(receiver, msg)){
			notification.setIsRead("1");
		}
		else{
			notification.setIsRead("0");
		}
		notificationDao.insert(notification);
	}
}
