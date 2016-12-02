package com.example.newmusic.fragment;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.newmusic.MvActivity;
import com.example.newmusic.R;
import com.example.newmusic.alipay.PayResult;
import com.example.newmusic.alipay.SignUtils;
import com.example.newmusic.custom.MyDialog;
import com.example.newmusic.interfaces.ModeCallBack;
import com.example.newmusic.model.MvMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;


public class IntroduceFragment extends Fragment implements View.OnClickListener, PlatformActionListener, Handler.Callback {

    public static final String TAG = IntroduceFragment.class.getSimpleName();

    private View layout;
    private TextView mContent;
    private TextView mData;
    private TextView mNumber;
    private TextView mShare;
    private TextView mReward;
    private TextView mDownLoad;
    private MvMode mvMode;
    private View mError;
    private PopupWindow sharePopupWindow;
    private View mQqShare;
    private View mQqZoneShare;
    private View mWechatShare;
    private View mMomentsShare;
    private NotificationManager manager;


    // 商户PID 都是以2088开头 阿里巴巴的谐音
    public static final String PARTNER = "2088111278561763";
    // 商户收款账号,基本都是邮箱
    public static final String SELLER = "gaoyandingzhi@126.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANEClM9ja39OuhbiFcPYG8nUt19TIGvnBjC2CGMV3BKY2pTolVuicMfM0yyxvwtewe7Wkk+06Zl8fjgIWZS8SsfOeznQZbJq236CbcFYIhDsorDllDwQ0Uk409WSjaOCDJamOjGeQjYqy3D7v+z+Z48ZvCOPleX2h415mHQeHWVdAgMBAAECgYB6FrHqOr7uTIRzHXltPu1shi7fJeWIYhjBl3NqvbghvNvho8KrFkYez8yDDQj1kVJjOz+YA6t4lrn77RS2xw4+fRJgBy/LD9ILectaThysuFt84yKooSuFAv1AQKMeVXkpnFuzzBFtxyuRPtPUYXftSvEm/9BapFHGEVCuT7RvAQJBAP9yq18VFhPQAfngld9n0NwmCO33kdbFYqVIWBNKZdvVZIqwIvnmTqsgQacrvWutsWauukKT7VzySkht/uE63j0CQQDRdjgqx4H7SfMjkaZK5nJ6ptuFgR19HkakOJZSIM78Ot3PzfHcnfYuCRjs8lIEWmhYqj2FE+BcZ9cejphGuTWhAkB0XimBXBq9ldGAonXD2whDcbQ5q8EtJKgmgUlWKFs0hQaTQ1/7lZYa0Mv3uq5EwlCBZXGGaNsFr351dl5Y/jdFAkA6D2DmSsL22rqwo1DK9jHJWbMDwJRh+CBwqNbSERIOzGprjZR7KLXycMcd9tVRK5Y87YN7/dR1CLuSVshS4kfBAkAW6ls9/RlBA6gOpDuq+Qn4CZUng3h7OJsDgzCY95RtuMISJNuVFcGC/XVKB+urkyfhR/H7I8HIPXQtNJenH9f2";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";
    private static final int PAY_FLAG = 100;

