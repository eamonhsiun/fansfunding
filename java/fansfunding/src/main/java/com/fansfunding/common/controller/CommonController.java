package com.fansfunding.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fansfunding.common.service.CheckerService;
import com.fansfunding.common.service.TokenService;
import com.fansfunding.utils.encrypt.AESUtils;
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
	 * 生成Checker
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path="newChecker")
	@ResponseBody
	public Status newChecker(@RequestParam String phone) throws Exception{
		if(checkerService.isTimeTooShort(phone)){
			return new Status(false, StatusCode.TOO_FREQUENT, null, null);
		}
		//TODO:这里还需要防止频繁请求
		int id =checkerService.genChecker(phone);
		
		if(id > 0){
			return new Status(true, StatusCode.SUCCESS,null,AESUtils.Encrypt(id+"", AESUtils.ENCRYPT_KEY));
		}else{
			return new Status(false,StatusCode.FAILED,null,null);
		}
	}
	
	@RequestMapping(path="newCheckerT")
	@ResponseBody
	public Status newCheckerT(@RequestParam String phone) throws Exception{
		if(checkerService.isTimeTooShort(phone)){
			return new Status(false, StatusCode.TOO_FREQUENT, null, null);
		}
		//TODO:这里还需要防止频繁请求
		int id =checkerService.genCheckerT(phone);
		
		if(id > 0){
			return new Status(true, StatusCode.SUCCESS,checkerService.check,AESUtils.Encrypt(id+"", AESUtils.ENCRYPT_KEY));
		}else{
			return new Status(false,StatusCode.FAILED,null,null);
		}
	}
}
