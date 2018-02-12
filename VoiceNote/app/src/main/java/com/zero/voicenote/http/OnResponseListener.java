package com.zero.voicenote.http;

import java.util.List;
import java.util.Map;

/**
 * Created by zero on 2018/2/11.
 */

public abstract class OnResponseListener {
    //服务器返回失败信息
    public void OnError(String error){

    }
    //成功请求数据
    public abstract void onSuccess(List<Map<String, Object>> data, ResultData resultData);

    //网络问题导致请求失败
    public void onFailure(){

    };
}
