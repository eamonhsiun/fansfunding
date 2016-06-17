package com.fansfunding.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.project.dao.FeedbackDao;
import com.fansfunding.project.entity.Feedback;

@Service
public class FeedbackService {
	@Autowired
	private FeedbackDao feedbackDao;
	
	/**
	 * 获取项目相关的回馈方式
	 * @param projectId 项目ID
	 * @return
	 */
	public List<Map<String,Object>> getAll(Integer projectId){
		List<Map<String,Object>> feedbacks=new ArrayList<>();
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
		return feedbacks;
	}
	/**
	 * 添加回馈
	 * @param feedback 回馈
	 */
	public void add(Feedback feedback){
		feedbackDao.insert(feedback);
	}
}
