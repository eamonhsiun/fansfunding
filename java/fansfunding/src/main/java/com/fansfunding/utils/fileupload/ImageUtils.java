package com.fansfunding.utils.fileupload;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片压缩（还是在APP上进行吧）
 * @author wangle
 *
 */
@SuppressWarnings("restriction")
public class ImageUtils{  
	private Image img;
	private int width;
	private int height;
	private String filePath;
	public ImageUtils(File image){
		filePath=image.getAbsolutePath();
		try {
			img = ImageIO.read(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		width = img.getWidth(null);    // 得到源图宽  
		height = img.getHeight(null);  // 得到源图长  
	}  
	/** 
	 * 按照宽度还是高度进行压缩 
	 * @param w int 最大宽度 
	 * @param h int 最大高度 
	 */  
	public void resizeFix(int w, int h){  
		if (width / height > w / h) {  
			resizeByWidth(w);  
		} else {
			resizeByHeight(h);  
		}  
	}  
	/** 
	 * 以宽度为基准，等比例放缩图片 
	 * @param w int 新宽度 
	 */  
	private void resizeByWidth(int w){  
		int h = (int) (height * w / width);  
		resize(w, h);
	}  
	/** 
	 * 以高度为基准，等比例缩放图片 
	 * @param h int 新高度 
	 */  
	private void resizeByHeight(int h){  
		int w = (int) (width * h / height);  
		resize(w, h);  
	}  
	/** 
	 * 强制压缩/放大图片到固定的大小 
	 * @param w int 新宽度 
	 * @param h int 新高度 
	 */  
	private void resize(int w, int h){
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢  
		BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );
		image.getGraphics().drawImage(img, 0, 0, w, h, null);
		File destFile = new File(filePath);
		if(destFile.exists()){
			destFile.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(destFile);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
			encoder.encode(image);
			out.close();
		} catch (ImageFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
