package com.zero.voicenote;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;
import com.zero.voicenote.database.DaoUtils;
import com.zero.voicenote.database.Note;
import com.zero.voicenote.database.NoteDao;
import com.zero.voicenote.util.Constant;


import java.io.IOException;
import java.io.Serializable;
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
import zero.com.utillib.utils.object.ObjUtils;
import zero.com.utillib.utils.view.Alert;


public class MainActivity extends BaseActivity {
    private Adapter adapter;
    private List<Map<String, Object>> data = new ArrayList<>();;
    private SlideAndDragListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        if (hasSignin()){
            top_left_tv.setText(HttpUtils.USER);
            top_right_tv.setText("同步");
        }else {
            top_left_tv.setText("登录");
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
                if (hasSignin()){
                    Alert.alertDialogTowBtn("是否要退出登录？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                            startActivity(intent);
                            App.spUtils.put(Constant.IsLogin, false);
                            finish();
                        }
                    });
                }else {
                   Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                   startActivity(intent);
                   finish();
                }
            }
        });

        top_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Logs.JLlog("data:"+data.toString());
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
    }

    private void initListview() {
        final Menu menu = new Menu(false, 0);//第1个参数表示滑动 item 是否能滑的过头，像弹簧那样( true 表示过头，就像 Gif 中显示的那样；false 表示不过头，就像 Android QQ 中的那样)
        menu.addItem(new MenuItem.Builder().setWidth(400)
                .setBackground(new ColorDrawable(Color.RED))
                .setText("删除")
                .setTextColor(Color.WHITE)
                .setDirection(MenuItem.DIRECTION_RIGHT)//设置方向 (默认方向为 DIRECTION_LEFT )
//                .setIcon(getResources().getDrawable(R.mipmap.back))// set icon
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
            holder.setTextView(R.id.message, ObjUtils.objToStr(map.get("message")));
            holder.setTextView(R.id.time, ObjUtils.objToStr(map.get("addTime")).substring(0,16));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Alert.alertDialogTowBtn("是否要退出？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    App.appExit();
                }
            });
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK){
            refreshData();
        }
    }

    private void refreshData(){
        data.clear();
        List<Note> notes = DaoUtils.query(Note.class, NoteDao.Properties.Name.eq(HttpUtils.USER), NoteDao.Properties.Flag.notEq(Constant.FLAG_DELETE));
        for (Note note : notes){
            data.add(note.toMap());
        }
        adapter.notifyDataSetChanged();
        Logs.JLlog(data.toString());
        Logs.JLlog(DaoUtils.query(Note.class, NoteDao.Properties.Name.eq(HttpUtils.USER)).toString());
    }
}
