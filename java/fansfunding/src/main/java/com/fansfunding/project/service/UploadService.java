package com.fansfunding.project.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fansfunding.project.dao.ResourceDao;
import com.fansfunding.project.entity.Resource;
import com.fansfunding.utils.fileupload.FileUpload;

@Service
public class UploadService {
	@Autowired
	private ResourceDao resourceDao;

	/**
	 * 上传项目图片
	 * @param categoryId 分类ID
	 * @param projectId 项目ID 
	 * @param files 图片
	 * @return
	 */
	public String[] uploadProjectImages(int categoryId,CommonsMultipartFile[] files){
		String dir=categoryId+"/"+System.currentTimeMillis();
		String[] paths=new String[files.length];
		for(int i=0;i<files.length;i++){
			try {
				paths[i]=FileUpload.save(files[i], FileUpload.Path.PROJECT_ATTACHMENT, dir);
				Resource resource=new Resource();
				resource.setPath(paths[i]);
				resource.setType("project_image");
				resourceDao.insert(resource);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return paths;
	}

	/**
	 * 上传回报图片
	 * @param categoryId
	 * @param projectId
	 * @param files
	 * @return
	 */
	public String[] uploadFeedbackImages(int categoryId,int projectId,CommonsMultipartFile[] files){
		String[] paths=new String[files.length];
		for(int i=0;i<files.length;i++){
			try {
				paths[i]=FileUpload.save(files[i], FileUpload.Path.PROJECT_ATTACHMENT, categoryId+"/"+projectId);
				Resource resource=new Resource();
				resource.setPath(paths[i]);
				resource.setType("feedback_image");
				resource.setMappingId(projectId);
				resourceDao.insert(resource);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return paths;
	}
	/**
	 * 上传动态图片
	 * @param categoryId
	 * @param projectId
	 * @param files
	 * @return
	 */
	public String[] uploadMomentImages(int categoryId,int projectId,CommonsMultipartFile[] files){
		String[] paths=new String[files.length];
		for(int i=0;i<files.length;i++){
			try {
				paths[i]=FileUpload.save(files[i], FileUpload.Path.PROJECT_ATTACHMENT, categoryId+"/"+projectId);
				Resource resource=new Resource();
				resource.setPath(paths[i]);
				resource.setType("moment_image");
				resource.setMappingId(projectId);
				resourceDao.insert(resource);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return paths;
	}
}
