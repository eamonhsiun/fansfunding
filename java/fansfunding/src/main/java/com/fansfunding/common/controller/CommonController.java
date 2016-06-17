package com.fansfunding.common.controller;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.common.entity.Token;
import com.fansfunding.common.service.CheckerService;
import com.fansfunding.common.service.TokenService;
import com.fansfunding.utils.response.PermissionCode;
import com.fansfunding.utils.response.Status;
import com.fansfunding.utils.response.StatusCode;

@Controller
@RequestMapping("common")
public class CommonController {
	@Autowired
	TokenService tokenService;
	
	@Autowired
	CheckerService checkerService;
	/**
	 * 生成token
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(path="newToken",method=RequestMethod.GET)
	@ResponseBody
	public Status newToken(@RequestParam String phone,HttpServletResponse resp){
		if(phone.length()!=11)
			return new Status(false,StatusCode.ERROR_DATA,null);
		Token rToken = tokenService.requestToken(PermissionCode.NO_PERMISSION,phone);
		resp.addCookie(new Cookie("tid", rToken.getId()+""));
		resp.addCookie(new Cookie("token", rToken.getValue()));
		return new Status(true,StatusCode.SUCCESS,rToken);
	}

	
	/**
	 * 生成Checker
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(path="newChecker",method=RequestMethod.GET)
	@ResponseBody
	public Status newChecker(@RequestParam String phone,HttpServletResponse resp){
		if(checkerService.isTimeTooShort(phone)){
			return new Status(false, StatusCode.TOO_FREQUENT, null);
		}
		//TODO:这里还需要防止频繁请求
		int id =checkerService.genChecker(phone);
		if(id > 0){
			resp.addCookie(new Cookie("cid", id+""));
			return new Status(true, StatusCode.SUCCESS, id);
		}else{
			return new Status(false,StatusCode.FAILD,null);
		}
	}
}
