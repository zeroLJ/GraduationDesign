package main;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ResponseParams {
	public List<?> resultList;
	public Map<String, Object> resultMap;
	public String msg;
	public File file;
	public boolean success = true;// Ä¬ÈÏ³É¹¦

	public ResponseParams() {
	
	}

	public ResponseParams(List<?> resultList, Map<String, Object> resultMap, String msg, File file, boolean success) {
		this.file = file;
		this.msg = msg;
		this.resultList = resultList;
		this.resultMap = resultMap;
		this.success = success;
	}

	public static ResponseParams failResult(String msg) {
		return new ResponseParams(null, null, msg, null, false);
	}

	public static ResponseParams successResult() {
		return new ResponseParams(null, null, null, null, true);
	}

	public static ResponseParams successResultList(List<?> resultList) {
		return new ResponseParams(resultList, null, null, null, true);
	}

	public static ResponseParams successResultMap(Map<String, Object> resultMap) {
		return new ResponseParams(null, resultMap, null, null, true);
	}
	
	public static ResponseParams successResultFile(File file) {
		return new ResponseParams(null, null, null, file, true);
	}
}
