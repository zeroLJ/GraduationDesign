package zero.com.utillib.http;

import android.content.DialogInterface;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import zero.com.utillib.utils.view.Alert;

/**
 * Created by zero on 2018/2/11.
 */

public abstract class OnResponseListener {
    //服务器返回失败信息
    public void OnError(String error){
        Alert.alertDialogOneBtn(error, "服务器发生错误", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    //成功请求数据
    public abstract void onSuccess(List<Map<String, Object>> data, ResultData resultData);

    //网络问题导致请求失败
    public void onFailure(okhttp3.Call call, final IOException e){
        if (e instanceof SocketTimeoutException){
            Alert.toast("连接超时，请稍后重试");
        }else if (e instanceof ConnectException){
            Alert.toast("网络异常，请稍后重试");
        }else {
            Alert.toast("网络异常，请稍后重试");
        }

    };

    //无论成功还是失败，最后调用
    public void OnFinal(){

    }
}
