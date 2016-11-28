package com.example.newmusic.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by Administrator on 2016/11/10.
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context, attrs);
        return recyclerView;
    }

    @Override
    protected boolean isReadyForPullEnd() {

        RecyclerView refreshableView = getRefreshableView();

        int height = refreshableView.getHeight();

        int childCount = refreshableView.getChildCount();

        View childAt = refreshableView.getChildAt(childCount - 1);

        int bottom =0;

        if (childAt != null) {
            bottom=childAt.getBottom();

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();

            int bottomMargin = layoutParams.bottomMargin;

            int paddingBottom = refreshableView.getPaddingBottom();

            return height==bottom+bottomMargin+paddingBottom;
        }

        return false;


    }

    @Override
    protected boolean isReadyForPullStart() {

        RecyclerView refreshableView = getRefreshableView();

        View childAt = refreshableView.getChildAt(0);

        if (childAt != null) {
            int top = childAt.getTop();

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();

            int topMargin = layoutParams.topMargin;

            int paddingTop = refreshableView.getPaddingTop();

            return top==topMargin+paddingTop;
        }

        return false;
    }
}
