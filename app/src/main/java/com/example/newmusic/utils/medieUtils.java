package com.example.newmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.newmusic.model.Mp3Info;

import java.util.ArrayList;

/**
 * Created by 15 on 2016/10/11.
 */

public class medieUtils {

    public static ArrayList<Mp3Info> getMp3Infos(Context context) {
        //  Log.d("MedisUtil", MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "");
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.DURATION + ">=180000",
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        );
        ArrayList<Mp3Info> mp3Infos = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            Mp3Info mp3Info = new Mp3Info();
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic != 0) {
                mp3Info.id = id;
                mp3Info.title = title;
                mp3Info.artist = artist;
                mp3Info.album = album;
                mp3Info.albumId = albumId;
                mp3Info.duration = duration;
                mp3Info.size = size;
                mp3Info.url = url;
                mp3Infos.add(mp3Info);
            }
        }
        cursor.close();
        return mp3Infos;
    }

    /**
     * 格式话时间，将毫秒转为分：秒格式
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

}