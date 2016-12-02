package com.example.newmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MvSearchActivity extends AppCompatActivity implements View.OnClickListener {
    private PullToRefreshListView refreshListView;
    private SearchView mSearchView;
    private TextView mCancel;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mv_search);
        initView();
    }

    private void initView() {
        mSearchView = (SearchView) findViewById(R.id.teach_mv_search_searchview);
        mCancel = (TextView) findViewById(R.id.teach_mv_search_cancel);
        mCancel.setOnClickListener(this);
        refreshListView = ((PullToRefreshListView) findViewById(R.id.teach_mv_search_pull_to_refresh));
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView = refreshListView.getRefreshableView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.teach_mv_search_cancel:
                finish();
                break;
        }
    }
}
