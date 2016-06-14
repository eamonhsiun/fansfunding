package com.fansfunding.project.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fansfunding.project.dao.ProjectDao;
import com.fansfunding.project.dao.ProjectDetailDao;
import com.fansfunding.project.entity.Project;
import com.fansfunding.project.entity.ProjectDetail;
import com.fansfunding.utils.fileupload.FileUpload;

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
		ProjectDetail pd=detailDao.selectByPrimaryKey(projectDao.selectByPrimaryKey(projectId).getDetailId());
		
		details.put("id", pd.getId());
		details.put("content", pd.getContent());
		details.put("images", pd.getImages());
		details.put("video", pd.getVideo());
		details.put("others", pd.getOthers());
		
		return details;
	}
	/**
	 * 判断该项目是否在该分类目录下
	 * @param catagoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	public boolean  inCatagory(int catagoryId,int projectId){
		return projectDao.selectByPrimaryKey(projectId).getCatagoryId().intValue()==catagoryId;
	}
	
	/**
	 * 上传附件
	 * @param catagoryId 分类ID
	 * @param projectId 项目ID 
	 * @param files 附件
	 * @return
	 */
	public boolean uploadAttachments(int catagoryId,int projectId,CommonsMultipartFile[] files){
		ProjectDetail projectDetail=detailDao.selectByPrimaryKey(projectDao.selectByProjectId(projectId).getDetailId());
		if(projectDetail==null){
			return false;
		}
		
		for(CommonsMultipartFile file:files){
			try {
				FileUpload.save(file, FileUpload.Path.PROJECT_ATTACHMENT, catagoryId+"/"+projectId);
//				project
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}
}
