package com.zero.voicenote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zero.com.utillib.adapter.CommoAdapter;
import zero.com.utillib.adapter.ViewHolder;
import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.utils.object.ObjUtils;
import zero.com.utillib.utils.view.Alert;


public class MainActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private List<Map<String, Object>> data;
    private SlideAndDragListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        if (hasSignin()){
            top_left_tv.setText(HttpUtils.USER);
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
                startActivity(intent);
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
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
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
                                return Menu.ITEM_SCROLL_BACK;
                        }
                        break;
                    default :
                        return Menu.ITEM_NOTHING;
                }
                return Menu.ITEM_NOTHING;
            }
        });

        data = new ArrayList<>();
        data.add(new HashMap<String, Object>());
        data.add(new HashMap<String, Object>());
        data.add(new HashMap<String, Object>());
        listView.setAdapter(new Adapter(this,data));
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

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
    }

    class Adapter extends CommoAdapter<Map<String, Object>> {

        public Adapter(Context context, List<Map<String, Object>> data) {
            super(context, data, R.layout.item_data);
        }

        @Override
        public void convert(ViewHolder holder, Map<String, Object> map, int position) {
            holder.setTextView(R.id.title, ObjUtils.objToStr(position));
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
}
