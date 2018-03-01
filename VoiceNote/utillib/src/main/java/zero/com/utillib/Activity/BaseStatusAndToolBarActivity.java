package zero.com.utillib.Activity;

import android.graphics.Color;

import com.yanzhenjie.sofia.Sofia;


/**
 * 可控制状态栏&&toolbar（activity必须无ActionBar）
 * 调用Sofia改变状态栏会导致 软件盘弹出时，底部控件被挡住。（有此需求不能使用此类）
 * 若单纯需要改变状态栏背景色，可使用BaseToolbarActivity并修改AppTheme中的colorPrimaryDark
 */
public class BaseStatusAndToolBarActivity extends BaseToolbarActivity {
    @Override
    protected void initView() {
        super.initView();
        setStatusBar();
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

}
