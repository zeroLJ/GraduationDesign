package zero.com.utillib.http;

import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zero.com.utillib.BuildConfig;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.ObjUtils;
import zero.com.utillib.utils.object.StringUtils;

/**
 * Created by zero on 2018/2/11.
 */

public class HttpUtils {
//    public static String URL = "http://2u02538w57.imwork.net:36920/VoiceNote/";//花生壳内网穿透用
//    public static String URL = "http://192.168.0.188:8081/VoiceNote/";//内网用
//    public static String URL = "http://192.168.0.111:8080/VoiceNote/";//内网用
//    public static String URL = "http://193.112.132.83:80/VoiceNote/";//腾讯云服务器公网
    public static String URL = "https://jhonliu.club/VoiceNote/";//腾讯云服务器公网
    public static String USER = "noUser";
    public static String PASSWORD = "";

    public static void doPost(String url, final OnResponseListener onResponseListener){
        doPost(url, null, onResponseListener);
    }
    public static void doPost(String url, Map<String,Object> map, final OnResponseListener onResponseListener){
       doPost(url, map,null,onResponseListener);
    }
    public static void doPost(String url, Map<String,Object> map, List<File> files, final OnResponseListener onResponseListener){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(20,TimeUnit.SECONDS);

        url = URL + url;
        Logs.JLlog(url);
        Request request=new Request.Builder().url(url)
                .post(getBody(map, files))
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
            public void onResponse(final okhttp3.Call call, final Response response) throws IOException {
                Logs.JLlog(("服务器返回代码：  " + response.code()));
//                String s = response.body().string();
                String s = URLDecoder.decode(ObjUtils.objToStr(response.header("data")), "UTF-8");
                Logs.JLlog("json:"+s);
                final ResultData resultData = new ResultData(s);
                if (resultData.isSuccess()){
                    Logs.JLlog("成功");
                    ActivityUtils.getTopActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onResponseListener.onSuccess(resultData.getResultList(), resultData);
                            onResponseListener.OnFinal();
                        }
                    });
                }else {
                    Logs.JLlog("有误");
                    ActivityUtils.getTopActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!resultData.getMsg().equals("")){
                                onResponseListener.OnError(resultData.getMsg());
                            }else {
                                onResponseListener.OnError("错误码：" + response.code());
                            }
                            onResponseListener.OnFinal();
                        }
                    });
                }

            }
        });
    }

    private static RequestBody getBody(Map<String,Object> params, List<File> files){
        RequestBody body;
        if (files == null || files.isEmpty()){
            FormBody.Builder formBodyBuild=new FormBody.Builder();
            formBodyBuild.add("name", USER);
            formBodyBuild.add("password", PASSWORD);
            //此处添加所需要的键值对
            if (params!=null){
                for (String key : params.keySet()){
                    formBodyBuild.add(key, ObjUtils.objToStr(params.get(key)));
                }
            }
            body = formBodyBuild.build();
        }else {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            builder.addFormDataPart("name", USER);
            builder.addFormDataPart("password", PASSWORD);
            if (params!=null){
                for (String key : params.keySet()) {
                    builder.addFormDataPart(key, params.get(key).toString());
                }
            }
            //追加文件

            for (int i = 0; i < files.size(); i++){
                File file = files.get(i);
//                builder.addFormDataPart(USER+"_0_"+key, file.getName(), RequestBody.create(null, file));
                builder.addFormDataPart("file_"+i, file.getName(), RequestBody.create(null, file));
            }
            body = builder.build();
        }
        return body;
    }


    //在服务器下载文件
    public static void doDomnLoad(String url, Map<String,Object> map, final String filePath, final OnResponseListener onResponseListener){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(20,TimeUnit.SECONDS);

        url = URL + url;
        Logs.JLlog("url:" + url);
        Request request=new Request.Builder().url(url)
//                .post(formBodyBuild.build())
                .post(getBody(map, null))
                .addHeader("Accept-Encoding", "identity")
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
            public void onResponse(final okhttp3.Call call, Response response) throws IOException {
                Logs.JLlog(("服务器返回代码：  " + response.code()));
                Logs.JLlog(("Content-Length:   " + response.header("Content-Length")));
                Logs.JLlog(("文件名:   " + response.header("FileName")));
                Logs.JLlog(("HasData：  " + response.header("HasData")));
                Logs.JLlog(("data：  " + response.header("data")));
//
                final ResultData resultData = new ResultData( URLDecoder.decode(ObjUtils.objToStr(response.header("data")), "UTF-8"));
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                if (StringUtils.isNotEmpty(response.header("FileName"))) {
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File file = new File(filePath);
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        //获取文件
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
//                        int progress = (int) (sum * 1.0f / total * 100);
//                        Logs.JLlog("progress=" + progress);
                        }
                        fos.flush();
                        Logs.JLlog("写入完成");
                        Logs.JLlog("文件下载成功");

                    } catch (Exception e) {
                        Logs.JLlog("文件下载失败");
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
                onResponseListener.onSuccess(resultData.getResultList(), resultData);
                onResponseListener.OnFinal();
            }
        });
    }


    //不需传参时，下载文件用
    public static void domnLoadFile(String url, final OnDomnLoadListener onDomnLoadListener){
        final String TAG = "ljl";
        Log.d(TAG, "开始下载");
        final String fileName = url.substring(url.lastIndexOf("/"));
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d(TAG, "下载失败");
                onDomnLoadListener.onFailure(call,e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                onDomnLoadListener.onStart();
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                File file = new File(Environment.getExternalStorageDirectory()+"/VoiceNote/" + fileName);
                if (!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        onDomnLoadListener.onDownLoad(progress);
                        Log.d(TAG, "progress=" + progress);
                    }
                    fos.flush();
                    onDomnLoadListener.onComplete(file);
                    Log.i(TAG, "文件下载成功");
                } catch (Exception e) {
                    onDomnLoadListener.onError(e);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    public interface OnDomnLoadListener{
        void onDownLoad(int progress);//正在下载，刷新进度
        void onStart();//开始下载前执行
        void onComplete(File file);//下载完成
        void onFailure(okhttp3.Call call, IOException e);//文件所在服务器返回错误
        void onError(Exception e);//文件下载中途发生错误
    }
}
