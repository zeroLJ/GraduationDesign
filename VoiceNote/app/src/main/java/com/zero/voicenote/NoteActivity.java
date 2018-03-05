package com.zero.voicenote;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.KeyboardUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.zero.voicenote.database.DaoUtils;
import com.zero.voicenote.database.Note;
import com.zero.voicenote.database.NoteDao;
import com.zero.voicenote.util.Constant;
import com.zero.voicenote.util.JsonParser;
import com.zero.voicenote.util.WavMergeUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jaygoo.widget.wlv.WaveLineView;
import okhttp3.Call;
import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.http.OnResponseListener;
import zero.com.utillib.http.ResultData;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.DateUtils;
import zero.com.utillib.utils.object.ObjUtils;
import zero.com.utillib.utils.object.StringUtils;
import zero.com.utillib.utils.view.Alert;


public class NoteActivity extends BaseActivity {
    // 语音听写对象
    private SpeechRecognizer mIat;
    int ret = 0; // 函数调用返回值
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private EditText result_edt, title_edt;
    private Button play_bt;
    private Map<String, Object> data = new HashMap<>();
    private String addTime;
    private boolean isNewNote = true;
    private WaveLineView waveLineView;
    private RelativeLayout tab_layout;
    private String path;
    private List<String> pathList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setReturnEnable(true);
        waveLineView = findViewById(R.id.waveLineView);
        play_bt = findViewById(R.id.play_bt);
        result_edt = findViewById(R.id.result_edt);
        title_edt = findViewById(R.id.title_edt);
        tab_layout = findViewById(R.id.tab_layout);
        Intent intent = getIntent();
        if (intent.getSerializableExtra("data") != null){
            data = (Map<String, Object>) intent.getSerializableExtra("data");
            addTime = ObjUtils.objToStr(data.get("addTime"));
            title_edt.setText(ObjUtils.objToStr(data.get("title")));
            result_edt.setText(ObjUtils.objToStr(data.get("message")));
            top_right_tv.setText("保存");
            title_edt.setHint(ObjUtils.objToStr(data.get("addTime")).substring(0,10)+"的笔记");
            isNewNote = false;
            checkAudio();
        }else {
            addTime = DateUtils.getNowTime();
            title_edt.setHint(DateUtils.getNowDate()+"的笔记");
            top_right_tv.setText("完成");
        }

        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5a8b9c86");
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, initListener);
        setParam();


        File file = new File(Environment.getExternalStorageDirectory()+"/VoiceNote/.nomedia");
        if (!file.exists()){
            file.mkdirs();
        }
    }

    private boolean isSending = false;
    @Override
    protected void initListener() {
        super.initListener();
        top_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSending) return;
                isSending = true;
                save();
            }
        });

        findViewById(R.id.note_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyboardUtils.showSoftInput(result_edt);
                if (!result_edt.hasFocus()){
                    result_edt.setSelection(result_edt.getText().toString().length());
                }
                return false;
            }
        });

        waveLineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveLineView.stopAnim();
                tab_layout.setVisibility(View.VISIBLE);
                waveLineView.setVisibility(View.INVISIBLE);
                play_bt.setVisibility(View.VISIBLE);
                if (mIat.isListening()){
                    mIat.cancel();
                }
            }
        });

        findViewById(R.id.speak_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(NoteActivity.this);
                waveLineView.setVisibility(View.VISIBLE);
                tab_layout.setVisibility(View.GONE);
                waveLineView.startAnim();
                if (mediaPlayer!=null){
                    play_bt.performClick();
                }
                play_bt.setVisibility(View.GONE);
                getNewPath();
                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    Alert.toast("听写失败,错误码：" + ret);
                    waveLineView.performClick();
                } else {
                    Alert.toast("请开始说话");
                }
            }
        });

        findViewById(R.id.keyboard_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.toggleSoftInput();
                if (!result_edt.hasFocus()){
                    result_edt.requestFocus();
                    result_edt.setSelection(result_edt.getText().toString().length());
                }
            }
        });

        findViewById(R.id.set_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Logs.JLlog("s:"+result_edt.getSelectionStart());
//                Logs.JLlog("e:"+result_edt.getSelectionEnd());
//
//                PopupWindow popupWindow = new PopupWindow(NoteActivity.this);
//                View view = View.inflate(NoteActivity.this,R.layout.item_data,null);
//                popupWindow.setContentView(view);
//                popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                popupWindow.setFocusable(true);
//                popupWindow.setOutsideTouchable(true);
//                int[] location = new int[2];
//                v.getLocationOnScreen(location);
//                Logs.JLlog(""+location[0]);
//                Logs.JLlog(""+location[1]);
//                Logs.JLlog(""+v.getHeight());
//                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]-v.getHeight()-view.getHeight());
            }
        });

        play_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null){
                    mediaPlayer.release();
                    mediaPlayer = null;
                    play_bt.setBackgroundResource(R.mipmap.play);
                }else {
                    play();
                }
            }
        });
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//            Alert.toast("开始语音识别");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
//            if(mTranslateEnable && error.getErrorCode() == 14002) {
//                Alert.toast( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
//            } else {
                Alert.toast(error.getPlainDescription(true));
