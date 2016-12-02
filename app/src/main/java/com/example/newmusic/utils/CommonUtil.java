package com.example.newmusic.utils;

import android.text.format.DateFormat;

/**
 * Created by Rock on 2016/11/10.
 */
public class CommonUtil {

    public static CharSequence format(int time){
        return DateFormat.format("mm:ss", time);
    }

}
