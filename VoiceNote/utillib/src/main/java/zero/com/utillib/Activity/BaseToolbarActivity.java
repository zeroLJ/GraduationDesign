package zero.com.utillib.Activity;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zero.com.utillib.R;

/**
 * 只可控制toolbar（activity必须无ActionBar）
 * 要修改toolbar背景通过修改toolbar_layout的背景实现，如：toolbar_layout.setBackgroundResource(R.mipmap.navigation_image_a);
 */
public class BaseToolbarActivity extends BaseCommonActivity {
    protected RelativeLayout toolbar_layout;
    private Toolbar mToolbar;
    protected ImageView back_iv;
    protected TextView top_left_tv, top_center_tv, top_right_tv;
    //是否有侧滑栏
    protected boolean hasNavigationView = false;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View mView;
    @Override
    protected void initView() {
        super.initView();
        setToolBar(layoutResID);
    }

    //设置侧滑栏内容,必须在setContentView前调用,若要侧滑栏不遮盖toolbar，自行在布局文件使用NavigationView
    protected void setNavigationView(int layoutResID){
        hasNavigationView = true;
        mView = View.inflate(this, layoutResID, null);
    }

    protected void setToolBar(int mLayoutResID) {
        View view;
        if (hasNavigationView){
            view = View.inflate(this, R.layout.default_layout_toolbar_navigationview, null);
            navigationView = view.findViewById(R.id.navigationView);
            navigationView.addView(mView);
            drawerLayout = view.findViewById(R.id.drawerLayout);
        }else {
            view = View.inflate(this, R.layout.default_layout_toolbar, null);
        }
        mToolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        onCreateCustomToolBar(mToolbar);
        if (hasNavigationView){
//            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.app_name, R.string.bottom_sheet_behavior);
//            mDrawerToggle.syncState();
//            drawerLayout.setDrawerListener(mDrawerToggle);

            back_iv.setImageResource(R.mipmap.menu_icon);
            back_iv.setVisibility(View.VISIBLE);
            back_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isOpenNavigationView()){
                        openNavigationView();
                    }else {
                        closeNavigationView();
                    }

                }
            });
        }
        View contentView = View.inflate(this, mLayoutResID, null);
        FrameLayout center_layout = view.findViewById(R.id.center_layout);
        center_layout.addView(contentView);
        setContentView(view);

    }

    protected void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
        getLayoutInflater().inflate(R.layout.toobar_default_style, toolbar);
        toolbar.setNavigationIcon(null);
        top_left_tv = (TextView) toolbar.findViewById(R.id.left_tv);
        top_right_tv = (TextView) toolbar.findViewById(R.id.right_tv);
        top_center_tv = (TextView) toolbar.findViewById(R.id.center_tv);
        back_iv = (ImageView) toolbar.findViewById(R.id.back_iv);
        toolbar_layout = (RelativeLayout) toolbar.findViewById(R.id.toolbar_layout);
//        top_center_tv.setText(getString(R.string.app_name));
        top_center_tv.setText(getTitle());
    }

    protected void setReturnEnable(boolean enable){
        if (enable){
            back_iv.setImageResource(R.mipmap.back);
            back_iv.setVisibility(View.VISIBLE);
            back_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    onBackClick();
                }
            });
//            top_left_tv.setVisibility(View.GONE);
        }else {
            back_iv.setVisibility(View.GONE);
//            top_left_tv.setVisibility(View.VISIBLE);
        }
    }

    protected void setTitle(String title){
        top_center_tv.setText(title);
    }

    protected void onBackClick(){

    }

    protected void closeNavigationView(){
        if (drawerLayout!=null){
            drawerLayout.closeDrawer(navigationView);
//            navigationView.addView();
        }
    }

    protected void openNavigationView(){
        if (drawerLayout!=null){
            drawerLayout.openDrawer(navigationView);
//            navigationView.addView();
        }
    }

    protected boolean isOpenNavigationView(){
        return drawerLayout.isDrawerOpen(navigationView);
    }


}
