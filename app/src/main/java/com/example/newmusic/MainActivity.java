package com.example.newmusic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.example.newmusic.fragment.LocalFragment;
import com.example.newmusic.fragment.MvFragment;
import com.example.newmusic.fragment.RadioFragment;

import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RadioGroup radioGroup;
    private FragmentTransaction transaction;
    private Fragment showfragment;
    private FragmentManager fm;


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

}
