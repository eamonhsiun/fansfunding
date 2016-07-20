package com.fansfunding.user.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fansfunding.user.dao.UserDao;
import com.fansfunding.user.entity.User;
import com.fansfunding.utils.fileupload.FileUpload;

@Service
public class UserSettingsService {
	@Autowired
	private UserDao userDao;
	/**
	 * 上传头像
	 * @return
	 */
	public boolean uploadHead(Integer userId,CommonsMultipartFile file){
		User user=userDao.selectById(userId);
		if(user==null){
			return false;
		}
		try{
			String head=FileUpload.save(file, FileUpload.Path.USER_HEAD, userId.toString());
			user.setHead(head);
			userDao.updateHead(user);
		}
		catch(IOException e){
			return false;
		}
		return true;
	}
}
