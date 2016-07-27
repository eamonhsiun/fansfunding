package com.fansfunding.utils.fileupload;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class DefaultImage {
	/**
	 * 默认头像
	 */
	public static String DEFAULT_HEAD;
	/**
	 * 默认项目封面
	 */
	public static String DEFAULT_PROJECT_COVER;
	/**
	 * 默认回报图片
	 */
	public static String DEFAULT_FEEDBACK_IMAGE;
	static{
		Properties prop=new Properties();
		Resource resource=new ClassPathResource("default.properties");
		try {
			prop.load(resource.getInputStream());
			DEFAULT_HEAD=prop.getProperty("default.head");
			DEFAULT_PROJECT_COVER=prop.getProperty("default.project.cover");
			DEFAULT_FEEDBACK_IMAGE=prop.getProperty("default.feedback.image");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private DefaultImage(){
		throw new RuntimeException("You can not new an instance of this class!");
	}
}
