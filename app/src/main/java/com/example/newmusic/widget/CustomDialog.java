package com.example.newmusic.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newmusic.R;

/**
 * Created by Administrator on 2016/11/29.
 */
public class CustomDialog extends ProgressDialog {

    private final String mContent;
    private final Context mContext;
    private final int mResId;
    private ImageView mImage;
    private TextView mTextView;
    private AnimationDrawable mImageBackground;

    public CustomDialog(Context context, String content, int id) {
        super(context);
        mContext = context;
        mContent = content;
        mResId = id;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initData();

    }

    private void initData() {
        mImage.setBackgroundResource(mResId);

        mImageBackground = (AnimationDrawable) mImage.getBackground();

        mImage.post(new Runnable() {
            @Override
            public void run() {
                mImageBackground.start();
            }
        });

        mTextView.setText(mContent);


    }

    private void initView() {
        setContentView(R.layout.dialog_item);

        mImage = (ImageView) findViewById(R.id.dialog_image);
        mTextView = (TextView) findViewById(R.id.dialog_content);

    }
}
