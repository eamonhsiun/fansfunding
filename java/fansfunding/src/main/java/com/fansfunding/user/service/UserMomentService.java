package com.fansfunding.user.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.project.dao.ResourceDao;
import com.fansfunding.project.entity.Resource;
import com.fansfunding.user.dao.UserMomentCommentDao;
import com.fansfunding.user.dao.UserMomentDao;
import com.fansfunding.user.entity.UserMoment;
import com.fansfunding.user.entity.UserMomentComment;

@Service
public class UserMomentService {
	@Autowired
	private UserMomentDao userMomentDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private UserMomentCommentDao userMomentCommentDao;
	
	public List<Map<String,Object>> getCommentByMomentId(int momentId) {
		List<Map<String,Object>> commentMap = new ArrayList<>();
		List<UserMomentComment> userMomentComments = userMomentCommentDao.selectByMomentId(momentId);
		for(UserMomentComment u:userMomentComments){
			Map<String,Object> comment=new HashMap<>();
			comment.put("id", u.getId());
			comment.put("content", u.getContent());
			comment.put("momentId", u.getMomentId());
			comment.put("userId", u.getUserId());
			comment.put("postTime", u.getCreateTime());
			commentMap.add(comment);
		}
		return commentMap;
	}
	
	
	/**
	 * 根据用户id获取用户动态
	 * @param userId
	 * @return
	 */
	public List<Map<String,Object>> getMomentsById(int userId) {
		List<Map<String,Object>> momentMap = new ArrayList<>();
		List<UserMoment> userMoments = userMomentDao.selectByUserId(userId);
		for(UserMoment u:userMoments){
			Map<String,Object> moment=new HashMap<>();
			moment.put("id", u.getId());
			moment.put("content", u.getContent());
			moment.put("postTime", u.getCreateTime());
			List<Resource> images=resourceDao.selectProjectImages(u.getId());
			String[] paths=new String[images.size()];
			for(int i=0;i<images.size();i++){
				paths[i]=images.get(i).getPath();
			}
			moment.put("images", paths);
			moment.put("comment", getCommentByMomentId(u.getId()));
			momentMap.add(moment);
		}

		return momentMap;
	}

	public Object getFriendsMomentsById(int userId) {
		List<Map<String,Object>> momentMap = new ArrayList<>();
		List<UserMoment> userMoments = userMomentDao.selectFriendsByUserId(userId);
		for(UserMoment u:userMoments){
			Map<String,Object> moment=new HashMap<>();
			moment.put("id", u.getId());
			moment.put("content", u.getContent());
			moment.put("postTime", u.getCreateTime());
			moment.put("friendId", u.getUserId());
			List<Resource> images=resourceDao.selectProjectImages(u.getId());
			String[] paths=new String[images.size()];
			for(int i=0;i<images.size();i++){
				paths[i]=images.get(i).getPath();
			}
			moment.put("images", paths);
			moment.put("comment", getCommentByMomentId(u.getId()));
			momentMap.add(moment);
		}
		return momentMap;
	}

	public boolean postMoment(int userId, String content, String images) {
		UserMoment moment = new UserMoment();
		moment.setContent(content);
		moment.setUserId(userId);
		
		userMomentDao.insert(moment);
		for(String s:images.split(",")){
			Resource resource=new Resource();
			resource.setMappingId(moment.getId());
			resource.setType("user_moments");
			resource.setPath(s);
			resourceDao.updateByPath(resource);
		}
		return true;
	}

	public boolean isExist(int momentId) {
		if(userMomentDao.selectById(momentId)!=null){
			return true;
		}
		return false;
	}

	public boolean postComment(int userId, int momentId, String content) {
		UserMomentComment userMomentComment = new UserMomentComment();
		userMomentComment.setUserId(userId);
		userMomentComment.setMomentId(momentId);
		userMomentComment.setContent(content);		
		userMomentCommentDao.insert(userMomentComment);
		return true;
	}



}
