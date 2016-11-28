package com.example.newmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.newmusic.MvActivity;
import com.example.newmusic.R;
import com.example.newmusic.adapters.MvAdapter;
import com.example.newmusic.model.MvList;
import com.example.newmusic.model.MvMode;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

public class MvFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2, View.OnClickListener, AdapterView.OnItemClickListener {

    private PullToRefreshListView refreshListView;
    public static final String TAG = MvFragment.class.getSimpleName();
    private int index = 1;
    public static final String MV_URL = "http://www.buyao.tv/appapi/index.php?appkey=BYMUSICOFFVN0DtKGcebowgEPLtASJfBBn6iOTQ&ac=video&page=";
    private View layout;
    private ListView mListView;
    private MvAdapter adapter;
    private View mSearch;
    private View mLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_mv, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setupView(State.DOWN);
    }

    private void setupView(final State state) {
        switch (state) {
            case DOWN:
                index = 1;
                break;
            case UP:
                index++;
                break;
        }
        OkHttpUtils.get()
                .url(MV_URL + index)
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
                        MvList mvList = gson.fromJson(response, MvList.class);
                        List<MvMode> mvModes = mvList.getBy_item();
                        switch (state) {
                            case DOWN:
                                adapter.updateRes(mvModes);
                                break;
                            case UP:
                                adapter.addRes(mvModes);
                                break;
                        }
                    }
                });
    }

    private void initView() {
        refreshListView = ((PullToRefreshListView) layout.findViewById(R.id.teach_mv_pull_to_refresh_lv));
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshListView.setOnRefreshListener(this);
        mListView = refreshListView.getRefreshableView();
        mListView.setOnItemClickListener(this);
        adapter = new MvAdapter(getContext(), null);
        mListView.setAdapter(adapter);
        mSearch = layout.findViewById(R.id.teach_mv_search);
        mLogin = layout.findViewById(R.id.teach_mv_login);
        mSearch.setOnClickListener(this);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        Log.e(TAG, "onPullDownToRefresh: ");
        setupView(State.DOWN);
        refreshListView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        Log.e(TAG, "onPullUpToRefresh: ");
        setupView(State.UP);
        refreshListView.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teach_mv_search:
                break;
            case R.id.teach_mv_login:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG, "onItemClick: " + position);
        MvMode item = adapter.getItem(position);
        Intent intent = new Intent(getContext(), MvActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("mv", item);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    enum State {
        DOWN, UP
    }
}