    private Handler mHandler = new Handler(this);
    private MyDialog myDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_introduce, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMv();
        initView();
        setupView();
    }

    private void getMv() {
        Intent intent = getActivity().getIntent();
        mvMode = ((MvMode) intent.getParcelableExtra("mv"));
        Log.e(TAG, "getMv: " + mvMode);
    }

    private void setupView() {
        mContent.setText(mvMode.getTitle());
        mNumber.setText(Integer.parseInt(mvMode.getViews()) / 1000 + "万");
        String format = new SimpleDateFormat("yyyy.MM.dd").format(new Date(Long.parseLong("1478585487")));
        mData.setText(format);
    }

    private void initView() {
        mContent = (TextView) layout.findViewById(R.id.teach_mv_content);
        mData = (TextView) layout.findViewById(R.id.teach_mv_data);
        mNumber = (TextView) layout.findViewById(R.id.teach_mv_number);
        mShare = (TextView) layout.findViewById(R.id.teach_mv_share);
        mReward = (TextView) layout.findViewById(R.id.teach_mv_reward);
        mDownLoad = (TextView) layout.findViewById(R.id.teach_mv_download);
        mShare.setOnClickListener(this);
        mReward.setOnClickListener(this);
        mDownLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teach_mv_share:
                View view = LayoutInflater.from(getContext()).inflate(R.layout.share_pou_window, null);
                sharePopupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
                sharePopupWindow.showAsDropDown(view);
                mError = view.findViewById(R.id.teach_mv_error);
                mQqShare = view.findViewById(R.id.teach_mv_qq_share);
                mQqZoneShare = view.findViewById(R.id.teach_mv_qq_zone_share);
                mWechatShare = view.findViewById(R.id.teach_mv_wechat_share);
                mMomentsShare = view.findViewById(R.id.teach_mv_moments_share);
                mError.setOnClickListener(this);
                mQqShare.setOnClickListener(this);
                mQqZoneShare.setOnClickListener(this);
                mWechatShare.setOnClickListener(this);
                mMomentsShare.setOnClickListener(this);
                break;
            case R.id.teach_mv_reward:
                myDialog = new MyDialog(getContext());
                myDialog.setDisplay();
                myDialog.setOnclick(this);
                break;
            case R.id.teach_mv_download:
                Toast.makeText(getContext(), "已添加到下载列表", Toast.LENGTH_SHORT).show();
                downloadMv();
                break;
            case R.id.teach_mv_error:
                sharePopupWindow.dismiss();
                break;
            case R.id.teach_mv_qq_share:
                showQQShare();
                break;
            case R.id.teach_mv_qq_zone_share:
                showQqZoneShare();
                break;
            case R.id.teach_mv_wechat_share:
                showWeChatShare();
                break;
            case R.id.teach_mv_moments_share:
                showWeBoShare();
                break;
            case R.id.teach_mv_reward_error:
                myDialog.dismiss();
                break;
            case R.id.teach_mv_pay:

                String orderInfo = getOrderInfo("", "", myDialog.getEditText().getText().toString());
                String sign = sign(orderInfo);
                try {
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // ② 拼接完整订单
                final String pay = orderInfo + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";
                // 构建PayTask支付
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        //
                        PayTask payTask = new PayTask(getActivity());
                        String payResult = payTask.pay(pay, true);
                        Message message = Message.obtain();
                        message.what = PAY_FLAG;
                        message.obj = payResult;
                        mHandler.sendMessage(message);
                    }
                };
                thread.start();
                break;
        }
    }

    private void downloadMv() {
        manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.download)
                .setContentTitle(mvMode.getTitle())
                .setProgress(100, 0, false)
                .setContentText("正在下载。。。");
        manager.notify(100, builder.build());
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(mvMode.getUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    if (connection.getResponseCode() == 200) {
                        InputStream inputStream = connection.getInputStream();
                        FileOutputStream outputStream = new FileOutputStream(Environment.getExternalStorageDirectory());
                        byte buffer[] = new byte[1024 * 8];
                        int len = 0;
                        int total = connection.getContentLength();
                        int current = 0;
                        while ((len = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, len);
                            current += len;
                            int progress = (int) (current * 1.0f / total * 100);
                            Log.e(TAG, "run: " + progress);
                            builder.setProgress(100, progress, false);
                            manager.notify(100, builder.build());
                        }
                        builder.setContentText("下载完成");
                        manager.notify(100, builder.build());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        };
        thread.start();

    }

    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    private void showQQShare() {
        ShareSDK.initSDK(getContext());
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle("");
        sp.setTitleUrl(mvMode.getUrl()); // 标题的超链接
        sp.setText(mvMode.getTitle());
        sp.setImageUrl(mvMode.getImage());
        sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");

        Platform qzone = ShareSDK.getPlatform(QQ.NAME);
        qzone.setPlatformActionListener(this); // 设置分享事件回调
// 执行图文分享
        qzone.share(sp);
    }

    private void showQqZoneShare() {
        ShareSDK.initSDK(getContext());
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle("");
        sp.setTitleUrl(mvMode.getUrl()); // 标题的超链接
        sp.setText(mvMode.getTitle());
        sp.setImageUrl(mvMode.getImage());
        sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");

        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
        qzone.setPlatformActionListener(this); // 设置分享事件回调
// 执行图文分享
        qzone.share(sp);
    }

    private void showWeChatShare() {
        ShareSDK.initSDK(getContext());
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setTitle("");
        sp.setTitleUrl(mvMode.getUrl()); // 标题的超链接
        sp.setText(mvMode.getTitle());
        sp.setImageUrl(mvMode.getImage());
        sp.setShareType(Platform.SHARE_VIDEO);
        sp.setUrl(mvMode.getUrl());
        sp.setSite("发布分享的网站名称");
        sp.setSiteUrl("发布分享网站的地址");

        Platform qzone = ShareSDK.getPlatform(Wechat.NAME);
        qzone.setPlatformActionListener(this); // 设置分享事件回调
// 执行图文分享
        qzone.share(sp);
    }

    private void showWeBoShare() {
        ShareSDK.initSDK(getContext());
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setText(mvMode.getTitle());
        sp.setImageUrl(mvMode.getImage());
        Platform qzone = ShareSDK.getPlatform(SinaWeibo.NAME);
        qzone.setPlatformActionListener(this); // 设置分享事件回调
// 执行图文分享
        qzone.share(sp);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case PAY_FLAG:
                // 支付结果转换为PayResult对象
                PayResult payResult = new PayResult((String) msg.obj);
                // 获取支付结果状态
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    Toast.makeText(getContext(), "支付成功", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.equals(resultStatus, "8000")) {
                    Toast.makeText(getContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "支付失败", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return true;
    }
}