//            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            Alert.toast("结束语音识别");
            Logs.JLlog("end");
            mIat.cancel();
            getNewPath();
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                Alert.toast("听写失败,错误码：" + ret);
                waveLineView.performClick();
            }
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Logs.JLlog( results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
                Logs.JLlog("last");
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            Logs.wslog("当前正在说话，音量大小：" + volume);
            Logs.wslog( "返回音频数据："+data.length);
            waveLineView.setVolume((int) ((volume+0.3)*30));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        String result = result_edt.getText().toString() + text;
        result_edt.setText(result);
        result_edt.setSelection(result.length());
    }

    private InitListener initListener = new InitListener() {
        @Override
        public void onInit(int code) {
           Logs.JLlog("SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Alert.toast("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

//        this.mTranslateEnable = mSharedPreferences.getBoolean( this.getString(R.string.pref_key_translate), false );
//        if( mTranslateEnable ){
//            Log.i( TAG, "translate enable" );
//            mIat.setParameter( SpeechConstant.ASR_SCH, "1" );
//            mIat.setParameter( SpeechConstant.ADD_CAP, "translate" );
//            mIat.setParameter( SpeechConstant.TRS_SRC, "its" );
//        }

        String lag = "mandarin";
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

//            if( mTranslateEnable ){
//                mIat.setParameter( SpeechConstant.ORI_LANG, "en" );
//                mIat.setParameter( SpeechConstant.TRANS_LANG, "cn" );
//            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

//            if( mTranslateEnable ){
//                mIat.setParameter( SpeechConstant.ORI_LANG, "cn" );
//                mIat.setParameter( SpeechConstant.TRANS_LANG, "en" );
//            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "10000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,  "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
//        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
//        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"pcm");
    }

    private void getNewPath(){
        if (path == null || new File(path).exists()){
            path = Environment.getExternalStorageDirectory()+"/VoiceNote/"
                    +DateUtils.getFileNameByDate(DateUtils.StringDateTime(addTime))+"/temp/"+System.currentTimeMillis()+".wav";
            pathList.add(path);
            mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, path);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        waveLineView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        waveLineView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        waveLineView.release();
    }


    private MediaPlayer mediaPlayer;
    private void play(){
        mediaPlayer = new MediaPlayer();
//        File file = new File(Environment.getExternalStorageDirectory()+"/msc/"+ObjUtils.objToStr(data.get("addTime")),"1"+".wav");
        File file = null;
        ArrayList<File> fileList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ObjUtils.objToStr(data.get("audioPath")))){
            File f = new File(ObjUtils.objToStr(data.get("audioPath")));
            if (f.exists()){
                fileList.add(f);
            }
        }
        for (String path : pathList){
            File mf = new File(path);
            if (mf.exists()){
                fileList.add(new File(path));
            }
        }
        final String path;
        Logs.JLlog(""+fileList.size());
        if (fileList.size() == 1){
            file = fileList.get(0);
        }else {
            file = new File(Environment.getExternalStorageDirectory()+"/VoiceNote/"
                    +DateUtils.getFileNameByDate(DateUtils.StringDateTime(addTime))+"/temp/"+"temp.wav");
            try {
                WavMergeUtil.mergeWav(fileList, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileInputStream fis = null;
        try {
            path = file.getAbsolutePath();
            fis = new FileInputStream(file);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    play_bt.setBackgroundResource(R.mipmap.play);
                    new File(path).delete();
                }
            });
            mediaPlayer.start();
            play_bt.setBackgroundResource(R.mipmap.stop);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    private void merge(final List<String> filePathList, final String mergePath) {
//        Logs.JLlog("merge");
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<File> fileList = new ArrayList<>();
//                if (StringUtils.isNotEmpty(ObjUtils.objToStr(data.get("audioPath")))){
//                    File f = new File(ObjUtils.objToStr(data.get("audioPath")));
//                    if (f.exists()){
//                        fileList.add(f);
//                    }
//                }
//                for (String path : filePathList){
//                    fileList.add(new File(path));
//                }
//                File file = new File(mergePath);
//                if (fileList.size() == 1){
//                    fileList.get(0).renameTo(file);
//                    Logs.JLlog("wav移动完成");
//                    return;
//                }
//                try {
//                    WavMergeUtil.mergeWav(fileList, file);
//                    for (File f : fileList){
//                        f.delete();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Logs.JLlog("wav合并完成");
//            }
//        });
//    }


    private void save() {
        String title = title_edt.getText().toString().trim();
        if (StringUtils.isEmpty(title)){
            title = title_edt.getHint().toString();
        }
        String audioPath = ObjUtils.objToStr(data.get("audioPath"));
        Logs.JLlog("save" + pathList.size());
        if (pathList.size()>0){
            audioPath = Environment.getExternalStorageDirectory()+"/VoiceNote/"
                    +DateUtils.getFileNameByDate(DateUtils.StringDateTime(addTime))+"/"+System.currentTimeMillis()+".wav";
        }
        ArrayList<File> fileList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ObjUtils.objToStr(data.get("audioPath")))){
            File f = new File(ObjUtils.objToStr(data.get("audioPath")));
            if (f.exists()){
                fileList.add(f);
            }
        }
        for (String path : pathList){
            fileList.add(new File(path));
        }
        File file = null;
        if (StringUtils.isNotEmpty(audioPath)){
            file = new File(audioPath);
            if (fileList.size() == 1){
                fileList.get(0).renameTo(file);
                Logs.JLlog("wav移动完成");
            }else {
                try {
                    WavMergeUtil.mergeWav(fileList, file);
                    for (File f : fileList){
                        f.delete();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Logs.JLlog("wav合并完成");
            }
        }


        if (isNewNote){
            final Note note = new Note(null, HttpUtils.USER, title,result_edt.getText().toString(),
                    audioPath, DateUtils.getNowTime(), null, Constant.FLAG_ADD);
            if (!hasSignin()){
                DaoUtils.insert(note);
                setResult(Activity.RESULT_OK);
                finish();
                isSending = false;
                return;
            }
            Map<String,Object> map = new HashMap<>();
            map.put("data", JSON.toJSONString(note));
            if (file!=null){
                map.put("file", file);
            }
            HttpUtils.doPostFile("NoteAdd", map, new OnResponseListener() {
                @Override
                public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                    note.setFlag(Constant.FLAG_COMPLETE);
                    DaoUtils.insert(note);
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    DaoUtils.insert(note);
                }
                @Override
                public void OnFinal() {
                    super.OnFinal();
                    setResult(Activity.RESULT_OK);
                    finish();
                    isSending = false;
                    Logs.JLlog(note.toString());
                }
            });
        }else {
            final Note note = DaoUtils.query(Note.class, NoteDao.Properties.Id.eq(Long.valueOf(ObjUtils.objToStr(data.get("id"))))).get(0);
            note.setMessage(result_edt.getText().toString());
            note.setTitle(title);
            note.setEditTime(DateUtils.getNowTime());
            note.setAudioPath(audioPath);
            note.setFlag(Constant.FLAG_EDIT);
            if (!hasSignin()){
                DaoUtils.updata(note);
                setResult(Activity.RESULT_OK);
                finish();
                isSending = false;
                return;
            }

            Map<String,Object> map = new HashMap<>();
            map.put("data", JSON.toJSONString(note));
            if (file!=null){
                map.put("file", file);
            }
            HttpUtils.doPostFile("NoteUpdate", map, new OnResponseListener() {
                @Override
                public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                    note.setFlag(Constant.FLAG_COMPLETE);
                    DaoUtils.updata(note);
                }

                @Override
                public void onFailure(Call call, IOException e) {

                }
                @Override
                public void OnFinal() {
                    super.OnFinal();
                    isSending = false;
                    Logs.JLlog(note.toString());
                }
            });
            DaoUtils.updata(note);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }


    private void checkAudio(){

        if (StringUtils.isNotEmpty(ObjUtils.objToStr(data.get("audioPath")))){
            File file = new File(ObjUtils.objToStr(data.get("audioPath")));
            Logs.JLlog("1"+ file.getAbsolutePath());
            if (!file.exists()){
                Map<String, Object> map = new HashMap<>();
                map.putAll(data);
                HttpUtils.doDomnLoad("NoteGetAudio", data, file.getAbsolutePath(), new OnResponseListener() {
                    @Override
                    public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {

                    }

                    @Override
                    public void OnFinal() {
                        super.OnFinal();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                });

            }

        }
    }
}
