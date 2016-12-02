package com.example.newmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/28.
 */
public class MvMode implements Parcelable{
    private  String image;
    private  String url;
    private  String title;
    private  String views;
    private  String regtime;
    private  String id;


    protected MvMode(Parcel in) {
        image = in.readString();
        url = in.readString();
        title = in.readString();
        views = in.readString();
        regtime = in.readString();
        id = in.readString();
    }

    public static final Creator<MvMode> CREATOR = new Creator<MvMode>() {
        @Override
        public MvMode createFromParcel(Parcel in) {
            return new MvMode(in);
        }

        @Override
        public MvMode[] newArray(int size) {
            return new MvMode[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(image);
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(views);
        dest.writeString(regtime);
        dest.writeString(id);
    }


}
