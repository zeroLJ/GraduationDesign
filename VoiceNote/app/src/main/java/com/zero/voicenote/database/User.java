package com.zero.voicenote.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zero on 2018/2/8.
 */

@Entity
public class User extends BaseEntity {
    @Unique
    @Id(autoincrement =true)
    private Long id;
    private String name;
    private String name_qq;
    private String name_sina;
    private String password;
    private String nickname;
    private String sex;
    private String birthday;
    @Generated(hash = 271671788)
    public User(Long id, String name, String name_qq, String name_sina,
            String password, String nickname, String sex, String birthday) {
        this.id = id;
        this.name = name;
        this.name_qq = name_qq;
        this.name_sina = name_sina;
        this.password = password;
        this.nickname = nickname;
        this.sex = sex;
        this.birthday = birthday;
    }
    @Generated(hash = 586692638)
    public User() {
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
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName_qq() {
        return this.name_qq;
    }
    public void setName_qq(String name_qq) {
        this.name_qq = name_qq;
    }
    public String getName_sina() {
        return this.name_sina;
    }
    public void setName_sina(String name_sina) {
        this.name_sina = name_sina;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
