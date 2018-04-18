package com.zero.voicenote;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.bugly.beta.Beta;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;
import com.zero.voicenote.database.DaoUtils;
import com.zero.voicenote.database.Note;
import com.zero.voicenote.database.NoteDao;
import com.zero.voicenote.util.Constant;
import com.zero.voicenote.util.ShareUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import zero.com.utillib.adapter.CommoAdapter;
import zero.com.utillib.adapter.ViewHolder;
import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.http.OnResponseListener;
import zero.com.utillib.http.ResultData;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.DateUtils;
import zero.com.utillib.utils.object.ObjUtils;
import zero.com.utillib.utils.object.StringUtils;
import zero.com.utillib.utils.view.Alert;


public class MainActivity extends BaseActivity implements WbShareCallback {
    private Adapter adapter;
    private List<Map<String, Object>> data = new ArrayList<>();;
    private SlideAndDragListView listView;
    private TextView name_tv;
    private ImageView head_icon_iv;
    private int rotation;
    //接收QQ授权结果
    private IUiListener iUiListener;
    //新浪分享接口实例，用于接收返回数据
    private WbShareHandler shareHandler;
    //新浪授权接口实例，用于接收返回数据
    SsoHandler mSsoHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationView(R.layout.navigationview_main);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        name_tv = findViewById(R.id.name_tv);
        head_icon_iv = findViewById(R.id.head_icon_iv);
        if (hasSignin()){
//            top_right_tv.setText("同步");
//            top_right_tv.setBackground(getResources().getDrawable(R.drawable.refresh_white));
            top_right_iv.setImageResource(R.drawable.refresh_white);
            top_right_iv.setPadding(ConvertUtils.dp2px(8),ConvertUtils.dp2px(8),ConvertUtils.dp2px(8),ConvertUtils.dp2px(8));
            name_tv.setText(App.spUtils.getString(Constant.Nickname,HttpUtils.USER));
        }

//        recyclerView = findViewById(R.id.recyclerView);
//        data = new ArrayList<>();
//        data.add(new HashMap<String, Object>());
//        data.add(new HashMap<String, Object>());
//        data.add(new HashMap<String, Object>());
//        adapter = new DataAdapter(this, data);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(
//                this, DividerItemDecoration.VERTICAL));
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(new DataAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                Alert.toast(""+position);
//            }
//        });

        if (hasSignin()){
            final File file = new File(Environment.getExternalStorageDirectory()+"/VoiceNote/" + HttpUtils.USER + "icon.jpg");
            if (file.exists()){
                head_icon_iv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
            HttpUtils.doDomnLoad("HeadIconGet",null, file.getAbsolutePath(), new OnResponseListener() {
                @Override
                public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                    head_icon_iv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    Logs.JLlog("设置");
                }
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void OnError(String error) {

                }
            });
        }
        initListview();
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.add_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivityForResult(intent,100);
            }
        });
        top_left_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openNavigationView();

            }
        });

        top_right_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AnimationDrawable animationDrawable = (AnimationDrawable) top_right_iv.getDrawable();
