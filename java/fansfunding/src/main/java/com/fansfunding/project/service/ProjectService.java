package com.fansfunding.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fansfunding.project.dao.ProjectDao;
import com.fansfunding.project.dao.ProjectDetailDao;
import com.fansfunding.project.entity.Project;

@Service
public class ProjectService {
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ProjectDetailDao detailDao;
	/**
	 * 获取分类下所有项目
	 * @param catagroyId 分类ID
	 * @return
	 */
	public List<Map<String,Object>> getByCatagoryId(int catagoryId){
		List<Map<String,Object>> projects=new ArrayList<Map<String,Object>>();
		projectDao.selectByCatagoryId(catagoryId).stream().forEach((e)->{
			Map<String,Object> project=new HashMap<>();
			project.put("id", e.getId());
			project.put("catagoryId", e.getCatagoryId());
			project.put("cover", e.getCover());
			project.put("description", e.getDescription());
			project.put("detailId", e.getDetailId());
			project.put("name", e.getName());
			project.put("sponsor", e.getSponsor());
			project.put("status", e.getStatus());
			project.put("targetDeadline", e.getTargetDeadline());
			project.put("targetMoney", e.getTargetMoney());
			projects.add(project);
		});
		return projects;
	}
	/**
	 * 根据项目ID获取项目详情
	 * @param projectId 项目ID
	 * @return
	 */
	public Map<String,Object> getByProjectId(Integer projectId){
		Map<String,Object> project=new HashMap<>();
		Project e=projectDao.selectByPrimaryKey(projectId);

		project.put("id", e.getId());
		project.put("catagoryId", e.getCatagoryId());
		project.put("cover", e.getCover());
		project.put("description", e.getDescription());
		project.put("detailId", e.getDetailId());
		project.put("name", e.getName());
		project.put("sponsor", e.getSponsor());
		project.put("status", e.getStatus());
		project.put("targetDeadline", e.getTargetDeadline());
		project.put("targetMoney", e.getTargetMoney());
		
		return project;
	}
	/**
	 * 获取详情
	 * @param projectId 项目ID
	 * @return
	 */
	public Map<String,Object> getDetails(Integer projectId){
		Map<String,Object> details=new HashMap<String,Object>();
		return details;
	}
	
}
