package zero.com.utillib.http;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        okHttpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(20,TimeUnit.SECONDS);
        FormBody.Builder formBodyBuild=new FormBody.Builder();
        formBodyBuild.add("name", USER);
        formBodyBuild.add("password", PASSWORD);
        //此处添加所需要的键值对
        if (map!=null){
            for (String key : map.keySet()){
                formBodyBuild.add(key, ObjUtils.objToStr(map.get(key)));
            }
        }
        Logs.JLlog("u" + URL);
        url = URL + url;
        Request request=new Request.Builder().url(url)
                .post(formBodyBuild.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final okhttp3.Call call, final IOException e) {
                Logs.JLlog("失败");
                Logs.JLlog(e.getMessage());
                ActivityUtils.getTopActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onResponseListener.onFailure(call, e);
                        onResponseListener.OnFinal();
                    }
                });
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
                    onResponseListener.OnFinal();
                }else {
                    Logs.JLlog("有误");
                    onResponseListener.OnError(resultData.getMsg());
                    onResponseListener.OnFinal();
                }

            }
        });
    }


    public static void doPost(String url, final OnResponseListener onResponseListener){
        doPost(url, null, onResponseListener);
    }
}
