package com.zero.voicenote.http;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.ObjUtils;

/**
 * Created by zero on 2018/2/11.
 */

public class HttpUtils {
    public static String URL = "http://192.168.0.188:8081/VoiceNote/";
    public static String USER = "";
    public static String PASSWORD = "";
    public static void doPost(String url, Map<String,Object> map, final OnResponseListener onResponseListener){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBodyBuild=new FormBody.Builder();
        formBodyBuild.add("name", USER);
        formBodyBuild.add("password", PASSWORD);
        //此处添加所需要的键值对
        if (map!=null){
            for (String key : map.keySet()){
                formBodyBuild.add(key, ObjUtils.objToStr(map.get(key)));
            }
        }
        url = URL + url;
        Request request=new Request.Builder().url(url)
                .post(formBodyBuild.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Logs.JLlog("失败");
                Logs.JLlog(e.getMessage());
                onResponseListener.onFailure();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String s = response.body().string();
                Logs.JLlog("json:"+s);
                Map map = JSON.parseObject(s, Map.class);
                Logs.JLlog("map:"+map.toString());
                ResultData resultData = new ResultData(s);
                if (resultData.isSuccess()){
                    Logs.JLlog("成功");
                    onResponseListener.onSuccess(resultData.getResultList(), resultData);
                }else {
                    Logs.JLlog("有误");
                    onResponseListener.OnError(resultData.getMsg());
                }

            }
        });
    }


    public static void doPost(String url, final OnResponseListener onResponseListener){
        doPost(url, null, onResponseListener);
    }
}
