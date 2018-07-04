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
	public static void response(HttpServletResponse response)throws IOException {
		response(response, "");
	}
	public static void response(HttpServletResponse response, String msg)throws IOException {
		response(response, msg, true);
	}
	public static void response(HttpServletResponse response, String msg,boolean success)throws IOException {
		response(response, null, null, msg, success);
	}
	public static void response(HttpServletResponse response, List<?> resultList)throws IOException {
		response(response, resultList, null, null, true);
	}
	public static void response(HttpServletResponse response, List<?> resultList, Map<String, Object> resultMap)throws IOException {
		response(response, resultList, resultMap, null, true);
	}
	public static void response(HttpServletResponse response, List<?> resultList, Map<String, Object> resultMap, boolean success)throws IOException {
		response(response, resultList, resultMap, null, success);
	}
	public static void response(HttpServletResponse response, List<?> resultList, Map<String, Object> resultMap, String msg)throws IOException {
		response(response, resultList, resultMap, msg, true);
	}
	public static void response(HttpServletResponse response, List<?> resultList, Map<String, Object> resultMap, String msg,boolean success) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("msg", msg);
		if (resultList!=null) {
//			map.put("resultList", JSON.toJSONString(resultList));
			map.put("resultList", resultList);
		}else {
			map.put("resultList", new ArrayList<>());
		}
		if (resultMap!=null) {
//			map.put("resultMap", JSON.toJSONString(resultMap));
			map.put("resultMap", resultMap);
		}else {
			map.put("resultMap", new HashMap<>());
		}
		String resultStr = JSON.toJSONString(map);
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().print(resultStr);
	}
	
	public static void responseFile(HttpServletResponse response, File file) throws IOException {
		OutputStream out = response.getOutputStream();  
        response.addHeader("Content-Disposition", "attachment;filename="  
                + file.getName()); 
        response.setContentType("application/octet-stream"); 
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
		
	}
	
	
	public static void responseFile(HttpServletResponse response, List<?> resultList, Map<String, Object> resultMap, String msg,File file) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", msg);
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
		
		OutputStream out = response.getOutputStream(); 
		response.setContentType("application/octet-stream"); 
		response.addHeader("data", URLEncoder.encode(resultStr,"UTF-8")); 
		if (file!=null) {
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
		}
        
	}
	
//	public static void responseFile(HttpServletResponse response, List resultList, Map<String, Object> resultMap, String msg,File file) throws IOException {
//		Map<String, Object> map = new HashMap();
//		map.put("success", true);
//		map.put("msg", msg);
//		if (resultList!=null) {
//			map.put("resultList", resultList);
//		}else {
//			map.put("resultList", new ArrayList<>());
//		}
//		if (resultMap!=null) {
//			map.put("resultMap", resultMap);
//		}else {
//			map.put("resultMap", new HashMap<>());
//		}
//		String resultStr = JSON.toJSONString(map);
//		
//		OutputStream out = response.getOutputStream();  
//        response.addHeader("Content-Disposition", "attachment;filename="  
//                + file.getName()); 
//        response.setContentType("application/octet-stream"); 
//        response.addHeader("Content-Length", "" + file.length()); 
//        response.addHeader("FileName", "" + file.getName()); 
//        //�����ļ�֮�⣬�Ƿ�������ͷ������������
//        response.addHeader("HasData", "true"); 
//        System.out.println("FileName:" + file.getName());  
//        BufferedInputStream bis = null;
//        BufferedOutputStream bos = null;
//        int length = 0;   
//        try {
//        	bis = new BufferedInputStream(new FileInputStream(file));
// 	        bos = new BufferedOutputStream(out);
//        	byte[] bytes = new byte[2048]; 
//        	
//        	
//			byte[] bs = resultStr.getBytes();
//		    for(int i = 0; i<bs.length; i++) {
//		    	bytes[i] = bs[i];
//		    }
//			bos.write(bytes,0, bytes.length);
//			
// 	        int read;
// 	        while ((read = bis.read(bytes,0,bytes.length)) != -1) {
// 	        	length = length + read;
// 				bos.write(bytes, 0, read);
// 			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();;
//		}finally {
//			if (bis!=null) {
//				bis.close();
//			}
//			if (bos!=null) {
//				bos.close();
//			}
//		}
//       
//        System.out.println("size:" + length);   
//	}
}