//                animationDrawable.start();
//                rotation = rotation + 90;
//                top_right_iv.animate().rotation(rotation);
                Animation rotateAnimation  = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setDuration(1000);
                rotateAnimation.setRepeatCount(0);
                rotateAnimation.setInterpolator(new LinearInterpolator());
                top_right_iv.startAnimation(rotateAnimation);
                List<Map<String, Object>> list = new ArrayList<>();
                final List<Note> notes = DaoUtils.query(Note.class, NoteDao.Properties.Name.eq(HttpUtils.USER), NoteDao.Properties.Flag.notEq(Constant.FLAG_COMPLETE));
                for (Note note : notes){
                    list.add(note.toMap());
                }
                Logs.JLlog(list.toString());
                Map<String, Object> map = new HashMap<>();
                map.put("data", JSON.toJSONString(list));
                showProgressDialog("正在同步，请稍后...");
                HttpUtils.doPost("NoteRefresh", map, new OnResponseListener() {
                    @Override
                    public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                        DaoUtils.getDao(Note.class).deleteAll();
                        DaoUtils.insert(Note.class, data);
                        Alert.toast("同步完成");
                        refreshData();
                    }

                    @Override
                    public void OnFinal() {
                        super.OnFinal();
                        dismissProgressDialog();
                    }
                });
            }
        });

        findViewById(R.id.logout_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert.alertDialogTowBtn("是否要退出当前帐号？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                        startActivity(intent);
                        App.spUtils.put(Constant.IsLogin, false);
                        finish();
                    }
                });
            }
        });

        head_icon_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(true){
//                    ShareUtil.saveCurrentImage(MainActivity.this);
//                    iUiListener = ShareUtil.shareToQQ_image(MainActivity.this, ShareUtil.getImagePath(MainActivity.this));
//                    return;
                    shareHandler = ShareUtil.shareToSINA(MainActivity.this,ShareUtil.getImagePath(MainActivity.this));
                    return;
                }*/

                PictureSelector.create(MainActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.default_style)
                        .selectionMode(PictureConfig.SINGLE)
                        .enableCrop(true)// 是否裁剪 true or false
                        .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                        .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                        .previewVideo(false)// 是否可预览视频 true or false
                        .withAspectRatio(1,1)//裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .enablePreviewAudio(false) // 是否可播放音频 true or false
                        .rotateEnabled(false)// 裁剪是否可旋转图片 true or false
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });

        findViewById(R.id.checkUpgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.checkUpgrade();
            }
        });
        findViewById(R.id.languageSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.spinner).performClick();
            }
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this,R.style.dialog);
                View view = View.inflate(MainActivity.this, R.layout.dialog_share, null);
                view.findViewById(R.id.wechat).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareUtil.shareToWeChat_web(MainActivity.this, ShareUtil.url);
                    }
                });
                view.findViewById(R.id.timeline).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareUtil.shareToTimeline_web(MainActivity.this, ShareUtil.url);
                    }
                });
                view.findViewById(R.id.qq).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iUiListener = ShareUtil.shareToQQ_web(MainActivity.this,ShareUtil.getMipmapPath(MainActivity.this, R.mipmap.note), ShareUtil.url);
                        return;
                    }
                });
                view.findViewById(R.id.weibo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareUtil.saveCurrentImage(MainActivity.this);
                        shareHandler = ShareUtil.shareToSINA_web(MainActivity.this, ShareUtil.url);
                    }
                });
                dialog.setContentView(view);
                Window window = dialog.getWindow();
                //设置弹出窗口大小
                window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //设置显示位置
                window.setGravity(Gravity.BOTTOM);
                //设置动画效果
                window.setWindowAnimations(R.style.AnimBottom);
                dialog.show();
            }
        });


    }

    private void initListview() {
        final Menu menu = new Menu(false, 0);//第1个参数表示滑动 item 是否能滑的过头，像弹簧那样( true 表示过头，就像 Gif 中显示的那样；false 表示不过头，就像 Android QQ 中的那样)
        menu.addItem(new MenuItem.Builder().setWidth(400)
                .setBackground(new ColorDrawable(getResources().getColor((R.color.red_delete))))
//                .setText("删除")
                .setTextColor(Color.WHITE)
                .setDirection(MenuItem.DIRECTION_RIGHT)//设置方向 (默认方向为 DIRECTION_LEFT )
                .setIcon(getResources().getDrawable(R.mipmap.delete))// set icon
                .build());

        listView.setMenu(menu);
        listView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, final int itemPosition, int buttonPosition, int direction) {
                switch (direction) {
                    case MenuItem.DIRECTION_LEFT:
                        switch (buttonPosition) {
                            case 0:
                                return Menu.ITEM_SCROLL_BACK;
                        }
                        break;
                    case MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition) {
                            case 0:
                                Alert.alertDialogTowBtn("确定要删除该笔记？", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final long id = ObjUtils.objToLong(data.get(itemPosition).get("id"));
                                        Note note = DaoUtils.query(Note.class, NoteDao.Properties.Id.eq(id)).get(0);
                                        note.setFlag(Constant.FLAG_DELETE);
                                        File file = new File(Environment.getExternalStorageDirectory()+"/VoiceNote/" + HttpUtils.USER + "/"
                                                + DateUtils.getFileNameByDate(DateUtils.StringDateTime(note.getAddTime())));
                                        deleteFile(file);
                                        DaoUtils.updata(note);
                                        refreshData();
                                        Map<String,Object> map = new HashMap<>();
                                        map.put("data", JSON.toJSONString(note));
                                        HttpUtils.doPost("NoteDelete", map, new OnResponseListener() {
                                            @Override
                                            public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                                                DaoUtils.delete(Note.class, NoteDao.Properties.Id.eq(id));
                                            }

                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                            }
                                        });
                                    }
                                });
                                return Menu.ITEM_SCROLL_BACK;
                        }
                        break;
                    default :
                        return Menu.ITEM_NOTHING;
                }
                return Menu.ITEM_NOTHING;
            }
        });
        adapter = new Adapter(this,data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = data.get(position);
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("data",(Serializable) map);
                startActivityForResult(intent, 100);
            }
        });

        listView.setOnDragDropListener(new SlideAndDragListView.OnDragDropListener() {
            Map t;
            @Override
            public void onDragViewStart(int beginPosition) {
                t = data.get(beginPosition);
            }

            @Override
            public void onDragDropViewMoved(int fromPosition, int toPosition) {
                Map map = data.remove(fromPosition);
                data.add(toPosition, map);
            }

            @Override
            public void onDragViewDown(int finalPosition) {
                data.set(finalPosition, t);
            }
        });
        refreshData();
    }

    class Adapter extends CommoAdapter<Map<String, Object>> {

        public Adapter(Context context, List<Map<String, Object>> data) {
            super(context, data, R.layout.item_data);
        }

        @Override
        public void convert(ViewHolder holder, Map<String, Object> map, int position) {
            holder.setTextView(R.id.title, ObjUtils.objToStr(map.get("title")));
            if (StringUtils.isEmpty(ObjUtils.objToStr(map.get("message")))){
                holder.setTextView(R.id.message, "点击进行编辑");
            }else {
                holder.setTextView(R.id.message, ObjUtils.objToStr(map.get("message")));
            }
            holder.setTextView(R.id.time, ObjUtils.objToStr(map.get("addTime")).substring(0,16));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (isOpenNavigationView()){
                closeNavigationView();
            }else {
                Alert.alertDialogTowBtn("是否要退出" + getString(R.string.app_name) + "?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityUtils.finishAllActivities();
                    }
                });
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
//        if (resultCode == Activity.RESULT_OK){
//            refreshData();
//        }

        if (iUiListener!= null){
            //此句很关键，不写此句则无法获取QQ授权后返回的信息
            Tencent.onActivityResultData(requestCode,resultCode,intent,iUiListener);
            iUiListener = null;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    refreshData();
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(intent);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    LocalMedia media = selectList.get(0);
                    String path = media.getPath();
                    String path_cut = path;
                    if (media.isCut()){
                        path_cut = media.getCutPath();
                    }
                    if (media.isCompressed()){
                        path_cut = media.getCompressPath();
                    }
                    head_icon_iv.setImageBitmap(BitmapFactory.decodeFile(path_cut));
                    Logs.JLlog(path);
                    Logs.JLlog(path_cut);
                    Map<String,Object> map = new HashMap<>();
                    File file = new File(path_cut);
                    if (file!=null){
                        map.put("file", file);
                    }
                    HttpUtils.doPostFile("HeadIconChange", map, new OnResponseListener() {
                        @Override
                        public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                        }
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void OnFinal() {
                            super.OnFinal();
                        }
                    });
                    break;
            }
        }
    }

    private void refreshData(){
        data.clear();
//        List<Note> notes = DaoUtils.query(Note.class, NoteDao.Properties.Name.eq(HttpUtils.USER), NoteDao.Properties.Flag.notEq(Constant.FLAG_DELETE));
        List<Note> notes = DaoUtils.getDao(Note.class).queryBuilder()
                .where( NoteDao.Properties.Name.eq(HttpUtils.USER), NoteDao.Properties.Flag.notEq(Constant.FLAG_DELETE))
                .orderDesc(NoteDao.Properties.EditTime, NoteDao.Properties.AddTime)
                .list();
        for (Note note : notes){
            data.add(note.toMap());
        }
        adapter.notifyDataSetChanged();
//        Logs.JLlog(data.toString());
//        Logs.JLlog(DaoUtils.query(Note.class, NoteDao.Properties.Name.eq(HttpUtils.USER)).toString());
    }

    private void deleteFile(File file) {
        Logs.JLlog("delete: " + file.getAbsolutePath());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }


    //以下方法都是新浪微博分享相关
    @Override
    public void onWbShareSuccess() {
        Toast.makeText(this,"分享成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWbShareCancel() {
        Toast.makeText(this,"取消分享",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWbShareFail() {
        Toast.makeText(this,"分享失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (shareHandler!= null){
            shareHandler.doResultIntent(intent,this);
            shareHandler = null;
        }

    }
}
