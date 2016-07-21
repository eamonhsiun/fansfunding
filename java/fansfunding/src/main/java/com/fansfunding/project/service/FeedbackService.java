package com.fansfunding.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.pay.dao.OrderDao;
import com.fansfunding.project.dao.FeedbackDao;
import com.fansfunding.project.dao.ProjectDao;
import com.fansfunding.project.dao.ResourceDao;
import com.fansfunding.project.entity.Feedback;
import com.fansfunding.project.entity.Resource;
import com.fansfunding.user.dao.UserDao;
import com.fansfunding.utils.pagination.Page;
import com.fansfunding.utils.pagination.PageAdapter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class FeedbackService {
	@Autowired
	private FeedbackDao feedbackDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private OrderDao orderDao;
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
		
		list.forEach((e)->{
			Map<String,Object> feedback=new HashMap<>();
			feedback.put("id", e.getId());
			feedback.put("projectId", e.getProjectId());
			feedback.put("title", e.getTitle());
			feedback.put("description", e.getDescription());
			feedback.put("limitation", e.getLimitation());
			List<Resource> images=resourceDao.selectFeedbackImages(e.getId());
			String[] paths=new String[images.size()];
			for(int i=0;i<images.size();i++){
				paths[i]=images.get(i).getPath();
			}
			feedback.put("images", paths);
			feedback.put("supportTimes", orderDao.selectByFeedbackId(e.getId()).size());
			feedbacks.add(feedback);
		});
		return PageAdapter.adapt(info, feedbacks);
	}
	/**
	 * 添加回馈
	 * @param feedback 回馈
	 */
	public void add(int projectId,String title,String description,double limitation ,String images){
		Feedback feedback=new Feedback();
		feedback.setProjectId(projectId);
		feedback.setTitle(title);
		feedback.setDescription(description);
		feedback.setLimitation(limitation);
		feedback.setCreateBy(userDao.selectById(projectDao.selectByProjectId(projectId).getSponsor()).getName());
		feedback.setUpdateBy(userDao.selectById(projectDao.selectByProjectId(projectId).getSponsor()).getName());
		feedbackDao.insert(feedback);
		for(String s:images.split(",")){
			Resource resource=new Resource();
			resource.setMappingId(feedback.getId());
			resource.setType("feedback_image");
			resource.setPath(s);
			resourceDao.updateByPath(resource);
		}
	}
	/**
	 * 回馈是否存在
	 * @param feedbackId 回馈ID
	 */
	public boolean isExist(int feedbackId){
		return feedbackDao.selectByPrimaryKey(feedbackId)!=null;
	}
}
