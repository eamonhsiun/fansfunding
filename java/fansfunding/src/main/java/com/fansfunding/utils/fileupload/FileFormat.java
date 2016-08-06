package com.fansfunding.utils.fileupload;

import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;

/**
 * 二进制文件格式判断
 * @author w-angler
 *
 */
public class FileFormat {
	private FileFormat() {}  
	/**
	 * 判断文件类型
	 * @param path 文件路径 
	 * @return 文件类型
	 */  
	public static FileType judge(String path) throws IOException {
		String fileHead = getFileContent(new FileInputStream(path));
		if (fileHead == null || fileHead.length() == 0) {
			return null;
		}
		return compare(fileHead.toUpperCase());
	}
	/** 
	 * 判断文件类型
	 * @param in 输入流 
	 * @return 文件类型
	 */  
	public static FileType judge(InputStream in) throws IOException {
		String fileHead = getFileContent(in);
		if (fileHead == null || fileHead.length() == 0) {
			return null;
		}
		return compare(fileHead.toUpperCase());
	}
	/**
	 * 是否是图片格式
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static boolean isImage(InputStream in)throws IOException{
		return isImage(judge(in));
	}
	public static boolean isImage(String path) throws IOException{
		return isImage(judge(path));
	}
	private static boolean isImage(FileType fileType){
		switch(fileType){
		case BMP:
			return true;
		case JPEG:
			return true;
		case PNG:
			return true;
		case GIF:
			return true;
		case TIFF:
			return true;
		default:
			return false;
		}
	}
	/**
	 * 比较
	 * @param fileHead
	 * @return
	 */
	private static FileType compare(String fileHead){
		FileType[] fileTypes = FileType.values();
		for (FileType type : fileTypes) {
			if (fileHead.startsWith(type.getValue())) {  
				return type;
			}
		}
		return FileType.UNKNOWN;
	}
	/** 
	 * 将文件头转换成16进制字符串
	 * @param src 字节流
	 * @return 16进制字符串
	 */
	private static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i]&0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		} 
		return stringBuilder.toString();
	}
	/** 
	 * 得到文件头 
	 * @param in 输入流
	 * @return 文件头 
	 * @throws IOException 
	 */
	private static String getFileContent(InputStream in) throws IOException {
		byte[] b = new byte[28];
		in.read(b, 0, 28);
		return bytesToHexString(b);
	}
}
