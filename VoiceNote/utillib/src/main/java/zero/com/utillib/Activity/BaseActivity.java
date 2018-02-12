package zero.com.utillib.Activity;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yanzhenjie.sofia.Sofia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseActivity extends AppCompatActivity {
    protected int layoutResID;
    public static MApplication app = MApplication.getInstance();
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        this.layoutResID = layoutResID;
        initView();
    }

    protected void initView(){
    }

    //设置状态栏
    protected void setStatusBar(){
        Sofia.with(this)
                .statusBarBackground(Color.WHITE)
                .statusBarDarkFont();
//        // 状态栏深色字体。
//        .statusBarDarkFont();
//
//        // 状态栏浅色字体。
//        .statusBarLightFont();
//
//        // 状态栏背景色。
//        .statusBarBackground(int statusBarColor);
//
//        // 状态栏背景Drawable。
//        .statusBarBackground(Drawable drawable);
//
//        // 状态栏背景透明度。
//        .statusBarBackgroundAlpha(int alpha);
//
//        // 导航栏背景色。
//        .navigationBarBackground(int navigationBarColor);
//
//        // 导航栏背景Drawable。
//        .navigationBarBackground(Drawable drawable);
//
//        // 导航栏背景透明度。
//        .navigationBarBackgroundAlpha(int alpha);
//
//        // 内容入侵状态栏。
//        .invasionStatusBar();
//
//        // 内容入侵导航栏。
//        .invasionNavigationBar();
//
//        // 让某一个View考虑状态栏的高度，显示在适当的位置，接受ViewId。
//        .fitsSystemWindowView(int viewId);
//
//        // 让某一个View考虑状态栏的高度，显示在适当的位置，接受View。
//        .fitsSystemWindowView(View view);
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
    public void handleSomethingElse(SomeOtherEventEvent event) {
        Toast.makeText(this, event.message + "2", Toast.LENGTH_SHORT).show();
    }
}
