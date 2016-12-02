package com.example.newmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/27.
 */
public class CommentFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2, View.OnClickListener {
    private PullToRefreshListView refreshListView;

    public static final String TAG = CommentFragment.class.getSimpleName();
    public static final String LEFT_URL = "http://www.buyao.tv/appapi/index.php?appkey=BYMUSICOFFVN0DtKGcebowgEPLtASJfBBn6iOTQ&ac=commentlist&id=";
    public static final String RIGHT_URL = "&page=";
    public int index = 0;
    private View layout;
    private ListView mListView;
    private MvCommentAdapter adapter;
    private View empty;
    private View mEtContent;
    private View mDeliver;
    private MvMode mvMode;

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

                        List<MvCommentMode> commentModes = mvCommentList.getBy_item();
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
        mEtContent = layout.findViewById(R.id.teach_mv_comment_et_content);
        mDeliver = layout.findViewById(R.id.teach_mv_comment_deliver);
        mDeliver.setOnClickListener(this);
        refreshListView = ((PullToRefreshListView) layout.findViewById(R.id.teach_comment_pull_to_refresh));
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
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

                break;
        }
    }

    enum State {
        DOWN, UP
    }

}
