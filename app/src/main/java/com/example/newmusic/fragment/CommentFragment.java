package com.example.newmusic.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newmusic.R;
import com.example.newmusic.adapters.MvCommentAdapter;
import com.example.newmusic.model.MvCommentList;
import com.example.newmusic.model.MvCommentMode;
import com.example.newmusic.model.MvMode;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/27.
 */
public class CommentFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2, View.OnClickListener, PlatformActionListener {
    private PullToRefreshListView refreshListView;

    public static final String TAG = CommentFragment.class.getSimpleName();
    public static final String LEFT_URL = "http://www.buyao.tv/appapi/index.php?appkey=BYMUSICOFFVN0DtKGcebowgEPLtASJfBBn6iOTQ&ac=commentlist&id=";
    public static final String RIGHT_URL = "&page=";
    public int index = 0;
    private View layout;
    private ListView mListView;
    private MvCommentAdapter adapter;
    private View empty;
    private EditText mEtContent;
    private View mDeliver;
    private MvMode mvMode;
    private List<MvCommentMode> commentModes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_comment, container, false);
        return layout;
    }

    private void getMv() {
        Intent intent = getActivity().getIntent();
        mvMode = ((MvMode) intent.getParcelableExtra("mv"));
        Log.e(TAG, "getMv: " + mvMode.getId());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMv();
        initView();
        setupView(State.DOWN);
    }

    private void setupView(final State state) {
        switch (state) {
            case DOWN:
                index = 0;
                break;
            case UP:
                index++;
                break;
        }
        OkHttpUtils.get()
                .url(LEFT_URL + Integer.parseInt(mvMode.getId()) + RIGHT_URL + index)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: ");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: " + response);
                        Gson gson = new Gson();
                        MvCommentList mvCommentList = gson.fromJson(response, MvCommentList.class);

                        commentModes = mvCommentList.getBy_item();
                        if (commentModes != null && commentModes.size() != 0) {
                            switch (state) {
                                case DOWN:
                                    adapter.updateRes(commentModes);
                                    break;
                                case UP:
                                    adapter.addRes(commentModes);
                                    break;
                            }
                        }
                    }
                });

    }

    private void initView() {
        mEtContent = ((EditText) layout.findViewById(R.id.teach_mv_comment_et_content));
        mDeliver = layout.findViewById(R.id.teach_mv_comment_deliver);
        mDeliver.setOnClickListener(this);
        refreshListView = ((PullToRefreshListView) layout.findViewById(R.id.teach_comment_pull_to_refresh));
        refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        refreshListView.setOnRefreshListener(this);
        mListView = refreshListView.getRefreshableView();
        empty = LayoutInflater.from(getContext()).inflate(R.layout.empty_layout, null);
        mListView.setEmptyView(empty);
        adapter = new MvCommentAdapter(getContext(), null);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        setupView(State.DOWN);
        refreshListView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        setupView(State.UP);
        refreshListView.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teach_mv_comment_deliver:
                String s = mEtContent.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setTitle("登录");
                    builder.setMessage("是否使用QQ登录?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.create().dismiss();
                            login();
                        }
                    });
                   builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           builder.create().dismiss();
                       }
                   });
                    builder.create().show();
                }else {
                    Toast.makeText(getContext(), "请输入评论内容", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Set<String> set = hashMap.keySet();
        for (String s : set) {
            Log.e(TAG, "onComplete: " + s + "----" + hashMap.get(s));
        }
        //用户唯一标识，通常用在第三方登录绑定
        String userId = platform.getDb().getUserId();
        Log.e(TAG, "onComplete: " + userId);
        //nickname   figureurl_qq_2
        final String nickname = (String) hashMap.get("nickname");
        final String url = (String) hashMap.get("figureurl_qq_2");
        Log.e(TAG, "onComplete: " + nickname + "-----" + "url");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<MvCommentMode> commentModes1 = new ArrayList<>();
                MvCommentMode mode = new MvCommentMode();
                mode.setNickname(nickname);
                mode.setFace(url);
                mode.setRegtime(new Date().getTime()+"");
                String content = mEtContent.getText().toString();
                mode.setContent(content);
                commentModes1.add(mode);
                commentModes1.addAll(commentModes);
                adapter.updateRes(commentModes1);
            }
        });

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.e(TAG, "onError: ");
        platform.removeAccount(true);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Log.e(TAG, "onCancel: ");
    }

    enum State {
        DOWN, UP
    }

    private void login() {
        // 首先 初始化 SharedSDK
        ShareSDK.initSDK(getContext());
        // 获取指定的平台
        Platform platform = ShareSDK.getPlatform(getContext(), QQ.NAME);
        // 为平台操作动作添加监听
        platform.setPlatformActionListener(this);
        // 调用平台认证
        platform.authorize();
        // showUser
        platform.showUser(null);
    }

}
