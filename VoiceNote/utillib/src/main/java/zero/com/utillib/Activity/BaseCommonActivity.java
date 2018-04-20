package zero.com.utillib.Activity;

import android.app.ProgressDialog;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseCommonActivity extends AppCompatActivity {
    protected int layoutResID;
    private ProgressDialog progressDialog;
    public static MApplication app = MApplication.getInstance();
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        this.layoutResID = layoutResID;
        initView();
        BarUtils.setStatusBarAlpha(this,50);//设置状态栏透明
    }


    protected void initView(){
    }

    //设置状态栏
    protected void setStatusBar(){

    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
//        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent2(MessageEvent event) {
        Toast.makeText(this, event.message+"3", Toast.LENGTH_SHORT).show();
    }

    // This method will be called when a SomeOtherEvent is posted
    @Subscribe
    public void handleSomethingElse(SomeOtherEvent event) {
        Toast.makeText(this, event.message + "2", Toast.LENGTH_SHORT).show();
    }

    protected void showProgressDialog(String msg){
        showProgressDialog(msg, false);
    }
    protected void showProgressDialog(String msg, boolean cancelAble){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(cancelAble);
        progressDialog.show();
    }

    protected void dismissProgressDialog(){
        progressDialog.dismiss();
    }
}
