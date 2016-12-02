package com.example.newmusic.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newmusic.MusicDetailsActivity;
import com.example.newmusic.PlayService;
import com.example.newmusic.R;
import com.example.newmusic.adapter.RadioRecyclerAdapter;
import com.example.newmusic.contants.HttpParams;

import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Random;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by Administrator on 2016/11/28.
 */
public class MusicDetailFragment extends BaseFragment implements PlayService.MusicUpdateLinstener, RadioRecyclerAdapter.SendPosition, SeekBar.OnSeekBarChangeListener, MusicDetailLyricFragment.Send, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = MusicDetailFragment.class.getSimpleName();
    private static final int SUCESSFUL = 100;
    private static final int PROGRESS = 101;
    private static final int SUCCESSFORIMAGE = 102;
    private View layout;
    private TextView mTitle;
    private ImageView mImage;
    private SeekBar mSeekBar;
    private TextView mCurrtime;
    private TextView mTotaltime;
    private MusicDetailsActivity musicDetailsActivity;

    private SimpleDateFormat simpleDateFormat;

    private int position;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCESSFUL) {
                if (playService != null) {
                    playService.play(position);
                }
            } else if (msg.what == PROGRESS) {
                mCurrtime.setText(simpleDateFormat.format((Integer) msg.obj));
                mSeekBar.setProgress((int) ((Integer) msg.obj * 1.0f / playService.getDuration() * 100));
            } else if (msg.what == SUCCESSFORIMAGE) {

                mDanmakuView.prepare(parser, danmakuContext);

            }
        }
    };
    private DanmakuView mDanmakuView;

    private boolean isShowDanmu = false;

    private DanmakuContext danmakuContext;

    private boolean isInall = false;

    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };
    private ImageView mBack;
    private ImageView mMore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicDetailLyricFragment.setSend(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        musicDetailsActivity = (MusicDetailsActivity) activity;
        musicDetailsActivity.setSendPosition(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        bindPlayService();
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unbindPlayService();
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isShowDanmu = false;
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_musicdetail, container, false);

        if (playService != null) {
            playService.setMusicUpdateLinstener(this);
        }

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        mSeekBar.setOnSeekBarChangeListener(this);

        simpleDateFormat = new SimpleDateFormat("mm:ss");

    }

    public void setupView(int position) {
        mTitle.setText(MusicDetailsActivity.data.get(position).getSongName());
        x.image().bind(mImage, MusicDetailsActivity.data.get(position).getImg());
        if (playService != null) {
            mTotaltime.setText(simpleDateFormat.format(playService.getDuration()));
        }
        handler.sendEmptyMessage(SUCCESSFORIMAGE);
    }

    private void initView() {
        mTitle = ((TextView) layout.findViewById(R.id.activity_music_details_title));
        mImage = ((ImageView) layout.findViewById(R.id.activity_music_details_image));
        mBack = ((ImageView) layout.findViewById(R.id.activity_music_details_back));
        mMore = ((ImageView) layout.findViewById(R.id.activity_music_details_more));
        mSeekBar = ((SeekBar) layout.findViewById(R.id.activity_music_details_SeekBar));
        mCurrtime = ((TextView) layout.findViewById(R.id.activity_music_details_currtime));
        mTotaltime = ((TextView) layout.findViewById(R.id.activity_music_details_total));

        mBack.setOnClickListener(this);
        mMore.setOnClickListener(this);


        mDanmakuView = (DanmakuView) layout.findViewById(R.id.activity_music_details_DanmakuView);
        mDanmakuView.enableDanmakuDrawingCache(true);

        mDanmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                isShowDanmu = true;
                mDanmakuView.start();
                generateSomeDanmaku();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext = DanmakuContext.create();


    }


    @Override
    public void onPublish(final int progress) {

    }

    @Override
    public void onChage(int currentIndex) {

    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void sendForDownload(int position) {

    }

    @Override
    public void sendForPlay(int position) {
        setupView(position);

        Log.e(TAG, "sendForPlay: " + position);

        playService.pose();

        this.position = position;

        handler.sendEmptyMessage(SUCESSFUL);

    }

    @Override
    public void sendImageView(ImageView imageView) {
        if (playService.isplay()) {
            playService.pose();
            imageView.setImageResource(R.mipmap.playsc);
        } else {
            playService.start();
            imageView.setImageResource(R.mipmap.pasuesc);
        }
    }

    @Override
    public void sendPlayMode(ImageView imageView) {
        int mode = (int) imageView.getTag();
        switch (mode) {
            case PlayService.ORDER_PLAY:
                imageView.setImageResource(R.mipmap.random);
                imageView.setTag(PlayService.RANDOM_PLAY);
                playService.setPlay_mode(PlayService.RANDOM_PLAY);
                Toast.makeText(getActivity(), "随机播放", Toast.LENGTH_SHORT).show();
                break;
            case PlayService.RANDOM_PLAY:
                imageView.setImageResource(R.mipmap.onlyome);
                imageView.setTag(PlayService.SINGLE_PLAY);
                playService.setPlay_mode(PlayService.SINGLE_PLAY);
                Toast.makeText(getActivity(), "单曲循环", Toast.LENGTH_SHORT).show();
                break;
            case PlayService.SINGLE_PLAY:
                imageView.setImageResource(R.mipmap.normal);
                imageView.setTag(PlayService.ORDER_PLAY);
                playService.setPlay_mode(PlayService.ORDER_PLAY);
                Toast.makeText(getActivity(), "顺序播放", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void sendCommStr(String CommStr) {
        addDanmaku(CommStr, true);
    }

    @Override
    public void publish(int progress) {


    }

    @Override
    public void change(int position) {

    }

    @Override
    public void progreses(int progress) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            playService.pose();
            playService.seekTo(progress * playService.getDuration() / 100);
            playService.start();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void sendPulish(int progress) {

        if (HttpParams.isOnPush) {

            Message obtain = Message.obtain();

            obtain.obj = progress;

            obtain.what = PROGRESS;

            handler.sendMessage(obtain);
        }

    }

    @Override
    public void sendChage(int currentIndex) {
        Log.e(TAG, "sendChage: " + currentIndex);
        mTitle.setText(MusicDetailsActivity.data.get(currentIndex).getSongName());
        x.image().bind(mImage, MusicDetailsActivity.data.get(currentIndex).getImg());
        mTotaltime.setText(simpleDateFormat.format(playService.getDuration()));
        handler.sendEmptyMessage(SUCCESSFORIMAGE);
    }

    @Override
    public void sendProgress(int progress) {
        Log.e(TAG, "sendProgress: " + progress);

    }

    /**
     * 随机生成一些弹幕内容以供测试
     */
    private void generateSomeDanmaku() {
        if (!isInall) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isShowDanmu) {
                        int time = new Random().nextInt(300);
                        String content = "" + time + time;
                        addDanmaku(content, false);
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            isInall = true;
        }
    }

    /**
     * 向弹幕View中添加一条弹幕
     *
     * @param content    弹幕的具体内容
     * @param withBorder 弹幕是否有边框
     */
    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = sp2px(20);
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(mDanmakuView.getCurrentTime());
        if (withBorder) {
            danmaku.borderColor = Color.GREEN;
        }
        mDanmakuView.addDanmaku(danmaku);
    }

    /**
     * sp转px的方法。
     */
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_music_details_back:
                getActivity().finish();
                break;
            case R.id.activity_music_details_more:
                showPupmenu(mMore);
                break;
        }
    }

    private void showPupmenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view, Gravity.BOTTOM);
        getActivity().getMenuInflater().inflate(R.menu.pupmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.pupmenu_open:
                mDanmakuView.prepare(parser, danmakuContext);
                break;
            case R.id.pupmenu_close:
                mDanmakuView.stop();
                break;
            case R.id.pupmenu_share:
                showShare();
                break;
        }

        return false;
    }

    private void showShare() {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("音乐");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(MusicDetailsActivity.data.get(playService.getCurrentIndex()).getSongName());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(MusicDetailsActivity.data.get(playService.getCurrentIndex()).getImg());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(MusicDetailsActivity.data.get(playService.getCurrentIndex()).getId());

// 启动分享GUI
        oks.show(getActivity());
    }

}
