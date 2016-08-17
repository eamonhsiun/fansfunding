package com.fansfunding.user.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fansfunding.project.dao.ResourceDao;
import com.fansfunding.project.entity.Resource;
import com.fansfunding.user.dao.UserDao;
import com.fansfunding.user.entity.User;
import com.fansfunding.utils.fileupload.FileUpload;

@Service
public class UserSettingsService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private ResourceDao resourceDao;
	/**
	 * 上传头像
	 * 
	 * @return
	 */
	public boolean uploadHead(Integer userId, CommonsMultipartFile file) {
		User user = userDao.selectById(userId);
		if (user == null) {
			return false;
		}
		try {
			String head = FileUpload.save(file, FileUpload.Path.USER_HEAD, userId.toString());
			user.setHead(head);
			userDao.updateHead(user);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public String[] uploadMomentImages(Integer userId, CommonsMultipartFile[] files) {
		String[] paths=new String[files.length];
		for(int i=0;i<files.length;i++){
			try {
				paths[i]=FileUpload.save(files[i], FileUpload.Path.USER_MOMENT, userId.toString());
				Resource resource=new Resource();
				resource.setPath(paths[i]);
				resource.setType("user_moments");
				resource.setDelFlag("0");
				resourceDao.insert(resource);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return paths;
	}



}
