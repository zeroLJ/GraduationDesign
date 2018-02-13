package com.zero.voicenote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setReturnEnable(true);
        top_right_tv.setText("完成");
    }
}
