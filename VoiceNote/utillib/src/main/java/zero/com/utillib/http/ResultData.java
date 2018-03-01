package zero.com.utillib.http;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.ObjUtils;

/**
 * Created by zero on 2018/2/11.
 */

public class ResultData {
    private boolean success;
    private String msg;
    private Map<String, Object> resultMap;
    private List<Map<String, Object>> resultList;

    public ResultData(boolean success, String msg, Map<String, Object> resultMap, List<Map<String, Object>> resultList){
        setSuccess(success);
        setMsg(msg);
        setResultMap(resultMap);
        setResultList(resultList);
    }

    public ResultData(String json){
        Map map = JSON.parseObject(json, Map.class);
        boolean success = ObjUtils.objToBoolean(map.get("success"));
        String msg = ObjUtils.objToStr(map.get("msg"));
        Map<String, Object> resultMap = (Map<String, Object>) map.get("resultMap");
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) map.get("resultList");
        Logs.JLlog("success"+success);
        Logs.JLlog("msg"+msg);
        Logs.JLlog("resultMap"+resultMap.toString());
        Logs.JLlog("resultList"+resultList.toString());
        setSuccess(success);
        setMsg(msg);
        setResultMap(resultMap);
        setResultList(resultList);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    public List<Map<String, Object>> getResultList() {
        return resultList;
    }

    public void setResultList(List<Map<String, Object>> resultList) {
        this.resultList = resultList;
    }
}
