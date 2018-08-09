package com.zero.voicenote;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.zero.voicenote.util.Constant;

import java.io.IOException;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.http.OnResponseListener;
import zero.com.utillib.http.ResultData;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.DateUtils;
import zero.com.utillib.utils.object.ObjUtils;
import zero.com.utillib.utils.object.StringUtils;
import zero.com.utillib.utils.view.Alert;

public class InfoActivity extends BaseActivity {
    private EditText nickname_et, job_et, telephone_et, e_mail_et;
    private TextView sex_tv, birthday_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setReturnEnable(true);
        nickname_et = findViewById(R.id.nickname);
        job_et = findViewById(R.id.job);
        telephone_et = findViewById(R.id.telephone);
        e_mail_et = findViewById(R.id.e_mail);
        sex_tv = findViewById(R.id.sex);
        birthday_tv = findViewById(R.id.birthday);
        nickname_et.setText(App.spUtils.getString(Constant.Nickname, HttpUtils.USER));
        top_right_tv.setText("保存");
        birthday_tv.setText("");
        showProgressDialog("正在获取数据...");
        HttpUtils.doPost("InfoGet", new OnResponseListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                Logs.JLlog(data.toString());
                if (data != null && data.size() > 0){
                    Map<String, Object> map = data.get(0);
                    nickname_et.setText(ObjUtils.objToStr(map.get("nickname")));
                    sex_tv.setText(ObjUtils.objToStr(map.get("sex")));
                    birthday_tv.setText(ObjUtils.objToStr(map.get("birthday")));
                    job_et.setText(ObjUtils.objToStr(map.get("job")));
                    telephone_et.setText(ObjUtils.objToStr(map.get("telephone")));
                    e_mail_et.setText(ObjUtils.objToStr(map.get("e_mail")));
                }
            }

            @Override
            public void OnFinal() {
                super.OnFinal();
                dismissProgressDialog();
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        sex_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                View view = View.inflate(InfoActivity.this, R.layout.dialog_sex,null);
                final RadioGroup radioGroup = view.findViewById(R.id.radiogroup);
                final String sex = sex_tv.getText().toString();
                if (sex.equals("男")){
                    radioGroup.check(R.id.male);
                }else if (sex.equals("女")){
                    radioGroup.check(R.id.female);
                }
                builder.setView(view);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (radioGroup.getCheckedRadioButtonId()){
                            case R.id.male:
                                sex_tv.setText("男");
                                break;
                            case R.id.female:
                                sex_tv.setText("女");
                                break;
                        }
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });

        birthday_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = 0;
                int month = 0;
                int day = 0;
                if (birthday_tv.getText().toString().equals("")){
                    Calendar calendar =Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR) - 20;
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                }else {
                    String date = birthday_tv.getText().toString();
                    try {
                        String[] s = date.split("-");
                        year = ObjUtils.objToInt(s[0]);
                        month = ObjUtils.objToInt(s[1]) - 1;
                        day = ObjUtils.objToInt(s[2]);
                    }catch (Exception e){}
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(InfoActivity.this,DatePickerDialog.THEME_HOLO_LIGHT,null, year, month, day);
                final DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMaxDate(System.currentTimeMillis());
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth()+1;
                        int day = datePicker.getDayOfMonth();
                        String date = year+"-"+String.format("%02d", month)+"-"+String.format("%02d", day);
                        birthday_tv.setText(date);
                    }
                });
                datePickerDialog.show();
            }
        });
        top_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nickName = nickname_et.getText().toString().trim();
                if (StringUtils.isEmpty(nickName)){
                    nickname_et.requestFocus();
                    nickname_et.setSelection(nickname_et.getText().toString().length());
                    Alert.toast("昵称不能为空！");
                    return;
                }
                String telephone = telephone_et.getText().toString().trim();
                if (StringUtils.isNotEmpty(telephone) && !StringUtils.isMobile(telephone)){
                    telephone_et.requestFocus();
                    telephone_et.setSelection(telephone_et.getText().toString().length());
                    Alert.toast("请输入正确的手机号码！");
                    return;
                }

                String e_mail= e_mail_et.getText().toString().trim();
                e_mail  = e_mail.replace(" ","");
                if (StringUtils.isNotEmpty(e_mail) && !RegexUtils.isEmail(e_mail)){
                    e_mail_et.requestFocus();
                    e_mail_et.setSelection(e_mail_et.getText().toString().length());
                    Alert.toast("请输入正确的邮箱！");
                    return;
                }

                String sex = sex_tv.getText().toString();
                String birthday = birthday_tv.getText().toString();
                String job = job_et.getText().toString().trim();
                Map<String, Object> params = new HashMap<>();
                params.put("nickName",nickName);
                params.put("sex",sex);
                params.put("birthday",birthday);
                params.put("job",job);
                params.put("telephone",telephone);
                params.put("e_mail", e_mail);
                showProgressDialog("正在保存...");
                Logs.JLlog(params.toString());
                HttpUtils.doPost("InfoUpdate", params, new OnResponseListener() {
                    @Override
                    public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                        Alert.toast("保存成功");
                        App.spUtils.put(Constant.Nickname, nickName);
//                        finish();
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


}
