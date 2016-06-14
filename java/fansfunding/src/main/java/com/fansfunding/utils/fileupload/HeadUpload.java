package com.fansfunding.utils.fileupload;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fansfunding.user.entity.User;


/**
 * 头像上传，限制大小
 * @author wangle
 *
 */
public class HeadUpload {

	private HeadUpload(){}

	private static String basePath;
	private static String headUrl;
	static{
		Properties prop=new Properties();
		Resource resource=new ClassPathResource("fileupload.properties");
		try {
			InputStream file = resource.getInputStream();
			prop.load(file);
		} catch (IOException e){
			e.printStackTrace();
		}
		basePath=prop.getProperty("base.path");
		headUrl=prop.getProperty("user.head");
	}

	public static String save(User user,CommonsMultipartFile image) throws IOException{
		String newPath=basePath+headUrl.replace("{userId}", String.valueOf(user.getId()));
		File file=new File(newPath);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream out=new FileOutputStream(file);
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		ImageIO.write(HeadUpload.change(image), "jpg", os);
		byte b[]=os.toByteArray();
		out.write(b);
		out.flush();
		out.close();
		return newPath;
	}
	private static BufferedImage change(CommonsMultipartFile image){
		BufferedImage img=null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(image.getBytes());
			img = ImageIO.read((InputStream)in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
}
