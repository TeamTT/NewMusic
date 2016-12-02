package com.example.newmusic.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import com.example.newmusic.PlayService;

/**
 * Created by Administrator on 2016/11/29.
 */
public abstract class BaseFragment extends Fragment {

    protected PlayService playService;

    private boolean isbind = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService.PlayBinder playBinder = (PlayService.PlayBinder) service;
            playService = playBinder.getPlayService();
            playService.setMusicUpdateLinstener(musicUpdateLinstener);
            musicUpdateLinstener.onChage(playService.getCurrentIndex());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isbind = false;
        }
    };

    private PlayService.MusicUpdateLinstener musicUpdateLinstener=new PlayService.MusicUpdateLinstener() {
        @Override
        public void onPublish(int progress) {
            publish(progress);
        }

        @Override
        public void onChage(int currentIndex) {
            change(currentIndex);
        }

        @Override
        public void onProgress(int progress) {
            progreses(progress);
        }


    };

    public abstract void publish(int progress);
    public abstract void change(int position);
    public abstract void progreses(int progress);

    /**
     * 绑定服务
     */
    public void bindPlayService() {
        if (!isbind) {
            Intent intent = new Intent(getActivity(), PlayService.class);
            getActivity().bindService(intent, connection, getActivity().BIND_AUTO_CREATE);
            isbind = true;
        }
    }

    /**
     * 解除绑定服务
     */
    public void unbindPlayService() {
        if (isbind) {
            getActivity().unbindService(connection);
            isbind = false;
        }
    }
}
