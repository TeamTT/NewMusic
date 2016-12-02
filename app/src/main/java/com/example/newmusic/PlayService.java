package com.example.newmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.newmusic.contants.HttpParams;
import com.example.newmusic.model.RadioDetailModel;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 音乐播放器实现的功能
 * 1.播放
 * 2.暂停
 * 3.上一首
 * 4.下一首
 * 5.获取当前的播放进度
 */

public class PlayService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static final String TAG = PlayService.class.getSimpleName();
    private MediaPlayer mediaPlayer;

    private int currentIndex = 0;//表示当前正在播放歌曲的位置

    private List<RadioDetailModel> infos;

    private boolean isPause = false;

    //播放模式
    public static final int ORDER_PLAY = 1;
    public static final int RANDOM_PLAY = 2;
    public static final int SINGLE_PLAY = 3;

    private int play_mode = ORDER_PLAY;

    public int getPlay_mode() {
        return play_mode;
    }

    public void setPlay_mode(int play_mode) {
        this.play_mode = play_mode;
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static MusicUpdateLinstener musicUpdateLinstener;

    public void setInfos(List<RadioDetailModel> infos) {
        this.infos = infos;
    }

    public PlayService() {
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean isPause() {
        return isPause;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(this);

        mediaPlayer.setOnErrorListener(this);

        Log.i("Main", "----------->7");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            executorService = null;
        }
    }


    private Random random = new Random();

    @Override
    public void onCompletion(MediaPlayer mp) {
        HttpParams.isOnPush = false;
        switch (play_mode) {
            case ORDER_PLAY:
                next();
                break;
            case RANDOM_PLAY:
                play(random.nextInt(infos.size()));
                break;
            case SINGLE_PLAY:
                play(currentIndex);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "onError: " );
        //Toast.makeText(this,"加载错误，请重新加载",Toast.LENGTH_LONG).show();
        return true;
    }


    public class PlayBinder extends Binder {
        public PlayService getPlayService() {
            return PlayService.this;
        }
    }

    private PlayBinder mBinder = new PlayBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 播放
     *
     * @param position
     */
    public void play(final int position) {

        HttpParams.isOnPush = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (position >= 0 && position <= infos.size() - 1) {
                        currentIndex = position;
                        RadioDetailModel mp3Info = infos.get(position);
                        Log.e(TAG, "play: " + mp3Info.getSongName());
                        Log.e(TAG, "play: " + mp3Info.getId());
                        isPause = false;
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(PlayService.this, Uri.parse(mp3Info.getId()));
                        mediaPlayer.prepare();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();

                Log.e(TAG, "onPrepared: " );

                Toast.makeText(PlayService.this,"加载成功",Toast.LENGTH_LONG).show();

                HttpParams.isStart = true;
                HttpParams.isOnPush = true;


                if (musicUpdateLinstener != null) {
                    musicUpdateLinstener.onChage(currentIndex);
                }

            }
        });

        mediaPlayer.start();

    }

    /**
     * 暂停
     */
    public void pose() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    /**
     * 下一首
     */
    public void next() {
        if (currentIndex == infos.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        play(currentIndex);
    }

    /**
     * 上一首
     */
    public void prve() {
        if (currentIndex == 0) {
            currentIndex = infos.size() - 1;
        } else {
            currentIndex--;
        }
        play(currentIndex);
    }

    /**
     * 开始播放
     */
    public void start() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPause = false;
        }
    }

    public boolean isplay() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public int getcurrentProgress() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int msec) {
        mediaPlayer.seekTo(msec);
    }

    /**
     * 更新状态的接口
     */
    public interface MusicUpdateLinstener {
        public void onPublish(int progress);

        public void onChage(int currentIndex);

        public void onProgress(int progress);
    }

    public void setMusicUpdateLinstener(MusicUpdateLinstener musicUpdateLinstener) {
        this.musicUpdateLinstener = musicUpdateLinstener;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (PlayService.musicUpdateLinstener != null && mediaPlayer != null && mediaPlayer.isPlaying() && HttpParams.isOnPush) {
                        SystemClock.sleep(500);
                        PlayService.musicUpdateLinstener.onPublish(getcurrentProgress());
                        PlayService.musicUpdateLinstener.onProgress(getcurrentProgress());
                    }
                }
            }
        }).start();
    }
}
