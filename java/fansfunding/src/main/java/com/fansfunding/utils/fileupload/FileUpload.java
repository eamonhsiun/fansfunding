package com.fansfunding.utils.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fansfunding.utils.encrypt.MD5Utils;

/**
 * 文件上传
 * @author wangle
 *
 */

public class FileUpload {
	/**
	 * 可以上传的最大文件大小（字节数）
	 */
	public static final int FILE_MAX_SIZE=10240000;
	/**
	 * 文件存储的路径
	 * @author wangle
	 *
	 */
	public static enum Path{
		USER_HEAD{
			public String getPath(){
				return FileUpload.userHead;
			}
		},
		PROJECT_ATTACHMENT{
			public String getPath(){
				return FileUpload.projectAttachment;
			}
		},
		CHAT_FILES{
			public String getPath(){
				return FileUpload.chatFiles;
			}
		},
		USER_MOMENT{
			public String getPath(){
				return FileUpload.userMoment;
			}
		};
		public abstract String getPath();
	}
	/**
	 * 根目录
	 */
	private static String basePath;
	/**
	 * 头像
	 */
	private static String userHead;
	/**
	 * 项目附件
	 */
	private static String projectAttachment;
	/**
	 * 群聊文件（图片等）
	 */
	private static String chatFiles;
	
	/**
	 * 用户动态
	 */
	private static String userMoment;

	//加载配置
	static{
		Properties prop=new Properties();
		Resource resource=new ClassPathResource("fileupload.properties");
		try {
			prop.load(resource.getInputStream());
			basePath=prop.getProperty("base.path");
			userHead=prop.getProperty("user.head");
			projectAttachment=prop.getProperty("project.attachment");
			chatFiles=prop.getProperty("chat.files");
			userMoment=prop.getProperty("user.moments");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 禁止实例化
	 */
	private FileUpload(){
		throw new RuntimeException("You can not new an instance of this class!");
	}
	/**
	 * 保存文件
	 */
	public static String save(CommonsMultipartFile file,Path path,String replace) throws IOException{

		FileItem fileItem=FileUpload.parse(file);
		String savePath=(path.getPath()+fileItem.getFileName()).replace("{placeholder}", replace);

		FileUpload.save(fileItem,savePath);

		return savePath;
	}
	private static void save(FileItem fileItem,String path) throws IOException{
		File file=new File(FileUpload.basePath+path);

		//如果文件存在，则删除
		if(file.exists()){
			file.delete();
		}

		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}

		file.createNewFile();

		FileOutputStream out=new FileOutputStream(file);
		out.write(fileItem.getFileContent());
		out.flush();
		out.close();	
	}
	/**
	 * 解析上传的文件
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	private static FileItem parse(CommonsMultipartFile file) throws IOException{
		return new FileItem(file.getBytes(),
				MD5Utils.MD5(file.getOriginalFilename())+System.currentTimeMillis()+"."
						+FileFormat.judge(file.getInputStream()).toString().toLowerCase());
	}

	private static class FileItem{
		private byte[] fileContent;
		private String fileName;

		FileItem(byte[] fileContent,String fileName){
			this.setFileContent(fileContent);
			this.setFileName(fileName);
		}

		byte[] getFileContent() {
			return fileContent;
		}

		void setFileContent(byte[] fileContent) {
			this.fileContent = fileContent;
		}

		String getFileName() {
			return fileName;
		}

		void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}
}
