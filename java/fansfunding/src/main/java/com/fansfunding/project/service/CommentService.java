package com.fansfunding.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fansfunding.project.dao.CommentDao;
import com.fansfunding.project.entity.Comment;
import com.fansfunding.user.dao.UserDao;import com.fansfunding.user.entity.User;
import com.fansfunding.utils.pagination.Page;
import com.fansfunding.utils.pagination.PageAdapter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service
public class CommentService {
	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private UserDao userDao;
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
	}
}
