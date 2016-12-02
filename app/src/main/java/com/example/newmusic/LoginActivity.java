package com.example.newmusic;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PHONE_REQUSTCODE = 100;
    private static final int ALBUM_REQUSTCODE = 101;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextView mPhoto;
    private TextView mCamera;
    private TextView mAlbum;
    private TextView mSubmit;
    private EditText mEtName;
    private EditText mEtFhone;
    private EditText mEtSchool;
    private EditText mEtIntroduce;
    private TextView mBack;
    private boolean isSetBackground = false;
    private byte[] mContent;
    private Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mBack = (TextView) findViewById(R.id.teach_mv_login_back);
        mPhoto = (TextView) findViewById(R.id.teach_mv_login_image);
        mCamera = (TextView) findViewById(R.id.teach_mv_login_camera);
        mAlbum = (TextView) findViewById(R.id.teach_mv_login_album);
        mSubmit = (TextView) findViewById(R.id.teach_mv_login_submit);
        mEtName = (EditText) findViewById(R.id.teach_mv_login_et_name);
        mEtFhone = (EditText) findViewById(R.id.teach_mv_login_et_phone);
        mEtSchool = (EditText) findViewById(R.id.teach_mv_login_et_school);
        mEtIntroduce = (EditText) findViewById(R.id.teach_mv_login_et_introduce);
        mCamera.setOnClickListener(this);
        mAlbum.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teach_mv_login_back:
                finish();
                break;
            case R.id.teach_mv_login_camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, PHONE_REQUSTCODE);
                }
                break;
            case R.id.teach_mv_login_album:
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType("image/jpeg");
                startActivityForResult(getImage, 0);
                break;
            case R.id.teach_mv_login_submit:
                String name = mEtName.getText().toString();
                String phone = mEtFhone.getText().toString();
                String school = mEtSchool.getText().toString();
                String introduce = mEtIntroduce.getText().toString();
                if (isSetBackground && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(school) && !TextUtils.isEmpty(introduce)) {
                    Toast.makeText(LoginActivity.this, "提交成功!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (!isSetBackground) {
                        Toast.makeText(LoginActivity.this, "请上传图片", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(name)) {
                        Toast.makeText(LoginActivity.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(school)) {
                        Toast.makeText(LoginActivity.this, "请输入学校名", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(introduce)) {
                        Toast.makeText(LoginActivity.this, "请输入简介", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PHONE_REQUSTCODE) {
            Bitmap bitmap = data.getParcelableExtra("data");
            mPhoto.setBackground(new BitmapDrawable(getResources(), bitmap));
            mPhoto.setText("");
            isSetBackground = true;
        } else if (resultCode == RESULT_OK && requestCode == 0) {
            ContentResolver resolver = getContentResolver();
            try {
                // 获得图片的uri
                Uri originalUri = data.getData();

                // 将图片内容解析成字节数组
                mContent = readStream(resolver.openInputStream(Uri.parse(originalUri.toString())));
                // 将字节数组转换为ImageView可调用的Bitmap对象
                myBitmap = getPicFromBytes(mContent, null);
                // //把得到的图片绑定在控件上显示
                mPhoto.setBackground(new BitmapDrawable(getResources(), myBitmap));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            mPhoto.setText("");
            isSetBackground = true;
        }
    }

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }
}
