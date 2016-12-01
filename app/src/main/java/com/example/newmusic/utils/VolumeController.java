package com.example.newmusic.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Android 中控制系统音量的是一个AudioManager，也是一个系统的服务
 *
 */
public class VolumeController {
    /**
     * 调高音量
     * @param context
     * @param yDelta     负值
     * @param screenWidth
     */
    public static void turnUp(Context context, float yDelta, int screenWidth){
        // 获取AudioManager
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 获取媒体的最大值
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取当前音量值
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 计算出变化的值
        int change = (int) (0.5 * maxVolume * yDelta / screenWidth);
        // 计算结果值
        int endVolume = Math.min(maxVolume, currentVolume - change);
        // 将最终值设置到音量上  三个参数  ① 设置的音频音量的类型  ② 我们要设置的音量值  ③ 我们设置系统所作出反应的一个标记
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,endVolume,AudioManager.FLAG_SHOW_UI);
    }

    public static void turnDown(Context context, float yDelta, int screenWidth){
        // 获取AudioManager
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 获取媒体的最大值
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取当前音量值
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 计算出变化的值
        int change = (int) (0.5 * maxVolume * yDelta / screenWidth);
        // 计算结果值
        int endVolume = Math.max(0, currentVolume - change);
        // 将最终值设置到音量上  三个参数  ① 设置的音频音量的类型  ② 我们要设置的音量值  ③ 我们设置系统所作出反应的一个标记
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,endVolume,AudioManager.FLAG_SHOW_UI);
    }



}
