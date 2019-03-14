package zero.com.utillib.rxhttp;


import java.io.IOException;
import java.util.Map;

import io.reactivex.ObservableEmitter;
import okhttp3.Call;
import okhttp3.Response;
import zero.com.utillib.utils.Logs;

/**
 * Created by ljl on 2019/3/4.
 */

public class RxHttp extends RxHttpBase {
    public static String URL = "https://jhonliu.club/VoiceNote/";//腾讯云服务器公网
    /**
     * 写入通用参数
     * @param params
     */
    @Override
    protected void initParams(Map<String, String> params) {
        super.initParams(params);

    }

    @Override
    protected String getUrl(Map<String, String> params, String url) {
        String finalUrl;
        if (params.get("url") != null) {
            finalUrl = params.get("url") + "/" + url;
        } else {
            finalUrl = URL + "/" + url;
        }
        return finalUrl;
    }

    @Override
    protected void onFailure(Call call, IOException e, ObservableEmitter<Response> ob) {
        super.onFailure(call, e, ob);
        e.printStackTrace();
        String typeErrMsg = e.getMessage();
        Logs.errlog(e.getLocalizedMessage(), e);
        if (typeErrMsg != null) {
            if (typeErrMsg.indexOf("Network is unreachable") > 0) {
                ob.onError(new Throwable("网络状态不佳，请稍后重试！"));
            } else if (typeErrMsg.indexOf("isConnected failed") > 0) {
                ob.onError(new Throwable("网络服务器已经关闭，请联系系统管理员！"));
            } else {
                ob.onError(new Throwable("网络状态不佳，请稍后重试！"));
            }
        } else {
            ob.onError(new Throwable("网络状态不佳，请稍后重新操作！**"));
        }
    }

    @Override
    protected void onResponse(Call call, Response response, ObservableEmitter<Response> ob) throws IOException {
        if (response.isSuccessful()) {
            super.onResponse(call, response, ob);
        } else {
            ob.onError(new Throwable("网络服务器代码没有同步，请联系管理员！"));
        }
    }
}
