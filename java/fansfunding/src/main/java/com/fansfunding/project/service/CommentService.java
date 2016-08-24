package com.fansfunding.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fansfunding.project.dao.CommentDao;
import com.fansfunding.project.dao.ProjectDao;
import com.fansfunding.project.entity.Comment;
import com.fansfunding.socket.dao.NotificationDao;
import com.fansfunding.socket.entity.CommentMessage;
import com.fansfunding.socket.entity.Notification;
import com.fansfunding.socket.entity.SocketResponse;
import com.fansfunding.socket.util.Dispatcher;
import com.fansfunding.socket.util.MessageConverter;
import com.fansfunding.user.dao.UserDao;import com.fansfunding.user.entity.User;
import com.fansfunding.user.service.UserService;
import com.fansfunding.utils.pagination.Page;
import com.fansfunding.utils.pagination.PageAdapter;
import com.fansfunding.utils.response.StatusCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service
public class CommentService {
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserService userService;
	@Autowired
	private NotificationDao notificationDao;
	/**
	 * 获取项目下的所有评论
	 * @param projectId 项目ID
	 * @return
	 */
	public Page getComments(int projectId,int page,int rows){
		List<Map<String,Object>> comments=new ArrayList<>();
		
		PageHelper.startPage(page, rows);
		List<Comment> list=commentDao.selectByProjectId(projectId);
		PageInfo<Comment> info=new PageInfo<Comment>(list);
		
		list.forEach((e)->{
			Map<String,Object> comment=new HashMap<>();
			User commenter=userDao.selectById(e.getUserId());
			comment.put("id", e.getId());
			comment.put("content", e.getContent());
			comment.put("projectId", e.getProjectId());
			comment.put("commenterId", commenter.getId());
			comment.put("commenterName", commenter.getName());
			comment.put("commenterNickname", commenter.getNickname());
			comment.put("commenterHead", commenter.getHead());
			comment.put("commentTime", e.getCreateTime());
			comment.put("pointTo", e.getPointTo());
			//如果该评论不是一级评论，添加评论指向人信息
			if(e.getPointTo().intValue()!=0){
				User pointTo=userDao.selectById(e.getPointTo());
				comment.put("pointToName", pointTo.getName());
				comment.put("pointToNickname", pointTo.getNickname());
			}
			comments.add(comment);
		});
		
		return PageAdapter.adapt(info, comments);
	}
	/**
	 * 添加评论
	 * @param comment
	 * @return
	 */
	@Transactional
	public void add(Comment comment){
		comment.setCreateBy(userDao.selectById(comment.getUserId()).getName());
		commentDao.insert(comment);
		//通知
		Map<String,Object> commenter=userService.getUserBasicMap(userService.getUserById(comment.getUserId()));
		Map<String,Object> pointTo=projectService.getByProjectId(comment.getProjectId());
		int receiver;
		if(comment.getPointTo().intValue()==0){
			receiver=projectDao.selectByProjectId(comment.getProjectId()).getSponsor();
		}
		else{
			receiver=comment.getPointTo().intValue();
		}
		String content=comment.getContent();
		CommentMessage msg=CommentMessage.builder()
				.comment(content)
				.pointTo(pointTo)
				.commenter(commenter)
				.type(1)
				.build();
		Notification notification=Notification.builder()
				.content(MessageConverter.objectToJson(new SocketResponse(true,3,StatusCode.SUCCESS,msg)))
				.createBy(comment.getUserId().toString())
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
