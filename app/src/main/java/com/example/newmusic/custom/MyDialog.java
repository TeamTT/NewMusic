package com.example.newmusic.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.newmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class MyDialog extends Dialog implements RadioGroup.OnCheckedChangeListener {
    private Window window = null;
    private TextView mRewardError;
    private EditText mMoney;
    private TextView mPay;

    private RadioGroup mRg1;
    private RadioGroup mRg2;


    public MyDialog(Context context) {
        super(context);
    }

    public void setDisplay() {
        setContentView(R.layout.custom_reward_dailog_layout);//设置对话框的布局
        initView();
        setProperty();
        show();//显示对话框
    }

    private void initView() {
        mRewardError = ((TextView) findViewById(R.id.teach_mv_reward_error));
        mMoney = (EditText) findViewById(R.id.teach_mv_et_money);
        mPay = ((TextView) findViewById(R.id.teach_mv_pay));
        mRg1 = (RadioGroup) findViewById(R.id.rg1);
        mRg2 = (RadioGroup) findViewById(R.id.rg2);
        mRg1.setOnCheckedChangeListener(this);
        mRg2.setOnCheckedChangeListener(this);
    }

    public void setOnclick(View.OnClickListener listener) {
        mRewardError.setOnClickListener(listener);
        mPay.setOnClickListener(listener);

    }

    //要显示这个对话框，只要创建该类对象．然后调用该函数即可．
    public void setProperty() {
        window = getWindow();//　　　得到对话框的窗口．
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x =0;//这两句设置了对话框的位置．0为中间
//        wl.y =280;
//        wl.alpha=0.6f;//这句设置了对话框的透明度
//        wl.gravity= Gravity.BOTTOM;
        window.setAttributes(wl);
    }

    public EditText getEditText() {

        return mMoney;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rg1:
                if (checkedId == R.id.rb1 || checkedId == R.id.rb2 || checkedId == R.id.rb3) {
                    mRg2.clearCheck();
                }
                break;
            case R.id.rg2:
                if (checkedId == R.id.rb4 || checkedId == R.id.rb5 || checkedId == R.id.rb6) {
                    mRg1.clearCheck();
                }
                break;
        }
    }
}
