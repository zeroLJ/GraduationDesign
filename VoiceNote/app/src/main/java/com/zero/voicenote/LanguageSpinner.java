package com.zero.voicenote;


import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.zero.voicenote.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zero.com.utillib.adapter.CommoAdapter;
import zero.com.utillib.adapter.ViewHolder;

/**
 * Created by zero on 2018/3/17.
 */

public class LanguageSpinner extends android.support.v7.widget.AppCompatSpinner {
    private List<Map<String,String>> spinnerList = new ArrayList<>();
    private SpinnerAdapter spinnerAdapter;
    public LanguageSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LanguageSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LanguageSpinner(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        Map<String,String> map = new HashMap<String,String>();
        map.put("language", "mandarin");
        map.put("text", "普通话");
        spinnerList.add(map);
        map = new HashMap<String,String>();
        map.put("language", "cantonese");
        map.put("text", "粤语");
        spinnerList.add(map);
        map = new HashMap<String,String>();
        map.put("language", "lmz");
        map.put("text", "四川话");
        spinnerList.add(map);
        map = new HashMap<String,String>();
        map.put("language", "henanese");
        map.put("text", "河南话");
        spinnerList.add(map);
        map = new HashMap<String,String>();
        map.put("language", "en_us");
        map.put("text", "英语");
        spinnerList.add(map);
        map = new HashMap<String,String>();
        map.put("language", null);
        map.put("text", "其他");
        spinnerList.add(map);
        spinnerAdapter = new CommoAdapter<Map<String, String>>(context, spinnerList, R.layout.support_simple_spinner_dropdown_item) {

            @Override
            public void convert(ViewHolder holder, Map<String, String> data, int position) {
                holder.setTextView(android.R.id.text1, data.get("text"));
                ((TextView)holder.getView(android.R.id.text1)).setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            }
        };
        this.setAdapter(spinnerAdapter);
        this.setSelection(App.spUtils.getInt(Constant.Select_Position, 0));
        this.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = spinnerList.get(position);
                String lag = map.get("language");
                App.spUtils.put(Constant.Select_Position, position);
                App.spUtils.put(Constant.Select_Language, lag);
                if (onItemSelectedListener!=null){
                    onItemSelectedListener.onItemSelected(spinnerList.get(position), position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    interface OnItemSelectedListener{
        void onItemSelected(Map<String,String> item, int position);
    }
    private OnItemSelectedListener onItemSelectedListener;
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener){
        this.onItemSelectedListener = onItemSelectedListener;
    }
}
