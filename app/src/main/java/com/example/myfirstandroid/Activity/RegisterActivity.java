
package com.example.myfirstandroid.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstandroid.R;
import com.example.myfirstandroid.Utils.AlbumUtil;
import com.example.myfirstandroid.Utils.ApplicationUtil;
import com.example.myfirstandroid.Utils.MyDatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class RegisterActivity extends AppCompatActivity {

    private TextView submit;
    private ImageView uploadHead;
    private EditText username, password, repassword;
    private CheckBox checkRules;

    private MyDatabaseHelper dbHelper;

    private static final int CHOOSE_PHOTO = 1;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new MyDatabaseHelper(this, "UserDB.db", null, 1);

        submit = findViewById(R.id.submit_info);
        uploadHead = findViewById(R.id.upload_head);

        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        repassword = findViewById(R.id.register_repassword);

        checkRules = findViewById(R.id.check_rules);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String username_str = username.getText().toString();
                String password_str = password.getText().toString();
                String repassword_str = repassword.getText().toString();

                if (checkRules.isChecked()){
                    if (password_str.equals(repassword_str)){
                        Cursor cursor = db.rawQuery("select * from user where name=?", new String[]{username_str});
                        if (cursor.getCount()>0){
                            Toast.makeText(RegisterActivity.this, "该用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();
                        }else {
                            ContentValues values = new ContentValues();
                            values.put("name", username_str);
                            values.put("password", password_str);
                            db.insert("User", null, values);
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                        cursor.close();
                    }else {
                        Toast.makeText(RegisterActivity.this, "两次密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                } else {
                      Toast.makeText(RegisterActivity.this, "请勾选同意使用条款", Toast.LENGTH_SHORT).show();
                }
            }
        });

        uploadHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else {
                    openAlbum();
                }
            }

        });

        ApplicationUtil.getInstance().addActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    private void openAlbum() {

        boolean flag = isExternalStorageWritable();
        Log.i(TAG, "外存是否可读写：" + flag);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "图片封装后的url："+data.toString());  // Intent { dat=content://media/external/images/media/30 flg=0x1 }
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath;
                    Log.i(TAG, "Build.VERSION.SDK_INT："+Build.VERSION.SDK_INT);
                    if (Build.VERSION.SDK_INT >= 19){
                        imagePath = AlbumUtil.handleImageOnKitKat(this, data);
                    }else {
                        Log.i(TAG, "SDK_INT < 19, 系统版本4.4以下");
                        imagePath = AlbumUtil.handleImageBeforeKitKat(this, data);
                    }
                    setHead(imagePath);
                }
                break;
            default:
                break;
        }
    }

    private void setHead(String imagePath) {
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
            try {
                String path = getCacheDir().getPath();
                File file = new File(path, "user_head");
                Log.i(TAG, "file路径"+file.toString());
                round.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            uploadHead.setImageBitmap(round);
        }else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}