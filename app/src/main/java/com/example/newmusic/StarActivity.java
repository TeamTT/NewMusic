package com.example.newmusic;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class StarActivity extends AppCompatActivity {

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(StarActivity.this, MainActivity.class);
            startActivity(intent);
            StarActivity.this.finish();
        }
    };
    private ImageView viewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);

        initView();

        handler.sendEmptyMessageDelayed(0,4000);

    }

    private void initView() {
        viewById = (ImageView) findViewById(R.id.img);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.imgalp);

        viewById.setAnimation(animation);
    }
}
