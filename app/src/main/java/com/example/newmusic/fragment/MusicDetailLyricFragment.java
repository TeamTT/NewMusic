package com.example.newmusic.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newmusic.MusicDetailsActivity;
import com.example.newmusic.NewMusicApp;
import com.example.newmusic.R;
import com.example.newmusic.contants.HttpParams;
import com.example.newmusic.widget.view.impl.DefaultLrcBuilder;
import com.example.newmusic.widget.view.impl.LrcRow;
import com.example.newmusic.widget.view.impl.LrcView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/28.
 */
public class MusicDetailLyricFragment extends BaseFragment {

    private static final String TAG = MusicDetailsActivity.class.getSimpleName();
    private View layout;
    private LrcView mLrcView;

    private static final int PROGRESS = 101;
    private static Send send;
    private boolean isDown = false;

    public static void setSend(Send send) {
        MusicDetailLyricFragment.send = send;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == PROGRESS) {
                mLrcView.seekLrcToTime(((Integer) msg.obj));
            }

        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " + MusicDetailsActivity.position);
        send.sendProgress(MusicDetailsActivity.position);
    }

    @Override
    public void onResume() {
        super.onResume();
        bindPlayService();
    }

    @Override
    public void onPause() {
        super.onPause();
        unbindPlayService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_musiclyricdetail, container, false);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        steupView(MusicDetailsActivity.position);
    }

    private void steupView(int position) {
        Log.e(TAG, "steupView: " + MusicDetailsActivity.data.get(position).getLrc());
        OkHttpUtils.get()
                .url(MusicDetailsActivity.data.get(position).getLrc())
                .addParams(HttpParams.CACHE_CONTROL, NewMusicApp.getCacheControl())
                .build()
                .execute(new FileCallBack(getActivity().getCacheDir().getAbsolutePath(), Math.random() * 10000 + "") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        Log.e(TAG, "onError: " + e.getCause());

                        DefaultLrcBuilder lrcBuilder = new DefaultLrcBuilder();

                        List<LrcRow> lrcRows = lrcBuilder.getLrcRows("暂无歌词");

                        mLrcView.setLrc(lrcRows);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        try {

                            Log.e(TAG, "onResponse: " + response);

                            FileInputStream inputStream = new FileInputStream(response);

                            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

                            byte[] buffer = new byte[1024];

                            int len = 0;

                            while ((len = inputStream.read(buffer)) > 0) {

                                arrayOutputStream.write(buffer, 0, len);

                                arrayOutputStream.flush();

                            }

                            inputStream.close();

                            //mTextView.setText(arrayOutputStream.toString("GB2312"));

                            DefaultLrcBuilder lrcBuilder = new DefaultLrcBuilder();

                            List<LrcRow> lrcRows = lrcBuilder.getLrcRows(arrayOutputStream.toString("GB2312"));

                            Log.e(TAG, "onResponse: " + lrcRows);

                            mLrcView.setLrc(lrcRows);

                            isDown = true;

                            arrayOutputStream.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initView() {
        mLrcView = (LrcView) layout.findViewById(R.id.activity_music_details_LrcView);
        //mTextView = (TextView) layout.findViewById(R.id.textview);
    }

    @Override
    public void publish(int progress) {

        if (HttpParams.isOnPush) {

            if (isDown) {
                Message obtain = Message.obtain();

                obtain.obj = progress;

                obtain.what = PROGRESS;

                handler.sendMessage(obtain);
            }


            send.sendPulish(progress);
        }
    }

    @Override
    public void change(int position) {
        isDown = false;

        steupView(position);

        send.sendChage(position);
    }

    @Override
    public void progreses(int progress) {

    }

    public interface Send {
        void sendPulish(int progress);

        void sendChage(int currentIndex);

        void sendProgress(int position);
    }

}
