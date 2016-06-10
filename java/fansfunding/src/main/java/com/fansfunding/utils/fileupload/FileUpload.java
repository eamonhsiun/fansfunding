package com.fansfunding.utils.fileupload;

import java.io.File;

import org.junit.Test;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 文件上传
 * @author wangle
 *
 */
public class FileUpload {
	public enum PATH{
		USER_HEAD,
		PROJECT_ATTACHMENT,
		CHAT_FILES
	}
	private FileUpload(){}
	
	
	
	public void save(){
	}
	private byte[] parse(CommonsMultipartFile file){
		return file.getBytes();
	}
	@Test
	public void test(){
	}
}
