package com.example.newmusic.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class MyDialog extends Dialog {
    private Window window=null;
    private TextView mRewardError;
    private EditText mMoney;
    private TextView mPay;
    private TextView mNumber1;
    private TextView mNumber2;
    private TextView mNumber3;
    private TextView mNumber4;
    private TextView mNumber5;
    private TextView mNumber6;

    public MyDialog(Context context) {
        super(context);
    }

    public void setDisplay(){
        setContentView(R.layout.custom_reward_dailog_layout);//设置对话框的布局
        initView();
        setProperty();
        show();//显示对话框
    }

    private void initView() {
        mRewardError = ((TextView) findViewById(R.id.teach_mv_reward_error));
        mMoney = (EditText) findViewById(R.id.teach_mv_et_money);
        mPay = ((TextView) findViewById(R.id.teach_mv_pay));

    }

    public  void setOnclick(View.OnClickListener listener){
        mRewardError.setOnClickListener(listener);
        mPay.setOnClickListener(listener);

    }
    //要显示这个对话框，只要创建该类对象．然后调用该函数即可．
    public void setProperty(){
        window=getWindow();//　　　得到对话框的窗口．
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
}
