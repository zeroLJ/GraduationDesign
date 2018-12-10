package main.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Encoder;

public  class ImageUtil {
	/**
	 * @Description: 根据图片地址转换为base64编码字符串
	 * @Author: 
	 * @CreateTime: 
	 * @return
	 */
	public static String getImageStr(String imgFile) {
	    InputStream inputStream = null;
	    byte[] data = null;
	    try {
	        inputStream = new FileInputStream(imgFile);
	        data = new byte[inputStream.available()];
	        inputStream.read(data);
	        inputStream.close();
	        // 加密
		    BASE64Encoder encoder = new BASE64Encoder();
		    return encoder.encode(data);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "";
	    }
	   
	}
}
