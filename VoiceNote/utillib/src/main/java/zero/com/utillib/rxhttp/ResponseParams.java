package zero.com.utillib.rxhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zero.com.utillib.utils.object.ObjUtils;

/**
 * 返回的结果,根据实际字段名进行修改
 */
public class ResponseParams implements Serializable {

    public boolean success;
    public String msg;
    public JSONObject mapStr;
    public JSONArray data;

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return ObjUtils.objToStr(msg);
    }

    public Map<String, Object> getMap() {
        if (mapStr != null) {
            return JSON.parseObject(mapStr.toJSONString(), Map.class);
        }
        return new HashMap<>();
    }


    public List<Map<String,Object>> getData() {
        if (data != null) {
            return JSON.parseObject(data.toJSONString(), List.class);
        }
        return new ArrayList<>();

    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}