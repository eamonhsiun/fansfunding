package com.fansfunding.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fansfunding.user.service.UserSettingsService;
import com.fansfunding.utils.fileupload.FileUpload;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping(path="user")
public class UserSettingsController {
	@Autowired
	private UserSettingsService settings;
	
	/**
	 * 获取头像
	 * @param userName
	 * @return
	 */
	@RequestMapping(path="{userId}/head",method=RequestMethod.GET)
	@ResponseBody
	public Status getHead(@PathVariable Integer userId){
		return new Status(true,StatusCode.SUCCESS,"GET",null);
	}
	/**
	 * 上传头像
	 * @param userName
	 * @return
	 */
	@RequestMapping(path="{userId}/head",method=RequestMethod.POST)
	@ResponseBody
	public Status postHead(@PathVariable Integer userId,@RequestParam CommonsMultipartFile file){
		if(!file.isEmpty()){
			if(file.getSize()>FileUpload.FILE_MAX_SIZE){
				return new Status(false,StatusCode.FILE_TOO_LARGE,"文件大小超过了上传限制",null);
			}
			if(settings.uploadHead(userId, file)){
				return new Status(true,StatusCode.FILEUPLOAD_ERROR,"文件上传成功",null);
			}
			return new Status(false,StatusCode.SUCCESS,"文件上传失败",null);
		}
		return new Status(false,StatusCode.ERROR_DATA,"文件不可为空",null);
	}
	/**
	 * 修改用户信息
	 * @param userId 用户ID
	 * @return
	 */
	@RequestMapping(path="{userId}/info",method=RequestMethod.POST)
	@ResponseBody
	public Status info_modify(@PathVariable Integer userId){
		return null;
	}
}
