package com.example.myfirstandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myfirstandroid.R;
import com.example.myfirstandroid.Utils.AlbumUtil;
import com.example.myfirstandroid.Utils.ApplicationUtil;
import com.example.myfirstandroid.Utils.MyDatabaseHelper;
import com.example.myfirstandroid.Utils.SharedPreUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LoginActivity extends AppCompatActivity {
    
    private MyDatabaseHelper dbHelper;

    private AppCompatButton login;
    private ImageView loginHead;

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new MyDatabaseHelper(this, "UserDB.db", null, 1);

        loginHead = findViewById(R.id.login_head);
        login = findViewById(R.id.check_user);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);

        String path = getCacheDir().getPath();
        String fileName = "user_head";
        File file = new File(path, fileName);
        if (file.exists()){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
                loginHead.setImageBitmap(round);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            loginHead.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.head));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String username_str = username.getText().toString();
                String password_str = password.getText().toString();
                Cursor cursor = db.rawQuery("select * from user where name=?",new String[]{username_str});
                if (cursor.getCount()==0){
                    Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT);
                }else {
                    if (cursor.moveToFirst()){
                        String password_db = cursor.getString(cursor.getColumnIndex("password"));
                        if (password_db.equals(password_str)){
                            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.IS_LOGIN, true);
                            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.LOGIN_DATA, username_str);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "密码错误，请确认后重新输入", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                cursor.close();
                db.close();
            }
        });
        ApplicationUtil.getInstance().addActivity(this);
    }
}