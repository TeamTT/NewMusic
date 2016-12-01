package com.example.newmusic.utils;

import android.app.Activity;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 系统亮度是系统中的配置文件
 * ContentResolver 获取（设置）系统的亮度信息等
 * 需使用 写系统设置权限
 */
public class LightnessController {

    public static void turnUp(Activity context, float yDelta, int screenWidth) {
        try {
            // 获取当前系统中的亮度值
            int currentLight = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
            // 系统亮度的最大值 就是我们的 '1', 0 - 255
            // 计算变化值
            float change = 1.5f * 255 * yDelta / screenWidth;
            // 需要获取我们页面窗口亮度属性
            WindowManager.LayoutParams attributes = context.getWindow().getAttributes();
            //  系统亮度值  0 - 1，需要将 0-255的值转换为 0-1
            attributes.screenBrightness = Math.min(255, currentLight - change) / 255;
            // 将属性设置回窗口
            context.getWindow().setAttributes(attributes);
            // 将亮度信息存入系统中
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) Math.min(255, currentLight - change));

        } catch (Exception e) {
            Toast.makeText(context, "系统不允许修改亮度", Toast.LENGTH_SHORT).show();
        }

    }

    public static void turnDown(Activity context, float yDelta, int screenWidth) {
        try {
            // 获取当前系统中的亮度值
            int currentLight = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
            // 系统亮度的最大值 就是我们的 '1', 0 - 255
            // 计算变化值
            float change = 1.5f * 255 * yDelta / screenWidth;
            // 需要获取我们页面窗口亮度属性
            WindowManager.LayoutParams attributes = context.getWindow().getAttributes();
            //  系统亮度值  0 - 1，需要将 0-255的值转换为 0-1
            attributes.screenBrightness = Math.max(0, currentLight - change) / 255;
            // 将属性设置回窗口
            context.getWindow().setAttributes(attributes);
            // 将亮度信息存入系统中
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) Math.max(0, currentLight - change));

        } catch (Exception e) {
            Toast.makeText(context, "系统不允许修改亮度", Toast.LENGTH_SHORT).show();
        }

    }

}
