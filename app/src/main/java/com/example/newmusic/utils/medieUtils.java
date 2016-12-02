package com.example.newmusic.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.example.newmusic.R;
import com.example.newmusic.model.Mp3Info;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by 15 on 2016/10/11.
 */

public class medieUtils {
    /**
     * 获取专辑封面的Uri
     */
    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");

    /**
     * 获取音乐的信息
     *
     * @param context
     * @param _id
     * @return
     */
    public static Mp3Info getMp3Info(Context context, long _id) {
        // Log.d("MediaUtil", MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "");
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media._ID + "=" + _id,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        );
        Mp3Info mp3Info = null;
        if (cursor.moveToNext()) {
            mp3Info = new Mp3Info();
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
            }
        }
        cursor.close();
        return mp3Info;
    }

    /**
     * 用于从数据库中查询歌曲的信息，保存在list当中
     *
     * @param context
     * @return
     */
    public static long[] getMap3InfoIds(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID},
                MediaStore.Audio.Media.DURATION + ">180000",
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        );
        long[] ids = null;
        if (cursor != null) {
            ids = new long[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                ids[i] = cursor.getLong(0);
            }
        }
        cursor.close();
        return ids;
    }

    /**
     * 用于从数据库中查询歌曲的信息，保存在list当中
     *
     * @param context
     * @return
     */
    public static ArrayList<Mp3Info> getMp3Infos(Context context) {
        //  Log.d("MedisUtil", MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "");
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.DURATION + ">=180000",
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        );
        ArrayList<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
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

    /**
     * 获取默认专辑的图片
     *
     * @param context
     * @param small
     * @return
     */
    public static Bitmap getDefaultArtwork(Context context, boolean small) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        if (small) {
            return BitmapFactory.decodeStream(context.getResources().
                    openRawResource(R.mipmap.music_album), null, opts);
        }
        return BitmapFactory.decodeStream(context.getResources()
                .openRawResource(R.mipmap.music_album), null, opts);
    }

    /**
     * 从文件当中获取专辑封面位图
     *
     * @param context
     * @param songid
     * @param albumid
     * @return
     */
    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
        Bitmap bm = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(albumArtUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }
            options.inSampleSize = 1;
            //只进行大小判断
            options.inJustDecodeBounds = true;
            //调用此方法得到options得到图片大小
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            //我们的目标是在800xel的滑脉显示
            //所以需要调用computeSampleSize得到图片缩放的比例
            options.inSampleSize = 100;
            //我们得到了缩放的比例，现在正式读入bitma数据
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            //根据options参数，减少所需要的内存
            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 获得封面对象位图
     *
     * @param context
     * @param song_id
     * @param album_id
     * @param allowdefault
     * @param small
     * @return
     */
    public static Bitmap getArtwork(Context context, long song_id, long album_id,
                                    boolean allowdefault, boolean small) {
        if (album_id < 0) {
            if (song_id < 0) {
                Bitmap bm = getArtworkFromFile(context, song_id, -1);
                if (bm != null) {
                    return bm;
                }
            }
            if (allowdefault) {
                return getDefaultArtwork(context, small);
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(albumArtUri, album_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                //先制定原始大小
                options.inSampleSize = 1;
                //只进行大小判断
                options.inJustDecodeBounds = true;
                //调用此方法得到options得到图片的大小
                BitmapFactory.decodeStream(in, null, options);
                //我们的目标实在n pixel的画面上显示，所以调用computeSampleSize得到图片的比例
                //这里的target为800是默认专辑图片大小决定的，800只是测试数字但是实验后发现完美的结合
                if (small) {
                    options.inSampleSize = computeSampleSize(options, 40);
                } else {
                    options.inSampleSize = computeSampleSize(options, 600);
                }
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, options);
            } catch (FileNotFoundException e) {
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null && allowdefault) {
                            return getDefaultArtwork(context, small);
                        }
                    }
                } else if (allowdefault) {
                    bm = getDefaultArtwork(context, small);
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 对图片进行合适的缩放
     *
     * @param options
     * @param target
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int target) {
        int w = options.outWidth;
        int h = options.outHeight;
        int candidateW = w / target;
        int candidateH = h / target;
        int candidate = Math.max(candidateW, candidateH);
        if (candidate == 0) {
            return 1;
        }
        if (candidate > 1) {
            if ((w > target) && (w / candidate) < target) {
                candidate -= 1;
            }
        }
        if (candidate > 1) {
            if ((h > target) && (h / candidate) < target) {
                candidate -= 1;
            }
        }
        return candidate;
    }
}