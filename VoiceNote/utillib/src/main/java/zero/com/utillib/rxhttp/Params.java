package zero.com.utillib.rxhttp;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljl on 2019/3/4.
 *
 */

public class Params {
    private String url;
    private Map<String, String> params;//参数
    private List<File> files; //上传文件的列表
    public Params(@NonNull String url){
        init(url, null, null);
    }
    public Params(@NonNull String url, Map<String, String> params){
        init(url, params, null);
    }
    public Params(@NonNull String url, Map<String, String> params, List<File> files){
        init(url, params, files);
    }

    private void init(@NonNull String url, Map<String, String> params, List<File> files){
        if (params == null){
            params = new HashMap<>();
        }
        if (files == null){
            files = new ArrayList<>();
        }
        this.url = url;
        this.params = params;
        this.files = files;
    }
    public String getUrl() {
        return url;
    }
    public Map<String, String> getParams() {
        return params;
    }

    public List<File> getFiles() {
        return files;
    }
}
