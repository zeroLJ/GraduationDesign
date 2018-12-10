package main.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

public class ResponseUtil{	
	public static void response(HttpServletResponse response, List<?> resultList, Map<String, Object> resultMap, String msg,File file, boolean success) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("msg", ObjUtils.objToStr(msg));
		if (resultList!=null) {
			map.put("resultList", resultList);
		}else {
			map.put("resultList", new ArrayList<>());
		}
		if (resultMap!=null) {
			map.put("resultMap", resultMap);
		}else {
			map.put("resultMap", new HashMap<>());
		}
		String resultStr = JSON.toJSONString(map);
		response.setContentType("text/html;charset=utf-8");//设置返回数据的编码，不需使用encode
//		response.addHeader("data", URLEncoder.encode(resultStr,"UTF-8")); 
//		response.addHeader("data", resultStr); 
		if (file!=null) {
			response.addHeader("data", URLEncoder.encode(resultStr,"UTF-8")); //若在header添加数据，需要encode
			OutputStream out = response.getOutputStream(); 
			response.setContentType("application/octet-stream"); 
			response.addHeader("Content-Disposition", "attachment;filename="  
	                + file.getName()); 
	        response.addHeader("Content-Length", "" + file.length()); 
	        response.addHeader("FileName", "" + file.getName()); 
	        System.out.println("FileName:" + file.getName());  
	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
	        int length = 0;   
	        try {
	        	bis = new BufferedInputStream(new FileInputStream(file));
	 	        bos = new BufferedOutputStream(out);
	        	byte[] bytes = new byte[2048]; 
	 	        int read;
	 	        while ((read = bis.read(bytes,0,bytes.length)) != -1) {
	 	        	length = length + read;
	 				bos.write(bytes, 0, read);
	 			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();;
			}finally {
				if (bis!=null) {
					bis.close();
				}
				if (bos!=null) {
					bos.close();
				}
			}
	        System.out.println("size:" + length);   
		}else {
			response.getWriter().print(resultStr);
		}
        
	}
}
