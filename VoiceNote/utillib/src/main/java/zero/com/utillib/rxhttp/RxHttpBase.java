package zero.com.utillib.rxhttp;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.ObjUtils;

/**
 * Created by ljl on 2019/3/1.
 */

public class RxHttpBase{
    private static OkHttpClient client = new OkHttpClient();
    protected RxHttpBase(){
    }

    public Observable<Response> getObservable(Params p){
        return getObservable(p, true);
    }

    public  Observable<Response> getObservable(final Params p, final boolean isDialog) {
//        new Params(null);
        return Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(final ObservableEmitter<Response> ob) throws Exception {
                String url = p.getUrl();
                Map<String,String> params = p.getParams();
                List<File> files = p.getFiles();

                String finalUrl = getUrl(params,url);
                Logs.JLlog("--url--" + finalUrl);
                try {
                    new URL(finalUrl);
                } catch (MalformedURLException e) {
                    ob.onError(new Throwable("url格式有误"));
                    return;
                }
                initParams(params);
                final Request request = getRequest(params, files, finalUrl);
                // 设置请求的超时时间
                int timeoutMillis = getTimeoutMillis();
                if (params.containsKey("timeoutMillis")){
                    timeoutMillis = ObjUtils.objToInt(params.get("timeoutMillis"));
                }
               /* if (isDialog) {
                    BaseActivity.dialogShow("请等待...", "正在加载数据...");
                }*/
                client.newBuilder()
                        .readTimeout(timeoutMillis, TimeUnit.MILLISECONDS)//
                        .build()
                        .newCall(request)
                        .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        /*if (isDialog) {
                            BaseActivity.dialogDismiss();
                        }*/
                        RxHttpBase.this.onFailure(call, e, ob);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        /*if (isDialog) {
                            BaseActivity.dialogDismiss();
                        }*/
                        RxHttpBase.this.onResponse(call,response,ob);
                    }
                });
            }
        }).subscribeOn(Schedulers.io())//在io线程发请求
                .observeOn(AndroidSchedulers.mainThread());//在主线程处理结果
    }

    private static MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static Request getRequest(Map<String, String> params, List<File> files, String finalUrl) {
        Request request;
        if (files != null) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (int i = 0; i < files.size(); i++) {
                File f = files.get(i);
                if (f != null) {
                    builder.addFormDataPart("img", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
                }
            }
            if (!params.isEmpty()) {
                // map 里面是请求中所需要的 key 和 value
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
            request = new Request.Builder().url(finalUrl).post(builder.build()).build();
        } else {
            FormBody.Builder formBody = new FormBody.Builder();
            if (!params.isEmpty()) {
                // map 里面是请求中所需要的 key 和 value
                for (String key : params.keySet()) {
                    formBody.add(key, ObjUtils.objToStr(params.get(key)));
                }
            }
            request = new Request.Builder().url(finalUrl).post(formBody.build()).build();
        }
        return request;
    }

    protected int getTimeoutMillis(){
        return 9000;//默认超时时间(单位:毫秒),默认查询时间9秒
    }

    protected String getUrl(Map<String, String> params, String url){
        return url;
    }

    protected void initParams(Map<String, String> params) {

    }

    protected void onFailure(Call call, IOException e,ObservableEmitter<Response> ob){

    }

    protected void onResponse(Call call, Response response,ObservableEmitter<Response> ob) throws IOException{
        ob.onNext(response);
    }

}
