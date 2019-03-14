package zero.com.utillib.rxhttp;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.view.Alert;

/**
 * Created by ljl on 2019/3/1.
 */

public abstract class RxHttpObserver implements Observer<Response> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Response o) {
        String result = "";
        try {
            result = o.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logs.JLlog("--response--"+ result);
        ResponseParams responseParams = JSON.parseObject(result, ResponseParams.class);
        if (responseParams.isSuccess()){
            onSuccessResult(responseParams.getData(), responseParams);
        }else {
            onFailResult(responseParams.getMsg(), responseParams);
        }
    }

    @Override
    public void onError(Throwable e) {
        Alert.toast(e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccessResult(List<Map<String, Object>> list, ResponseParams obj);

    public void onFailResult(String error, ResponseParams obj){
        Alert.alertDialogOneBtn(error);
    }

}
