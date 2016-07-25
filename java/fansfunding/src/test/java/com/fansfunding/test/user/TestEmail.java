package com.fansfunding.test.user;

import junit.framework.Assert;

import org.junit.Test;

import com.fansfunding.utils.CheckUtils;
import com.fansfunding.utils.encrypt.MD5Utils;

public class TestEmail {
	@Test
	public void testEmail(){
		Assert.assertEquals(true, CheckUtils.isEmail("048117wangle@gmail.com"));
	}
	@Test
	public void addAccount(){
		System.out.println(MD5Utils.MD5("123456"));
	}
}
