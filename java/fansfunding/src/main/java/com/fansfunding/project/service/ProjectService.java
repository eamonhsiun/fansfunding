package com.fansfunding.project.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import com.fansfunding.user.dao.UserDao;
import com.fansfunding.utils.fileupload.FileUpload;
import com.fansfunding.utils.pagination.Page;
import com.fansfunding.utils.pagination.PageAdapter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ProjectService {
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ProjectDetailDao detailDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 添加项目
	 * @param name 
	 * @param categoryId 
	 * @param cover 
	 * @param sponsor 
	 * @param targetDeadline 
	 * @param targetMoney 
	 * @param description 
	 * @param content 
	 * @param images 
	 * @param others 
	 * @param video 
	 * 
	 */
	public void addProject(String name, Integer categoryId, String cover, Integer sponsor, Date targetDeadline, Long targetMoney, String description, String content, String images, String others, String video){
		ProjectDetail projectDetail = new ProjectDetail();
		projectDetail.setContent(content);
		projectDetail.setCreateBy("admin");
		projectDetail.setDelFlag("0");
		projectDetail.setImages(images);
		projectDetail.setOthers(others);
		projectDetail.setVideo(video);
		Integer detailId = detailDao.insert(projectDetail);
		
		
		Project project = new Project();
		project.setCategoryId(categoryId);
		project.setCover(cover);
		project.setCreateBy("admin");
		project.setDelFlag("0");
		project.setDescription(description);
		
		project.setDetailId(detailId);
		project.setName(name);
		project.setSponsor(sponsor);
		project.setStatus("未完成");
		project.setTargetDeadline(targetDeadline);
		project.setTargetMoney(targetMoney);
		projectDao.insert(project);
	}
	
	
	
	/**
	 * 获取分类下所有项目
	 * @param categroyId 分类ID
	 * @return
	 */
	public Page getByCategoryId(int categoryId,int page,int rows){
		List<Map<String,Object>> projects=new ArrayList<Map<String,Object>>();
		
		PageHelper.startPage(page, rows);
		List<Project> list=projectDao.selectByCategoryId(categoryId);
		PageInfo<Project> info=new PageInfo<>(list);
		
		list.forEach((e)->{
			Map<String,Object> project=new HashMap<>();
			project.put("id", e.getId());
			project.put("categoryId", e.getCategoryId());
			project.put("cover", e.getCover());
			project.put("description", e.getDescription());
			project.put("detailId", e.getDetailId());
			project.put("name", e.getName());
			project.put("sponsor", e.getSponsor());
			project.put("status", e.getStatus());
			project.put("targetDeadline", e.getTargetDeadline());
			project.put("targetMoney", e.getTargetMoney());
			project.put("sponsorNickname", userDao.selectById(e.getSponsor()).getNickname());
			projects.add(project);
		});
		
		return PageAdapter.adapt(info, projects);
	}
	
	/**
	 * 根据项目ID获取项目详情
	 * @param projectId 项目ID
	 * @return
	 */
	public Map<String,Object> getByProjectId(Integer projectId){
		Map<String,Object> project=new HashMap<>();
		Project e=projectDao.selectByProjectId(projectId);

		project.put("id", e.getId());
		project.put("categoryId", e.getCategoryId());
		project.put("cover", e.getCover());
		project.put("description", e.getDescription());
		project.put("detailId", e.getDetailId());
		project.put("name", e.getName());
		project.put("sponsor", e.getSponsor());
		project.put("status", e.getStatus());
		project.put("targetDeadline", e.getTargetDeadline());
		project.put("targetMoney", e.getTargetMoney());
		project.put("sponsorNickname", userDao.selectById(e.getSponsor()).getNickname());
		return project;
	}
	/**
	 * 获取详情
	 * @param projectId 项目ID
	 * @return
	 */
	public Map<String,Object> getDetails(Integer projectId){
		Map<String,Object> details=new HashMap<String,Object>();
		ProjectDetail pd=detailDao.selectByProjectId(projectId);
		
		details.put("id", pd.getId());
		details.put("content", pd.getContent());
		details.put("images", pd.getImages());
		details.put("video", pd.getVideo());
		details.put("others", pd.getOthers());
		
		return details;
	}
	/**
	 * 判断该项目是否在该分类目录下
	 * @param categoryId 分类ID
	 * @param projectId 项目ID
	 * @return
	 */
	public boolean  inCategory(int categoryId,int projectId){
		return projectDao.selectByProjectId(projectId).getCategoryId().intValue()==categoryId;
	}
	
	/**
	 * 上传附件
	 * @param categoryId 分类ID
	 * @param projectId 项目ID 
	 * @param files 附件
	 * @return
	 */
	public boolean uploadAttachments(int categoryId,int projectId,CommonsMultipartFile[] files){
		ProjectDetail projectDetail=detailDao.selectByProjectId(projectId);
		if(projectDetail==null){
			return false;
		}
		//TODO 更新项目详情
		for(CommonsMultipartFile file:files){
			try {
				FileUpload.save(file, FileUpload.Path.PROJECT_ATTACHMENT, categoryId+"/"+projectId);
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}
}
