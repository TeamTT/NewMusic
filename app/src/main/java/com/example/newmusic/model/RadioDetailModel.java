package com.example.newmusic.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/28.
 */
public class RadioDetailModel implements Serializable {

    private String id;
    private String img;
    private String lrc;
    private String songName;
    private String singlerName;

    public RadioDetailModel(String id, String img, String lrc, String songName, String singlerName) {
        this.id = id;
        this.img = img;
        this.lrc = lrc;
        this.songName = songName;
        this.singlerName = singlerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinglerName() {
        return singlerName;
    }

    public void setSinglerName(String singlerName) {
        this.singlerName = singlerName;
    }
}
