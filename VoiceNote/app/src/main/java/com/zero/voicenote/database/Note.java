package com.zero.voicenote.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zero on 2018/2/13.
 */
@Entity
public class Note extends BaseEntity {
    @Unique
    @Id(autoincrement =true)
    private Long id;
    @NotNull
    private String name;
    private String title;
    private String message;
    private String audioPath;
    @NotNull
    private String addTime;
    private String editTime;
    private String flag;//0为已经与服务器数据同步，1为新增，2为修改，3为删除
    @Generated(hash = 1581996020)
    public Note(Long id, @NotNull String name, String title, String message,
            String audioPath, @NotNull String addTime, String editTime,
            String flag) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.message = message;
        this.audioPath = audioPath;
        this.addTime = addTime;
        this.editTime = editTime;
        this.flag = flag;
    }
    @Generated(hash = 1272611929)
    public Note() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getAudioPath() {
        return this.audioPath;
    }
    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
    public String getAddTime() {
        return this.addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
    public String getEditTime() {
        return this.editTime;
    }
    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }
    public String getFlag() {
        return this.flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
   
}
