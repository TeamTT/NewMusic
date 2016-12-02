package com.example.newmusic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.newmusic.fragment.LocalFragment;
import com.example.newmusic.fragment.MvFragment;
import com.example.newmusic.fragment.RadioFragment;

import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RadioGroup radioGroup;
    private FragmentTransaction transaction;
    private Fragment showfragment;
    private FragmentManager fm;
    private boolean isExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //设置radioGroup中的radioButton选择监听
        radioGroup.setOnCheckedChangeListener(this);

        initFragment();

    }

    /**
     * 第一次进来所见的Fragment
     */
    private void initFragment() {
        fm = getSupportFragmentManager();

        transaction = fm.beginTransaction();

        showfragment = new MvFragment();

        transaction.add(R.id.main_framelayout, showfragment, MvFragment.TAG);

        transaction.commit();

    }

    /**
     * 初始化view
     */
    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.main_radiobtn_mv:
                FragmentTransaction(MvFragment.TAG, MvFragment.class);
                break;
            case R.id.main_radiobtn_radio:
                FragmentTransaction(RadioFragment.TAG, RadioFragment.class);
                break;
            case R.id.main_radiobtn_local:
                FragmentTransaction(LocalFragment.TAG, LocalFragment.class);
                break;
        }
    }

    private void FragmentTransaction(String TAG, Class<? extends Fragment> cls) {
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();

        transaction.hide(showfragment);

        showfragment = this.fm.findFragmentByTag(TAG);

        if (showfragment != null) {
            transaction.show(showfragment);
        } else {
            try {
                showfragment = cls.getConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            transaction.add(R.id.main_framelayout, showfragment, TAG);

        }
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isExit) {
            // 修改标记位
            isExit = true;
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            // 调用定时器的定时任务      ① 具体的任务  ② 延迟时间
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    // 还原状态
                    isExit = false;
                }
            }, 3 * 1000);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
