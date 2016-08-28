package com.fansfunding.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.project.dao.ResourceDao;
import com.fansfunding.project.entity.Resource;
import com.fansfunding.project.service.ProjectService;
import com.fansfunding.socket.util.PushService;
import com.fansfunding.user.dao.UserMomentCommentDao;
import com.fansfunding.user.dao.UserMomentDao;
import com.fansfunding.user.dao.UserMomentLikeDao;
import com.fansfunding.user.entity.UserMoment;
import com.fansfunding.user.entity.UserMomentComment;
import com.fansfunding.user.entity.UserMomentLike;
import com.fansfunding.utils.pagination.Page;
import com.fansfunding.utils.pagination.PageAdapter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class UserMomentService {
	@Autowired
	private UserMomentDao userMomentDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private UserMomentCommentDao userMomentCommentDao;
	@Autowired
	private UserMomentLikeDao userMomentLikeDao;
	@Autowired
	private PushService push;
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;

	public Map<String, Object> getMomentById(int momentId) {
		Map<String, Object> moment = new HashMap<>();
		UserMoment u = userMomentDao.selectById(momentId);

		moment.put("momentId", u.getId());
		moment.put("user", userService.getUserBasicMap(u.getUserId()));
		moment.put("project", projectService.getByProjectId(u.getLinkProject()));
		moment.put("content", u.getContent());
		moment.put("postTime", u.getCreateTime());

		List<Resource> images = resourceDao.selectMomentImages(u.getId());
		String[] paths = new String[images.size()];
		for (int i = 0; i < images.size(); i++) {
			paths[i] = images.get(i).getPath();
		}
		moment.put("images", paths);
		List<Map<String, Object>> comments = getCommentByMomentId(u.getId());
		List<Map<String, Object>> likes = getMomentLike(u.getId());

		moment.put("comment", comments);
		moment.put("likeNum", likes.size());
		moment.put("commentNum", comments.size());
		moment.put("forwardNum", 0);
		moment.put("origin", u.getOrigin());

		return moment;
	}

	public List<Map<String, Object>> getCommentByMomentId(int momentId) {

		List<Map<String, Object>> commentMap = new ArrayList<>();
		List<UserMomentComment> userMomentComments = userMomentCommentDao.selectByMomentId(momentId);
		for (UserMomentComment u : userMomentComments) {
			Map<String, Object> comment = new HashMap<>();
			comment.put("commentId", u.getId());
			comment.put("content", u.getContent());
			comment.put("user", userService.getUserBasicMap(u.getUserId()));
			comment.put("postTime", u.getCreateTime());
			commentMap.add(comment);
		}
		return commentMap;
	}

	/**
	 * 根据用户id获取用户动态
	 * 
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page getMomentsById(int userId, int page, int rows) {
		PageHelper.startPage(page, rows);
		List<Map<String, Object>> momentMap = new ArrayList<>();
		List<UserMoment> userMoments = userMomentDao.selectByUserId(userId);
		PageInfo<UserMoment> info = new PageInfo<>(userMoments);

		genMomentMap(userId,momentMap, userMoments);
		return PageAdapter.adapt(info, momentMap);
	}

	public Page getFriendsMomentsById(int userId, int page, int rows) {
		PageHelper.startPage(page, rows);
		List<Map<String, Object>> momentMap = new ArrayList<>();
		List<UserMoment> userMoments = userMomentDao.selectFriendsByUserId(userId);
		PageInfo<UserMoment> info = new PageInfo<>(userMoments);

		genMomentMap(userId,momentMap, userMoments);
		return PageAdapter.adapt(info, momentMap);
	}

	public boolean postMoment(int userId, String content, String images, int origin, int linkCategory,
			int linkProject) {
		UserMoment moment = new UserMoment();
		moment.setContent(content);
		moment.setUserId(userId);
		moment.setOrigin(origin);
		moment.setLinkProject(linkProject);
		moment.setLinkCategory(linkCategory);

		userMomentDao.insert(moment);
		for (String s : images.split(",")) {
			Resource resource = new Resource();
			resource.setMappingId(moment.getId());
			resource.setType("user_moments");
			resource.setPath(s);
			resourceDao.updateByPath(resource);
		}
		return true;
	}

	public boolean isExist(int momentId) {
		if (userMomentDao.selectById(momentId) != null) {
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
		// 通知
		Map<String, Object> pointTo = this.getMomentById(momentId);
		int receiver = userMomentDao.selectById(momentId).getUserId();
		push.pushMommentComment(receiver, userMomentComment.getUserId(), pointTo, content);
		return true;
	}

	/**
	 * 获取点赞信息
	 * 
	 * @param momentId
	 * @return
	 */
	public List<Map<String, Object>> getMomentLike(int momentId) {
		List<Map<String, Object>> momentMap = new ArrayList<>();
		List<UserMomentLike> momentLike = userMomentLikeDao.selectByMomentId(momentId);
		for (UserMomentLike l : momentLike) {
			Map<String, Object> like = new HashMap<>();
			like.put("userId", l.getUserId());
			like.put("nickName", l.getNickname());
			momentMap.add(like);
		}
		return momentMap;
	}

	public boolean postLike(int userId, int momentId) {
		try {
			UserMomentLike userMomentLike = new UserMomentLike();
			userMomentLike.setMomentId(momentId);
			userMomentLike.setUserId(userId);
			userMomentLikeDao.insert(userMomentLike);
			// 推送点赞通知
			//push.pushLike(userMomentDao.selectById(momentId).getUserId(), userId, this.getMomentById(momentId));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean postDisLike(int userId, int momentId) {
		try {
			UserMomentLike userMomentLike = new UserMomentLike();
			userMomentLike.setMomentId(momentId);
			userMomentLike.setUserId(userId);
			userMomentLikeDao.deleteByPrimaryKey(userMomentLike);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private void genMomentMap(int userId, List<Map<String, Object>> momentMap, List<UserMoment> userMoments) {
		for (UserMoment u : userMoments) {
			Map<String, Object> moment = new HashMap<>();
			moment.put("momentId", u.getId());
			moment.put("user", userService.getUserBasicMap(u.getUserId()));
			moment.put("project", projectService.getByProjectId(u.getLinkProject()));
			moment.put("content", u.getContent());
			moment.put("postTime", u.getCreateTime());

			List<Resource> images = resourceDao.selectMomentImages(u.getId());
			String[] paths = new String[images.size()];
			for (int i = 0; i < images.size(); i++) {
				paths[i] = images.get(i).getPath();
			}
			moment.put("images", paths);
			List<Map<String, Object>> comments = getCommentByMomentId(u.getId());
			List<Map<String, Object>> likes = getMomentLike(u.getId());

			moment.put("comment", comments);
			moment.put("isLike", isLike(userId,u.getId()));
			moment.put("likeNum", likes.size());
			moment.put("commentNum", comments.size());
			moment.put("forwardNum", 0);
			moment.put("origin", u.getOrigin());
			momentMap.add(moment);
		}
	}

	private boolean isLike(int userId,int momentId) {
		UserMomentLike userMomentLike=new UserMomentLike();
		userMomentLike.setMomentId(momentId);
		userMomentLike.setUserId(userId);
		if(userMomentLikeDao.selectByPrimaryKey(userMomentLike)!=null)return true;
		return false;
	}

}
