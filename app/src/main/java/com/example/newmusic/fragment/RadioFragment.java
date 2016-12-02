package com.example.newmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newmusic.LoginActivity;
import com.example.newmusic.MusicDetailsActivity;
import com.example.newmusic.NewMusicApp;
import com.example.newmusic.PlayService;
import com.example.newmusic.R;
import com.example.newmusic.adapter.RadioRecyclerAdapter;
import com.example.newmusic.contants.HttpParams;
import com.example.newmusic.model.RadioDetailModel;
import com.example.newmusic.model.RadioModel;
import com.example.newmusic.widget.CustomDialog;
import com.example.newmusic.widget.PullToRefreshRecyclerView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/27.
 */
public class RadioFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, RadioRecyclerAdapter.SendPosition, View.OnClickListener, PlayService.MusicUpdateLinstener {

    public static final String TAG = RadioFragment.class.getSimpleName();
    private static final String GET_URL1 = "http://s.music.qq.com/fcgi-bin/music_search_new_platform?t=0&n=20&aggr=1&cr=1&loginUin=0&format=json&inCharset=GB2312&outCharset=utf-8&notice=0&platform=jqminiframe.json&needNewCode=0&p=";
    private static final String GET_URL2 = "&catZhida=0&remoteplace=sizer.newclient.next_song&w=";
    private static final int SUCCESSFORUP = 100;
    private static final int REFRESH = 101;
    private static final int SUCCESSFOADD = 102;
    private static final int GET_KEY = 103;
    private int page = 1;
    private String searchName = "小沈阳";
    private String key;
    private View layout;

    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    private List<RadioDetailModel> data;
    private List<RadioDetailModel> dataforadd;
    private RadioRecyclerAdapter adapter;

    private ImageView mImageView;
    private TextView mTitle;
    private TextView mSingler;
    private ImageView mPlay;
    private ImageView mNext;
    private LinearLayout mLinearLayout;
    private int position;


