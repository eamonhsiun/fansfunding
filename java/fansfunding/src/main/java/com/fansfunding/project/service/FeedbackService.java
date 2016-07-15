package com.fansfunding.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.project.dao.FeedbackDao;
import com.fansfunding.project.entity.Feedback;
import com.fansfunding.utils.pagination.Page;
import com.fansfunding.utils.pagination.PageAdapter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class FeedbackService {
	@Autowired
	private FeedbackDao feedbackDao;
	
	/**
	 * 获取项目相关的回馈方式
	 * @param projectId 项目ID
	 * @return
	 */
	public Page getFeedbacks(int projectId,int page,int rows){
		List<Map<String,Object>> feedbacks=new ArrayList<>();
		
		PageHelper.startPage(page, rows);
		List<Feedback> list=feedbackDao.selectByProjectId(projectId);
		PageInfo<Feedback> info=new PageInfo<Feedback>(list);
		
		feedbackDao.selectByProjectId(projectId).forEach((e)->{
			Map<String,Object> feedback=new HashMap<>();
			feedback.put("id", e.getId());
			feedback.put("projectId", e.getProjectId());
			feedback.put("title", e.getTitle());
			feedback.put("description", e.getDescription());
			feedback.put("limitation", e.getLimitation());
			feedback.put("images", e.getImages());
			feedbacks.add(feedback);
		});
		return PageAdapter.adapt(info, feedbacks);
	}
	/**
	 * 添加回馈
	 * @param feedback 回馈
	 */
	public void add(Feedback feedback){
		feedbackDao.insert(feedback);
	}
}