    private ImageView mSearch;
    private EditText mEdittext;
    private Button mButton;
    private TextView mTv;
    private CustomDialog customDialog;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SUCCESSFORUP) {
                adapter.updataRes(data);
                customDialog.dismiss();
                handler.sendEmptyMessage(REFRESH);
                playService.setInfos(data);
            } else if (msg.what == SUCCESSFOADD) {
                adapter.updataRes(dataforadd);
                handler.sendEmptyMessage(REFRESH);
                playService.setInfos(dataforadd);
            } else if (msg.what == REFRESH) {
                pullToRefreshRecyclerView.onRefreshComplete();
            } else if (msg.what == GET_KEY) {
                setupView();
            }
        }
    };
    private View mLogin;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (playService != null) {
            playService.setMusicUpdateLinstener(this);
        }


        getKey();

    }


    @Override
    public void onPause() {
        super.onPause();
        unbindPlayService();

    }

    @Override
    public void onResume() {
        super.onResume();

        bindPlayService();

        if (playService != null && playService.isplay()) {

            int position = playService.getCurrentIndex();

            if (dataforadd.size() != 0) {

                Log.e(TAG, "change: " + position);

                RadioDetailModel detailModel = dataforadd.get(position);

                x.image().bind(mImageView, detailModel.getImg());

                mTitle.setText(detailModel.getSongName());

                mSingler.setText(detailModel.getSinglerName());
            }
        }

        if (playService != null && playService.isplay()) {
            mPlay.setImageResource(R.mipmap.pause);
        } else {
            mPlay.setImageResource(R.mipmap.play);
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_radio, container, false);

        customDialog = new CustomDialog(getContext(), "正在加载...", R.drawable.dialog_anim);
        customDialog.show();

        data = new ArrayList<>();
        dataforadd = new ArrayList<>();

        return layout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }


    private void setupView() {
        OkHttpUtils.get()
                .url(GET_URL1 + page + GET_URL2 + searchName)
                .addHeader(HttpParams.CACHE_CONTROL,NewMusicApp.getCacheControl())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        Log.e(TAG, "onError: " + e.getCause());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: " + response);
                        Gson gson = new Gson();
                        RadioModel radioModel = gson.fromJson(response, RadioModel.class);
                        List<RadioModel.NewData.Song.List> list = radioModel.getData().getSong().getList();

                        data.clear();
                        dataforadd.clear();

                        for (int i = 0; i < list.size(); i++) {
                            RadioModel.NewData.Song.List list1 = list.get(i);

                            Log.e(TAG, "onResponse: " + i);

                            String list1F = list1.getF();

                            getDetail(list1F);
                        }

                        handler.sendEmptyMessage(SUCCESSFORUP);

                    }
                });
    }

    private void addSetupView() {
        OkHttpUtils.get()
                .url(GET_URL1 + page + GET_URL2 + searchName)
                .addParams(HttpParams.CACHE_CONTROL, NewMusicApp.getCacheControl())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        Log.e(TAG, "onError: " + e.getCause());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: " + response);
                        Gson gson = new Gson();
                        RadioModel radioModel = gson.fromJson(response, RadioModel.class);
                        List<RadioModel.NewData.Song.List> list = radioModel.getData().getSong().getList();

                        data.clear();

                        for (int i = 0; i < list.size(); i++) {
                            RadioModel.NewData.Song.List list1 = list.get(i);

                            String list1F = list1.getF();

                            getDetail(list1F);
                        }

                        handler.sendEmptyMessage(SUCCESSFOADD);

                    }
                });
    }

    private void initView() {
        pullToRefreshRecyclerView = (PullToRefreshRecyclerView) layout.findViewById(R.id.fragment_radio_ptrrv);
        mImageView = ((ImageView) layout.findViewById(R.id.fragment_radio_image));
        mTitle = ((TextView) layout.findViewById(R.id.fragment_radio_title));
        mSingler = ((TextView) layout.findViewById(R.id.fragment_radio_singler));
        mPlay = ((ImageView) layout.findViewById(R.id.fragment_radio_image_play));
        mNext = ((ImageView) layout.findViewById(R.id.fragment_radio_image_next));
        mLinearLayout = ((LinearLayout) layout.findViewById(R.id.fragment_radio_LinearLayout));
        mLogin = layout.findViewById(R.id.fragment_radio_login);

        mSearch = ((ImageView) layout.findViewById(R.id.fragment_radio_search));
        mEdittext = (EditText) layout.findViewById(R.id.fragment_radio_EditText);
        mButton = (Button) layout.findViewById(R.id.fragment_radio_Button);
        mTv = (TextView) layout.findViewById(R.id.fragment_radio_tv);

        mEdittext.setVisibility(View.GONE);
        mButton.setVisibility(View.GONE);

        mSearch.setOnClickListener(this);
        mButton.setOnClickListener(this);

        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mTitle.setOnClickListener(this);
        mSingler.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mLogin.setOnClickListener(this);

        mLinearLayout.setVisibility(View.GONE);

        pullToRefreshRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecyclerView.setOnRefreshListener(this);

        RecyclerView refreshableView = pullToRefreshRecyclerView.getRefreshableView();
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        refreshableView.setLayoutManager(layout);

        adapter = new RadioRecyclerAdapter(null, this.getContext(), this);
        refreshableView.setAdapter(adapter);
    }

    private void getDetail(String s) {

        int count = 0;

        String id = "";
        String img = "";
        String lrc = "";
        String songName = "";
        String singlerName = "";

        for (int i = 0; i < 24; i++) {

            int indexOf = s.indexOf("|", count + 1);


            if (i == 0) {
                lrc = s.substring(count, indexOf);
                String s1 = "http://music.qq.com/miniportal/static/lyric";
                String s2 = "/" + (Integer.parseInt(lrc) % 100) + "/";
                String s3 = lrc + ".xml";
                lrc = s1 + s2 + s3;
                Log.e(TAG, "getDetail: " + lrc);
            }

            if (i == 20) {
                id = s.substring(count + 1, indexOf);
                String s1 = "http://cc.stream.qqmusic.qq.com/C200";
                String s2 = ".m4a?vkey=";

                String s3 = "&guid=376051132&fromtag=0";
                id = s1 + id + s2 + key + s3;
            }

            if (i == 1) {
                songName = s.substring(count + 1, indexOf);
            }

            if (i == 3) {
                singlerName = s.substring(count + 1, indexOf);
            }

            if (i == 22) {
                img = s.substring(count + 1, indexOf);
                String s1 = "http://imgcache.qq.com/music/photo/mid_album_90";
                String s2 = "/" + img.charAt(img.length() - 2);
                String s3 = "/" + img.charAt(img.length() - 1);
                String s4 = "/" + img + ".jpg";
                img = s1 + s2 + s3 + s4;

            }

            count = indexOf;
        }

        RadioDetailModel detailModel = new RadioDetailModel(id, img, lrc, songName, singlerName);

        data.add(detailModel);
        dataforadd.add(detailModel);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 1;
        setupView();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page++;
        addSetupView();
    }

    @Override
    public void sendForDownload(int position) {
        Log.e(TAG, "sendForDownload: " + position);

    }

    @Override
    public void sendForPlay(int position) {
        Log.e(TAG, "sendForPlay: " + position);
        playService.play(position);

        this.position = position;

        RadioDetailModel detailModel = data.get(position);

        mLinearLayout.setVisibility(View.VISIBLE);

        x.image().bind(mImageView, detailModel.getImg());

        mTitle.setText(detailModel.getSongName());

        mSingler.setText(detailModel.getSinglerName());

    }

    @Override
    public void sendImageView(ImageView imageView) {

    }

    @Override
    public void sendPlayMode(ImageView imageView) {

    }

    @Override
    public void sendCommStr(String CommStr) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_radio_image_play:
                if (playService.isplay()) {
                    playService.pose();
                    ((ImageView) v).setImageResource(R.mipmap.play);
                } else {
                    playService.start();
                    ((ImageView) v).setImageResource(R.mipmap.pause);
                }
                break;

            case R.id.fragment_radio_image_next:
                next(position);

                if (playService != null && playService.isplay()) {

                    int position = playService.getCurrentIndex();

                    if (dataforadd.size() != 0) {

                        Log.e(TAG, "change: " + position);

                        RadioDetailModel detailModel = dataforadd.get(position);

                        x.image().bind(mImageView, detailModel.getImg());

                        mTitle.setText(detailModel.getSongName());

                        mSingler.setText(detailModel.getSinglerName());
                    }
                }

                if (playService != null && playService.isplay()) {
                    mPlay.setImageResource(R.mipmap.pause);
                } else {
                    mPlay.setImageResource(R.mipmap.play);
                }

                break;

            case R.id.fragment_radio_title:
            case R.id.fragment_radio_singler:
            case R.id.fragment_radio_image:
                if (HttpParams.isOnPush) {
                    HttpParams.SIZE = dataforadd.size();
                    Intent intent = new Intent(getActivity(), MusicDetailsActivity.class);
                    for (int i = 0; i < HttpParams.SIZE; i++) {
                        intent.putExtra("model" + i, dataforadd.get(i));
                    }
                    intent.putExtra("position", position);
                    intent.putExtra("flag", playService.isplay());
                    intent.putExtra("Tag", playService.getPlay_mode());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "正在加载音乐，请稍等....", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.fragment_radio_search:
                mTv.setVisibility(View.GONE);
                mLogin.setVisibility(View.GONE);
                mButton.setVisibility(View.VISIBLE);
                mEdittext.setVisibility(View.VISIBLE);
                break;
            case R.id.fragment_radio_Button:
                String result = mEdittext.getText().toString();
                searchName = result;
                setupView();

                mEdittext.setVisibility(View.GONE);
                mButton.setVisibility(View.GONE);
                mLogin.setVisibility(View.VISIBLE);
                mTv.setVisibility(View.VISIBLE);

                mEdittext.setText("");

                break;
            case R.id.fragment_radio_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void next(int position) {

        position++;

        playService.play(position);

    }


    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onChage(int currentIndex) {

    }

    @Override
    public void onProgress(int progress) {

    }

    public void getKey() {

        OkHttpUtils.get()
                .url("http://c.y.qq.com/base/fcgi-bin/fcg_musicexpress.fcg?json=3&guid=376051132")
                .addParams(HttpParams.CACHE_CONTROL, NewMusicApp.getCacheControl())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        Log.e(TAG, "onError: " + e.getCause());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        int indexOf = response.indexOf("key");
                        key = response.substring(indexOf + 7, response.length() - 4);
                        handler.sendEmptyMessage(GET_KEY);
                    }
                });
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


}
